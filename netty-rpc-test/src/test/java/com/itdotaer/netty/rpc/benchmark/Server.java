package com.itdotaer.netty.rpc.benchmark;

import com.itdotaer.netty.rpc.common.constant.Constants;
import com.itdotaer.netty.rpc.server.RpcServer;
import com.itdotaer.netty.rpc.service.HelloService;
import com.itdotaer.netty.rpc.service.HelloServiceImpl;

/**
 * ServerTest
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class Server {

    public static void main(String[] args) {
        System.setProperty(Constants.ZOOKEEPER_HOST_PROP, "127.0.0.1");
        System.setProperty(Constants.ZOOKEEPER_PORT_PROP, "2181");
        
        RpcServer rpcServer = new RpcServer<HelloServiceImpl>(HelloService.class.getName());
        rpcServer.setRef(new HelloServiceImpl());
        rpcServer.start("127.0.0.1", 9003);
    }

}
