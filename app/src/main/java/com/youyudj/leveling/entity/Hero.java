package com.youyudj.leveling.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/22.
 */
public class Hero {
    public static HashMap<String, String> heros = new HashMap<String, String>();

    public static String heroToString() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> it : heros.entrySet()) {
            sb.append(it.getKey()).append(":").append(it.getValue()).append(",");
        }
        return sb.toString();
    }

    public static String heroNameToString() {
        StringBuffer sb = new StringBuffer();
        for (String name : heros.keySet()) {
            sb.append(name).append(",");
        }
        return sb.toString();
    }
}
