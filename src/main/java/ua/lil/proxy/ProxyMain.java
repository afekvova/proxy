package ua.lil.proxy;

import java.io.IOException;

public class ProxyMain {

    public ProxyMain() throws IOException {
        Core.info("Started proxy by Team LiL");
        Core core = new Core();
        Core.setInstance(core);
        core.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.exit(0);
            }
        });

        while (core.isEnabled()) {
            String line = core.getConsoleReader().readLine("> ");
            if (line == null || core.executeCommand(line)) continue;
            core.getConsoleSender().sendMessage("Command not found!");
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        try {
            new ProxyMain();
        } catch (IOException exception) {
            Core.info("Error start proxy: " + exception.getMessage());
        }
        Core.info("Proxy start in " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
