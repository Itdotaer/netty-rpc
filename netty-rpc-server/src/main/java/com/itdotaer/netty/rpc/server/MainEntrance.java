package com.itdotaer.netty.rpc.server;

import com.itdotaer.netty.rpc.common.service.HelloService;

/**
 * com.itdotaer.netty.rpc.server.Main
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class Main {

    public static void main(String[] args) {
        RpcServer rpcServer = new RpcServer();

        rpcServer.start(HelloService.class.getName(), "127.0.0.1", Integer.parseInt(args[0]));
    }

}
