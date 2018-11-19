package com.itdotaer.netty.rpc.repository;

import com.itdotaer.netty.rpc.models.RegisterModel;

import java.util.List;

/**
 * Register
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public interface Register {

    boolean foundProvider(RegisterModel registerModel);

    boolean foundConsumer(RegisterModel registerModel);

    List<String> queryProviders(RegisterModel registerModel);

}
