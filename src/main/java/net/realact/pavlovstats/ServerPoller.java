package net.realact.pavlovstats;

import java.io.IOException;
import java.util.Date;

public final class ServerPoller
{
    private final RconClient rconClient;
    private final ScoreboardUtil scoreboardUtil;
    private final PlayerUtil playerUtil;

    public ServerPoller(final RconClient rconClient, final ScoreboardUtil scoreboardUtil, final PlayerUtil playerUtil)
    {
        this.rconClient = rconClient;
        this.scoreboardUtil = scoreboardUtil;
        this.playerUtil = playerUtil;
    }

    public void poll()
    {
        try
        {
            ScoreboardEntity scoreboard = scoreboardUtil.getScoreboardFromRcon();
            if(scoreboard == null)
            {
                return;
            }

            if(!isSameScoreboard(scoreboard))
            {
                scoreboardUtil.saveScoreboard(scoreboard);
                scoreboard.started = new Date();
            }

            playerUtil.updatePlayerStats(scoreboard, scoreboardUtil.currentScoreboard);
            scoreboardUtil.currentScoreboard = scoreboard;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            try
            {
                rconClient.close();
            }
            catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }

    private boolean isSameScoreboard(ScoreboardEntity scoreboard)
    {
        if(scoreboardUtil.currentScoreboard == null)
        {
            scoreboard.started = new Date();
            scoreboardUtil.currentScoreboard = scoreboard;
            return true;
        }

        final int redScoreLast = scoreboardUtil.currentScoreboard.redTeamScore;
        final int blueScoreLast = scoreboardUtil.currentScoreboard.blueTeamScore;
        final int redScore = scoreboard.redTeamScore;
        final int blueScore = scoreboard.blueTeamScore;

        return scoreboardUtil.currentScoreboard.mapName.equalsIgnoreCase(scoreboard.mapName) &&
               scoreboardUtil.currentScoreboard.gameMode.equalsIgnoreCase(scoreboard.gameMode) &&
               redScore >= redScoreLast &&
               blueScore >= blueScoreLast;
    }
}
