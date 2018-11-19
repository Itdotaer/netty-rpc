package com.itdotaer.netty.rpc.repository;

import com.itdotaer.netty.rpc.models.RegisterModel;
import com.itdotaer.netty.rpc.models.ZKNode;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ZKRegister
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class ZKRegister implements Register {

    private static Logger logger = LoggerFactory.getLogger(ZKRegister.class);

    private String host;
    private int port;

    public ZKRegister(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean foundProvider(RegisterModel registerModel) {
        ZKNode zkNode = registerModel.toZkNode();
        try {
            ZKClient.getInstance(host, port)
                    .createEphemeral(zkNode.getPath(), zkNode.getData());
        } catch (ZkInterruptedException e) {
            logger.error("foundProvider", e);
            return false;
        } catch (IllegalArgumentException | ZkException e) {
            logger.error("foundProvider", e);
            return false;
        } catch (RuntimeException e) {
            logger.error("foundProvider", e);
            return false;
        }

        return true;
    }

    @Override
    public boolean foundConsumer(RegisterModel registerModel) {
        ZKNode zkNode = registerModel.toZkNode();
        try {
            ZKClient.getInstance(host, port)
                    .createEphemeral(zkNode.getPath(), zkNode.getData());
        } catch (ZkInterruptedException e) {
            logger.error("foundConsumer", e);
            return false;
        } catch (IllegalArgumentException | ZkException e) {
            logger.error("foundConsumer", e);
            return false;
        } catch (RuntimeException e) {
            logger.error("foundConsumer", e);
            return false;
        }

        return true;
    }

    @Override
    public List<String> queryProviders(RegisterModel registerModel) {
        ZKNode zkNode = registerModel.toQueryZkNode();
        return ZKClient.getInstance(host, port)
                .getChildren(zkNode.getPath());
    }

}
