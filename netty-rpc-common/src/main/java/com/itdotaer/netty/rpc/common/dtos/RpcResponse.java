package com.itdotaer.netty.rpc.common.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * RpcResponse
 *
 * @author jt_hu
 * @date 2018/9/30
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requestId;
    private String error;
    private Object result;

}
