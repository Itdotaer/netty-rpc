package com.itdotaer.netty.rpc.server;

import com.itdotaer.netty.rpc.common.dtos.RpcRequest;
import com.itdotaer.netty.rpc.common.dtos.RpcResponse;
import com.itdotaer.netty.rpc.common.service.HelloService;
import com.itdotaer.netty.rpc.common.service.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * RpcServerHandler
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class RpcServerHandler extends SimpleChannelInboundHandler {

    private static Logger logger = LoggerFactory.getLogger(RpcServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        logger.info("RpcServerHandler->channelRead0", o);

        RpcRequest request = (RpcRequest) o;
        channelHandlerContext.writeAndFlush(process(request));
    }

    private RpcResponse process(RpcRequest request) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());

        if (HelloService.class.getName().equals(request.getClassName())) {
            try {
                Object object = Class.forName(HelloServiceImpl.class.getName()).newInstance();
                Method method = HelloServiceImpl.class.getMethod(request.getMethodName(), request.getParameterTypes());

                Object result = method.invoke(object, request.getParameters());
                response.setResult(result);
            } catch (ClassNotFoundException
                    | IllegalAccessException
                    | InstantiationException
                    | NoSuchMethodException
                    | InvocationTargetException e) {
                logger.error("RpcServerHandler->process", e);
                response.setError(e.toString());
            }
        }

        return response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("RpcServerHandler->exceptionCaught", cause);
    }

}
