package com.katzenklappe.smartHome.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Secrets {

    public static String getPASSWORD() {
        return getProperty("PASSWORD");
    }

    public static String getUSERNAME() {
        return getProperty("USERNAME");
    }

    public static String getAUTH_TOKEN(){
        return getProperty("AUTH_TOKEN");
    }

    private static String getProperty(String propName){
        try {
            Properties prop = new Properties();
            FileReader reader = new FileReader("secrets.properties");
            prop.load(reader);
            reader.close();
            return prop.getProperty(propName);
        }catch (IOException e){
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}
