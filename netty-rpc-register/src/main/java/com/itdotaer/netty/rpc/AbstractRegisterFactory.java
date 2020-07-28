package com.itdotaer.netty.rpc;

import java.util.List;

/**
 * AbstractRegisterFactory
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public abstract class AbstractRegisterFactory {

    /**
     * found provider
     *
     * @param serviceName
     * @param host
     * @param port
     * @return
     */
    public abstract boolean foundProvider(String serviceName, String host, int port);

    /**
     * found consumer
     *
     * @param serviceName
     * @param host
     * @param port
     * @return
     */
    public abstract boolean foundConsumer(String serviceName, String host, int port);

    /**
     * query all providers for load balance
     *
     * @param serviceName
     * @return
     */
    public abstract List<String> queryProviders(String serviceName);

}
