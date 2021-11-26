package ua.lil.proxy.api.command;

import ua.lil.proxy.Core;

public class ConsoleSender
        implements CommandSender {
    @Override
    public void sendMessage(String message) {
        Core.info(message);
    }

    @Override
    public void sendMessage(String[] arrayMessage) {
        for (String message : arrayMessage)
            Core.info(message);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}

