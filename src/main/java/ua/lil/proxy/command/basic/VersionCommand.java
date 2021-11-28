package ua.lil.proxy.command.basic;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;
import ua.lil.proxy.helpers.LogHelper;

public final class VersionCommand extends Command {
    public VersionCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Print Proxy version";
    }

    @Override
    public void invoke(String... args) {
        LogHelper.subInfo("Lil Proxy version: %s (build #%s)", "1.0.3", "1.0.1");
    }
}
