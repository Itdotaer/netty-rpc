package com.itdotaer.netty.rpc.service;

import lombok.Data;

import java.io.Serializable;

/**
 * SayHelloRequest
 *
 * @author jt_hu
 * @date 2020/7/24
 */
@Data
public class SayHelloRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;

}
