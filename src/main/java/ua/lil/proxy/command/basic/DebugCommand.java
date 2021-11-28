package ua.lil.proxy.command.basic;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;
import ua.lil.proxy.helpers.LogHelper;

public final class DebugCommand extends Command {
    public DebugCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return "[true/false]";
    }

    @Override
    public String getUsageDescription() {
        return "Enable or disable debug logging at runtime";
    }

    @Override
    public void invoke(String... args) {
        boolean newValue;
        if (args.length >= 1) {
            newValue = Boolean.parseBoolean(args[0]);
            LogHelper.setDebugEnabled(newValue);
        } else {
            newValue = LogHelper.isDebugEnabled();
        }

        LogHelper.subInfo("Debug enabled: " + newValue);
    }
}
