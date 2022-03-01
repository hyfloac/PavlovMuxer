package net.realact.pavlovstats.commands;

public abstract class Command
{
    public final String command;

    protected Command(final String command, final String... args)
    {
        this.command = toCommand(command, args);
    }

    private static String toCommand(final String command, final String... args)
    {
        String argsString = "";
        if(args != null && args.length > 0)
        {
            argsString = String.join(" ", args);
        }

        return command + ' ' + argsString;
    }
}
