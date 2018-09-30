package com.itdotaer.netty.rpc.server;

import com.itdotaer.netty.rpc.common.rpc.RpcBase;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RpcServer
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class RpcServer implements RpcBase {

    private static Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private volatile EventLoopGroup bossGroup;
    private volatile EventLoopGroup workerGroup;
    private volatile Boolean isStop = true;

    /**
     * 准备nio groups
     */
    @Override
    public synchronized void prepareWorkGroup() {
        if (isStop) {
            bossGroup = new NioEventLoopGroup();
            workerGroup = new NioEventLoopGroup();

            logger.info(RpcServer.class.getSimpleName() + " worker groups were prepared.");
        } else {
            logger.error(RpcServer.class.getSimpleName() + " is started.");
        }
    }

    /**
     * 绑定端口和注册处理方法
     */
    private void bind(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1000)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024 * 1024,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        socketChannel.pipeline().addLast(new ObjectEncoder());
                        socketChannel.pipeline().addLast(new RpcServerHandler());
                    }
                });

        synchronized (isStop) {
            isStop = false;
        }

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(RpcServer.class.getSimpleName() + "->bind", e);
        }
    }

    /**
     * 启动方法
     */
    @Override
    public void start(String address, int port) {
        prepareWorkGroup();

        bind(port);
    }

    /**
     * 停止方法
     */
    @Override
    public void stop() {
        synchronized (isStop) {
            if (isStop) {
                logger.error(RpcServer.class.getSimpleName() + " has be stopped.");
            } else {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();

                isStop = true;
            }
        }
    }

}
