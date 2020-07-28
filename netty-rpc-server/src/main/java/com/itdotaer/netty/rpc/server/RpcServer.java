package com.itdotaer.netty.rpc.server;

import com.itdotaer.netty.rpc.AbstractRegisterFactory;
import com.itdotaer.netty.rpc.RegisterFactoryProducer;
import com.itdotaer.netty.rpc.common.coders.Decoder;
import com.itdotaer.netty.rpc.common.coders.Encoder;
import com.itdotaer.netty.rpc.models.RegisterType;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class RpcServer<T> {

    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);
    private static AbstractRegisterFactory registerFactory = RegisterFactoryProducer.getFactory(RegisterType.ZOOKEEPER);

    private volatile EventLoopGroup bossGroup;
    private volatile EventLoopGroup workerGroup;
    private volatile Boolean isStop = true;

    private String interfaceId;
    private T ref;

    public RpcServer(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }

    /**
     * 准备nio groups
     */
    public synchronized void prepareWorkGroup() {
        if (isStop) {
            bossGroup = new NioEventLoopGroup(1);
            workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);

            logger.info(RpcServer.class.getSimpleName() + " worker groups were prepared.");
        } else {
            logger.error(RpcServer.class.getSimpleName() + " is started.");
        }
    }

    /**
     * 绑定端口和注册处理方法
     */
    private void bind(String serviceName, String address, int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1000)
                .handler(new LoggingHandler(LogLevel.ERROR))
                .childHandler(new ChannelInitializer<SocketChannel>(){
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new Decoder());
                        socketChannel.pipeline().addLast(new Encoder());
                        socketChannel.pipeline().addLast(new RpcServerHandler(interfaceId, ref));
                    }
                });

        synchronized (isStop) {
            isStop = false;
        }

        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            registerFactory.foundProvider(serviceName, address, port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(RpcServer.class.getSimpleName() + "->bind", e);
        }
    }

    /**
     * 启动方法
     */
    public void start(String address, int port) {
        prepareWorkGroup();
        bind(interfaceId, address, port);
    }

    /**
     * 停止方法
     */
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
