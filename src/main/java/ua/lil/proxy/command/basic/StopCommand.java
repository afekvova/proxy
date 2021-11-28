package ua.lil.proxy.command.basic;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;

public final class StopCommand extends Command {
    public StopCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Stop LaunchServer";
    }

    @Override
    @SuppressWarnings("CallToSystemExit")
    public void invoke(String... args) {
        this.proxyMain.stop();
    }
}
