package ua.lil.proxy.api.command;

import ua.lil.proxy.Core;

import java.util.Collection;
import java.util.HashMap;

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap();

    public void registerCommand(String command, Command executer) {
        this.commands.put(command, executer);
        Core.info("New command '" + command + "' registed!");
    }

    public void unregisterCommand(String command) {
        if (this.commands.remove(command) != null)
            Core.info("Command '" + command + "' unregisted!");
    }

    public void unregisterCommands() {
        this.commands.clear();
        Core.info("All commands unregisted!");
    }

    public boolean executeCommand(CommandSender sender, String command, String[] args) {
        Command executer = this.commands.get(command);
        if (executer == null)
            return false;
        
        executer.execute(sender, args);
        return true;
    }

    public Command getCommand(String command) {
        return this.commands.get(command);
    }

    public Collection<String> getCommands() {
        return this.commands.keySet();
    }
}

