package com.leveling.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by myipp on 2018/3/30.
 */

public class JsonBuilder {
    public static JSONObject build(String... args) {
        JSONObject json = new JSONObject();
        try {
            for (int i = 0; i < args.length; i += 2) {
                json.put(args[i], args[i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String buildText(String... args) {
        JSONObject json = new JSONObject();
        try {
            for (int i = 0; i < args.length; i += 2) {
                json.put(args[i], args[i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
