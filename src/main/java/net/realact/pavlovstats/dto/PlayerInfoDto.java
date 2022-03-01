package net.realact.pavlovstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class PlayerInfoDto
{
    @JsonProperty("PlayerName")
    public String playerName;
    @JsonProperty("UniqueId")
    public String uniqueId;
    @JsonProperty("KDA")
    public String kda;
    @JsonProperty("Score")
    public String score;
    @JsonProperty("Dead")
    public String dead;
    @JsonProperty("Cash")
    public String cash;
    @JsonProperty("TeamId")
    public String teamId;
}
