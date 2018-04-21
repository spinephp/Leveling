package com.youyudj.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.BeaterOrder;
import com.youyudj.leveling.entity.Url;
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

public class DashouChedanActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tuikuan_id;
    private EditText tuikuan_reason;
    private ImageView zj1,zj2,zj3;
    private LinearLayout img_tk_back;
    private Button player_conform_tj;
    private String msg;
    private String s;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static final String BEENAUTH = "BEENAUTH";
    String imagePath;
    String test,dd ;
    private String newName = "image.jpg";
    private String imageName1,imageName2,imageName3;
    private JSONObject json = new JSONObject();
    public static String ts;
    private String iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashou_chedan);
        tuikuan_id = (TextView) findViewById(R.id.ds_tuikuan_id);
        tuikuan_id.setText(Beater_Order_Activity.od);
        img_tk_back = (LinearLayout) findViewById(R.id.img_dstk_back);
        Intent intent = getIntent();
        iv = intent.getStringExtra("orderid");
        img_tk_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashouChedanActivity.this,Sure_Order_Activity.class);
                intent.putExtra("orderid",iv);
                startActivity(intent);
                finish();
            }
        });
        tuikuan_reason = (EditText) findViewById(R.id.ds_tuikuan_reason);
        zj1 = (ImageView) findViewById(R.id.ds_zj1);
        zj2 = (ImageView) findViewById(R.id.ds_zj2);
        zj3 = (ImageView) findViewById(R.id.ds_zj3);
        player_conform_tj = (Button) findViewById(R.id.ds_player_conform_tj);
        zj1.setOnClickListener(this);
        zj2.setOnClickListener(this);
        zj3.setOnClickListener(this);
        player_conform_tj.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DashouChedanActivity.this,Sure_Order_Activity.class);
        intent.putExtra("orderid",iv);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ds_zj1:
                s = "1";
                showChoosePicDialog();
                break;
            case R.id.ds_zj2:
                s = "2";
                showChoosePicDialog();
                break;
            case R.id.ds_zj3:
                s = "3";
                showChoosePicDialog();
                break;
            case R.id.ds_player_conform_tj:
                getData(json);
                if (tuikuan_reason.getText().toString().equals("")){
                    Toast.makeText(DashouChedanActivity.this,"请输入撤单原因",Toast.LENGTH_SHORT).show();
                }else  if (tuikuan_reason.getText().toString().trim().length()>100){
                    Toast.makeText(DashouChedanActivity.this,"撤单原因不超过100字",Toast.LENGTH_SHORT).show();
                } else if (imageName1==null&&imageName2==null&&imageName3==null){
                    Toast.makeText(DashouChedanActivity.this,"请上传相关证据截图",Toast.LENGTH_SHORT).show();
                }else {
                    new AlertDialog.Builder(DashouChedanActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确定提交？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    BeaterOrder.orderType = "6";
                                    getData(json);
                                    String url13 = "/api/Order/PostReceiverRevoke";
                                    HttpPostUtils.httpPostFile(13,url13,json,handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
        }
    }

    private void getData(JSONObject json) {
        try {
            json.put("OrderID",Beater_Order_Activity.od);
            json.put("Cause",tuikuan_reason.getText().toString());
            json.put("Evidence1",imageName1);
            json.put("Evidence2",imageName2);
            json.put("Evidence3",imageName3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 13:
                    String res = (String) msg.obj;
                    JSONObject result = null;
                    try {
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success=="true"){
                            ts = "1";
                            Intent intent = new Intent(DashouChedanActivity.this,TsActivity.class);
                            startActivity(intent);
                            finish();
                        }else if (success=="false"){
                            Toast.makeText(DashouChedanActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    public void showChoosePicDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片");
        String[] items = {"选择本地照片","拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dailog, int witch) {
                // TODO Auto-generated method stub
                switch(witch){
                    case CHOOSE_PICTURE://选择本地照片
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                Toast.makeText(DashouChedanActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else{
                Bitmap photo = (Bitmap)msg.obj;
                if(s=="1"){
                    zj1.setImageBitmap(photo);
                }else if(s=="2"){
                    zj2.setImageBitmap(photo);
                }else if(s=="3"){
                    zj3.setImageBitmap(photo);
                }
                Toast.makeText(DashouChedanActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
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
        intent.putExtra("aspectX", 0.8);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 400);
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
            if(s=="1"){
                zj1.setImageBitmap(photo);
            }else if(s=="2"){
                zj2.setImageBitmap(photo);
            }else if(s=="3"){
                zj3.setImageBitmap(photo);
            }
//            authentication3.setImageBitmap(photo);
            uploadPic(photo);
        }
    }
    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
//		imagePath = Utils.savePhoto(bitmap,
//				Environment.getExternalStorageDirectory().getAbsolutePath(),
//				String.valueOf(System.currentTimeMillis()));
        imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        dd = Environment.getExternalStorageDirectory().getAbsolutePath();
        test = String.valueOf(System.currentTimeMillis())+".png";
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
        String boundary = "*************************";
        String Ticket = HttpPostUtils.getTicket();
        try {
            String actionUrl = Url.urlUploadFile +"revoke";
            URL url = new URL(actionUrl);//服务器地址
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设置传送的method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty *///设置请求属性
            con.setRequestProperty("Authorization", "BasicAuth" + " "+Ticket);
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
			/* 将Response显示于Dialog */
            //showDialog("上传结果" + b.toString().trim());
            if(s == "1"){
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                imageName1 = data;
            }
            if (s == "2"){
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                imageName2 = data;
            }
            if (s == "3"){
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                imageName3 = data;
            }
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            //Toast.makeText(DashouChedanActivity.this,"上传失败，请检查网络",Toast.LENGTH_SHORT).show();
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
            // handler.sendMessage(msg);
            Looper.loop();//从消息队列取消
        }

    };

}
