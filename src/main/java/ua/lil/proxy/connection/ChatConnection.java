package ua.lil.proxy.connection;

import io.netty.channel.ChannelHandlerContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import ua.lil.proxy.io.AbstractPacket;
import ua.lil.proxy.io.handler.InitialHandler;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatConnection implements Connection {

    ChannelHandlerContext channel;
    String name;
    int port;

    @Override
    public String getAddress() {
        return InitialHandler.getChannelIp(this.channel.channel());
    }

    @Override
    public Connection getConnection() {
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public ChannelHandlerContext getChannel() {
        return this.channel;
    }

    @Override
    public void onConnect() {
        System.out.println("connect");
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void sendPacket(AbstractPacket packet) {

    }
}

