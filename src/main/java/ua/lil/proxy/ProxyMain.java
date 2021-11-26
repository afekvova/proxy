package ua.lil.proxy;

import java.io.IOException;

public class ProxyMain {

    public ProxyMain() throws IOException {
        System.out.println("Started proxy by Team LiL");
        Core core = new Core();
        Core.setInstance(core);
        core.start();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("Stop core");
                core.stop();
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
            System.out.println("Error start proxy: " + exception.getMessage());
        }
        System.out.println("Proxy start in " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
