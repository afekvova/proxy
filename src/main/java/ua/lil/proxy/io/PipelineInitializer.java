package ua.lil.proxy.io;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import ua.lil.proxy.io.decoder.PacketDecoder;
import ua.lil.proxy.io.encoder.PacketEncoder;
import ua.lil.proxy.io.handler.InitialHandler;

public class PipelineInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new PacketDecoder());
        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new InitialHandler());
    }
}

