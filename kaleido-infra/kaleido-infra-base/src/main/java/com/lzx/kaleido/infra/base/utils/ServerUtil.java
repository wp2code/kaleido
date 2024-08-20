package com.lzx.kaleido.infra.base.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author lwp
 * @date 2024-07-11
 **/
@Slf4j
@UtilityClass
public class ServerUtil {
    
    /**
     * @return
     */
    public String getServerIp() {
        final String sysType = System.getProperties().getProperty("os.name");
        String ip = null;
        if (sysType.toLowerCase().startsWith("win")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            ip = getIpByEthNum("eth0");
        }
        return ip;
    }
    
    @SuppressWarnings("rawtypes")
    private String getIpByEthNum(String ethNum) {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual()) {// 如果是回环和虚拟网络地址的话继续
                    continue;
                }
                if (ethNum.equals(netInterface.getName())) {
                    Enumeration<InetAddress> addressList = netInterface.getInetAddresses();
                    while (addressList.hasMoreElements()) {
                        InetAddress address = addressList.nextElement();
                        if (address instanceof Inet4Address) {
                            ip = address;
                            break;
                        }
                    }
                }
                if (ip != null) {
                    break;
                }
            }
            if (ip != null) {
                return ip.getHostAddress();
            } else {
                return "127.0.0.1";
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
            return "127.0.0.1";
        }
    }
}
