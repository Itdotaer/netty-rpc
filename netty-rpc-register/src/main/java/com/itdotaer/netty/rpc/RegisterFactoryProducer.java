package com.itdotaer.netty.rpc;

import com.itdotaer.netty.rpc.models.RegisterType;

/**
 * RegisterProducer
 *
 * @author jt_hu
 * @date 2020/7/24
 */
public class RegisterFactoryProducer {

    public static AbstractRegisterFactory getFactory(RegisterType registerType) {
        AbstractRegisterFactory factory;

        switch (registerType) {
            default:
                factory = new ZKRegisterFactory();
                break;
        }

        return factory;
    }

}
