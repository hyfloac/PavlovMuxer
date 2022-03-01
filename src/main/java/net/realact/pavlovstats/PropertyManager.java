package net.realact.pavlovstats;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropertyManager
{
    public static final String DatabaseUrl;
    public static final String DatabaseUser;
    public static final String DatabasePassword;
    public static final String DatabaseName;
    public static final String DatabaseUserTableName;
    public static final String DatabaseScoreboardTableName;
    public static final String RconHost;
    public static final int RconPort;
    public static final String RconPassword;
    public static final int RconCommandSleep;
    public static final int PollInterval;

    static
    {
        final Properties props = new Properties();

        try
        {
            props.load(new FileInputStream("./application.properties"));
        }
        catch(IOException ignored)
        {
        }
        DatabaseUrl = props.getProperty("database.url", "monogdb://localhost:27017");
        DatabaseUser = props.getProperty("database.user", "user");
        DatabasePassword = props.getProperty("database.password", "password");
        DatabaseName = props.getProperty("database.name", "Stats");
        DatabaseUserTableName = props.getProperty("database.user-table-name", "Users");
        DatabaseScoreboardTableName = props.getProperty("database.scoreboard-table-name", "Scoreboards");
        RconHost = props.getProperty("rcon.host", "localhost");
        RconPort = Integer.parseInt(props.getProperty("rcon.port", "9100"));
        RconPassword = props.getProperty("rcon.password", "password");
        RconCommandSleep = Integer.parseInt(props.getProperty("rcon.command-sleep", "5000"));
        PollInterval = Integer.parseInt(props.getProperty("rcon.polling-interval", "5000"));
    }
}
