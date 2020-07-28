package com.itdotaer.netty.rpc;

import com.itdotaer.netty.rpc.models.RegisterModel;
import com.itdotaer.netty.rpc.models.ZKNode;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ZKRegisterFactory
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class ZKRegisterFactory extends AbstractRegisterFactory {

    private static final Logger logger = LoggerFactory.getLogger(ZKRegisterFactory.class);
    /**
     * 正常在线服务
     */
    private final static byte[] PROVIDER_ONLINE = new byte[] { 1 };


    /**
     * found provider
     *
     * @param serviceName
     * @param host
     * @param port
     * @return
     */
    @Override
    public boolean foundProvider(String serviceName, String host, int port) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);
        registerModel.setHost(host);
        registerModel.setPort(port);

        ZKNode zkNode = registerModel.toZkNode();
        try {
            ZKClient.getInstance().create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(zkNode.getPath(), PROVIDER_ONLINE);
        } catch (IllegalArgumentException e) {
            logger.error("foundProvider", e);
            return false;
        } catch (RuntimeException e) {
            logger.error("foundProvider", e);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * found consumer
     *
     * @param serviceName
     * @param host
     * @param port
     * @return
     */
    @Override
    public boolean foundConsumer(String serviceName, String host, int port) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);
        registerModel.setHost(host);
        registerModel.setPort(port);
        registerModel.setIsProvider(false);

        ZKNode zkNode = registerModel.toZkNode();

        try {
            ZKClient.getInstance().create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(zkNode.getPath(), PROVIDER_ONLINE);
        } catch (IllegalArgumentException e) {
            logger.error("foundConsumer", e);
            return false;
        } catch (RuntimeException e) {
            logger.error("foundConsumer", e);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * query all providers for load balance
     *
     * @param serviceName
     * @return
     */
    @Override
    public List<String> queryProviders(String serviceName) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);

        ZKNode zkNode = registerModel.toQueryZkNode();

        try {
            return ZKClient.getInstance()
                    .getChildren().forPath(zkNode.getPath());
        } catch (Exception e) {
            logger.error("queryProviders", e);
        }

        return null;
    }
}
