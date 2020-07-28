package com.itdotaer.netty.rpc.client;

import com.itdotaer.netty.rpc.RegisterFactoryProducer;
import com.itdotaer.netty.rpc.models.RegisterModel;
import com.itdotaer.netty.rpc.models.RegisterType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Random;

/**
 * LoadBalance
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class LoadBalance {

    public static RegisterModel getBalancedHost(String serviceName) throws Exception {
        List<String> providers = RegisterFactoryProducer.getFactory(RegisterType.ZOOKEEPER).queryProviders(serviceName);
        if (CollectionUtils.isEmpty(providers)) {
            throw new Exception("no providers");
        }

        int randomNum = (new Random()).nextInt(providers.size());

        String hostWithPort = providers.get(randomNum);
        String[] splitArray = hostWithPort.split(":");

        RegisterModel registerModel = new RegisterModel();
        registerModel.setHost(splitArray[0]);
        registerModel.setPort(Integer.parseInt(splitArray[1]));

        return registerModel;
    }

}
