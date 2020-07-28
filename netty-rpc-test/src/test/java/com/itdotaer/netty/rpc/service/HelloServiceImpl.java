package com.itdotaer.netty.rpc.service;

/**
 * HelloServiceImpl
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public SayHelloResponse sayHello(SayHelloRequest input) {
        SayHelloResponse resp = new SayHelloResponse();
        resp.setMsg("We have received : " + input);

        return resp;
    }

    @Override
    public String sayHello(String msg) {
        return "We have received : " + msg;
    }
}
