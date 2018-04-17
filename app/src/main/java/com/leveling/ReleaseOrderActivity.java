package com.leveling;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.entity.Hero;
import com.leveling.entity.OrderInfo;
import com.leveling.entity.Url;
import com.leveling.ui.EasyLoading;
import com.leveling.ui.StatusControl;
import com.leveling.utils.HttpFileHelper;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ReleaseOrderActivity extends AppCompatActivity implements OnClickListener {
    Calendar calendar = Calendar.getInstance();
    @InjectView(R.id.new_game_account)
    EditText new_game_account;
    @InjectView(R.id.to_input_new_pwd)
    EditText to_input_new_pwd;
    @InjectView(R.id.game_title)
    EditText game_title;
    @InjectView(R.id.fuwuqi)
    TextView fuwuqi;
    @InjectView(R.id.choose_fuwuqi)
    RelativeLayout choose_fuwuqi;
    @InjectView(R.id.qufu)
    TextView qufu;
    @InjectView(R.id.required_money)
    TextView required_money;
    @InjectView(R.id.shoose_qufu)
    RelativeLayout shoose_qufu;
    @InjectView(R.id.ReceiverNumber)
    EditText ReceiverNumber;
    @InjectView(R.id.Rune1)
    ImageView Rune1;// 符文截图：传URL
    @InjectView(R.id.Rune2)
    ImageView Rune2;// 符文截图：传URL
    @InjectView(R.id.shilitu_1)
    TextView shilitu_1;
    @InjectView(R.id.shilitu_2)
    TextView shilitu_2;
    @InjectView(R.id.img_release_back)
    LinearLayout img_release_back;
    @InjectView(R.id.release_order_conform_btn_topay)
    Button release_order_conform_btn_topay;
    @InjectView(R.id.choose_hero1)
    RelativeLayout choose_hero1;
    private JSONObject json = new JSONObject();
    private String image_url1 = "", image_url2 = "";
    private String s;
    private String gameos;
    private String msg;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    protected static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private static final String BEENAUTH = "BEENAUTH";
    String imagePath;
    private String newName = "image.jpg";
    private String[] arr;
    private String test;
    private String ds;
    View view;
    private static final int CROP_PHOTO = 2;
    private static final int PICK_PIC = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE1 = 4;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 5;
    private File output;
    Dialog dia, dia1;
    WindowManager wm;
    private ImageView[] heroHeads = new ImageView[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fill_in_order1_layout);
        ButterKnife.inject(this);
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        shilitu_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dia.show();
            }
        });
        Context context = ReleaseOrderActivity.this;
        dia = new Dialog(context, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.activity_start_dialog);
        ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
        //imageView.setBackgroundResource(R.drawable.imggg1);
        imageView.setBackgroundResource(R.drawable.imggg2);
        //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
        dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
        Window w = dia.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        lp.y = 40;
        dia.onWindowAttributesChanged(lp);
        imageView.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });

        shilitu_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dia1.show();
            }
        });

        Context context1 = ReleaseOrderActivity.this;
        dia1 = new Dialog(context, R.style.edit_AlertDialog_style);
        dia1.setContentView(R.layout.activity_start_dialog1);
        ImageView imageView1 = (ImageView) dia1.findViewById(R.id.start_img1);
        imageView1.setBackgroundResource(R.drawable.imggg1);
        dia1.setCanceledOnTouchOutside(true);
        Window w1 = dia1.getWindow();
        WindowManager.LayoutParams lp1 = w.getAttributes();
        lp1.x = 0;
        lp1.y = 40;
        dia.onWindowAttributesChanged(lp1);
        imageView1.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dia.dismiss();
                    }
                });
        Intent intent = getIntent();
        heroHeads[0] = (ImageView) findViewById(R.id.heropic1);
        heroHeads[1] = (ImageView) findViewById(R.id.heropic2);
        heroHeads[2] = (ImageView) findViewById(R.id.heropic3);
        init();
    }

    private void init() {
        Hero.heros.clear();
        choose_hero1.setOnClickListener(this);
        required_money.setText(MainFragment.price);
        img_release_back.setOnClickListener(this);
        choose_fuwuqi.setOnClickListener(this);
        shoose_qufu.setOnClickListener(this);
        Rune1.setOnClickListener(this);
        Rune2.setOnClickListener(this);
        release_order_conform_btn_topay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        getData(json);
        switch (id) {
            case R.id.choose_fuwuqi:
                showDialog();
                break;
            case R.id.shoose_qufu:
                showDialog1();
                break;
            case R.id.choose_hero1:
                Intent intent = new Intent();
                intent.putExtra("maxSelectCount", 3);
                intent.setClass(ReleaseOrderActivity.this, StrongHeroActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.Rune1:
                s = "1";
                showChoosePicDialog();
                break;
            case R.id.Rune2:
                s = "2";
                showChoosePicDialog();
                break;
            case R.id.img_release_back:
                //Intent intent1 = new Intent(ReleaseOrderActivity.this, MainActivity.class);
                //startActivity(intent1);
                finish();
                break;
            case R.id.release_order_conform_btn_topay:
                if (new_game_account.getText().toString().equals("")) {
                    Toast.makeText(ReleaseOrderActivity.this, "请输入您的联系电话", Toast.LENGTH_SHORT).show();
                } else if (to_input_new_pwd.getText().toString().equals("")) {
                    Toast.makeText(ReleaseOrderActivity.this, "请输入您的微信号", Toast.LENGTH_SHORT).show();
                } else if (fuwuqi.getText().toString().equals("") || qufu.getText().toString().equals("")) {
                    Toast.makeText(ReleaseOrderActivity.this, "请选择游戏区服", Toast.LENGTH_SHORT).show();
                }
//                else if(Hero.heros.size() == 0){
//                    Toast.makeText(ReleaseOrderActivity.this, "请指定英雄", Toast.LENGTH_SHORT).show();
//                }
//                else if (image_url1 == null) {
//                    Toast.makeText(ReleaseOrderActivity.this, "请上传段位截图", Toast.LENGTH_SHORT).show();
//                } else if (image_url2 == null) {
//                    Toast.makeText(ReleaseOrderActivity.this, "请上传铭文截图", Toast.LENGTH_SHORT).show();
//                }
                else {
                    new MyCount2().execute();
                    getData(json);
                    String url = "/api/Order/PostOrderRelease";
                    HttpPostUtils.httpPostFile(8, url, json, handler);
                }
                break;

            default:
                break;
        }
    }

    class MyCount2 extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] != 0) {
                release_order_conform_btn_topay.setClickable(false);
            } else {
                release_order_conform_btn_topay.setClickable(true);
            }
        }

        //倒计时
        @Override
        protected Integer doInBackground(Integer... params) {
            for (int i = 1; i >= 0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
            }
            return null;
        }
    }

    private void getData(JSONObject json) {
        try {
            json.put("GameType", 1);
            if (fuwuqi.getText().toString().equals("手Q安卓")) {
                json.put("GameOS", 1);
            } else if (fuwuqi.getText().toString().equals("手Q苹果")) {
                json.put("GameOS", 2);
            } else if (fuwuqi.getText().toString().equals("微信安卓")) {
                json.put("GameOS", 3);
            } else if (fuwuqi.getText().toString().equals("微信苹果")) {
                json.put("GameOS", 4);
            } else if (fuwuqi.getText().toString().equals("网通")) {
                json.put("GameOS", 5);
            } else if (fuwuqi.getText().toString().equals("电信")) {
                json.put("GameOS", 6);
            }
            json.put("GameArea", qufu.getText().toString());
            json.put("Title", game_title.getText().toString());
            json.put("Type", MainFragment.play_type);
            json.put("ReceiverNumber", ReceiverNumber.getText().toString());
            json.put("ReceiverGender", "");
            json.put("ReceiverAgeMin", "");
            json.put("ReceiverAgeMax", "");
            json.put("PiblisherBigRank", MainFragment.big);
            json.put("PiblisherMediumRank", MainFragment.middle);
            json.put("PiblisherRank", MainFragment.small);
            json.put("PiblisherGoalBigRank", MainFragment.big1);
            json.put("PiblisherGoalMediumRank", MainFragment.middle1);
            json.put("PiblisherGoalRank", MainFragment.small1);
            json.put("ReceiverHero", Hero.heroNameToString());
            json.put("TaskTime", MainFragment.time);
            json.put("OrderPrice", MainFragment.price);
            json.put("GameAccount", new_game_account.getText().toString());
            json.put("GamePwd", to_input_new_pwd.getText().toString());
            json.put("GameNicekName", "");
            json.put("HeroNumber", image_url1);
            json.put("Rune", image_url2);
            json.put("Piblisher", HttpPostUtils.getUserId());
            json.put("RankType", "");
            json.put("LOLType", "");
            json.put("LOLRankType", "");
            json.put("LOLRank", "");
            json.put("LOLGoalRank", "");
            json.put("LOLNeedToPlay", "");
            json.put("LOLWinning", "");
            json.put("LOLFail", "");
            json.put("LOLWinPoint", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private final void showDialog() {
        List<String> data;
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        if (MainFragment.choose_game == 1) {
            data = Arrays.asList(new String[]{"手Q安卓", "手Q苹果", "微信安卓", "微信苹果"});
        } else {
            data = Arrays.asList(new String[]{"网通", "电信"});
        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        fuwuqi.setText(itemValue);
                        msg = itemValue;
                        ds = itemValue;
                        if (itemValue == "手Q安卓") {
                            gameos = "1";
                            qufu.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType=" + MainFragment.choose_game + "&GameOS=" + gameos;
                            HttpGetUtils.httpGetFile(12, url, handler);
                        } else if (itemValue == "手Q苹果") {
                            gameos = "2";
                            qufu.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType=" + MainFragment.choose_game + "&GameOS=" + gameos;
                            HttpGetUtils.httpGetFile(12, url, handler);
                        } else if (itemValue == "微信安卓") {
                            gameos = "3";
                            qufu.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType=" + MainFragment.choose_game + "&GameOS=" + gameos;
                            HttpGetUtils.httpGetFile(12, url, handler);
                        } else if (itemValue == "微信苹果") {
                            gameos = "4";
                            qufu.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType=" + MainFragment.choose_game + "&GameOS=" + gameos;
                            HttpGetUtils.httpGetFile(12, url, handler);
                        }
                    }
                }).create();
        dialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 8:
                    try {
                        String res = (String) msg.obj;
                        if (res == null)
                            return;
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        if (success == "true") {
                            JSONObject inresult = new JSONObject(data);
                            String orderId = inresult.getString("ID");
                            String money = inresult.getString("Money");
                            OrderInfo.order_id = orderId;
                            OrderInfo.pay_money = money;
                            Intent intent = new Intent(ReleaseOrderActivity.this, PayOrderActivity.class);
                            startActivity(intent);
                            ReleaseOrderActivity.this.finish();
                        } else if (success == "false") {
                            Toast.makeText(ReleaseOrderActivity.this, err, Toast.LENGTH_LONG)
                                    .show();
                            return;
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case 12:
                    try {
                        String oj = (String) msg.obj;
                        if (oj == null) {
                            return;
                        }
                        JSONObject result = new JSONObject(oj);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            JSONArray ar = result.getJSONArray("Data");
                            ArrayList<String> stringArrayList = new ArrayList<String>();
                            for (int i = 0; i < ar.length(); i++) {
                                JSONObject json_data = ar.getJSONObject(i);
                                stringArrayList.add(json_data.getString("GameArea"));
                            }
                            arr = stringArrayList.toArray(new String[stringArrayList.size()]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                case 14:
                case 15: {
                    loadDataToImageView(msg.obj, heroHeads[msg.what - 13]);
                    break;
                }
                case 6:
                    byte[] data1 = (byte[]) msg.obj;
                    if (data1 == null) {
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    Rune1.setImageBitmap(bitmap1);
                    break;
                default:
                    break;
            }
        }
    };

    void loadDataToImageView(Object imgData, ImageView imgView) {
        byte[] data = (byte[]) imgData;
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            imgView.setImageBitmap(bitmap);
            imgView.setVisibility(View.VISIBLE);
        }
    }

    private void showDialog1() {

        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        if (gameos == "1") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "2") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "3") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "4") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "5") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "6") {
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            qufu.setText(itemValue);
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
        } else if (gameos == "") {
            List<String> data = Arrays.asList(new String[]{"请选择游戏区服"});
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            //Toast.makeText(getApplicationContext(), itemValue, Toast.LENGTH_SHORT).show();
                        }
                    }).create();
            dialog.show();
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
                        if (ContextCompat.checkSelfPermission(ReleaseOrderActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat
                                    .requestPermissions(
                                            ReleaseOrderActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);

                        } else {
                            choosePhoto();
                        }
                        break;
                    case TAKE_PICTURE://拍照
                        if (ContextCompat.checkSelfPermission(ReleaseOrderActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat
                                    .requestPermissions(
                                            ReleaseOrderActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                        } else {
                            takePhoto();
                        }

                }
            }
        });
        builder.create().show();
    }

    private void takePhoto() {
        File file = new File(Environment.getExternalStorageDirectory(), "拍照");
        if (!file.exists()) {
            file.mkdir();
        }
        output = new File(file, System.currentTimeMillis() + ".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        tempUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "image.jpg"));
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    private void choosePhoto() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            int i = 0;
            for (String pic : Hero.heros.values()) {
                String url = "/api/File/GetHerohead?filename=" + pic;
                HttpFileHelper.httpGetFile(13 + i++, url, handler);
            }
            for(; i < 3; i++){
                heroHeads[i].setVisibility(View.GONE);
            }
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
                Toast.makeText(ReleaseOrderActivity.this, "文件读取失败", Toast.LENGTH_SHORT).show();
            else {
                Bitmap bmp = (Bitmap) msg.obj;
                if (s == "1") {
                    Rune1.setImageBitmap(bmp);
                } else if (s == "2") {
                    Rune2.setImageBitmap(bmp);
                }
                Toast.makeText(ReleaseOrderActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                //uploadPic(bmp);
            }
        }
    };

    private void startPhotoZoom(Uri uri) {
        // TODO Auto-generated method stub
        if (uri == null) {
            Log.i("tag", "the uri is not exist");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        //intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    private void setImageToView(Intent data) {
        // TODO Auto-generated method stub
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            if (s == "1") {
                Rune1.setImageBitmap(photo);
            } else if (s == "2") {
                Rune2.setImageBitmap(photo);
            }
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // TODO Auto-generated method stub
        imagePath = Utils.savePhoto(bitmap,
                Environment.getExternalStorageDirectory().getAbsolutePath(),
                String.valueOf(System.currentTimeMillis()));
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
            String actionUrl = Url.urlUploadFile + "order";
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
                image_url1 = data;
            }
            if (s == "2") {
                JSONObject result = new JSONObject(b.toString().trim());
                String data = result.getString("Data");
                image_url2 = data;
            }
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            Toast.makeText(ReleaseOrderActivity.this, "上传失败，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Looper.prepare();//创建消息循环
            uploadFile();
            Message msg = new Message();
            //handler.sendMessage(msg);
            Looper.loop();//从消息队列取消
            System.out.print(msg);
        }

    };


}
