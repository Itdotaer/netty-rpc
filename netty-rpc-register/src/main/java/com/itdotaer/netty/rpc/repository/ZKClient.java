package com.itdotaer.netty.rpc.repository;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZKRegister
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class ZKRegister {

    private static Logger logger = LoggerFactory.getLogger(ZKRegister.class);

    private static final int DEFAULT_TIME_OUT = 5000;
    private volatile ZkClient zkClient;

    public ZkClient getInstance(String host, int port) {
        if (zkClient == null) {
            synchronized (zkClient) {
                if (zkClient == null) {
                    zkClient = new ZkClient(host + ":" + port, DEFAULT_TIME_OUT);
                }
            }
        }

        return zkClient;
    }

    public boolean close() {

        try {
            zkClient.close();
            zkClient = null;
        } catch (ZkInterruptedException ex) {
            logger.error("ZKClient close failed", ex);
            return false;
        }

        return true;
    }

}
