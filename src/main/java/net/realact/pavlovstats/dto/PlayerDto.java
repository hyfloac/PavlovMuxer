package net.realact.pavlovstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PlayerDto
{
    @JsonProperty("PlayerName")
    public String playerName;
    @JsonProperty("UniqueId")
    public String uniqueId;
}
