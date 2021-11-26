package ua.lil.proxy.api.command;

public interface CommandSender {

    public void sendMessage(String msg);

    public void sendMessage(String[] msg);

    public boolean isAdmin();
}

