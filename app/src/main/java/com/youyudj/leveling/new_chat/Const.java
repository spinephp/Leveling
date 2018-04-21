package com.youyudj.leveling.new_chat;

import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.utils.HttpPostUtils;

/**
 * Created by lin on 2018/3/27.
 */

public class Const {

    public static String BASE_URL = Url.urlIM + "/Chat/";

    public static String LOGIN = BASE_URL + "Login";

    public static String CREATE_SESSION = BASE_URL + "CreateSession";

    public static String SEND = BASE_URL + "Send";

    public static String SEND_IMAGE = BASE_URL + "SendImage";

    public static String GET_IMAGE = BASE_URL + "GetImage";

    public static String QUERY = BASE_URL + "Query";

    public static String CLOSE_SESSION = BASE_URL + "CloseSession";

    public static String QUERY_LIST = BASE_URL + "QueryList";

    public static String GET_FRIEND_LIST = BASE_URL+"GetFriendList";

    public static String getQueryString(String baseURL, String... args) {
        StringBuffer url = new StringBuffer();
        url.append(baseURL);
        if (args.length > 0) {
            url.append("?" + args[0] + "=" + args[1]);
        }
        for (int i = 2; i < args.length; i += 2) {
            url.append("&" + args[i] + "=" + args[i + 1]);
        }
        return url.toString();
    }

    public static String getQueryStringWithToken(String baseURL, String... args) {
        StringBuffer url = new StringBuffer();
        url.append(baseURL);
        url.append("?token=" + IMSession.getInstance().getToken());
        for (int i = 0; i < args.length; i += 2) {
            url.append("&" + args[i] + "=" + args[i + 1]);
        }
        return url.toString();
    }

    public static String getQueryString(String baseURL, Object... args) {
        StringBuffer url = new StringBuffer();
        url.append(baseURL);
        if (args.length > 0) {
            url.append("?" + args[0].toString() + "=" + args[1].toString());
        }
        for (int i = 2; i < args.length; i += 2) {
            url.append("&" + args[i].toString() + "=" + args[i + 1].toString());
        }
        return url.toString();
    }

    public static String getQueryStringWithToken(String baseURL, Object... args) {
        StringBuffer url = new StringBuffer();
        url.append(baseURL);
        url.append("?token=" + IMSession.getInstance().getToken());
        for (int i = 0; i < args.length; i += 2) {
            url.append("&" + args[i].toString() + "=" + args[i + 1].toString());
        }
        return url.toString();
    }

}
