package ua.lil.proxy.io.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import ua.lil.proxy.io.AbstractPacket;
import ua.lil.proxy.io.PacketMapper;

public class PacketEncoder extends MessageToByteEncoder<AbstractPacket> {
    
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket packet, ByteBuf buf) throws Exception {
        PacketMapper.writePacket(packet, buf);
    }
}

