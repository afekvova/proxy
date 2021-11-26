package ua.lil.proxy.api.plugin;

import ua.lil.proxy.connection.Connection;
import ua.lil.proxy.io.AbstractPacket;

public interface Listener {

    public void handle(Connection connection, AbstractPacket packet);
}

