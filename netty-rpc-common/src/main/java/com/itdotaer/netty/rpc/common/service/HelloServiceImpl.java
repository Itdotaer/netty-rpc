package com.itdotaer.netty.rpc.common.service;

/**
 * HelloServiceImpl
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String input) {
        return "We have received : " + input;
    }
}
