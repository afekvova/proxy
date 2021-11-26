package ua.lil.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import jline.console.ConsoleReader;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ua.lil.proxy.api.command.CommandManager;
import ua.lil.proxy.api.command.ConsoleSender;
import ua.lil.proxy.api.plugin.ListenerManager;
import ua.lil.proxy.commands.StopCommand;
import ua.lil.proxy.io.PipelineInitializer;
import ua.lil.proxy.io.protocol.MessagePacket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Core {

    static long startTime;
    @Getter
    static Core instance;
    boolean enabled;
    @Getter
    static boolean debug;
    final ListenerManager listenerManager;
    final ConsoleReader consoleReader;
    final ConsoleSender consoleSender;
    //    private final ConcurrentHashMap<String, > proxys = new ConcurrentHashMap();
    private Channel channel;
    final CommandManager commandManager;
    private EventLoopGroup bossGroup, workerGroup;
    private static final SimpleDateFormat dataFormat = new SimpleDateFormat("dd MMM HH:mm:ss");

    public static void setInstance(Core instance) {
        if (instance == null)
            throw new RuntimeException("Instance cannot be null!");

        if (Core.instance != null)
            throw new RuntimeException("Instance already set!");

        Core.instance = instance;
    }

    public Core() throws IOException {
        this.consoleSender = new ConsoleSender();
        this.consoleReader = new ConsoleReader();
        this.consoleReader.setExpandEvents(false);
        this.listenerManager = new ListenerManager();
        this.commandManager = new CommandManager();
    }

    protected void start() {
        this.listenerManager.registerListener(MessagePacket.class, (connection, packet) -> {
            MessagePacket loginPacket = (MessagePacket) packet;
            Core.info(connection.getName() + ": " + loginPacket.getMessage());
        });
        
        this.commandManager.registerCommand("stop", new StopCommand());
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        this.startListener(5000);
        this.enabled = true;
        startTime = System.currentTimeMillis();
    }

    private void startListener(int port) {
        ChannelFutureListener listener = channelFuture -> {
            if (channelFuture.isSuccess()) {
                this.channel = channelFuture.channel();
                Core.info("Listening on /0.0.0.0:" + port);
            } else {
                Core.info("Could not bind to host /0.0.0.0:" + port);
                channelFuture.cause().printStackTrace();
            }
        };
        ((ServerBootstrap) new ServerBootstrap().group(this.bossGroup, this.workerGroup).channel(NioServerSocketChannel.class)).childHandler(new PipelineInitializer()).bind(port).addListener(listener).syncUninterruptibly();
    }

    public void stop() {
        this.enabled = false;
        Core.info("Closing listener channel...");
        this.stopListener();
        this.workerGroup.shutdownGracefully();
        this.bossGroup.shutdownGracefully();
        Core.info("GGWP!");
        System.exit(0);
    }

    private void stopListener() {
        ChannelFuture future = this.channel.close();
        future.syncUninterruptibly();
    }

    protected boolean executeCommand(String commandLine) {
        String[] split = commandLine.split(" ");
        String command = split[0].toLowerCase();
        String[] args = split.length == 1 ? new String[]{} : commandLine.substring(command.length() + 1).split(" ");
        return this.commandManager.executeCommand(this.consoleSender, command, args);
    }

    public static void info(String msg) {
        System.out.println(dataFormat.format(new Date()) + " [Core] " + msg);
    }
}

