package com.itdotaer.netty.rpc.client;

import com.itdotaer.netty.rpc.common.service.HelloService;

import java.lang.reflect.Proxy;

/**
 * com.itdotaer.netty.rpc.client.MainEntrance
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class MainEntrance {

    public static void main(String[] args) throws Exception {
        RpcClient rpcClient = new RpcClient();

        RpcClientHandler rpcClientHandler = rpcClient.start(HelloService.class.getName());

        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(),
                new Class<?>[]{HelloService.class},
                new RpcRequestProxy(HelloService.class, rpcClientHandler));

        String result = helloService.sayHello("abc");

        System.out.println(result);

        rpcClient.stop();
    }

}
