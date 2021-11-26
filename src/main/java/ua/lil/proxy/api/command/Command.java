package ua.lil.proxy.api.command;

public interface Command {

    public void execute(CommandSender commandSender, String[] args);
    
}

