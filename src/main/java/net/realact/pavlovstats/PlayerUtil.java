package net.realact.pavlovstats;

import net.realact.pavlovstats.commands.InspectPlayerCommand;
import net.realact.pavlovstats.commands.RefreshListCommand;
import net.realact.pavlovstats.dto.PlayerDto;
import net.realact.pavlovstats.dto.PlayerInfoDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class PlayerUtil
{
    private final RconClient rconClient;
    private final DatabaseManager database;

    public PlayerUtil(final RconClient rconClient, final DatabaseManager database)
    {
        this.rconClient = rconClient;
        this.database = database;
    }

    public final List<PlayerInfoDto> getCurrentPlayersFromServer() throws IOException
    {
        final RefreshListCommand.Response basicPlayerList = rconClient.send(RefreshListCommand.INSTANCE, RefreshListCommand.Response.class);

        if(basicPlayerList == null || basicPlayerList.playerList == null || basicPlayerList.playerList.size() == 0)
        {
            return Collections.emptyList();
        }

        final List<PlayerInfoDto> playerInfoList = new ArrayList<>(basicPlayerList.playerList.size());

        for(PlayerDto player : basicPlayerList.playerList)
        {
            final InspectPlayerCommand.Response playerInfo = rconClient.send(new InspectPlayerCommand(player.uniqueId), InspectPlayerCommand.Response.class);
            if(playerInfo != null)
            {
                playerInfoList.add(playerInfo.playerInfo);
            }
        }

        return playerInfoList;
    }

    public final void updatePlayerStats(final ScoreboardEntity scoreboardEntity, final ScoreboardEntity previousScoreboard)
    {
        for(UserEntity player : scoreboardEntity.redTeam)
        {
            findAndSavePlayer(previousScoreboard.redTeam, player);
            findAndSavePlayer(previousScoreboard.blueTeam, player);
        }
        for(UserEntity player : scoreboardEntity.blueTeam)
        {
            findAndSavePlayer(previousScoreboard.redTeam, player);
            findAndSavePlayer(previousScoreboard.blueTeam, player);
        }
    }

    private void findAndSavePlayer(final List<UserEntity> players, final UserEntity player)
    {
        for(UserEntity previousPlayer : players)
        {
            if(player.steamId == previousPlayer.steamId)
            {
                savePlayer(player, previousPlayer, false);
            }
        }
    }

    private void savePlayer(final UserEntity player, final UserEntity previousPlayer, boolean force)
    {
        long killsDelta = 0;
        long deathsDelta = 0;
        long assistsDelta = 0;
        player.lastOnline = new Date();

        if(player.kills != previousPlayer.kills ||
           player.deaths != previousPlayer.deaths ||
           player.assists != previousPlayer.assists)
        {
            killsDelta = player.kills - previousPlayer.kills;
            deathsDelta = player.deaths - previousPlayer.deaths;
            assistsDelta = player.assists - previousPlayer.assists;
            if(killsDelta + deathsDelta + assistsDelta != 0)
            {
                force = true;
            }
        }

        final UserEntity loadedPlayer = database.getUserById(player.steamId);

        if(loadedPlayer != null)
        {
            player.kills = loadedPlayer.kills + killsDelta;
            player.deaths = loadedPlayer.deaths + deathsDelta;
            player.assists = loadedPlayer.assists + assistsDelta;

            if(force)
            {
                database.insertUser(player);
            }
        }
        else
        {
            database.insertUser(player);
        }
    }
}
