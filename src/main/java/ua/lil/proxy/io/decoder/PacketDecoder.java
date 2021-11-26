package ua.lil.proxy.io.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import ua.lil.proxy.io.AbstractPacket;
import ua.lil.proxy.io.PacketMapper;

import java.util.List;

public class PacketDecoder extends ReplayingDecoder<AbstractPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        out.add(PacketMapper.readPacket(buf));
    }
}

