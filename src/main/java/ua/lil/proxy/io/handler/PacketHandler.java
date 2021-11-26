package ua.lil.proxy.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import ua.lil.proxy.Core;
import ua.lil.proxy.connection.Connection;
import ua.lil.proxy.io.AbstractPacket;

@AllArgsConstructor
public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    private final Connection connection;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        this.connection.onDisconnect();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        Core.info("[" + this.connection.getName() + "] PacketHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket) throws Exception {
        if (Core.isDebug())
            Core.info("[" + this.connection.getName() + "] PacketHandler has read: " + abstractPacket.toString());
        Core.getInstance().getListenerManager().handleListeners(this.connection, abstractPacket);
    }
}

