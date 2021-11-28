package ua.lil.proxy.command.handler;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;
import ua.lil.proxy.command.CommandException;
import ua.lil.proxy.command.basic.*;
import ua.lil.proxy.helpers.LogHelper;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CommandHandler implements Runnable {

    private final Map<String, Command> commands = new ConcurrentHashMap<>(32);

    protected CommandHandler(ProxyMain proxyMain) {
        registerCommand("help", new HelpCommand(proxyMain));
        registerCommand("version", new VersionCommand(proxyMain));
        registerCommand("stop", new StopCommand(proxyMain));
        registerCommand("debug", new DebugCommand(proxyMain));
        registerCommand("clear", new ClearCommand(proxyMain));
        registerCommand("gc", new GCCommand(proxyMain));
        registerCommand("send", new SendCommand(proxyMain));
    }

    private static String[] parse(CharSequence line) throws CommandException {
        boolean quoted = false;
        boolean wasQuoted = false;

        Collection<String> result = new LinkedList<>();
        StringBuilder builder = new StringBuilder(100);
        for (int i = 0; i <= line.length(); i++) {
            boolean end = i >= line.length();
            char ch = end ? '\0' : line.charAt(i);

            if (end || !quoted && Character.isWhitespace(ch)) {
                if (end && quoted) {
                    throw new CommandException("Quotes wasn't closed");
                }

                if (wasQuoted || builder.length() > 0) {
                    result.add(builder.toString());
                }

                wasQuoted = false;
                builder.setLength(0);
                continue;
            }

            switch (ch) {
                case '"':
                    quoted = !quoted;
                    wasQuoted = true;
                    break;
                case '\\':
                    if (i + 1 >= line.length()) {
                        throw new CommandException("Escape character is not specified");
                    }
                    char next = line.charAt(i + 1);
                    builder.append(next);
                    i++;
                    break;
                default:
                    builder.append(ch);
                    break;
            }
        }

        return result.toArray(new String[result.size()]);
    }

    @Override
    public final void run() {
        try {
            readLoop();
        } catch (IOException e) {
            LogHelper.error(e);
        }
    }

    public abstract void bell() throws IOException;

    public abstract void clear() throws IOException;

    public abstract String readLine() throws IOException;

    public final Map<String, Command> commandsMap() {
        return Collections.unmodifiableMap(commands);
    }

    public final void eval(String line, boolean bell) {
        LogHelper.info("Command '%s'", line);

        String[] args;
        try {
            args = parse(line);
            if (args.length > 0) args[0] = args[0].toLowerCase();
        } catch (Throwable exc) {
            LogHelper.error(exc);
            return;
        }

        eval(args, bell);
    }

    public final void eval(String[] args, boolean bell) {
        if (args.length == 0)
            return;

        long start = System.currentTimeMillis();
        try {
            lookup(args[0]).invoke(Arrays.copyOfRange(args, 1, args.length));
        } catch (Throwable exc) {
            LogHelper.error(exc);
        }

        long end = System.currentTimeMillis();
        if (bell && end - start >= 5_000L) {
            try {
                bell();
            } catch (IOException e) {
                LogHelper.error(e);
            }
        }
    }

    public final Command lookup(String name) throws CommandException {
        Command command = commands.get(name);
        if (command == null)
            throw new CommandException(String.format("Unknown command: '%s'", name));

        return command;
    }

    public final void registerCommand(String name, Command command) {
        this.commands.putIfAbsent(name.toLowerCase(), command);
        LogHelper.info("Register new command '" + name + "'!");
    }

    private void readLoop() throws IOException {
        for (String line = readLine(); line != null; line = readLine())
            eval(line, true);
    }
}
