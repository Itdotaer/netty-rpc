package com.itdotaer.netty.rpc.utils;

import com.itdotaer.netty.rpc.models.RegisterModel;
import com.itdotaer.netty.rpc.repository.ZKRegister;

import java.util.List;

/**
 * RegisterHelper
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class RegisterHelper {

    private static volatile ZKRegister zkRegister;

    public static ZKRegister getZKRegister() {
        if (zkRegister == null) {
            synchronized (RegisterHelper.class) {
                if (zkRegister == null) {
                    zkRegister = new ZKRegister("127.0.0.1", 2181);
                }
            }
        }

        return zkRegister;
    }

    public static void foundProvider(String serviceName, String host, int port) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);
        registerModel.setHost(host);
        registerModel.setPort(port);

        getZKRegister().foundProvider(registerModel);
    }

    public static void foundConsumer(String serviceName, String host, int port) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);
        registerModel.setHost(host);
        registerModel.setPort(port);
        registerModel.setIsProvider(false);

        getZKRegister().foundConsumer(registerModel);
    }

    public static List<String> queryProviders(String serviceName) {
        RegisterModel registerModel = new RegisterModel();
        registerModel.setServiceName(serviceName);
        return getZKRegister().queryProviders(registerModel);
    }

}
