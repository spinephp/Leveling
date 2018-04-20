package com.youyudj.leveling.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.youyudj.leveling.entity.Url;

public class Utilsp {

	interface IRunner{
		Object run(int actionID, String url, Handler handler, Handler handlerProgress) throws Exception;
	}
	
	static class HttpGetRunner implements IRunner{
		@Override
		public Object run(int actionID, String url, Handler handler, Handler handlerProgress)
				throws Exception {
			Object ret = null;
			try{
				String fullURL = url;
				if (!url.startsWith("http://") && !url.startsWith("https://"))
					fullURL = Url.urlShort + url;
				HttpGet httpGet = new HttpGet(fullURL);
				HttpClient client = new DefaultHttpClient();	
				String Ticket = HttpPostUtils.getTicket();
				httpGet.addHeader("Authorization",  "BasicAuth"+" "+Ticket);
				httpGet.addHeader("Cookie", CookieManager.getCookie());
				HttpResponse response = client.execute(httpGet);
				
				CookieManager.updateCookie(response);
				int statusCode = response.getStatusLine().getStatusCode();
				if(statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_BAD_REQUEST){
					//String res = EntityUtils.toString(response.getEntity(), "UTF-8");
					long totalLength = response.getEntity().getContentLength();
					InputStream is = response.getEntity().getContent();
					BufferedInputStream bis = new BufferedInputStream(is, 1024 * 8);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					int len = 0;
					int nowPer = -1;
					long readTotal = 0;
					byte[] buffer = new byte[1024 * 4];
					//--apk
					FileOutputStream fos = null;
					//fos = new FileOutputStream(new File("/sdcard/" + "Leveling.apk"));
					fos = new FileOutputStream("/sdcard/" + "Leveling.apk");
					//--
					while ((len = bis.read(buffer)) != -1) {
						//下载apk
						fos.write(buffer, 0, len);

						out.write(buffer, 0, len);
						readTotal += len;
						if(totalLength > 0 && handlerProgress != null) {
							int per = (int) ((double) readTotal / totalLength * 100);
							if(nowPer != per) {
								handlerProgress.sendEmptyMessage(per);
								nowPer = per;
							}
						}
					}
					//--
					//fos.flush();
					//--
					ret = out.toByteArray();
					bis.close();
					out.close();
					//--
					fos.close();
				}else{
					throw new Exception("Invalid status code " + statusCode);
				}
			}catch(Exception e){
				throw e;
			}
			return ret;
		}
	}
	static void Execute(final int actionID, final String url, final Handler handler, final Handler handlerProgress, final IRunner runner){
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stu
				int actID = actionID;
				Object ret = null;
				try {
					super.run();
					ret = runner.run(actionID, url, handler, handlerProgress);
				} catch (Exception e) {
					actID = -actionID;
					ret = e;
				}
				Message msg = new Message();
				msg.what = actID;
				msg.obj = ret;
				handler.sendMessage(msg);
			}
		}.start();
	}
	public static void httpGetFile(int actionID, String url, Handler handler)
	{
		IRunner runner = new HttpGetRunner();
		Execute(actionID, url, handler, null, runner);
	}

	public static void httpGetFileWithProgress(String url, Handler handler)
	{
		IRunner runner = new HttpGetRunner();
		Execute(101, url, handler, handler, runner);
	}
}
