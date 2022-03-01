package net.realact.pavlovstats;

public final class Main
{
    public static void main(String[] args) throws InterruptedException
    {
        final DatabaseManager databaseManager = DatabaseManager.create();
        final RconClient rconClient = RconClient.create(PropertyManager.RconCommandSleep, PropertyManager.RconHost, PropertyManager.RconPort, PropertyManager.RconPassword);
        final PlayerUtil playerUtil = new PlayerUtil(rconClient, databaseManager);
        final ScoreboardUtil scoreboardUtil = new ScoreboardUtil(rconClient, databaseManager, playerUtil);
        final ServerPoller serverPoller = new ServerPoller(rconClient, scoreboardUtil, playerUtil);

        while(true)
        {
            serverPoller.poll();
            Thread.sleep(PropertyManager.PollInterval);
        }
    }
}
