package com.leveling.new_chat;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.leveling.chat.utils.FileSaveUtil;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.Utils;
import com.yanzhenjie.nohttp.ByteArrayBinary;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by lin on 2018/3/27.
 */

public class ChatBaseActivity extends Activity {

    private static String TAG = ChatBaseActivity.class.getSimpleName();

    private static RequestQueue requestQueue = null;

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = NoHttp.newRequestQueue();
        return requestQueue;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void sendRequestByGet(String url, int index) {
        Request<byte[]> request = NoHttp.createByteArrayRequest(url, RequestMethod.GET);
        request.addHeader("Authorization", "BasicAuth " + HttpPostUtils.getTicket());
        getRequestQueue().add(index, request, ViewGetImageResponse);
    }

    public void sendRequest(String url, int index) {
        Request<JSONObject> request = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
        request.addHeader("Authorization", "BasicAuth " + HttpPostUtils.getTicket());
        getRequestQueue().add(index, request, ViewPageOnResponse);
    }

    public void sendImageRequest(String url, int index, String imagePath) {
        try {
            Request<JSONObject> request = NoHttp.createJsonObjectRequest(url, RequestMethod.POST);
            request.addHeader("Authorization", "BasicAuth " + HttpPostUtils.getTicket());
//            FileInputStream fs = new FileInputStream(imagePath);
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while (-1 != (len = fs.read(buffer))) {
//                outStream.write(buffer, 0, len);
//            }
//            outStream.close();
//            fs.close();
            request.add("images", new File(imagePath));// new ByteArrayBinary(outStream.toByteArray(), "img" + System.currentTimeMillis() + ".jpg"));
            getRequestQueue().add(index, request, ViewPageOnResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //http下载图片回复
    private OnResponseListener<byte[]> ViewGetImageResponse = new OnResponseListener<byte[]>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<byte[]> response) {
            getSuccessImageResponse(what, response);
        }

        @Override
        public void onFailed(int what, Response<byte[]> response) {
        }

        @Override
        public void onFinish(int what) {
        }
    };


    public void getSuccessImageResponse(int what, Response<byte[]> response) {

    }


    //http请求回复
    private OnResponseListener<JSONObject> ViewPageOnResponse = new OnResponseListener<JSONObject>() {
        @Override
        public void onStart(int what) {
        }

        @Override
        public void onSucceed(int what, Response<JSONObject> response) {
            getSuccessResponse(what, response);
        }

        @Override
        public void onFailed(int what, Response<JSONObject> response) {
            getFailedResponse(what, response);
        }

        @Override
        public void onFinish(int what) {

        }
    };


    public void getSuccessResponse(int what, Response<JSONObject> response) {
    }


    public void getFailedResponse(int what, Response<JSONObject> response) {

    }


}
