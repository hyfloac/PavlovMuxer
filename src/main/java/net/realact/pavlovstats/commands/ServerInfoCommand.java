package net.realact.pavlovstats.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.realact.pavlovstats.dto.ServerInfoDto;

public final class ServerInfoCommand extends Command
{
    public static final ServerInfoCommand INSTANCE = new ServerInfoCommand();

    public ServerInfoCommand()
    {
        super("ServerInfo");
    }

    public static final class Response
    {
        @JsonProperty("ServerInfo")
        public ServerInfoDto serverInfo;
    }
}
