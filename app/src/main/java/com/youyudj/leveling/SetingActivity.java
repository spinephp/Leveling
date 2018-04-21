package com.youyudj.leveling;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.personcenter.ChangeLoginPasswordActivity;
import com.youyudj.leveling.personcenter.ChangePayPasswordActivity;
import com.youyudj.leveling.personcenter.UpdatePhoneActivity;
import com.youyudj.leveling.ui.EasyLoading;
import com.youyudj.leveling.ui.StatusControl;
import com.youyudj.leveling.utils.HttpFileHelper;
import com.youyudj.leveling.utils.HttpGetUtils;
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

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/25 14:37
 * Update:2017/11/25
 * Version:
 * *******************************************************
 */
public class SetingActivity extends AppCompatActivity {
    private TextView old_nickname;
    private ImageView picture;
    private static final String LOGIN = "login";
    private static final String ISSAVEPASS = "savePassWord";
    private static final String AUTOLOGIN = "autoLogin";
    private static final String NICKNAME = "nickname";
    private static final String PICTURE1 = "picture";
    private static final String CNICKNAME = "cnickname";
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    ProgressBar progressBar11;
    TextView text_percent1;
    protected static Uri tempUri;
    private UserInfo userInfo;
    public static String img_name;
    private LinearLayout sp;
    String imagePath;
    String name;
    String dd;
    private String newName = "image.jpg";
    private String tt;
    FileInputStream fStream;
    private LinearLayout check_update;
    private ImageView change_img;
    private ImageView img_pic_back;
    private static final String PICTURE = "picture";
    private JSONObject json = new JSONObject();
    private String msgg,new_name;
    private TextView banben;
    private LinearLayout ll_beater_number;
    private TextView beater_number;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seting_layout);
        userInfo = new UserInfo(this);
        LinearLayout la_seting_upload = (LinearLayout) findViewById(R.id.la_seting_upload);
        if (HttpPostUtils.getRole()==0||HttpPostUtils.getRole()==1||HttpPostUtils.getRole()==2||HttpPostUtils.getRole()==5||HttpPostUtils.getRole()==6){
            la_seting_upload.setVisibility(View.VISIBLE);
        }
        picture = (ImageView) findViewById(R.id.first_picture);
        progressBar11 = (ProgressBar) findViewById(R.id.progressBar11);
        text_percent1 = (TextView) findViewById(R.id.text_percent1);
        check_update = (LinearLayout) findViewById(R.id.check_update);
        ll_beater_number = (LinearLayout) findViewById(R.id.ll_beater_number);
        beater_number = (TextView) findViewById(R.id.beater_number);
//        check_update.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String urlUploadFile = "/api/SystemInfor/GetAppVersion";
//                HttpGetUtils.httpGetFile(1011,urlUploadFile,handler);
//            }
//        });
        old_nickname = (TextView) findViewById(R.id.old_nickname);
        sp = (LinearLayout) findViewById(R.id.sp);
        TextView tv1 = (TextView) findViewById(R.id.shengji);
        ImageView im1 = (ImageView) findViewById(R.id.shengji_btn);
        banben = (TextView) findViewById(R.id.banben);
        banben.setText(MainActivity.getVersion());
        LinearLayout setting_title_back = (LinearLayout) findViewById(R.id.setting_title_back);
        findViewById(R.id.setting_title_back).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (HttpPostUtils.getRole()==0){
                    Intent intent1 = new Intent(SetingActivity.this,MainActivity.class);
                    intent1.putExtra("id",11);
                    startActivity(intent1);
                }else {
                    finish();
                }
            }
        });
        if(HttpPostUtils.getRole() == 0) {
            tv1.setText("打手认证");
            findViewById(R.id.la_seting_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "/api/Authentication/GetAuthenticationState";
                    HttpGetUtils.httpGetFile(10001,url,handler);
                }
            });
        }else if (HttpPostUtils.getRole() == 1||HttpPostUtils.getRole() == 2||HttpPostUtils.getRole() == 3||HttpPostUtils.getRole() == 4) {
            tv1.setText("我要升级");
            ll_beater_number.setVisibility(View.VISIBLE);
            ll_beater_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(beater_number.getText());
                    Toast.makeText(SetingActivity.this, "打手编号已复制", Toast.LENGTH_LONG).show();
                }
            });
            beater_number.setText(HttpPostUtils.getNumber());
            findViewById(R.id.la_seting_upload).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "/api/Authentication/GetAuthenticationState";
                    HttpGetUtils.httpGetFile(10002, url, handler);
                }
            });
        }else if (HttpPostUtils.getRole() == 5||HttpPostUtils.getRole() == 6||HttpPostUtils.getRole() == 7||HttpPostUtils.getRole() == 8){
            la_seting_upload.setVisibility(View.GONE);
            ll_beater_number.setVisibility(View.VISIBLE);
            ll_beater_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(beater_number.getText());
                    Toast.makeText(SetingActivity.this, "复制成功，可以发给朋友们了。", Toast.LENGTH_LONG).show();
                }
            });
            beater_number.setText(HttpPostUtils.getNumber());
        }

        findViewById(R.id.la_seting_change_nickname).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(),UpdateNickNameActivity.class));
                final EditText inputServer = new EditText(SetingActivity.this);
                inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                AlertDialog.Builder builder = new AlertDialog.Builder(SetingActivity.this);
                builder.setTitle("请输入昵称").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new_name = inputServer.getText().toString();
                        old_nickname.setText(inputServer.getText().toString());
                        if ( old_nickname.getText().toString().equals("")){
                            Toast.makeText(SetingActivity.this,"请输入昵称",Toast.LENGTH_LONG).show();
                        }else if (old_nickname.getText().toString().trim().length()>6){
                            Toast.makeText(SetingActivity.this,"昵称太长，请输入6字以内昵称",Toast.LENGTH_LONG).show();
                        }else{
                            getDate(json);
                            String url = "/api/Users/PostUpdateNickName";
                            HttpPostUtils.httpPostFile(9,url, json, handler);
                        }

                    }
                });
                builder.show();
            }
        });

        findViewById(R.id.la_seting_change_picture).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),UpdatePictureActivity.class));
                showChoosePicDialog();
            }
        });
        findViewById(R.id.la_seting_change_phone).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UpdatePhoneActivity.class));
            }
        });
        findViewById(R.id.la_seting_change_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangeLoginPasswordActivity.class));
            }
        });
        findViewById(R.id.la_seting_change_pay_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ChangePayPasswordActivity.class));
            }
        });
        findViewById(R.id.back_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo.clear();
                Log.d("name",HttpPostUtils.getPhone()+"11");
                userInfo.setUserInfo(ISSAVEPASS, false);
                userInfo.setUserInfo(AUTOLOGIN, false);
                userInfo.setUserInfo(LOGIN,false);
                HttpPostUtils.setTicket(null);
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
        if (HttpPostUtils.getNickname()==""){
            old_nickname.setText("昵称");
            if (HttpPostUtils.getPicture()=="null"){
                picture.setImageResource(R.drawable.userhead);
            }else{
                String uuq = "/api/Users/GetPicture";
                HttpGetUtils.httpGetFile(1002,uuq,handler);
            }
        }else{
            String uu = "/api/Users/GetNickName";
            HttpGetUtils.httpGetFile(1001,uu,handler);
        }
    }
    protected void getDate(JSONObject json) {
        // TODO Auto-generated method stub
        try {
            json.put("nickname", old_nickname.getText().toString().trim());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what < 0){
                return;
            }
            switch (msg.what) {
                case 1001:
                    String res1 = (String) msg.obj;
                    if (res1 == null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res1);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success=="true"){
                                old_nickname.setText(data);
                                if (HttpPostUtils.getPicture()=="null"){
                                    picture.setImageResource(R.drawable.userhead);
                                }else{
                                    String uuq = "/api/Users/GetPicture";
                                    HttpGetUtils.httpGetFile(1002,uuq,handler);
                                }
                            }else{
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1002:
                    String res3 = (String) msg.obj;
                    if (res3 == null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res3);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success=="true"){
                                if (data.equals("")){
                                    picture.setBackgroundResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename=" + data;
                                    HttpFileHelper.httpGetFile(6, url, handler);
                                }
                            }else{
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 999:
                    String res = (String) msg.obj;
                    if (res==null){
                        return;
                    }
                    try {
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        if (success=="true"){
                            Toast.makeText(SetingActivity.this, "头像更换成功", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(SetingActivity.this, "头像更换失败", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    byte[] data1 = (byte[])msg.obj;
                    if (data1 == null){
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    picture.setImageBitmap(bitmap1);
                    break;
                case 9:
                    String res2 = (String) msg.obj;
                    if (res2 == null){
                        return;
                    }
                    try {
                        JSONObject obj = new JSONObject(res2);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            msgg = "1";
                            Toast.makeText(SetingActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                        }else if(success == "false"){
                            Toast.makeText(SetingActivity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 10001:
                    String res21 = (String) msg.obj;
                    if (res21 == null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res21);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (data.equals("1")){
                                startActivity(new Intent(SetingActivity.this,BeaterAuthSuccessActivity.class));
                            }else if (data.equals("2")){
                                Toast.makeText(SetingActivity.this,"您已经通过认证，请重新登陆进入打手界面",Toast.LENGTH_LONG).show();
                                userInfo.clear();
                                startActivity(new Intent(SetingActivity.this,LoginActivity.class));
                            }else if (data.equals("3")){
                                startActivity(new Intent(SetingActivity.this,BeaterAuthFailedActivity.class));
                            }else{
                                startActivity(new Intent(SetingActivity.this,BeaterAuthActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 10002:
                    String res22 = (String) msg.obj;
                    if (res22 == null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res22);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (data.equals("1")){
                                startActivity(new Intent(SetingActivity.this,BeaterAuthSuccessActivity.class));
                            }else if (data.equals("2")){
                                Toast.makeText(SetingActivity.this,"您已经通过认证",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SetingActivity.this,LoginActivity.class));
                            }else if (data.equals("3")){
                                startActivity(new Intent(SetingActivity.this,UpFailedActivity.class));
                            }else{
                                startActivity(new Intent(SetingActivity.this,MineUpLoadActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1011:
                    String res11 = (String) msg.obj;
                    if (res11 == null){
                        return;
                    }else{
                        try {
                            JSONObject result = new JSONObject(res11);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success=="true"){
                                JSONObject jj = new JSONObject(data);
                                dd = jj.getString("VersionNumber");
                                name = jj.getString("FileName");
                                if (banben.getText().toString().equals(dd)){
                                    Toast.makeText(SetingActivity.this,"当前已是最新版本",Toast.LENGTH_LONG).show();
                                }else{
                                    new AlertDialog.Builder(SetingActivity.this).setTitle("提示")//设置对话框标题
                                            .setMessage("确认更新最新版本?")//设置显示的内容
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                    text_percent1.setVisibility(View.VISIBLE);
                                                    progressBar11.setVisibility(View.VISIBLE);
                                                    // TODO Auto-generated method stub
                                                    String uuq = "/api/File/GetAppFile?filename="+name;
                                                    HttpFileHelper.httpGetFileWithProgress(uuq, handlerDownloadNewVersionProgress);
                                                    sp.setVisibility(View.VISIBLE);
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {//响应事件
                                            // TODO Auto-generated method stub
                                        }
                                    }).show();//在按键响应事件中显示此对话框
                                }
                            }else{
                                return;
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
    Handler handlerDownloadNewVersionProgress = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           // System.out.println(msg.what);
            int per = msg.what;
            if(msg.what == 101){
                text_percent1.setVisibility(View.GONE);
                progressBar11.setVisibility(View.GONE);
                String str = "/"+"Leveling.apk";
                //banben.setText(dd);
                String fileName = Environment.getExternalStorageDirectory() + str;
                Intent intent =new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(msg.what < 0){
                text_percent1.setVisibility(View.GONE);
                progressBar11.setVisibility(View.GONE);
                Toast.makeText(SetingActivity.this, "更新失败", Toast.LENGTH_LONG).show();
//                if(msg.obj != null){
//                    String path = Environment.getExternalStorageDirectory() + "/log.txt";
//                    try {
//                        FileOutputStream str = new FileOutputStream(path);
//                        str.write(msg.obj.toString().getBytes());
//                        str.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Toast.makeText(SetingActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(SetingActivity.this, "no error info", Toast.LENGTH_LONG).show();
//                }
            }else if(msg.what <= 100 && msg.what >= 0){
                text_percent1.setText("" + per + "%");
            }
        }
    };
    public void showChoosePicDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (requestCode == 1 ) {
            finish();
        }
        if (resultCode == RESULT_OK) {//如果返回码可以用
            Uri uri = null;
            switch (requestCode) {
                case TAKE_PICTURE: {
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
            if (uri != null) {
                final Uri finalUri = uri;
                EasyLoading.doWork(this, "处理中，请稍后...", new EasyLoading.WorkerListener() {
                    @Override
                    public void run(StatusControl statusControl) {
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(finalUri));
                            //Bitmap bmp1 = Utils.toRoundBitmap(bitmap); // 这个时候的图片已经被处理成圆形的了
                            //bitmap.recycle();
                            imagePath = Utils.savePhoto(bitmap,
                                    Environment.getExternalStorageDirectory().getAbsolutePath(),
                                    String.valueOf(System.currentTimeMillis()));
                            statusControl.update("上传中，请稍后...");
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

    Handler handlerUpdatePhotoSelect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0)
                Toast.makeText(SetingActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap photo = (Bitmap) msg.obj;
                picture.setImageBitmap(photo);
                Toast.makeText(SetingActivity.this,"上传成功",Toast.LENGTH_LONG).show();
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
            picture.setImageBitmap(photo);
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
           String actionUrl = Url.urlUploadFile +"userhead";
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
            img_name = data;
            tt = data;
            getData(json);
            String url1 = "/api/Users/PostUpdatePicture";
            HttpPostUtils.httpPostFile(999,url1,json,handler);
            userInfo.setUserInfo(PICTURE,data);
            userInfo.setUserInfo(PICTURE,data);
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(SetingActivity.this,"上传失败，请检查网络",Toast.LENGTH_LONG).show();
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
    /* 显示Dialog的method */
//    private void showDialog(String mess) {
//        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess)
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).show();
//    }
    @Override
    public void onBackPressed() {
       // Toast.makeText(getApplicationContext(), "onBackPressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
        if (HttpPostUtils.getRole()==0){
            Intent intent1 = new Intent(SetingActivity.this,MainActivity.class);
            intent1.putExtra("id",11);
            startActivity(intent1);
        }else {
            finish();
        }
    }
}
