package ua.lil.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ua.lil.proxy.command.handler.CommandHandler;
import ua.lil.proxy.command.handler.StdCommandHandler;
import ua.lil.proxy.config.Settings;
import ua.lil.proxy.connection.ChatConnection;
import ua.lil.proxy.helpers.CommonHelper;
import ua.lil.proxy.helpers.JVMHelper;
import ua.lil.proxy.helpers.LogHelper;
import ua.lil.proxy.io.PipelineInitializer;
import ua.lil.proxy.io.protocol.UserMessagePacket;
import ua.lil.proxy.plugin.ListenerManager;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class ProxyMain {

    static final Path WORKING_DIR = Paths.get(System.getProperty("user.dir"));
    final ConcurrentMap<String, ChatConnection> users = new ConcurrentHashMap<>();

    @Getter
    static ProxyMain instance;
    final boolean portable;
    @Getter
    boolean enabled;

    @Getter
    final ListenerManager listenerManager;

    private Channel channel;
    private EventLoopGroup bossGroup, workerGroup;
    @Getter
    private final CommandHandler commandHandler;

    public ProxyMain(boolean portable) throws IOException {
        instance = this;
        Settings.IMP.reload(WORKING_DIR.resolve("config.yml").toFile());
        this.portable = portable;
        this.listenerManager = new ListenerManager();

        //Initial command handler?
        this.commandHandler = new StdCommandHandler(this, true);
        CommonHelper.newThread("Command Thread", false, this.commandHandler).start();
    }

    protected void start() {
        this.listenerManager.registerListener(UserMessagePacket.class, (connection, packet) -> {
            UserMessagePacket userMessagePacket = (UserMessagePacket) packet;
            this.users.values().forEach(chatConnection -> chatConnection.sendPacket(userMessagePacket));
            if (LogHelper.isDebugEnabled())
                LogHelper.info("[CHAT] " + userMessagePacket.getUserName() + ": " + userMessagePacket.getMessage());
        });

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.startListener(Settings.IMP.PORT);
        this.enabled = true;
    }

    private void startListener(int port) {
        ChannelFutureListener listener = channelFuture -> {
            if (channelFuture.isSuccess()) {
                this.channel = channelFuture.channel();
                LogHelper.debug("Listening on /0.0.0.0:" + port);
            } else {
                LogHelper.debug("Could not bind to host /0.0.0.0:" + port);
                channelFuture.cause().printStackTrace();
            }
        };

        ((ServerBootstrap) new ServerBootstrap().group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class)).childHandler(new PipelineInitializer()).bind(port).addListener(listener).syncUninterruptibly();
    }

    public void stop() {
        this.enabled = false;
        LogHelper.info("Closing listener channel...");
        this.stopListener();
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
        LogHelper.info("GGWP!");
        JVMHelper.RUNTIME.exit(0);
    }

    private void stopListener() {
        ChannelFuture future = this.channel.close();
        future.syncUninterruptibly();
    }

    public static void main(String[] args) throws Exception {
        LogHelper.addOutput(WORKING_DIR.resolve("proxy.log"));
        LogHelper.info("Start proxy by Team LiL");

        // Start Proxy
        long start = System.currentTimeMillis();
        try {
            new ProxyMain(true).start();
        } catch (Throwable exc) {
            LogHelper.error(exc);
            return;
        }
        long end = System.currentTimeMillis();
        LogHelper.info("Proxy started in %dms", end - start);
    }

    public void addUser(String name, ChatConnection connection) {
        this.users.putIfAbsent(name.toLowerCase(), connection);
    }

    public ChatConnection getUser(String name) {
        return this.users.getOrDefault(name.toLowerCase(), null);
    }

    public void removeUser(String name) {
        this.users.remove(name.toLowerCase());
    }
}
