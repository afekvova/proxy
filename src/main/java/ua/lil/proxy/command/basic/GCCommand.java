package ua.lil.proxy.command.basic;

import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.command.Command;
import ua.lil.proxy.helpers.JVMHelper;
import ua.lil.proxy.helpers.LogHelper;

public final class GCCommand extends Command {
    public GCCommand(ProxyMain proxyMain) {
        super(proxyMain);
    }

    @Override
    public String getArgsDescription() {
        return null;
    }

    @Override
    public String getUsageDescription() {
        return "Perform Garbage Collection and print memory usage";
    }

    @Override
    public void invoke(String... args) throws Throwable {
        LogHelper.subInfo("Performing full GC");
        JVMHelper.fullGC();
        
        long max = JVMHelper.RUNTIME.maxMemory() >> 20;
        long free = JVMHelper.RUNTIME.freeMemory() >> 20;
        long total = JVMHelper.RUNTIME.totalMemory() >> 20;
        long used = total - free;
        LogHelper.subInfo("Heap usage: %d / %d / %d MiB", used, total, max);
    }
}
