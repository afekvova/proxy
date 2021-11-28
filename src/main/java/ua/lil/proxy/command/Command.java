package ua.lil.proxy.command;


import ua.lil.proxy.ProxyMain;

import java.util.UUID;

public abstract class Command {

    protected final ProxyMain proxyMain;

    protected Command(ProxyMain proxyMain) {
        this.proxyMain = proxyMain;
    }

    protected static UUID parseUUID(String s) throws CommandException {
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException ignored) {
            throw new CommandException(String.format("Invalid UUID: '%s'", s));
        }
    }

    public abstract String getArgsDescription();

    public abstract String getUsageDescription();

    public abstract void invoke(String... args) throws Throwable;

    protected final void verifyArgs(String[] args, int min) throws CommandException {
        if (args.length < min)
            throw new CommandException("Command usage: " + getArgsDescription());
    }
}
