package com.itdotaer.netty.rpc.client;

import com.itdotaer.netty.rpc.common.dtos.RpcRequest;
import com.itdotaer.netty.rpc.common.dtos.RpcResponse;
import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;


/**
 * RpcClientHandler
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class RpcClientHandler extends SimpleChannelInboundHandler {

    private static Logger logger = LoggerFactory.getLogger(RpcClientHandler.class);
    private Channel channel;
    private Map<String, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        logger.info("RpcServerHandler->channelRead0", o);

        RpcResponse response = (RpcResponse) o;
        String requestId = response.getRequestId();
        if (pendingRpc.containsKey(requestId)) {
            RpcFuture future = pendingRpc.get(requestId);

            future.done(response);
            pendingRpc.remove(requestId);
        } else {
            logger.error("Not exist requestId");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcServerHandler->exceptionCaught", cause);
    }

    public Object sendRequest(RpcRequest request) throws ExecutionException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        pendingRpc.put(request.getRequestId(), new RpcFuture());
        channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> countDownLatch.countDown());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            logger.error("RpcServerHandler->sendRequest" , e);
        }

        Future<RpcResponse> future = pendingRpc.get(request.getRequestId());

        return future.get().getResult();
    }

}
