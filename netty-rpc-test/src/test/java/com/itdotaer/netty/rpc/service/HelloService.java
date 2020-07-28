package com.itdotaer.netty.rpc.service
        ;

/**
 * HelloService
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public interface HelloService {

    SayHelloResponse sayHello(SayHelloRequest input);

    String sayHello(String msg);

}
