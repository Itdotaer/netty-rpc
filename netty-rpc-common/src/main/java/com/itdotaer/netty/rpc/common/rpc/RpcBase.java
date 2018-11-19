package com.itdotaer.netty.rpc.common.rpc;

/**
 * RpcBase
 *
 * @author jt_hu
 * @date 2018/9/30
 */
public interface RpcBase {

    void prepareWorkGroup();

    void start(String serviceName, String address, int port);

    void stop();

}
