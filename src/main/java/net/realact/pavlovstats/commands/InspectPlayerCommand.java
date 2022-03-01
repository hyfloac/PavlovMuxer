package net.realact.pavlovstats.commands;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.realact.pavlovstats.dto.PlayerInfoDto;

public final class InspectPlayerCommand extends Command
{
    public InspectPlayerCommand(final String uniqueId)
    {
        super("InspectPlayer", uniqueId);
    }

    public static final class Response
    {
        @JsonProperty("PlayerInfo")
        public PlayerInfoDto playerInfo;
    }
}
