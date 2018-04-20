package com.youyudj.leveling.utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.os.Message;

import com.youyudj.leveling.entity.Url;

public class HttpGetUtils {
    interface IRunner {
        Object run(int actionID, String url, Handler handler) throws Exception;
    }

    static class HttpGetRunner implements HttpGetUtils.IRunner {
        @Override
        public Object run(int actionID, String url, Handler handler)
                throws Exception {
            String res = null;
            try {
                String fullURL = url;
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    fullURL = Url.urlShort + url;
                HttpGet httpGet = new HttpGet(fullURL);
                HttpClient client = new DefaultHttpClient();
                String Ticket = HttpPostUtils.getTicket();
                httpGet.addHeader("Authorization", "BasicAuth " + Ticket);
                httpGet.addHeader("Cookie", CookieManager.getCookie());
                HttpResponse response = client.execute(httpGet);
                CookieManager.updateCookie(response);
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST) {
                    res = EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } catch (Exception e) {
                throw e;
            }
            return res;
        }
    }

    static void Execute(final int actionID, final String url, final Handler handler, final HttpGetUtils.IRunner runner) {
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stu
                int actID = actionID;
                Object res = null;
                try {
                    super.run();
                    res = runner.run(actionID, url, handler);
                } catch (Exception e) {
                    actID = -actionID;
                }
                Message msg = new Message();
                msg.what = actID;
                msg.obj = res;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public static void httpGetFile(int actionID, String url, Handler handler) {
        IRunner runner = new HttpGetRunner();
        Execute(actionID, url, handler, runner);
    }
}



