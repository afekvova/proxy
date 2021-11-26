package ua.lil.proxy.connection;

import io.netty.channel.ChannelHandlerContext;
import ua.lil.proxy.io.AbstractPacket;

public interface Connection {

    public String getName();

    public ChannelHandlerContext getChannel();

    public void onConnect();

    public void onDisconnect();

    public void sendPacket(AbstractPacket packet);

    public String getAddress();

    public Connection getConnection();
}

