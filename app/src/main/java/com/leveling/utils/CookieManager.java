package com.youyudj.leveling.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
/**
 * Created by Administrator on 2017/12/6.
 */

public class CookieManager {

    static Map<String, String> cookies = new HashMap<String, String>();

    static void updateCookie(HttpResponse resp){
        Header[] headers = resp.getHeaders("SET-COOKIE");
        if(headers.length == 0)
            return;
        Header h = headers[0];
        String strCK = h.getValue();
        String[] items = strCK.split(";");
        for(int i = 0; i < items.length; i++){
            String ck = items[i];
            int sp = ck.indexOf('=');
            if(sp == -1)
                return;
            String name = ck.substring(0, sp);
            String value = ck.substring(sp + 1);
            if(!cookies.containsKey(name)){
                cookies.put(name, value);
            }
        }
    }

    static String getCookie(){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> pair : cookies.entrySet()) {
            sb.append(pair.getKey()).append("=").append(pair.getValue()).append(";");
        }
        if(sb.length() > 0)
            sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
