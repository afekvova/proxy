package ua.lil.proxy.io;

import io.netty.channel.ChannelHandlerContext;
import ua.lil.proxy.helpers.LogHelper;


public abstract class ChannelWrapper {

    public abstract String getChannelName();

    public abstract String getWrapperName();

    protected abstract ChannelHandlerContext getChannel();

    protected abstract void onConnect();

    protected abstract void onDisconnect();

    protected abstract void onPacketRead(AbstractPacket var1);

    protected void packetRead(AbstractPacket packet) {
        LogHelper.info("[/" + this.getChannelName() + "] " + this.getWrapperName() + " IN: " + packet.toString());
        this.onPacketRead(packet);
    }

    public void sendPacket(AbstractPacket packet) {
        LogHelper.info("[/" + this.getChannelName() + "] " + this.getWrapperName() + " OUT: " + packet.toString());
        this.getChannel().writeAndFlush(packet);
    }
}

