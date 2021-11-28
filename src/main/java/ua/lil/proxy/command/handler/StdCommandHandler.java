package ua.lil.proxy.command.handler;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.helpers.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;

public final class StdCommandHandler extends CommandHandler {
    
    private final BufferedReader reader;

    public StdCommandHandler(ProxyMain proxyMain, boolean readCommands) {
        super(proxyMain);
        reader = readCommands ? LogHelper.newReader(System.in) : null;
    }

    @Override
    public void bell() {

    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("clear terminal");
    }

    @Override
    public String readLine() throws IOException {
        return reader == null ? null : reader.readLine();
    }
}
