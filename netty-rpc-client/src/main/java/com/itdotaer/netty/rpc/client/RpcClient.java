package com.itdotaer.netty.rpc.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * RpcClient
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class RpcClient {

    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private CopyOnWriteArrayList<RpcClientHandler> connectedHandlers = new CopyOnWriteArrayList<>();
    private volatile EventLoopGroup group;

    public RpcClientHandler chooseHandler() throws InterruptedException {
        while (connectedHandlers.size() == 0) {
            Thread.sleep(10L);
        }

        return connectedHandlers.get(0);
    }

    public synchronized void prepareWorkGroup() {
        group = new NioEventLoopGroup();
    }

    public void start(String address, int port) {
        prepareWorkGroup();
        connect(address, port);
    }

    private void connect(String address, int port) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new LoggingHandler(LogLevel.INFO));
                        socketChannel.pipeline().addLast(new ObjectDecoder(1024,
                                ClassResolvers.cacheDisabled(this.getClass().getClassLoader())));
                        socketChannel.pipeline().addLast(new ObjectEncoder());
                        socketChannel.pipeline().addLast(new RpcClientHandler());
                    }
                });

        ChannelFuture channelFuture = bootstrap.connect(address, port);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                logger.debug("Successfully connect to remote server. ");
                RpcClientHandler handler = channelFuture.channel().pipeline().get(RpcClientHandler.class);

                connectedHandlers.add(handler);
            }
        });
    }

    public void stop() {
        this.group.shutdownGracefully();
    }

}
