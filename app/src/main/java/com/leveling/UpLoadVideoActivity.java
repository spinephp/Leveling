package com.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.entity.Url;
import com.leveling.ui.EasyLoading;
import com.leveling.ui.StatusControl;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class UpLoadVideoActivity extends AppCompatActivity {

    private Button video_picture, video_content;
    private LinearLayout la_video_name, la_videoimg;
    private ImageView videos;
    private TextView biaoqian;
    private String mess;
    FileInputStream fStream;
    protected static Uri tempUri;
    private static final String BEENAUTH = "BEENAUTH";
    public static String money;
    String imagePath;
    String test, dd;
    private String imageName1;
    private String newName = "image.jpg";
    LinearLayout layPer;
    private TextView textPer;
    private String video_name;
    private String title;
    private JSONObject json = new JSONObject();
    private TextView video_t;
    String filPath;
    boolean uploading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video_img);
        LinearLayout img_video_back = (LinearLayout) findViewById(R.id.img_video_back);
        la_video_name = (LinearLayout) findViewById(R.id.la_video_name);
        la_videoimg = (LinearLayout) findViewById(R.id.la_videoimg);
        videos = (ImageView) findViewById(R.id.videos);
        biaoqian = (TextView) findViewById(R.id.biaoqian);
        layPer = (LinearLayout) findViewById(R.id.lay_percent);
        textPer = (TextView) findViewById(R.id.text_percent);
        video_t = (TextView) findViewById(R.id.video_t);
        img_video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        la_video_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 100);
            }
        });
        la_videoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText inputServer = new EditText(UpLoadVideoActivity.this);
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
                AlertDialog.Builder builder = new AlertDialog.Builder(UpLoadVideoActivity.this);
                builder.setTitle("请输入视频标题").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        video_t.setText(inputServer.getText().toString());
                        if (video_t.getText().toString().equals("")) {
                            Toast.makeText(UpLoadVideoActivity.this, "请输入视频标题", Toast.LENGTH_LONG).show();
                        } else if (video_t.getText().toString().trim().length() > 15) {
                            Toast.makeText(UpLoadVideoActivity.this, "视频标题，请输入15字以内视频标题", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.btn_upload).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploading) {
                    Toast.makeText(UpLoadVideoActivity.this, "正在上传，请稍后", Toast.LENGTH_LONG).show();
                } else {
                    if (biaoqian.getText().toString().equals("")) {
                        Toast.makeText(UpLoadVideoActivity.this, "请选择视频标签", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (video_t.getText().toString().equals("")) {
                        Toast.makeText(UpLoadVideoActivity.this, "请输入视频标题", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (filPath == null) {
                        Toast.makeText(UpLoadVideoActivity.this, "请选择视频", Toast.LENGTH_LONG).show();
                        return;
                    }
                    layPer.setVisibility(View.VISIBLE);
                    textPer.setText("0%");
                    new Upload(filPath).start();
                }
            }
        });
    }

    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = Arrays.asList(new String[]{"坦克", "刺客", "法师", "射手", "战士", "辅助", "超神", "五杀", "四杀"});
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        mess = itemValue;
                        biaoqian.setText(mess);
                    }
                }).create();
        dialog.show();
    }

    Handler handlerUpdatePercent = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int per = msg.what;
            if (per == -1) {
                uploading = false;
                filPath = null;
                Toast.makeText(UpLoadVideoActivity.this, "上传失败", Toast.LENGTH_LONG);
                UpLoadVideoActivity.this.layPer.setVisibility(View.GONE);
            } else if (per == 101) {
                uploading = false;
                filPath = null;
                Toast.makeText(UpLoadVideoActivity.this, "视频上传成功，等待审核", Toast.LENGTH_LONG);
                UpLoadVideoActivity.this.layPer.setVisibility(View.GONE);
            } else {
                textPer.setText("" + per + "%");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//如果返回码可以用
            Uri uri = null;
            switch (requestCode) {
                case 100: {
                    if (null != data) {
                        uri = data.getData();
                        if (uri == null) {
                            return;
                        } else {
                            Cursor c = getContentResolver().query(uri,
                                    new String[]{MediaStore.MediaColumns.DATA}, null,
                                    null, null);
                            if (c != null && c.moveToFirst()) {
                                filPath = c.getString(0);
                                if(new File(filPath).length() > 50 * 1024 * 1024){
                                    Toast.makeText(this, "视频大小不超过50M", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                final Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filPath, MediaStore.Video.Thumbnails.MINI_KIND);
                                EasyLoading.doWork(this, "处理中，请稍后...", new EasyLoading.WorkerListener() {
                                    @Override
                                    public void run(StatusControl statusControl) {
                                        imagePath = Utils.savePhoto(bitmap,
                                                Environment.getExternalStorageDirectory().getAbsolutePath(),
                                                String.valueOf(System.currentTimeMillis()));
                                        statusControl.update("上传中，请稍后...");
                                        int ret = 0;
                                        if(uploadFile())
                                            ret = 1;
                                        else
                                            ret = 0;
                                        Message msg = new Message();
                                        msg.what = ret;
                                        msg.obj = bitmap;
                                        handlerUpdatePhotoSelect.sendMessage(msg);
                                    }
                                });
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    Handler handlerUpdatePhotoSelect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0)
                Toast.makeText(UpLoadVideoActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap photo = (Bitmap) msg.obj;
                videos.setImageBitmap(photo);
                Toast.makeText(UpLoadVideoActivity.this, "图片上传成功", Toast.LENGTH_LONG).show();
            }
        }
    };

    public class Upload extends Thread {
        String filpath;

        public Upload(String filpath) {
            super();
            this.filpath = filpath;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            super.run();
            uploadFile(filpath);
        }
    }

    //上传视频
    public void uploadFile(String imageFilePath) {
        int bufSize = 1024 * 100;
        String boundary = "******************";
        String end = "\r\n";
        String twoHyphens = "--";
        String Ticket = HttpPostUtils.getTicket();
        String actionUrl = Url.urlUploadFile + "video";
        //String actionUrl = "http://192.168.1.69:20801/api/File/PostUploadFile?catalog="+"video";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setChunkedStreamingMode(bufSize);

            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "BasicAuth" + " " + Ticket);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"headimg\";filename=\"" + "\"" + end);
            ds.writeBytes(end);
            File file = new File(imageFilePath);
            fStream = new FileInputStream(file);
            byte[] buffer = new byte[bufSize];
            int length = -1;
            long total = file.length();
            long sents = 0;
            while ((length = fStream.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
                ds.flush();
                sents += length;
                handlerUpdatePercent.sendEmptyMessage((int) ((double) sents / total * 100));
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fStream.close();
            ds.flush();
            InputStream is = con.getInputStream();
            int ch;
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            while (true) {
                int len = is.read(buffer);
                if (len > 0) {
                    bs.write(buffer, 0, len);
                }
                if (len < buffer.length)
                    break;
            }
            String strRet = new String(bs.toByteArray(), Charset.forName("utf-8"));
            JSONObject result = new JSONObject(strRet);
            String data = result.getString("Data");
            video_name = data;
            if (video_name == null || video_name.length() == 0)
                throw new Exception("上传失败");
            // handle.sendEmptyMessage(0);
            getData(json);
            String url1 = "/api/Video/PostVideo";
            HttpPostUtils.httpPostFile(12, url1, json, handle);
            ds.close();
            handlerUpdatePercent.sendEmptyMessage(101);
        } catch (Exception e) {
            e.printStackTrace();
            handlerUpdatePercent.sendEmptyMessage(-1);
        }
    }

    private void getData(JSONObject json) {
        try {
            json.put("Title", video_t.getText().toString());
            json.put("URL", video_name);
            json.put("Name", HttpPostUtils.getNickname());
            json.put("lable", mess);
            json.put("img", imageName1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 12:
                    String res1 = (String) msg.obj;
                    if (res1 == null) {
                        return;
                    } else {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res1);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            if (success == "true") {
                                Toast.makeText(UpLoadVideoActivity.this, "视频上传成功", Toast.LENGTH_LONG).show();
                                layPer.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(UpLoadVideoActivity.this, err, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    //上传文件至server的方法
    private boolean uploadFile() {
        // TODO Auto-generated method stub
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String Ticket = HttpPostUtils.getTicket();
        try {
            String actionUrl = Url.urlUploadFile + "videoIMG";
            URL url = new URL(actionUrl);//服务器地址
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            /* 设置传送的method=POST */
            con.setRequestMethod("POST");
            /* setRequestProperty *///设置请求属性
            con.setRequestProperty("Authorization", "BasicAuth" + " " + Ticket);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "utf-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            /* 设置DataOutputStream *///数据输出流
            //heading为服务器接收的键
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"headimg\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);
            /* 取得文件的FileInputStream */ //文件输入流
            FileInputStream fStream = new FileInputStream(imagePath);//要上传的图片路径，
            /* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            JSONObject result = new JSONObject(b.toString().trim());
            String data = result.getString("Data");
            imageName1 = data;
            ds.close();
            return true;
        } catch (Exception e) {
            //Toast.makeText(UpLoadVideoActivity.this, "上传失败，请检查网络", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}


