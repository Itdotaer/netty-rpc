package com.itdotaer.netty.rpc.repository;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZKClient
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class ZKClient {

    private static Logger logger = LoggerFactory.getLogger(ZKClient.class);

    private static final int DEFAULT_TIME_OUT = 5000;
    private static volatile ZkClient zkClient;

    public static ZkClient getInstance(String host, int port) {
        if (zkClient == null) {
            synchronized (ZKClient.class) {
                if (zkClient == null) {
                    zkClient = new ZkClient(host + ":" + port, DEFAULT_TIME_OUT);
                }
            }
        }

        return zkClient;
    }

    public static boolean close() {
        try {
            zkClient.close();
            zkClient = null;
        } catch (ZkInterruptedException e) {
            logger.error("ZKClient close failed", e);
            return false;
        }

        return true;
    }

}
