package com.leveling.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by myipp on 2018/4/4.
 */

public class BitmapLoader {

    //保存bitmap到本地
    public static void saveImage(Bitmap bmp, String at, String fileName) {
        if (bmp == null || fileName == null)
            return;
        String parPath = Path.getImagePath(at);
        File path = new File(parPath);
        if (!path.exists())
            path.mkdirs();
        File file = new File(path.getAbsolutePath() + "/" + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getImageFromLocalFirstNorDownload(String url, final String at, final String fileName, final Handler handler, final int actionID) {
        final String localPath = Path.getImagePath(at, fileName);
        if (new File(localPath).exists()) {
            Message msg = new Message();
            msg.what = actionID;
            /* start of james edit */
            try {
                msg.obj = BitmapFactory.decodeFile(localPath);
            }catch (OutOfMemoryError ex){
                ex.printStackTrace();
            }
            /* end of james edit */
            handler.sendMessage(msg);
        } else {
            HttpFileHelper.httpGetFile(actionID, url, new Handler() {
                @Override
                public void handleMessage(Message m) {
                    super.handleMessage(m);
                    Bitmap bmp = null;
                    byte[] data = (byte[]) m.obj;
                    if (data != null) {
                        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (bmp != null)
                            saveImage(bmp, at, fileName);
                    }
                    Message msg = new Message();
                    msg.what = actionID;
                    msg.obj = bmp;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    public static class StatusBitmap {
        private JSONObject status;
        private Bitmap bitmap;

        public JSONObject getStatus() {
            return status;
        }

        public void setStatus(JSONObject status) {
            this.status = status;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }
    }

    public static void getImageFromLocalFirstNorDownloadWithStatusResponse(String url, final String at, final String fileName, final Handler handler, final int actionID) {
        final String localPath = Path.getImagePath(at, fileName);
        if (new File(localPath).exists()) {
            StatusBitmap sb = new StatusBitmap();
            sb.setStatus(null);
            /* start of james edit */
            try {
                sb.setBitmap(BitmapFactory.decodeFile(localPath));
            }catch (OutOfMemoryError ex){
                ex.printStackTrace();
            }
            /* end of james edit */
            Message msg = new Message();
            msg.what = actionID;
            msg.obj = sb;
            handler.sendMessage(msg);
        } else {
            HttpFileHelper.httpGetFile(actionID, url, new Handler() {
                @Override
                public void handleMessage(Message m) {
                    super.handleMessage(m);
                    Bitmap bmp = null;
                    byte[] data = (byte[]) m.obj;
                    String statusText = null;
                    int imageDataFrom = 0;
                    for (int i = 0; i < data.length; i++) {
                        if (data[i] == '\n') {
                            statusText = new String(data, 0, i);
                            imageDataFrom = i + 1;
                            break;
                        }
                    }
                    JSONObject status = null;
                    if (statusText != null) {
                        try {
                            status = new JSONObject(statusText);
                            if (status.getString("Succ") != null || status.getBoolean("Succ")) {
                                String fileName = status.getString("Data");
                                bmp = BitmapFactory.decodeByteArray(data, imageDataFrom, data.length - imageDataFrom);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    StatusBitmap sb = new StatusBitmap();
                    sb.setStatus(status);
                    sb.setBitmap(bmp);
                    if (bmp != null)
                        saveImage(bmp, at, fileName);
                    Message msg = new Message();
                    msg.what = actionID;
                    msg.obj = sb;
                    handler.sendMessage(msg);
                }
            });
        }
    }
}
