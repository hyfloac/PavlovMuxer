package net.realact.pavlovstats;

import net.realact.pavlovstats.commands.ServerInfoCommand;
import net.realact.pavlovstats.dto.PlayerInfoDto;
import net.realact.pavlovstats.dto.ServerInfoDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public final class RconDataConverter
{
    private static final Pattern kdaSplitter = Pattern.compile("/");
    private static final Pattern userNameSimplifier = Pattern.compile("[^a-zA-Z0-9]");

    private RconDataConverter()
    { }

    public static List<UserEntity> convertPlayers(final List<PlayerInfoDto> rconPlayers)
    {
        final List<UserEntity> ret = new ArrayList<>(rconPlayers.size());
        for(PlayerInfoDto dto : rconPlayers)
        {
            ret.add(convertPlayer(dto));
        }
        return ret;
    }

    public static UserEntity convertPlayer(PlayerInfoDto rconPlayer)
    {
        final UserEntity user = new UserEntity();
        user.steamId = Long.parseLong(rconPlayer.uniqueId);
        user.steamIdString = rconPlayer.uniqueId;
        user.userName = rconPlayer.playerName;
        user.simplifiedUserName = userNameSimplifier.matcher(rconPlayer.playerName).replaceAll("");
        user.lastOnline = new Date();
        final String[] kdaSplit = kdaSplitter.split(rconPlayer.kda);
        if(kdaSplit.length == 3){
            user.kills = Integer.parseInt(kdaSplit[0]);
            user.deaths = Integer.parseInt(kdaSplit[1]);
            user.assists = Integer.parseInt(kdaSplit[2]);
        }
        return user;
    }

    public static ScoreboardEntity convertScoreboard(final List<PlayerInfoDto> rconPlayers, ServerInfoDto serverInfo)
    {
        final ScoreboardEntity scoreboard = new ScoreboardEntity();
        scoreboard.gameMode = serverInfo.gameMode;
        scoreboard.mapName = serverInfo.mapLabel;
        scoreboard.playerCount = serverInfo.playerCount;
        scoreboard.redTeamScore = Integer.parseInt(serverInfo.team0Score);
        scoreboard.blueTeamScore = Integer.parseInt(serverInfo.team1Score);

        final List<PlayerInfoDto> redTeam = new ArrayList<>();
        final List<PlayerInfoDto> blueTeam = new ArrayList<>();

        for(PlayerInfoDto player : rconPlayers)
        {
            if(player.teamId.equalsIgnoreCase("0"))
            {
                redTeam.add(player);
            }
            else
            {
                blueTeam.add(player);
            }
        }

        scoreboard.redTeam = convertPlayers(redTeam);
        scoreboard.blueTeam = convertPlayers(blueTeam);

        return scoreboard;
    }
}
