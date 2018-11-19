package com.itdotaer.netty.rpc.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * RegisterModel
 *
 * @author jt_hu
 * @date 2018/11/19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel {

    private String serviceName;
    private String host;
    private Integer port;
    private Boolean isProvider = true;
    private Date createTime;

    public ZKNode toZkNode() {
        ZKNode zkNode = new ZKNode();

        zkNode.setPath("/netty-rpc/" + serviceName + "/" + (isProvider ? "providers" : "consumers") + "/" + host + ":" + port);
        zkNode.setData(createTime);

        return zkNode;
    }

    public ZKNode toQueryZkNode() {
        ZKNode zkNode = new ZKNode();

        zkNode.setPath("/netty-rpc/" + serviceName + "/" + (isProvider ? "providers" : "consumers"));

        return zkNode;
    }

}
