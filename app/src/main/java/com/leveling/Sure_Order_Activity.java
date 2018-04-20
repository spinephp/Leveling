package com.youyudj.leveling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.BeaterOrder;
import com.youyudj.leveling.new_chat.ChatActivity;
import com.youyudj.leveling.utils.HttpFileHelper;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class Sure_Order_Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView your_order_state, your_game_name, your_order_title, your_game, your_type, your_content, your_qufu, your_time, your_money, your_hero_number, your_level, your_zhanghao, your_mima, your_hero, your_beater, your_release_time, your_order_numbers;
    private ImageView your_order_detial_header, ds_zj10, ds_zj20, ds_zj30;
    private Button in_your_order_details_cxcd, in_your_order_details_pingjia, your_contact, your_order_details_delete, your_order_details_topay, your_order_details_revoke,
            your_order_details_finish, your_order_details_agree, your_order_details_disagree, your_order_details_agree_money;
    private RelativeLayout your_details;
    private LinearLayout xxx1, xxx2, xxx3, cd_zj1, dwjt1, mwdj1, zdyx;
    private TextView in_your_tkr, in_your_tkm;
    public static String img1, img2, tt;
    private JSONObject json = new JSONObject();
    private List<HashMap<String, Object>> list;
    private String test;
    private LinearLayout dashoupingjia1, zjjt;
    private ZzRatingBar ratingBar1;
    private TextView tvResult1, in_your_tkm1;
    private int pingjia11;
    private String zjimg1, zjimg2, zjimg3;
    public static String Orderid, receiverId, receivernickname;
    private boolean click = true;
    Dialog dia, dia1, dia2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure__order);
        LinearLayout img_orderpp_back = (LinearLayout) findViewById(R.id.img_orderpp_back);
        img_orderpp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        test = intent.getStringExtra("orderid");
        String url = "/api/Order/GetReceiverOrderInfor?orderid=" + test;
        HttpGetUtils.httpGetFile(10, url, handler);
        zdyx = (LinearLayout) findViewById(R.id.zdyx);
        if (HttpPostUtils.getRole() == 1 || HttpPostUtils.getRole() == 2 || HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4) {
            zdyx.setVisibility(View.VISIBLE);
        } else {
            zdyx.setVisibility(View.GONE);
        }
        your_order_state = (TextView) findViewById(R.id.in_your_order_state);
        zjjt = (LinearLayout) findViewById(R.id.zjjt1);
        your_contact = (Button) findViewById(R.id.in_your_contact);
        your_details = (RelativeLayout) findViewById(R.id.in_your_details);
        xxx1 = (LinearLayout) findViewById(R.id.xxx1);
        xxx2 = (LinearLayout) findViewById(R.id.xxx2);
        xxx3 = (LinearLayout) findViewById(R.id.xxx3);
        dwjt1 = (LinearLayout) findViewById(R.id.dwjt1);
        mwdj1 = (LinearLayout) findViewById(R.id.mwdj1);
        cd_zj1 = (LinearLayout) findViewById(R.id.cd_zj1);
        in_your_tkr = (TextView) findViewById(R.id.in_your_tkr);
        in_your_tkm1 = (TextView) findViewById(R.id.in_your_tkm1);
        in_your_tkm = (TextView) findViewById(R.id.in_your_tkm);
        ds_zj10 = (ImageView) findViewById(R.id.ds_zj101);
        ds_zj20 = (ImageView) findViewById(R.id.ds_zj201);
        ds_zj30 = (ImageView) findViewById(R.id.ds_zj301);
        ds_zj10.setOnClickListener(this);
        ds_zj20.setOnClickListener(this);
        ds_zj30.setOnClickListener(this);
        your_order_detial_header = (ImageView) findViewById(R.id.in_your_order_detial_header);
        your_game_name = (TextView) findViewById(R.id.in_your_game_name);
        your_contact = (Button) findViewById(R.id.in_your_contact);
        your_contact.setOnClickListener(this);
        dashoupingjia1 = (LinearLayout) findViewById(R.id.dashoupingjia1);
        your_order_title = (TextView) findViewById(R.id.in_your_order_title);
        your_game = (TextView) findViewById(R.id.in_your_game);
        your_type = (TextView) findViewById(R.id.in_your_type);
        your_content = (TextView) findViewById(R.id.in_your_content);
        your_qufu = (TextView) findViewById(R.id.in_your_qufu);
        your_time = (TextView) findViewById(R.id.in_your_time);
        your_money = (TextView) findViewById(R.id.in_your_money);
        your_hero_number = (TextView) findViewById(R.id.in_your_hero_number);
        //your_hero_number.setOnClickListener(this);
        your_level = (TextView) findViewById(R.id.in_your_level);
        //your_level.setOnClickListener(this);
        your_zhanghao = (TextView) findViewById(R.id.in_your_zhanghao);
        your_mima = (TextView) findViewById(R.id.in_your_mima);
        your_hero = (TextView) findViewById(R.id.in_your_hero);
        your_beater = (TextView) findViewById(R.id.in_your_beater);
        your_release_time = (TextView) findViewById(R.id.in_your_release_time);
        your_order_numbers = (TextView) findViewById(R.id.in_your_order_numbers);
        //评价
        ratingBar1 = (ZzRatingBar) findViewById(R.id.zzratingbar1);
        tvResult1 = (TextView) findViewById(R.id.tv_result1);
        ratingBar1.setStarSizeInPixel(100);
        ratingBar1.setClickEnable(true);
        ratingBar1.setOnRatingChangedListener(new ZzRatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChanged(int current, int count) {
                tvResult1.setText("rating:" + current + ",total:" + count);
                pingjia11 = current;
            }
        });

        your_order_details_revoke = (Button) findViewById(R.id.in_your_order_details_revoke);
        your_order_details_revoke.setOnClickListener(this);
        your_order_details_finish = (Button) findViewById(R.id.in_your_order_details_finish);
        your_order_details_finish.setOnClickListener(this);
        your_order_details_agree = (Button) findViewById(R.id.in_your_order_details_agree);
        your_order_details_agree.setOnClickListener(this);
        your_order_details_disagree = (Button) findViewById(R.id.in_your_order_details_disagree);
        your_order_details_disagree.setOnClickListener(this);
        your_order_details_agree_money = (Button) findViewById(R.id.in_your_order_details_agree_money);
        your_order_details_agree_money.setOnClickListener(this);
        in_your_order_details_pingjia = (Button) findViewById(R.id.in_your_order_details_pingjia);
        in_your_order_details_pingjia.setOnClickListener(this);
        in_your_order_details_cxcd = (Button) findViewById(R.id.in_your_order_details_cxcd);
        in_your_order_details_cxcd.setOnClickListener(this);
        //代练中
        if (Beater_Order_Activity.test == "") {
            your_order_state.setText("代练中");
            your_order_details_revoke.setVisibility(View.VISIBLE);
            your_order_details_finish.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "100") {
            your_order_state.setText("代练中");
            your_order_details_revoke.setVisibility(View.VISIBLE);
            your_order_details_finish.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "2") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("代练中");
            your_order_details_revoke.setVisibility(View.VISIBLE);
            your_order_details_finish.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "3") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("撤单中");
            in_your_order_details_cxcd.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "4") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("待确认");
            // your_order_details_finish.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "5") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("退款中");
            your_order_details_disagree.setVisibility(View.VISIBLE);
            your_order_details_agree.setVisibility(View.VISIBLE);
            zjjt.setVisibility(View.VISIBLE);
            cd_zj1.setVisibility(View.VISIBLE);
            xxx1.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "6") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("待评价");
            dashoupingjia1.setVisibility(View.VISIBLE);
            in_your_order_details_pingjia.setVisibility(View.VISIBLE);
        } else if (Beater_Order_Activity.test == "20") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("已完成");
        } else if (Beater_Order_Activity.test == "7") {
            Beater_Order_Activity.test.isEmpty();
            your_order_state.setText("已完成");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.in_your_contact:
                //Intent intent0 = new Intent(Sure_Order_Activity.this, ListViewChat1Activity.class);
                Intent it = new Intent(Sure_Order_Activity.this, ChatActivity.class);
                it.putExtra("orderID", test);
                startActivity(it);
                //startActivity(intent0);
                break;
            case R.id.in_your_hero_number:
                Intent intent = new Intent(Sure_Order_Activity.this, CheckImgActivity.class);
                startActivity(intent);
                break;
            case R.id.in_your_level:
                Intent intent1 = new Intent(Sure_Order_Activity.this, CheckImgActivity1.class);
                startActivity(intent1);
                break;
            case R.id.in_your_order_details_revoke:
                Intent inte = new Intent(Sure_Order_Activity.this, DashouChedanActivity.class);
                inte.putExtra("orderid", test);
                startActivity(inte);
                finish();
                break;
            case R.id.in_your_order_details_finish:
                new AlertDialog.Builder(Sure_Order_Activity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认订单已完成？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                BeaterOrder.orderType = "2";
                                String url14 = "/api/Order/GetReceiverFinish?Orderid=" + test;
                                HttpGetUtils.httpGetFile(14, url14, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.in_your_order_details_agree:
                new AlertDialog.Builder(Sure_Order_Activity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认同意退款？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                BeaterOrder.orderType = "3";
                                if (click) {
                                    click = false;
                                    your_order_details_agree.setClickable(false);
                                }
                                String url15 = "/api/Order/GetReceiverCheck?orderid=" + test + "&result=" + 1;
                                HttpGetUtils.httpGetFile(15, url15, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.in_your_order_details_disagree:
                new AlertDialog.Builder(Sure_Order_Activity.this).setTitle("提示")//设置对话框标题
                        .setMessage("您不同意退款客服将会介入")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                if (click) {
                                    click = false;
                                    your_order_details_disagree.setClickable(false);
                                }
                                String url16 = "/api/Order/GetReceiverCheck?orderid=" + test + "&result=" + 0;
                                HttpGetUtils.httpGetFile(16, url16, handler);

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.in_your_order_details_agree_money:

                break;
            case R.id.in_your_order_details_pingjia:
                if (pingjia11 == 0) {
                    Toast.makeText(Sure_Order_Activity.this, "请进行评价", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(Sure_Order_Activity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确定提交评价？")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    BeaterOrder.orderType = "4";
                                    String url17 = "/api/Order/GetReceiverEvaluate?orderid=" + test + "&Credit=" + pingjia11;
                                    HttpGetUtils.httpGetFile(17, url17, handler);
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//响应事件
                            // TODO Auto-generated method stub
                        }
                    }).show();//在按键响应事件中显示此对话框
                }
                break;
            case R.id.in_your_order_details_cxcd:
                new AlertDialog.Builder(Sure_Order_Activity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确定取消撤单？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                BeaterOrder.orderType = "5";
                                String url18 = "/api/Order/GetUnReceiverRevoke?orderid=" + test;
                                HttpGetUtils.httpGetFile(18, url18, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.ds_zj101:
                if(dia != null)
                    dia.show();
                break;
            case R.id.ds_zj201:
                if(dia1 != null)
                    dia1.show();
                break;
            case R.id.ds_zj301:
                if(dia2 != null)
                    dia2.show();
                break;
            default:
                break;
        }
    }

    public void getData(JSONObject json) {
        try {
            json.put("Orderid", test);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
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
                            JSONObject str = new JSONObject(data);
                            Orderid = str.getString("OrderID");
                            //Orderid,receiverId,receivernickname
                            receiverId = str.getString("PiblisherID");
                            receivernickname = str.getString("Piblisher");
                            String OrderNumber = str.getString("OrderNumber");
                            your_game_name.setText(str.getString("ReceiverNickName"));
                            your_order_title.setText(str.getString("Title"));
                            if (str.getInt("GameType") == 1) {
                                your_game.setText("王者荣耀");
                                //dwjt1.setVisibility(View.VISIBLE);
                                //mwdj1.setVisibility(View.VISIBLE);
                                if (str.getInt("Type") == 1) {
                                    your_type.setText("代练");
                                } else if (str.getInt("Type") == 2) {
                                    your_type.setText("陪练");
                                }
                                your_content.setText(str.getString("PiblisherBigRank") + str.getString("PiblisherMediumRank") + str.getString("PiblisherRank") + "到" + str.getString("PiblisherGoalBigRank") + str.getString("PiblisherGoalMediumRank") + str.getString("PiblisherGoalRank"));
                            } else if (str.getInt("GameType") == 2) {
                                your_game.setText("英雄联盟");
                                your_type.setText("单双排");
//                                if (str.getString("RankType").equals("1")){
//                                    your_type.setText("单双排");
//                                }else{
//                                    your_type.setText("灵活组排");
//                                }

                                your_content.setText(str.getString("LOLRank") + "到" + str.getString("LOLGoalRank"));
                            }
                            if (Integer.parseInt(str.getString("GameOS")) == 1) {
                                your_qufu.setText("手Q安卓" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 2) {
                                your_qufu.setText("手Q苹果" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 3) {
                                your_qufu.setText("微信安卓" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 4) {
                                your_qufu.setText("微信苹果" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 5) {
                                your_qufu.setText("网通" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 6) {
                                your_qufu.setText("电信" + "/" + str.getString("GameArea"));
                            } else if (Integer.parseInt(str.getString("GameOS")) == 7) {
                                your_qufu.setText("其他" + "/" + str.getString("GameArea"));
                            }
                            your_time.setText(str.getString("TaskTime"));
                            your_money.setText("￥" + str.getString("OrderPrice"));

                            //your_hero.setText(str.getString("ReceiverHero"));
                            your_release_time.setText(str.getString("PiblishDateTime"));
                            your_order_numbers.setText(str.getString("OrderNumber"));
                            your_zhanghao.setText(str.getString("GameAccount"));
                            your_mima.setText(str.getString("GamePwd"));
                            in_your_tkm.setText("￥" + str.getString("OrderPrice"));
                            //Float mo = damage-Float.parseFloat(str.getString("OrderPrice"));
                            if (str.getString("DamageType").equals("1")) {
                                in_your_tkm.setText("￥" + Float.parseFloat(str.getString("OrderPrice")));
                                xxx2.setVisibility(View.VISIBLE);
                            } else if (str.getString("DamageType").equals("2")) {
                                in_your_tkm1.setText("￥" + Float.parseFloat(str.getString("Damage")));
                                xxx3.setVisibility(View.VISIBLE);
                            }
                            in_your_tkr.setText(str.getString("Cause"));
                            //your_beater.setText(str.getString("ReceiverHero"));
                            img1 = str.getString("HeroNumber");
                            img2 = str.getString("Rune");
                            zjimg1 = str.getString("Evidence1");
                            zjimg2 = str.getString("Evidence2");
                            zjimg3 = str.getString("Evidence3");
                            if (str.getString("ReceiverHero").equals("null")) {
                                your_hero.setText(str.getString(""));
                            } else {
                                your_hero.setText(str.getString("ReceiverHero"));
                            }
                            String url = "/api/File/GetUserhead?filename=" + str.getString("PiblisherPicture");
                            HttpFileHelper.httpGetFile(111, url, handler);
                        } else if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 111:
                    byte[] data = (byte[]) msg.obj;
                    if (data == null) {
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    your_order_detial_header.setImageBitmap(bitmap);
                    String url = "/api/File/GetRefund?filename=" + zjimg1;
                    HttpFileHelper.httpGetFile(1, url, handler);
                    break;
                case 1:
                    byte[] dataa = (byte[]) msg.obj;
                    if (dataa == null) {
                        return;
                    }
                    Bitmap bitmapa = BitmapFactory.decodeByteArray(dataa, 0, dataa.length);
                    ds_zj10.setImageBitmap(bitmapa);
                    Context context = Sure_Order_Activity.this;
                    dia = new Dialog(context, R.style.edit_AlertDialog_style);
                    dia.setContentView(R.layout.activity_start_dialog);
                    ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
                    //imageView.setBackgroundResource(R.drawable.imggg1);
                    imageView.setImageBitmap(bitmapa);
                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
                    dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
                    Window w = dia.getWindow();
                    WindowManager.LayoutParams lp = w.getAttributes();
                    lp.x = 0;
                    lp.y = 40;
                    dia.onWindowAttributesChanged(lp);
                    imageView.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dia.dismiss();
                                }
                            });
                    String url12 = "/api/File/GetRefund?filename=" + zjimg2;
                    HttpFileHelper.httpGetFile(2, url12, handler);
                    break;
                case 2:
                    byte[] data1 = (byte[]) msg.obj;
                    if (data1 == null) {
                        return;
                    }
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    ds_zj20.setImageBitmap(bitmap1);
                    Context context1 = Sure_Order_Activity.this;
                    dia1 = new Dialog(context1, R.style.edit_AlertDialog_style);
                    dia1.setContentView(R.layout.activity_start_dialog);
                    ImageView imageView1 = (ImageView) dia1.findViewById(R.id.start_img);
                    //imageView.setBackgroundResource(R.drawable.imggg1);
                    imageView1.setImageBitmap(bitmap1);
                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
                    dia1.setCanceledOnTouchOutside(true); // Sets whether this dialog is
                    Window w1 = dia1.getWindow();
                    WindowManager.LayoutParams lp1 = w1.getAttributes();
                    lp1.x = 0;
                    lp1.y = 40;
                    dia.onWindowAttributesChanged(lp1);
                    imageView1.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dia1.dismiss();
                                }
                            });
                    String url1 = "/api/File/GetRefund?filename=" + zjimg3;
                    HttpFileHelper.httpGetFile(3, url1, handler);
                    break;
                case 3:
                    byte[] data2 = (byte[]) msg.obj;
                    if (data2 == null) {
                        return;
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                    ds_zj30.setImageBitmap(bitmap2);
                    Context context2 = Sure_Order_Activity.this;
                    dia2 = new Dialog(context2, R.style.edit_AlertDialog_style);
                    dia2.setContentView(R.layout.activity_start_dialog);
                    ImageView imageView2 = (ImageView) dia2.findViewById(R.id.start_img);
                    //imageView.setBackgroundResource(R.drawable.imggg1);
                    imageView2.setImageBitmap(bitmap2);
                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
                    dia2.setCanceledOnTouchOutside(true); // Sets whether this dialog is
                    Window w2 = dia2.getWindow();
                    WindowManager.LayoutParams lp2 = w2.getAttributes();
                    lp2.x = 0;
                    lp2.y = 40;
                    dia2.onWindowAttributesChanged(lp2);
                    imageView2.setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dia2.dismiss();
                                }
                            });
                    break;
                case 12:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            // Toast.makeText(Sure_Order_Activity.this,success,Toast.LENGTH_LONG).show();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            // Toast.makeText(Sure_Order_Activity.this,success,Toast.LENGTH_LONG).show();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 15:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            // Toast.makeText(Sure_Order_Activity.this,success,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 16:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            // Toast.makeText(Sure_Order_Activity.this,success,Toast.LENGTH_LONG).show();
                            your_order_details_disagree.setVisibility(View.GONE);
                            your_order_details_agree.setVisibility(View.GONE);
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 17:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 18:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Intent intent = new Intent(Sure_Order_Activity.this, Beater_Order_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(Sure_Order_Activity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }

        }
    };

}
