package com.leveling;

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
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.entity.ApplyAuthentication;
import com.leveling.entity.Url;
import com.leveling.ui.EasyLoading;
import com.leveling.ui.StatusControl;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
public class MineUpLoadActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView high_duanwei;
    private com.leveling.AmountView1 dsOrderMaxNumber;
    private ImageView duanwei_img;
    private TextView selsect_duanwei;
    private Button topay_money;
    private String msg;
    private JSONObject json = new JSONObject();
    private String munber, num;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static final String BEENAUTH = "BEENAUTH";
    public static String money;
    String imagePath;
    String test, dd;
    private String imageName1;
    private String newName = "image.jpg";
    private String gameTypeNumber, rank_Type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_upload_layout);
        LinearLayout img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (HttpPostUtils.getRole() == 1 || HttpPostUtils.getRole() == 2) {
            gameTypeNumber = "1";
        } else if (HttpPostUtils.getRole() == 5 || HttpPostUtils.getRole() == 6) {
            gameTypeNumber = "2";
        }
        if (HttpPostUtils.getRole() == 1 || HttpPostUtils.getRole() == 5) {
            rank_Type = "1";
        } else if (HttpPostUtils.getRole() == 2 || HttpPostUtils.getRole() == 6) {
            rank_Type = "2";
        }
        high_duanwei = (TextView) findViewById(R.id.high_duanwei);
        dsOrderMaxNumber = (AmountView1) findViewById(R.id.dsOrderMaxNumber);
        selsect_duanwei = (TextView) findViewById(R.id.selsect_duanwei);
        duanwei_img = (ImageView) findViewById(R.id.duanwei_img);
        topay_money = (Button) findViewById(R.id.topay_money);
        Log.d("ROLE", HttpPostUtils.getRole() + "");
        if (HttpPostUtils.getRole() == 1) {
            high_duanwei.setText("王者荣耀打手");
        } else if (HttpPostUtils.getRole() == 2) {
            high_duanwei.setText("王者荣耀高级打手");
        } else if (HttpPostUtils.getRole() == 5) {
            high_duanwei.setText("英雄联盟打手");
        } else if (HttpPostUtils.getRole() == 6) {
            high_duanwei.setText("英雄联盟高级打手");
        }
        topay_money.setOnClickListener(this);
        duanwei_img.setOnClickListener(this);
        selsect_duanwei.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.selsect_duanwei:
                showDialog();
                break;
            case R.id.duanwei_img:
                showChoosePicDialog();
                break;
            case R.id.topay_money:
                if (selsect_duanwei.getText().toString().equals("")) {
                    Toast.makeText(MineUpLoadActivity.this, "请选择可接段位", Toast.LENGTH_SHORT).show();
                } else if (AmountView1.last_amount == 0) {
                    Toast.makeText(MineUpLoadActivity.this, "最大可接单数不能为0", Toast.LENGTH_SHORT).show();
                } else if (imageName1 == null) {
                    Toast.makeText(MineUpLoadActivity.this, "请上传段位截图", Toast.LENGTH_SHORT).show();
                } else {
                    getData(json);
                    String url = "/api/Authentication/PostApplyAuthentication";
                    HttpPostUtils.httpPostFile(110, url, json, handler);
                }
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            String data = result.getString("Data");
                            JSONObject inresult1 = new JSONObject(data);
                            String id = inresult1.getString("ID");
                            String money = inresult1.getString("Money");
                            ApplyAuthentication.id = id;
                            ApplyAuthentication.money = money;
                            Intent intent = new Intent(MineUpLoadActivity.this, PayMActivity.class);
                            startActivity(intent);
                        } else if (success == "false") {
                            Toast.makeText(MineUpLoadActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private void getData(JSONObject json) {
        try {
            json.put("GameType", Integer.parseInt(gameTypeNumber));
            json.put("RankType", Integer.parseInt(rank_Type));
            json.put("Type", 2);
            json.put("Authentication3", imageName1);
            json.put("OrderMaxNumber", AmountView1.last_amount);
            json.put("BigRankMax", msg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = null;
        if (HttpPostUtils.getRole() == 1 || HttpPostUtils.getRole() == 2 || HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4) {
            num = "1";
            data = Arrays.asList(new String[]{"青铜", "白银", "黄金", "铂金", "钻石", "星耀", "王者"});
        } else if (HttpPostUtils.getRole() == 5 || HttpPostUtils.getRole() == 6 || HttpPostUtils.getRole() == 7 || HttpPostUtils.getRole() == 8) {
            num = "2";
            data = Arrays.asList(new String[]{"青铜", "白银", "黄金", "铂金", "钻石", "大师", "王者"});
        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        msg = itemValue;
                        if (msg == "青铜" || msg == "白银" || msg == "黄金" || msg == "铂金") {
                            if (num == "1") {
                                if (HttpPostUtils.getRole() != 1) {
                                    Toast.makeText(MineUpLoadActivity.this, "您为王者荣耀高级打手，请选择钻石以上段位！", Toast.LENGTH_LONG).show();
                                    selsect_duanwei.setText("");
                                } else {
                                    munber = "3";
                                    selsect_duanwei.setText(itemValue);
                                }
                            } else if (num == "2") {
                                if (HttpPostUtils.getRole() != 5) {
                                    Toast.makeText(MineUpLoadActivity.this, "您为英雄联盟高级打手，请选择钻石以上段位！", Toast.LENGTH_LONG).show();
                                    selsect_duanwei.setText("");
                                } else {
                                    munber = "7";
                                    selsect_duanwei.setText(itemValue);
                                }
                            }

                        } else if (msg == "钻石" || msg == "星耀" || msg == "王者" || msg == "大师") {
                            if (num == "1") {
                                if (HttpPostUtils.getRole() != 2) {
                                    Toast.makeText(MineUpLoadActivity.this, "您为王者荣耀普通打手，请选择钻石以下段位！", Toast.LENGTH_LONG).show();
                                    selsect_duanwei.setText("");
                                } else {
                                    munber = "4";
                                    selsect_duanwei.setText(itemValue);
                                }
                            } else if (num == "2") {
                                if (HttpPostUtils.getRole() != 6) {
                                    Toast.makeText(MineUpLoadActivity.this, "您为英雄联盟普通打手，请选择钻石以下段位！", Toast.LENGTH_LONG).show();
                                    selsect_duanwei.setText("");
                                } else {
                                    munber = "8";
                                    selsect_duanwei.setText(itemValue);
                                }
                            }
                        }

                    }
                }).create();
        dialog.show();
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
                            dd = Environment.getExternalStorageDirectory().getAbsolutePath();
                            test = String.valueOf(System.currentTimeMillis()) + ".png";
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
                Toast.makeText(MineUpLoadActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap photo = (Bitmap) msg.obj;
                duanwei_img.setImageBitmap(photo);
                Toast.makeText(MineUpLoadActivity.this, "上传成功", Toast.LENGTH_LONG).show();
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
            duanwei_img.setImageBitmap(photo);
//            authentication3.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
//		imagePath = Utils.savePhoto(bitmap,
//				Environment.getExternalStorageDirectory().getAbsolutePath(),
//				String.valueOf(System.currentTimeMillis()));
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
        String boundary = "******";
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
            JSONObject result = new JSONObject(b.toString().trim());
            String data = result.getString("Data");
            imageName1 = data;
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MineUpLoadActivity.this, "上传失败，请检查网络", Toast.LENGTH_LONG).show();
        }
    }

    Runnable runnable = new Runnable() {

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
//    /* 显示Dialog的method */
//    private void showDialog(String mess) {
//        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess)
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                }).show();
//    }
}
