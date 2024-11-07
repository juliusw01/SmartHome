package com.katzenklappe.smartHome.misc;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectSH {
    public static String findConection() throws UnknownHostException{
        String sh = "smarthome0";
        for(int i = 1; i < 10; i++ ) {
            try {
                String host = sh + i;
                InetAddress shConnection = InetAddress.getByName(host);
                if (shConnection.isReachable(10000)) {
                    System.out.println("Found smart home device at " + shConnection.getHostAddress());
                    return shConnection.getHostAddress();
                }
            } catch (UnknownHostException e) {
                e.getMessage();
            } catch (Exception e) {
                e.getMessage();
            }
        }
        throw new UnknownHostException("Smart Home device could not be found");
    }
}
