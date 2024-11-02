package com.katzenklappe.smartHome.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Secrets {

    public static String getPASSWORD() {
        System.out.println("Retrieving PW");
        return getProperty("PASSWORD");
    }

    public static String getUSERNAME() {
        System.out.println("Retrieving Username");
        return getProperty("USERNAME");
    }

    public static String getAUTH_TOKEN(){
        System.out.println("Retrieving API Key");
        return getProperty("AUTH_TOKEN");
    }

    private static String getProperty(String propName){
        try {
            Properties prop = new Properties();
            FileReader reader = new FileReader("secrets.properties");
            prop.load(reader);
            reader.close();
            System.out.println("Property: " + prop.getProperty(propName));
            return prop.getProperty(propName);
        }catch (IOException e){
            System.out.println("Error retrieving: ");
            System.out.println(e.getMessage());
            return e.getMessage();
        }
    }
}
