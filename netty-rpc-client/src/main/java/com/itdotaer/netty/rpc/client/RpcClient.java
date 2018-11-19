package com.itdotaer.netty.rpc.client;

import com.itdotaer.netty.rpc.models.RegisterModel;
import com.itdotaer.netty.rpc.utils.RegisterHelper;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
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

/**
 * RpcClient
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class RpcClient {

    private static Logger logger = LoggerFactory.getLogger(RpcClient.class);
    private volatile EventLoopGroup group;


    public synchronized void prepareWorkGroup() {
        group = new NioEventLoopGroup();
    }

    public RpcClientHandler start(String serviceName) throws Exception {
        prepareWorkGroup();

        // load balance
        RegisterModel chosenProvider = LoadBalance.getBalancedHost(serviceName);

        RpcClientHandler rpcClientHandler = connect(chosenProvider.getHost(), chosenProvider.getPort());
        RegisterHelper.foundConsumer(serviceName, chosenProvider.getHost(), chosenProvider.getPort());

        return rpcClientHandler;
    }

    private RpcClientHandler connect(String address, int port) throws InterruptedException {
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

        ChannelFuture channelFuture = bootstrap.connect(address, port).sync();

        return channelFuture.channel().pipeline().get(RpcClientHandler.class);

    }

    public void stop() {
        this.group.shutdownGracefully();
    }

}
