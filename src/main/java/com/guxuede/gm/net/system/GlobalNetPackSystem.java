package com.guxuede.gm.net.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.guxuede.gm.net.server.GameWorld;
import com.guxuede.gm.net.server.PackageChannelServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;


public class GlobalNetPackSystem extends EntitySystem {

    public static final int PORT = Integer.parseInt(System.getProperty("port", "8992"));

    private final GameWorld gameWorld;
    private final Engine engine;

    public GlobalNetPackSystem(Engine engine, GameWorld gameWorld) {
        this.engine = engine;
        this.gameWorld = gameWorld;

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new PackageChannelServerInitializer());
            workerGroup.scheduleAtFixedRate(gameWorld,100000000,100000000, TimeUnit.NANOSECONDS);
            b.bind(PORT).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
