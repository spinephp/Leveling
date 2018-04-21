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
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.entity.Hero;
import com.youyudj.leveling.entity.Url;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.ui.EasyLoading;
import com.youyudj.leveling.ui.StatusControl;
import com.youyudj.leveling.utils.HttpFileHelper;
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
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class Authentication1Activity extends AppCompatActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener {
    @InjectView(R.id.leveling)
    TextView leveling;
    @InjectView(R.id.GameType)
    RadioGroup gameType;
    @InjectView(R.id.WKG)
    RadioButton wang;
    @InjectView(R.id.LOL)
    RadioButton ying;
    @InjectView(R.id.choosed_level)
    RelativeLayout choosed_level;
    @InjectView(R.id.choose_level)
    ImageView choose_level;
    @InjectView(R.id.duanwei_choose)
    TextView duanwei_choose;
    @InjectView(R.id.choose_hero)
    RelativeLayout choose_hero;
    @InjectView(R.id.heropic1)
    de.hdodenhof.circleimageview.CircleImageView heropic1;
    @InjectView(R.id.heropic2)
    de.hdodenhof.circleimageview.CircleImageView heropic2;
    @InjectView(R.id.heropic3)
    de.hdodenhof.circleimageview.CircleImageView heropic3;
    @InjectView(R.id.heropic4)
    de.hdodenhof.circleimageview.CircleImageView heropic4;
    @InjectView(R.id.Authentication1)
    ImageView authentication1;
    @InjectView(R.id.Authentication2)
    ImageView authentication2;
    @InjectView(R.id.OrderMaxNumber)
    com.youyudj.leveling.AmountView1 OrderMaxNumber1;
    @InjectView(R.id.author_next)
    Button player_conform;
    private JSONObject json = new JSONObject();
    private String msg;
    private int type;
    private String imageName1, imageName2;
    private String res;
    private int t_res;
    private String rank_Type, gameTypeNumber;
    Intent intent;
    private String s;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static final String BEENAUTH = "BEENAUTH";
    private UserInfo userInfo;
    String imagePath;
    String test, dd;
    private String newName = "image.jpg";
    private LinearLayout img_bttwo_back;
    private String key1, key2, key3, key4;
    private String value1, value2, value3, value4;
    private String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beater_auth1_layout);
        ButterKnife.inject(this);
        userInfo = new UserInfo(this);
        img_bttwo_back = (LinearLayout) findViewById(R.id.img_bttwo_back);
        img_bttwo_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }

    private void init() {
        Hero.heros.clear();
        gameTypeNumber = "1";
        wang.setChecked(true);
        gameType.setOnCheckedChangeListener(this);
        choose_level.setOnClickListener(this);
        choose_hero.setOnClickListener(this);
        authentication1.setOnClickListener(this);
        authentication2.setOnClickListener(this);
        player_conform.setOnClickListener(this);
        duanwei_choose.setOnClickListener(this);
    }

    private void getDate(JSONObject json) {
        try {
            json.put("GameType", gameTypeNumber);
            json.put("RankType", rank_Type);
            json.put("Type", 2);
            json.put("Name", ApplyAuthentication.Name);
            json.put("IDNumber", ApplyAuthentication.IDNumber);
            json.put("Authentication1", ApplyAuthentication.Authentication3);
            json.put("Authentication2", ApplyAuthentication.Authentication4);
            json.put("Authentication3", imageName1);
            json.put("Authentication4", imageName2);
            if (gameTypeNumber.equals("2")) {// 英雄联盟无需英雄
                json.put("Hero1", null);
                json.put("Hero2", null);
                json.put("Hero3", null);
                json.put("Hero4", null);
            } else {
                json.put("Hero1", key1);
                json.put("Hero2", key2);
                json.put("Hero3", key3);
                json.put("Hero4", key4);
            }
            json.put("OrderMaxNumber", AmountView1.last_amount);
            json.put("BigRankMax", msg);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.duanwei_choose:
                showDialog();
                break;
            case R.id.choose_hero:
                Intent intent = new Intent();
                intent.putExtra("maxSelectCount", 4);
                intent.setClass(Authentication1Activity.this, StrongHeroActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.Authentication1:
                s = "1";
                showChoosePicDialog();
                break;
            case R.id.Authentication2:
                s = "2";
                showChoosePicDialog();
                break;
            case R.id.author_next:
                if (duanwei_choose.getText().toString().equals("")) {
                    Toast.makeText(Authentication1Activity.this, "请选择可接段位", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (key1 == null) {
//                    Toast.makeText(Authentication1Activity.this, "至少选择一个强势英雄", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (imageName1 == null) {
                    Toast.makeText(Authentication1Activity.this, "请上传最近一百场截图", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (imageName2 == null) {
                    Toast.makeText(Authentication1Activity.this, "请上传当前段位截图", Toast.LENGTH_SHORT).show();
                    return;
                }
                getDate(json);
                String url = "/api/Authentication/PostApplyAuthentication";
                HttpPostUtils.httpPostFile(110, url, json, handler);
                break;
            default:
                break;
        }
    }

    private final void showDialog() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = null;
        if (gameTypeNumber == "1") {
            data = Arrays.asList(new String[]{"青铜", "白银", "黄金", "铂金", "钻石", "星耀", "王者"});
        } else if (gameTypeNumber == "2") {
            data = Arrays.asList(new String[]{"青铜", "白银", "黄金", "铂金", "钻石", "大师", "王者"});
        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        duanwei_choose.setText(itemValue);
                        msg = itemValue;
                        if (gameTypeNumber == "1") {
                            if (msg == "青铜" || msg == "白银" || msg == "黄金" || msg == "铂金") {
                                rank_Type = "1";
                            } else if (msg == "钻石" || msg == "星耀" || msg == "王者") {
                                rank_Type = "2";
                            }
                        } else if (gameTypeNumber == "2") {
                            if (msg == "青铜" || msg == "白银" || msg == "黄金" || msg == "铂金") {
                                rank_Type = "1";
                            } else if (msg == "钻石" || msg == "大师" || msg == "王者") {
                                rank_Type = "2";
                            }
                        }
                    }
                }).create();
        dialog.show();
    }

    void downHeadImage(int actionID, String name) {
        if (name == null)
            return;
        String url = "/api/File/GetHerohead?filename=" + name;
        HttpFileHelper.httpGetFile(actionID, url, handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x1000){
            setResult(resultCode);
            if(resultCode == 0x1001)
                this.finish();
            return;
        }
        if (resultCode == 100) {//如果是返回的标识
            String[] keys = Hero.heros.keySet().toArray(new String[0]);
            heropic1.setImageBitmap(null);
            heropic2.setImageBitmap(null);
            heropic3.setImageBitmap(null);
            heropic4.setImageBitmap(null);
            value1 = value2 = value3 = value4 = null;
            if (keys.length == 1) {
                key1 = keys[0];
                value1 = Hero.heros.get(key1);
            }
            if (keys.length == 2) {
                key1 = keys[0];
                key2 = keys[1];
                value1 = Hero.heros.get(key1);
                value2 = Hero.heros.get(key2);
            }
            if (keys.length == 3) {
                key1 = keys[0];
                key2 = keys[1];
                key3 = keys[2];
                value1 = Hero.heros.get(key1);
                value2 = Hero.heros.get(key2);
                value3 = Hero.heros.get(key3);
            }
            if (keys.length == 4) {
                key1 = keys[0];
                key2 = keys[1];
                key3 = keys[2];
                key4 = keys[3];
                value1 = Hero.heros.get(key1);
                value2 = Hero.heros.get(key2);
                value3 = Hero.heros.get(key3);
                value4 = Hero.heros.get(key4);
            }
            downHeadImage(12, value1);
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
                Toast.makeText(Authentication1Activity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap photo = (Bitmap) msg.obj;
                if (s == "1") {
                    authentication1.setImageBitmap(photo);
                } else if (s == "2") {
                    authentication2.setImageBitmap(photo);
                }
                Toast.makeText(Authentication1Activity.this, "上传成功", Toast.LENGTH_SHORT).show();
                //uploadPic(photo);
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 110:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
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
                            userInfo.setUserInfo(BEENAUTH, true);
                            Intent intent = new Intent(Authentication1Activity.this, PayCashPledgeActivity.class);
                            startActivityForResult(intent, 0x1000);
                            //startActivity(intent);
                            //finish();
                        } else if (success == "false") {
                            Toast.makeText(Authentication1Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    byte[] data = (byte[]) msg.obj;
                    if (data == null) {
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    heropic1.setImageBitmap(bitmap);
                    downHeadImage(13, value2);
                    //Toast.makeText(Authentication1Activity.this,StrongHeroActivity.pic_name2+"",Toast.LENGTH_LONG).show();
                    break;
                case 13:
                    byte[] data1 = (byte[]) msg.obj;
                    if (data1 == null) {
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    heropic2.setImageBitmap(bitmap1);
                    downHeadImage(14, value3);
                    break;
                case 14:
                    byte[] data3 = (byte[]) msg.obj;
                    if (data3 == null) {
                        return;
                    }
                    Bitmap bitmap3 = BitmapFactory.decodeByteArray(data3, 0, data3.length);
                    heropic3.setImageBitmap(bitmap3);
                    downHeadImage(15, value4);
                    break;
                case 15:
                    byte[] data4 = (byte[]) msg.obj;
                    if (data4 == null) {
                        return;
                    }
                    Bitmap bitmap4 = BitmapFactory.decodeByteArray(data4, 0, data4.length);
                    heropic4.setImageBitmap(bitmap4);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int id = group.getId();
        switch (id) {
            case R.id.GameType:
                if (checkedId == R.id.WKG) {
                    gameTypeNumber = "1";
                    duanwei_choose.setText("");
                    findViewById(R.id.item_qiangshi_hero).setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.LOL) {
                    gameTypeNumber = "2";
                    duanwei_choose.setText("");
                    findViewById(R.id.item_qiangshi_hero).setVisibility(View.GONE);
                }
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
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 0.8);
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
                authentication1.setImageBitmap(photo);
            } else if (s == "2") {
                authentication2.setImageBitmap(photo);
            }
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
        imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
        dd = Environment.getExternalStorageDirectory().getAbsolutePath();
        test = String.valueOf(System.currentTimeMillis()) + ".png";
        Log.e("imagePath", imagePath + "");
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
			/* 将Response显示于Dialog */
            //showDialog("上传结果" + b.toString().trim());
            if (s == "1") {
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                imageName1 = data;
                //showDialog("结果：" + "上传成功");
            }
            if (s == "2") {
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                imageName2 = data;
            }
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            Toast.makeText(Authentication1Activity.this, "上传失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();//创建消息循环
            uploadFile();
            Message msg = new Message();
            Looper.loop();//从消息队列取消
        }

    };

}




