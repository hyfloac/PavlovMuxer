package net.realact.pavlovstats;

import net.realact.pavlovstats.commands.ServerInfoCommand;
import net.realact.pavlovstats.dto.PlayerInfoDto;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public final class ScoreboardUtil
{
    private final RconClient rconClient;
    private final DatabaseManager database;
    private final PlayerUtil playerUtil;

    public ScoreboardEntity currentScoreboard;

    public ScoreboardUtil(final RconClient rconClient, final DatabaseManager database, final PlayerUtil playerUtil)
    {
        this.rconClient = rconClient;
        this.database = database;
        this.playerUtil = playerUtil;
    }

    public ScoreboardEntity getScoreboardFromRcon() throws IOException
    {
        final List<PlayerInfoDto> players = playerUtil.getCurrentPlayersFromServer();
        final ServerInfoCommand.Response serverInfo = rconClient.send(ServerInfoCommand.INSTANCE, ServerInfoCommand.Response.class);
        if(serverInfo == null)
        {
            return null;
        }
        return RconDataConverter.convertScoreboard(players, serverInfo.serverInfo);
    }

    public void saveScoreboard(ScoreboardEntity scoreboardEntity)
    {
        scoreboardEntity.concluded = new Date();
        database.insertScoreboard(scoreboardEntity);
    }

}
