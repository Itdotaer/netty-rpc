package com.itdotaer.netty.rpc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ZKNode
 *
 * @author jt_hu
 * @date 2018/11/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZKNode {

    private String path;
    private Object data;

}
