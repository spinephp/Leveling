package com.youyudj.leveling;

import android.app.AlertDialog;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.ui.EasyLoading;
import com.youyudj.leveling.ui.StatusControl;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.youyudj.leveling.utils.Utils;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BeaterAuthActivity extends AppCompatActivity implements OnClickListener {
    Calendar calendar = Calendar.getInstance();
    @InjectView(R.id.true_name)
    EditText name;
    @InjectView(R.id.true_IDNumber)
    EditText idNumber;
    @InjectView(R.id.Authentication3)
    ImageView authentication3;
    @InjectView(R.id.Authentication4)
    ImageView authentication4;
    @InjectView(R.id.player_conform_nextbtn)
    Button player_conform_next;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    String imagePath;
    String test, dd;
    WindowManager wm1;
    private String newName = "image.jpg";
    private String s;
    private LinearLayout img_btone_back;
    String userIdCard1, userIdCard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beater_auth_one_layout);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ButterKnife.inject(this);
        img_btone_back = (LinearLayout) findViewById(R.id.img_btone_back);
        img_btone_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wm1 = this.getWindowManager();
        authentication3.setOnClickListener(this);
        authentication4.setOnClickListener(this);
        player_conform_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.Authentication3:
                s = "1";
                showChoosePicDialog();
                break;
            case R.id.Authentication4:
                s = "2";
                showChoosePicDialog();
                break;
            case R.id.player_conform_nextbtn:
                String nameText = name.getText().toString();
                if (nameText.length() == 0){
                    Toast.makeText(BeaterAuthActivity.this, "姓名不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(!Utils.isChinese(nameText)) {
                    Toast.makeText(BeaterAuthActivity.this, "请输入中文姓名", Toast.LENGTH_LONG).show();
                    return;
                }
                String idCardText = idNumber.getText().toString();
                if(idCardText.length() != 18) {
                    Toast.makeText(BeaterAuthActivity.this, "请输入18位身份证号码", Toast.LENGTH_LONG).show();
                    return;
                }
                ApplyAuthentication.Name = nameText;
                ApplyAuthentication.IDNumber = idCardText;
                if (userIdCard1 == null || userIdCard1.length() == 0) {
                    Toast.makeText(BeaterAuthActivity.this, "请上传身份证正面截图", Toast.LENGTH_LONG).show();
                    return;
                }
                if (userIdCard2 == null || userIdCard2.length() == 0) {
                    Toast.makeText(BeaterAuthActivity.this, "请上传身份证背面截图", Toast.LENGTH_LONG).show();
                    return;
                }
                ApplyAuthentication.Authentication3 = userIdCard1;
                ApplyAuthentication.Authentication4 = userIdCard2;
                Intent intent = new Intent(BeaterAuthActivity.this, Authentication1Activity.class);
                //startActivity(intent);
                startActivityForResult(intent, 0x1000);
                //finish();
                break;
            default:
                break;
        }
    }

    public void showChoosePicDialog() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择图片");
        String[] items = {"选择本地照片", "拍照"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dailog, int witch) {
                // TODO Auto-generated method stub
                switch (witch) {
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
                                .getExternalStorageDirectory(), "image.jpg"));
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
        if(requestCode == 0x1000){
            if(resultCode == 0x1001)
                this.finish();
            return;
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

    Handler handlerUpdatePhotoSelect = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0)
                Toast.makeText(BeaterAuthActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap photo = (Bitmap) msg.obj;
                if (s == "1") {
                    authentication3.setImageBitmap(photo);
                } else if (s == "2") {
                    authentication4.setImageBitmap(photo);
                }
                Toast.makeText(BeaterAuthActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                //uploadPic(photo);
            }
        }
    };

    /***
     * 裁剪图片方法
     */

    private void startPhotoZoom(Uri uri) {
        // TODO Auto-generated method stub
        if (uri == null) {
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
     **/
    private void setImageToView(Intent data) {
        // TODO Auto-generated method stub
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = Utils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            if (s == "1") {
                authentication3.setImageBitmap(photo);
            } else if (s == "2") {
                authentication4.setImageBitmap(photo);
            }
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
        imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        if (imagePath != null) {
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
        String boundary = "*****************************";
        String Ticket = HttpPostUtils.getTicket();
        try {
            String actionUrl = Url.urlUploadFile + "Authentication";
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
			/* 将Response显示于Dialog */
            //showDialog("上传结果" + b.toString().trim());
            if (s == "1") {
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                userIdCard1 = data;
            }
            if (s == "2") {
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                userIdCard2 = data;
            }
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            //Toast.makeText(BeaterAuthActivity.this, "上传失败，请检查网络", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    Runnable runnable = new Runnable() {

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
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject json = new JSONObject();
        }

        ;
    };
    /* 显示Dialog的method */
//    private void showDialog(String mess) {
//        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess)
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).show();
//    }
}
