package net.realact.pavlovstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class ServerInfoDto
{
    @JsonProperty("MapLabel")
    public String mapLabel;
    @JsonProperty("GameMode")
    public String gameMode;
    @JsonProperty("ServerName")
    public String serverName;
    @JsonProperty("Teams")
    public String teams;
    @JsonProperty("Team0Score")
    public String team0Score;
    @JsonProperty("Team1Score")
    public String team1Score;
    @JsonProperty("RoundState")
    public String roundState;
    @JsonProperty("PlayerCount")
    public String playerCount;
}
