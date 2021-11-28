package ua.lil.proxy.io.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ua.lil.proxy.ProxyMain;
import ua.lil.proxy.connection.ChatConnection;
import ua.lil.proxy.connection.Connection;
import ua.lil.proxy.helpers.LogHelper;
import ua.lil.proxy.io.AbstractPacket;
import ua.lil.proxy.io.protocol.HandshakePacket;

import java.net.InetSocketAddress;

public class InitialHandler extends SimpleChannelInboundHandler<AbstractPacket> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] -> InitialHandler has connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler has disconneted.");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LogHelper.info("[/" + InitialHandler.getChannelIp(ctx.channel()) + "] InitialHandler ERROR: " + cause.getMessage());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractPacket packet) throws Exception {
        if (packet instanceof HandshakePacket) {
            LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] -> InitialHandler has read handshake: " + packet.toString());
            HandshakePacket handshakePacket = (HandshakePacket) packet;
            if (ProxyMain.getInstance().getUser(handshakePacket.getName()) != null) {
                handshakePacket.setAllowed(false);
                handshakePacket.setCancelReason("Username already exists");
                channelHandlerContext.writeAndFlush(handshakePacket);
                channelHandlerContext.close();
                return;
            }

            Connection connection = new ChatConnection(channelHandlerContext, handshakePacket.getName());
            handshakePacket.setAllowed(true);
            channelHandlerContext.writeAndFlush(handshakePacket);
            channelHandlerContext.pipeline().removeLast();
            connection.onConnect();
            channelHandlerContext.pipeline().addLast(new PacketHandler(connection));
        } else {
            channelHandlerContext.close();
            LogHelper.info("[/" + InitialHandler.getChannelIp(channelHandlerContext.channel()) + "] InitialHandler ERROR: the first packet should be Handshake!");
        }
    }

    public static String getChannelIp(Channel channel) {
        return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
    }
}

