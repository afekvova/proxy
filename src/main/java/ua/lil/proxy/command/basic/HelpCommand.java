package ua.lil.proxy.command.basic;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;
import ua.lil.proxy.command.CommandException;
import ua.lil.proxy.helpers.LogHelper;

import java.util.Map.Entry;

public final class HelpCommand extends Command {

    public HelpCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    private static void printCommand(String name, Command command) {
        String args = command.getArgsDescription();
        LogHelper.subInfo("%s %s - %s", name, args == null ? "[nothing]" : args, command.getUsageDescription());
    }

    @Override
    public String getArgsDescription() {
        return "[command name]";
    }

    @Override
    public String getUsageDescription() {
        return "Print command usage";
    }

    @Override
    public void invoke(String... args) throws CommandException {
        if (args.length < 1) {
            printCommands();
            return;
        }

        printCommand(args[0]);
    }

    private void printCommand(String name) throws CommandException {
        printCommand(name, this.proxyMain.getCommandHandler().lookup(name));
    }

    private void printCommands() {
        for (Entry<String, Command> entry : this.proxyMain.getCommandHandler().commandsMap().entrySet())
            printCommand(entry.getKey(), entry.getValue());
    }
}
