package com.youyudj.leveling.common;

import android.os.Handler;
import android.os.Message;

import com.youyudj.leveling.utils.HttpPostUtils;
import com.net.cookie.Objects;

/**
 * Created by myipp on 2018/3/30.
 */

public class SystemLogHelper {
    static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static void Error(String text){
        String url = "/api/SystemErrorLog/Error";
        HttpPostUtils.httpPostFile(0, url, JsonBuilder.build("ErrorText", text), handler);
    }

    public static void Error(Exception ex){
        if(ex != null) {
            String url = "/api/SystemErrorLog/Error";
            HttpPostUtils.httpPostFile(0, url, JsonBuilder.build("ErrorText", ex.toString()), handler);
        }
    }

    public static void Error(Object obj){
        if(obj != null) {
            String url = "/api/SystemErrorLog/Error";
            HttpPostUtils.httpPostFile(0, url, JsonBuilder.build("ErrorText", obj.toString()), handler);
        }
    }

    public static void Debug(String text){
        String url = "/api/SystemErrorLog/Debug";
        HttpPostUtils.httpPostFile(0, url, JsonBuilder.build("ErrorText", text), handler);
    }

    public static void Info(String text){
        String url = "/api/SystemErrorLog/Info";
        HttpPostUtils.httpPostFile(0, url, JsonBuilder.build("ErrorText", text), handler);
    }
}
