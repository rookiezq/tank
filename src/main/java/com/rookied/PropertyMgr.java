package com.rookied;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zhangqiang
 * @date 2021/4/25
 */
public class PropertyMgr {
    static Properties props = new Properties();
    static{
        try {
            props.load(PropertyMgr.class.getClassLoader().getResourceAsStream("config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return (String)props.get(key);
    }

    public static void main(String[] args) {
        System.out.println(PropertyMgr.get("initTankCount"));
    }
}
