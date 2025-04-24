package com.katzenklappe.smartHome.misc;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class ConnectSH {
    public static String findConection() throws UnknownHostException{
        String sh = "smarthome0";
        for(int i = 1; i < 10; i++ ) {
            try {
                String host = sh + i;
                InetAddress shConnection = InetAddress.getByName(host);
                log.info("Host: " + InetAddress.getByName(host));
                if (shConnection.isReachable(10000)) {
                    log.info("Found smart home device at " + shConnection.getHostAddress());
                    return shConnection.getHostAddress();
                }
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        log.error("No smart home device detected");
        throw new UnknownHostException("Smart Home device could not be found");
    }
}
