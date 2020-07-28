package com.itdotaer.netty.rpc;

import com.itdotaer.netty.rpc.common.constant.Constants;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * ZKClient
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class ZKClient implements Closeable {

    private static Logger logger = LoggerFactory.getLogger(ZKClient.class);

    private static final int DEFAULT_TIME_OUT = 5000;
    private static volatile CuratorFramework zkClient;

    public static CuratorFramework getInstance() {
        if (zkClient == null) {
            synchronized (ZKClient.class) {
                if (zkClient == null) {
                    String host = System.getProperty(Constants.ZOOKEEPER_HOST_PROP);
                    String port = System.getProperty(Constants.ZOOKEEPER_PORT_PROP);

                    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
                    CuratorFrameworkFactory.Builder zkClientBuilder = CuratorFrameworkFactory.builder()
                            .connectString(host + ":" + port)
                            .sessionTimeoutMs(DEFAULT_TIME_OUT * 30)
                            .connectionTimeoutMs(DEFAULT_TIME_OUT * 3)
                            .canBeReadOnly(false)
                            .retryPolicy(retryPolicy)
                            .defaultData(null);

                    zkClient = zkClientBuilder.build();

                    zkClient.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                        @Override
                        public void stateChanged(CuratorFramework client, ConnectionState newState) {

                            if (logger.isInfoEnabled()) {
                                logger.info("reconnect to zookeeper,recover provider and consumer data");
                            }
                        }
                    });

                    start();
                }
            }
        }

        return zkClient;
    }

    public static synchronized boolean start() {
        if (zkClient == null) {
            logger.warn("Start zookeeper registry must be do init first!");
            return false;
        }
        if (zkClient.getState() == CuratorFrameworkState.STARTED) {
            return true;
        }
        try {
            zkClient.start();
        } catch (Exception e) {
            logger.error("Start zookeeper exception", e);
        }
        return zkClient.getState() == CuratorFrameworkState.STARTED;
    }

    @Override
    public void close() {
        if (zkClient != null && zkClient.getState() == CuratorFrameworkState.STARTED) {
            zkClient.close();
        }
    }
}
