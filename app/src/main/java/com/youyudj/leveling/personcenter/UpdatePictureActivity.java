package com.youyudj.leveling.personcenter;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youyudj.leveling.Authentication1Activity;
import com.youyudj.leveling.R;
import com.youyudj.leveling.UpLoadVideoActivity;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.ui.EasyLoading;
import com.youyudj.leveling.ui.StatusControl;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.youyudj.leveling.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdatePictureActivity extends AppCompatActivity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private UserInfo userInfo;
    String imagePath;
    private String newName = "image.jpg";
    private String actionUrl = "http://47.100.31.245:8082/api/File/PostUploadFile?catalog="+"userhead";
    FileInputStream fStream;
    private ImageView change_img;
    private Button change_img_btn;
    private LinearLayout img_pic_back;
    private JSONObject json = new JSONObject();
    private String tt;
    private static final String PICTURE = "picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_picture);
        userInfo = new UserInfo(this);
        change_img_btn = (Button) findViewById(R.id.change_img_btn);
        change_img = (ImageView) findViewById(R.id.change_img);
        img_pic_back = (LinearLayout) findViewById(R.id.img_pic_back);
        img_pic_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change_img_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                showChoosePicDialog();
            }
        });
    }

    public void showChoosePicDialog() {
        // TODO Auto-generated method stub
        Builder builder = new Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片","拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dailog, int witch) {
                // TODO Auto-generated method stub
                switch(witch){
                    case CHOOSE_PICTURE://选择本地照片
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE://拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(),"image.jpg"));
                        //指定图片保存路径，image.jpg为一个临时文件，每次拍照后都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                }
            }
        });
        builder.create().show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {//如果返回码可以用
            Uri uri = null;
            switch (requestCode) {
                case TAKE_PICTURE:{
                    uri = tempUri;
                    //startPhotoZoom(tempUri);
                    break;
                }
                case CHOOSE_PICTURE: {
                    uri = data.getData();
                    //startPhotoZoom(data.getData());//裁剪图片
                    break;
                }
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data);//将裁剪的图片显示在界面上
                    }
                    break;
                default:
                    break;
            }
            if(uri != null) {
                final Uri finalUri = uri;
                EasyLoading.doWork(this, "处理中，请稍后...", new EasyLoading.WorkerListener() {
                    @Override
                    public void run(StatusControl statusControl) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(finalUri));
                            //Bitmap bmp1 = Utils.toRoundBitmap(bitmap); // 这个时候的图片已经被处理成圆形的了
                            //bitmap.recycle();
                            statusControl.update("上传中，请稍后...");
                            imagePath = Utils.savePhoto(bitmap,
                                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    String.valueOf(System.currentTimeMillis()));
                            uploadFile();
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmap;
                            handlerUpdatePhotoSelect.sendMessage(msg);
                        } catch (FileNotFoundException e) {
                            handlerUpdatePhotoSelect.sendEmptyMessage(0);
                        }
                    }
                });
            }
        }
    }

    Handler handlerUpdatePhotoSelect = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0)
                Toast.makeText(UpdatePictureActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else{
                Bitmap photo = (Bitmap)msg.obj;
                change_img.setImageBitmap(photo);
                //uploadPic(photo);
            }
        }
    };

    /***
     * 裁剪图片方法
     * */

    private void startPhotoZoom(Uri uri) {
        // TODO Auto-generated method stub
        if(uri==null){
            Log.i("tag", "the uri is not exist");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }
    /**
     * 保存裁剪后的图片
     * **/
    private void setImageToView(Intent data) {
        // TODO Auto-generated method stub
        Bundle extras = data.getExtras();
        if(extras!=null){
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            change_img.setImageBitmap(photo);
            uploadPic(photo);
        }
    }


    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
        imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            //imagePath上传了
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    //上传文件至server的方法
    private void uploadFile() {
        // TODO Auto-generated method stub
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        String Ticket = HttpPostUtils.getTicket();
        try {

            URL url = new URL(actionUrl);//服务器地址
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设置传送的method=POST */
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "BasicAuth" + " "+Ticket);
			/* setRequestProperty *///设置请求属性
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
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
            fStream = new FileInputStream(imagePath);//要上传的图片路径，
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
			/* 将Response显示于Dialog */
            JSONObject result = new JSONObject(b.toString().trim());
            String data = result.getString("Data");
            getData(json);
            tt = data;
            String url1 = "api/Users/PostUpdatePicture";
            HttpPostUtils.httpPostFile(999,url1,json,handler);
            userInfo.setUserInfo(PICTURE,data);
            //showDialog("上传结果" + b.toString().trim());
            Log.e("文件传输结果", b.toString().trim());
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            showDialog("上传失败" + e);
            Log.i("败", "错误原因：" + e);
        }
    }

    private void getData(JSONObject json) {
        try {
            json.put("piceture",tt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable(){
        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();//创建消息循环

            uploadFile();
            Message msg = new Message();
            handler.sendMessage(msg);
            Looper.loop();//从消息队列取消
        }
    };
      private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 999:
                    String res = (String) msg.obj;
                    try {
                        JSONObject result = new JSONObject(res);
                        if (res==null){
                            return;
                        }
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        if (success=="true"){
                            Toast.makeText(UpdatePictureActivity.this, "头像更换成功", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(UpdatePictureActivity.this, "头像更换失败", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        };
    };
    /* 显示Dialog的method */
    private void showDialog(String mess) {
        new Builder(this).setTitle("Message").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
//
}

