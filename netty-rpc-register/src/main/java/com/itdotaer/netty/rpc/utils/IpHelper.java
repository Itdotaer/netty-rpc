package com.itdotaer.netty.rpc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * IpHelper
 *
 * @author jt_hu
 * @date 2018/11/19
 */
public class IpHelper {

    private static Logger logger = LoggerFactory.getLogger(IpHelper.class);

    public static String getLocalAddress(){
        String ip = "";

        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("getLocalAddress", e);
        }

        return ip;
    }

}
