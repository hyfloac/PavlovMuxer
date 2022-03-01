package net.realact.pavlovstats.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.realact.pavlovstats.dto.PlayerDto;

import java.util.List;

public final class RefreshListCommand extends Command
{
    public static final RefreshListCommand INSTANCE = new RefreshListCommand();

    public RefreshListCommand()
    {
        super("RefreshList");
    }

    public static final class Response
    {
        @JsonProperty("PlayerList")
        public List<PlayerDto> playerList;
    }
}
