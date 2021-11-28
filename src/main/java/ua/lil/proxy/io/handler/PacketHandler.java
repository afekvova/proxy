package ua.lil.proxy.io.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.connection.Connection;
import ua.lil.proxy.helpers.LogHelper;
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
        LogHelper.info("[" + this.connection.getName() + "] PacketHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket abstractPacket) throws Exception {
        if (LogHelper.isDebugEnabled())
            LogHelper.info("[" + this.connection.getName() + "] PacketHandler has read: " + abstractPacket.toString());
        ProxyMain.getInstance().getListenerManager().handleListeners(this.connection, abstractPacket);
    }
}

