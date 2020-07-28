package com.itdotaer.netty.rpc.benchmark;

import com.itdotaer.netty.rpc.client.RpcClient;
import com.itdotaer.netty.rpc.client.RpcClientHandler;
import com.itdotaer.netty.rpc.client.RpcRequestProxy;
import com.itdotaer.netty.rpc.common.constant.Constants;
import com.itdotaer.netty.rpc.service.HelloService;
import com.itdotaer.netty.rpc.service.SayHelloRequest;
import com.itdotaer.netty.rpc.service.SayHelloResponse;

import java.lang.reflect.Proxy;

/**
 * Client
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class Client {

    public static void main(String[] args) throws Exception {
        System.setProperty(Constants.ZOOKEEPER_HOST_PROP, "127.0.0.1");
        System.setProperty(Constants.ZOOKEEPER_PORT_PROP, "2181");

        RpcClient rpcClient = new RpcClient();

        RpcClientHandler rpcClientHandler = rpcClient.start(HelloService.class.getName());

        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class},
                new RpcRequestProxy(HelloService.class, rpcClientHandler));

        SayHelloRequest request = new SayHelloRequest();
        request.setMsg("abc");
        SayHelloResponse response = helloService.sayHello(request);

//        String response = helloService.sayHello("jdiofj");

        System.out.println(response);

        rpcClient.stop();
    }

}
