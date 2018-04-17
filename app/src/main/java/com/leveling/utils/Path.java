package com.leveling.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by myipp on 2018/4/4.
 */

public class Path {
    public static String getImagePath(String at){
        File root = new File(Environment.getExternalStorageDirectory(), "/youyudj/Image/" + at);
        return root.getAbsolutePath();
    }

    public static String getImagePath(String at, String fileName){
        File root = new File(Environment.getExternalStorageDirectory(), "/youyudj/Image/" + at + "/" + fileName);
        return root.getAbsolutePath();
    }
}
