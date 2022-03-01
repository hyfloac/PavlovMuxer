package net.realact.pavlovstats;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.realact.pavlovstats.commands.Command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

public final class RconClient implements Closeable
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final long sleepTime;
    private final String host;
    private final short port;
    private final String passwordHash;
    private Socket clientSocket;
    private PrintWriter writer;
    private long lastTimestamp;

    private RconClient(final long sleepTime, final String host, final short port, final String passwordHash)
    {
        this.sleepTime = sleepTime;
        this.host = host;
        this.port = port;
        this.passwordHash = passwordHash;
    }

    public static RconClient create(final long sleepTime, final String host, final int port, final String password)
    {
        final String passwordHash = hashPassword(password);
        if(passwordHash == null)
        {
            return null;
        }

        return new RconClient(sleepTime, host, (short) port, passwordHash);
    }

    @Override
    public void close() throws IOException
    {
        if(isConnected())
        {
            clientSocket.close();
            clientSocket = null;
        }
    }

    public <T> T send(Command command, Class<T> responseType) throws IOException
    {
        if(!connect())
        {
            return null;
        }

        awaitNextCommand();
        writer.write(command.command);
        writer.flush();

        final String response = getResponse();
        return MAPPER.readValue(response, responseType);
    }

    private String getResponse() throws IOException
    {
        final InputStream is = clientSocket.getInputStream();
        final StringBuilder builder = new StringBuilder(is.available());

        int c;
        do
        {
            c = is.read();
            if(c > -1)
            {
                builder.append((char) c);
            }
        }
        while(c > 0 && is.available() > 0);

        return builder.toString();
    }

    private boolean isConnected()
    {
        return clientSocket != null && clientSocket.isConnected();
    }

    private boolean connect() throws IOException
    {
        if(!isConnected())
        {
            clientSocket = new Socket(host, port);
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            final String response = getResponse();

            if(response.contains("Password"))
            {
                return authenticate();
            }
            return false;
        }
        return true;
    }

    private boolean authenticate() throws IOException
    {
        writer.write(passwordHash);
        writer.flush();
        awaitNextCommand();

        final String response = getResponse();

        return response.contains("Authenticated=1");
    }

    private void awaitNextCommand()
    {
        final long currentTime = System.currentTimeMillis();
        final long tsDifference = currentTime - lastTimestamp;

        if(lastTimestamp == 0)
        {
            sleep(sleepTime);
        }
        else if(sleepTime > tsDifference)
        {
            sleep(sleepTime - tsDifference);
        }

        lastTimestamp = System.currentTimeMillis();
    }

    private void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ignored)
        {
            Thread.yield();
        }
    }

    private static char getHexChar(final int a)
    {
        switch(a)
        {
            case 0x0: return '0';
            case 0x1: return '1';
            case 0x2: return '2';
            case 0x3: return '3';
            case 0x4: return '4';
            case 0x5: return '5';
            case 0x6: return '6';
            case 0x7: return '7';
            case 0x8: return '8';
            case 0x9: return '9';
            case 0xA: return 'A';
            case 0xB: return 'B';
            case 0xC: return 'C';
            case 0xD: return 'D';
            case 0xE: return 'E';
            case 0xF: return 'F';
            default:  return '?';
        }
    }

    private static String hashPassword(final String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            char[] hexHash = new char[bytes.length * 2];
            for(int i = 0; i < bytes.length; ++i)
            {
                hexHash[i * 2] = getHexChar((bytes[i] >>> 4) & 0xF);
                hexHash[i * 2 + 1] = getHexChar(bytes[i] & 0xF);
            }
            return new String(hexHash);
        }
        catch(Throwable e)
        {
            return null;
        }
    }
}
