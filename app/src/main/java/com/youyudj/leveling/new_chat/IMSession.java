package com.youyudj.leveling.new_chat;

import android.os.Handler;
import android.os.Message;

import com.youyudj.leveling.chat.app.ChatApplication;
import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by myipp on 2018/4/4.
 */

public class IMSession implements Runnable {

    public interface OnMessageArrivalListener {
        void onMessageArrival(JSONArray msgArr);
    }

    public interface OnLoginResultListener {
        void onLoginResult(String token);
    }

    Semaphore semaphore = null;
    List<OnMessageArrivalListener> listeners = new ArrayList<>();
    OnLoginResultListener loginResultListener;
    String token = null;
    Thread queryThread = null;

    static IMSession instance;

    public static IMSession getInstance() {
        if (instance == null)
            instance = new IMSession();
        return instance;
    }

    public void setLoginResultListener(OnLoginResultListener loginResultListener) {
        this.loginResultListener = loginResultListener;
    }

    public IMSession() {
    }

    private void runQuery() {
        queryThread = new Thread(this);
        queryThread.start();
    }

    public void stopQuery() {
        if (queryThread != null) {
            if (semaphore != null)
                semaphore.release();
            queryThread.interrupt();
        }
    }

    public String getToken() {
        return token;
    }

    public boolean login(String phone, String pass) {
        try {
            JSONObject data = new JSONObject();
            data.put("phone", phone);
            data.put("pass", pass);
            HttpPostUtils.httpPostFile(0, Const.LOGIN, data, handler);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addOnMessageArrivalListener(OnMessageArrivalListener lis) {
        synchronized (listeners) {
            listeners.add(lis);
        }
    }

    public void removeOnMessageArrivalListener(OnMessageArrivalListener lis) {
        synchronized (listeners) {
            listeners.remove(lis);
        }
    }

    @Override
    public void run() {
        while (queryThread!=null && !queryThread.isInterrupted()) {
            long st = System.currentTimeMillis();
            doWork();
            long ct = 1000 - (System.currentTimeMillis() - st);
            if (ct > 0) {
                try {
                    Thread.sleep(ct);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        queryThread = null;
    }

    void doWork() {
        semaphore = new Semaphore(0);
        try {
            if (HttpPostUtils.getTicket() != null) {
                String url = Const.getQueryStringWithToken(Const.QUERY);
                HttpPostUtils.httpPostFile(1, url, "", handler);
                semaphore.tryAcquire(60, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonText = (String) msg.obj;
            if (jsonText == null)
                return;
            JSONObject json = null;
            try {
                json = new JSONObject(jsonText);
                if (msg.what == 0) {
                    JSONObject data = json.getJSONObject("Data");
                    token = data.getString("Token");
                    runQuery();
                    if (loginResultListener != null)
                        loginResultListener.onLoginResult(token);
                } else if (msg.what == 1) {
                    if (!json.isNull("Data")) {
                        JSONArray msgArr = json.getJSONArray("Data");
                        for (OnMessageArrivalListener lis : listeners) {
                            try {
                                lis.onMessageArrival(msgArr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (semaphore != null)
                    semaphore.release();
            } catch (JSONException e) {
                if(msg.what == 0){
                    if (loginResultListener != null)
                        loginResultListener.onLoginResult(null);
                }
                e.printStackTrace();
            }
        }
    };
}
