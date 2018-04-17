package com.leveling.personcenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.PayOrderActivity;
import com.leveling.R;
import com.leveling.ShowImage1Activity;
import com.leveling.ShowImageActivity;
import com.leveling.TuikaunActivity;
import com.leveling.ZzRatingBar;
import com.leveling.entity.OrderInfo;
import com.leveling.entity.OrderType;
import com.leveling.new_chat.ChatActivity;
import com.leveling.utils.BitmapLoader;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;
import com.leveling.utils.HttpFileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class LevelingActivity extends AppCompatActivity implements View.OnClickListener {

    private ZzRatingBar ratingBar;

    private TextView tvResult;

    private TextView your_order_state, your_game_name, your_order_title, your_game, your_type, your_content, your_qufu, your_time, your_money, your_hero_number, your_level, your_zhanghao, your_mima, your_hero, your_beater, your_release_time, your_order_numbers;
    private ImageView your_order_detial_header, ds_zj10, ds_zj20, ds_zj30;
    private Button your_order_details_pingjia, your_contact, your_order_details_delete, your_order_details_topay, your_order_details_revoke,
            your_order_details_finish, your_order_details_agree, your_order_details_disagree, your_order_details_agree_money, stop_money, kefu_money;
    private RelativeLayout your_details;
    private LinearLayout tk_res, tk_mon, cd_res, cd_zj, dwjt, mwdj, zjjt, tk_mon1, ppppp;
    public static String img1, img2;
    public static String money, Orderid, receiverId, receivernickname;
    private JSONObject json = new JSONObject();
    private List<HashMap<String, Object>> list;
    private String orderType;
    private LinearLayout pingjia;
    private int pingjia1;
    private TextView tk_re, tk_mo, cd_re, tk_mo1;
    private String cdimg1, cdimg2, cdimg3;
    private boolean click = true;
    Dialog dia, dia1, dia2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leveling);
        LinearLayout img_orderpp_back = (LinearLayout) findViewById(R.id.img_orderpp_back);
        img_orderpp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(LevelingActivity.this,AllOrdersActivity.class);
                //startActivity(intent);
                finish();
            }
        });
        Intent intent = getIntent();
        orderType = intent.getStringExtra("orderidType");
        Orderid = intent.getStringExtra("orderid");
        String url = "/api/Order/GetPilblisherOrderInfor?orderid=" + Orderid;
        HttpGetUtils.httpGetFile(10, url, handler);
        ds_zj10 = (ImageView) findViewById(R.id.ds_zj10);
        ds_zj20 = (ImageView) findViewById(R.id.ds_zj20);
        ds_zj30 = (ImageView) findViewById(R.id.ds_zj30);
        ds_zj10.setOnClickListener(this);
        ds_zj20.setOnClickListener(this);
        ds_zj30.setOnClickListener(this);
        tk_mon1 = (LinearLayout) findViewById(R.id.tk_mon1);
        tk_mo1 = (TextView) findViewById(R.id.tk_mo1);
        zjjt = (LinearLayout) findViewById(R.id.zjjt);
        ppppp = (LinearLayout) findViewById(R.id.ppppp);
        your_order_state = (TextView) findViewById(R.id.your_order_state);
        your_contact = (Button) findViewById(R.id.your_contact);
        dwjt = (LinearLayout) findViewById(R.id.dwjt);
        mwdj = (LinearLayout) findViewById(R.id.mwdj);
        your_details = (RelativeLayout) findViewById(R.id.your_details);
        if (orderType.equals("2") || orderType.equals("3") || orderType.equals("4") || orderType.equals("5") || orderType.equals("6") || orderType.equals("7")) {
            your_details.setVisibility(View.VISIBLE);
        }
        your_order_detial_header = (ImageView) findViewById(R.id.your_order_detial_header);
        your_game_name = (TextView) findViewById(R.id.your_game_name);
        your_contact = (Button) findViewById(R.id.your_contact);
        your_contact.setOnClickListener(this);
        pingjia = (LinearLayout) findViewById(R.id.pingjia);
        your_order_title = (TextView) findViewById(R.id.your_order_title);
        your_game = (TextView) findViewById(R.id.your_game);
        your_type = (TextView) findViewById(R.id.your_type);
        your_content = (TextView) findViewById(R.id.your_content);
        your_qufu = (TextView) findViewById(R.id.your_qufu);
        your_time = (TextView) findViewById(R.id.your_time);
        your_money = (TextView) findViewById(R.id.your_money);
        your_hero_number = (TextView) findViewById(R.id.your_hero_number);
        //your_hero_number.setOnClickListener(this);
        your_level = (TextView) findViewById(R.id.your_level);
        tk_re = (TextView) findViewById(R.id.tk_re);
        tk_mo = (TextView) findViewById(R.id.tk_mo);
        //your_level.setOnClickListener(this);
        your_zhanghao = (TextView) findViewById(R.id.your_zhanghao);
        your_mima = (TextView) findViewById(R.id.your_mima);
        your_hero = (TextView) findViewById(R.id.your_herodd);
        your_beater = (TextView) findViewById(R.id.your_beater);
        tk_res = (LinearLayout) findViewById(R.id.tk_res);
        tk_mon = (LinearLayout) findViewById(R.id.tk_mon);
        cd_res = (LinearLayout) findViewById(R.id.cd_res);
        cd_re = (TextView) findViewById(R.id.cd_re);
        cd_zj = (LinearLayout) findViewById(R.id.cd_zj);
        your_release_time = (TextView) findViewById(R.id.your_release_time);
        your_order_numbers = (TextView) findViewById(R.id.your_order_numbers);
        your_order_details_delete = (Button) findViewById(R.id.your_order_details_delete);
        your_order_details_delete.setOnClickListener(this);
        your_order_details_topay = (Button) findViewById(R.id.your_order_details_topay);
        your_order_details_topay.setOnClickListener(this);
        your_order_details_revoke = (Button) findViewById(R.id.your_order_details_revoke);
        your_order_details_revoke.setOnClickListener(this);
        your_order_details_finish = (Button) findViewById(R.id.your_order_details_finish);
        your_order_details_finish.setOnClickListener(this);
        your_order_details_agree = (Button) findViewById(R.id.your_order_details_agree);
        your_order_details_agree.setOnClickListener(this);
        your_order_details_disagree = (Button) findViewById(R.id.your_order_details_disagree);
        your_order_details_disagree.setOnClickListener(this);
        your_order_details_agree_money = (Button) findViewById(R.id.your_order_details_agree_money);
        your_order_details_agree_money.setOnClickListener(this);
        your_order_details_pingjia = (Button) findViewById(R.id.your_order_details_pingjia);
        your_order_details_pingjia.setOnClickListener(this);
        stop_money = (Button) findViewById(R.id.stop_money);
        stop_money.setOnClickListener(this);
        kefu_money = (Button) findViewById(R.id.kefu_money);
        kefu_money.setOnClickListener(this);
        ratingBar = (ZzRatingBar) findViewById(R.id.zzratingbar);
        tvResult = (TextView) findViewById(R.id.tv_result);
        ratingBar.setStarSizeInPixel(100);
        ratingBar.setClickEnable(true);
        ratingBar.setOnRatingChangedListener(new ZzRatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChanged(int current, int count) {
                tvResult.setText("rating:" + current + ",total:" + count);
                pingjia1 = current;
            }
        });
        //首页
        if (orderType.length() == 0) {
            your_order_state.setText("未付款");
            your_order_details_delete.setVisibility(View.VISIBLE);
            your_order_details_topay.setVisibility(View.VISIBLE);
        }
        if (orderType.equals("0")) {
            your_order_state.setText("未付款");
            your_order_details_delete.setVisibility(View.VISIBLE);
            your_order_details_topay.setVisibility(View.VISIBLE);
        }

        //未接手
        if (orderType.equals("10")) {
            your_order_state.setText("未接手");
            your_order_details_revoke.setVisibility(View.VISIBLE);
        }
        //未接手
        if (orderType.equals("1")) {
            your_order_state.setText("未接手");
            your_order_details_revoke.setVisibility(View.VISIBLE);
        }
        //代练中
        if (orderType.equals("2")) {
            your_order_state.setText("代练中");
        }
        //撤单中
        if (orderType.equals("3")) {
            your_order_state.setText("撤单中");
            cd_res.setVisibility(View.VISIBLE);
            cd_zj.setVisibility(View.VISIBLE);
            your_order_details_agree.setVisibility(View.VISIBLE);
            your_order_details_disagree.setVisibility(View.VISIBLE);
            zjjt.setVisibility(View.VISIBLE);
        }
        //待确认
        if (orderType.equals("4")) {
            your_order_state.setText("待确认");
            your_order_details_finish.setVisibility(View.VISIBLE);
            your_order_details_agree_money.setVisibility(View.VISIBLE);
        }
        //退款中
        if (orderType.equals("5")) {
            your_order_state.setText("退款中");
            stop_money.setVisibility(View.VISIBLE);
            //tk_res.setVisibility(View.VISIBLE);
        }
        //待评价
        if (orderType.equals("6")) {
            your_order_state.setText("待评价");
            your_order_details_pingjia.setVisibility(View.VISIBLE);
            pingjia.setVisibility(View.VISIBLE);
        }
        //已完成
        if (orderType.equals("7")) {
            your_order_state.setText("已完成");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(LevelingActivity.this,AllOrdersActivity.class);
        //startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.your_contact:
                if (receiverId == null) {
                    return;
                }
//                Intent intent0 = new Intent(LevelingActivity.this, ListViewChatActivity.class);
//                intent0.putExtra("friID", receiverId);
//                intent0.putExtra("orderID", Orderid);
//                startActivity(intent0);
                Intent it = new Intent(LevelingActivity.this, ChatActivity.class);
                it.putExtra("orderID", Orderid);
                startActivity(it);
                break;
            case R.id.your_hero_number:
                Intent intent = new Intent(LevelingActivity.this, ShowImageActivity.class);
                startActivity(intent);
                break;
            case R.id.your_level:
                Intent intent1 = new Intent(LevelingActivity.this, ShowImage1Activity.class);
                startActivity(intent1);
                break;
            case R.id.your_order_details_delete:
                getData(json);
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认删除订单?")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                OrderType.orderType = "8";
                                getData(json);
                                String url = "/api/Order/PostDelOrder";
                                HttpPostUtils.httpPostFile(12, url, json, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.your_order_details_topay: {
                OrderInfo.order_id = Orderid;
                OrderInfo.pay_money = money;
                startActivity(new Intent(LevelingActivity.this, PayOrderActivity.class));
                finish();
                break;
            }
            case R.id.your_order_details_revoke:
                getData(json);
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认撤销订单？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                OrderType.orderType = "103";
                                getData(json);
                                String url13 = "/api/Order/PostRevoke";
                                HttpPostUtils.httpPostFile(13, url13, json, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.your_order_details_finish:
                getData(json);
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认已完成订单？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                getData(json);
                                String url14 = "/api/Order/PostPiblisherFinish?Orderid=" + Orderid;
                                HttpPostUtils.httpPostFile(14, url14, json, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框

                break;
            case R.id.your_order_details_agree:
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认同意打手撤单？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                OrderType.orderType = "123";
                                if (click) {
                                    click = false;
                                    your_order_details_disagree.setClickable(false);
                                }
                                String url15 = "/api/Order/GetPiblisherCheck?orderid=" + Orderid + "&result=" + 1;
                                HttpGetUtils.httpGetFile(15, url15, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.your_order_details_disagree:
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("您不同意撤单客服将会介入")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                if (click) {
                                    click = false;
                                    your_order_details_disagree.setClickable(false);
                                }
                                String url16 = "/api/Order/GetPiblisherCheck?orderid=" + Orderid + "&result=" + 0;
                                HttpGetUtils.httpGetFile(16, url16, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框

                break;
            case R.id.your_order_details_agree_money:
                Intent in = new Intent(LevelingActivity.this, TuikaunActivity.class);
                in.putExtra("orderid", Orderid);
                in.putExtra("orderNo", your_order_numbers.getText().toString());
                startActivity(in);
                finish();
                break;
            case R.id.your_order_details_pingjia:
                if (pingjia1 == 0) {
                    Toast.makeText(LevelingActivity.this, "请先进行评价", Toast.LENGTH_LONG).show();
                } else {
                    new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                            .setMessage("确认提交评价?")//设置显示的内容
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    // TODO Auto-generated method stub
                                    String url17 = "/api/Order/GetPiblisherEvaluate?orderid=" + Orderid + "&Credit=" + pingjia1;
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
            case R.id.stop_money:
                new AlertDialog.Builder(LevelingActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认取消退款？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                OrderType.orderType = "4";
                                String url18 = "/api/Order/GetUnRefund?orderid=" + Orderid;
                                HttpGetUtils.httpGetFile(18, url18, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            case R.id.ds_zj10:
                if(dia != null)
                    dia.show();
                break;
            case R.id.ds_zj20:
                if(dia1 != null)
                    dia1.show();
                break;
            case R.id.ds_zj30:
                if(dia2 != null)
                    dia2.show();
                break;
            default:
                break;
        }
    }

    public void getData(JSONObject json) {
        try {
            json.put("Orderid", Orderid);
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
                        } else {
                            JSONObject result = new JSONObject(res);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            if (success == "true") {
                                String data = result.getString("Data");
                                JSONObject str = new JSONObject(data);
                                Orderid = str.getString("OrderID");
                                String reciverId = str.getString("ReceiverNumber");
                                your_game_name.setText(HttpPostUtils.getNickname());
                                your_order_title.setText(str.getString("Title"));
                                if (str.getInt("GameType") == 1) {
                                    your_game.setText("王者荣耀");
                                    //dwjt.setVisibility(View.VISIBLE);
                                    //mwdj.setVisibility(View.VISIBLE);
                                    ppppp.setVisibility(View.VISIBLE);
                                    your_content.setText(str.getString("PiblisherBigRank") + str.getString("PiblisherMediumRank") + str.getString("PiblisherRank") + "到" + str.getString("PiblisherGoalBigRank") + str.getString("PiblisherGoalMediumRank") + str.getString("PiblisherGoalRank"));
                                    if (str.getInt("Type") == 1) {
                                        your_type.setText("代练");
                                    } else if (str.getInt("Type") == 2) {
                                        your_type.setText("陪练");
                                    }
                                } else if (str.getInt("GameType") == 2) {
                                    your_game.setText("英雄联盟");
                                    //dwjt.setVisibility(View.GONE);
                                    //mwdj.setVisibility(View.GONE);
                                    ppppp.setVisibility(View.GONE);
                                    your_type.setText(str.getString("LOLType"));
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
                                money = str.getString("OrderPrice");
                                your_release_time.setText(str.getString("PiblishDateTime"));
                                your_order_numbers.setText(str.getString("OrderNumber"));
                                tk_re.setText(str.getString("Cause"));
                                img1 = str.getString("HeroNumber");
                                img2 = str.getString("Rune");
                                receiverId = str.getString("ReceiverID");
                                receivernickname = str.getString("ReceiverNickName");
                                your_zhanghao.setText(str.getString("GameAccount"));
                                your_mima.setText(str.getString("GamePwd"));
                                tk_re.setText(str.getString("Cause"));
                                if (str.getString("DamageType").equals("1")) {
                                    tk_mo.setText("￥" + Float.parseFloat(str.getString("OrderPrice")));
                                    tk_mon.setVisibility(View.VISIBLE);
                                } else if (str.getString("DamageType").equals("2")) {
                                    tk_mo1.setText("￥" + Float.parseFloat(str.getString("Damage")));
                                    tk_mon1.setVisibility(View.VISIBLE);
                                }
                                cd_re.setText(str.getString("Cause"));
                                cdimg1 = str.getString("Evidence1");
                                cdimg2 = str.getString("Evidence2");
                                cdimg3 = str.getString("Evidence3");
                                String url = "/api/File/GetUserhead?filename=" + HttpPostUtils.getPicture();
                                BitmapLoader.getImageFromLocalFirstNorDownload(url, "userhead", HttpPostUtils.getPicture(), handler, 111);
                                if (str.getString("ReceiverHero").equals("null")) {
                                    your_hero.setText(str.getString(""));
                                } else {
                                    your_hero.setText(str.getString("ReceiverHero"));
                                }
                                your_beater.setText(str.getString("ReceiverNumber2"));
                            } else if (success == "false") {
                                Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 111:
                    Bitmap bmp = (Bitmap)msg.obj;
                    if(bmp != null)
                        your_order_detial_header.setImageBitmap(bmp);
                    String url = "/api/File/GetRevoke?filename=" + cdimg1;
                    HttpFileHelper.httpGetFile(1, url, handler);
                    break;
                case 1:
                    byte[] dataa = (byte[]) msg.obj;
                    Bitmap bitmapa = BitmapFactory.decodeByteArray(dataa, 0, dataa.length);
                    ds_zj10.setImageBitmap(bitmapa);
                    Context context = LevelingActivity.this;
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
                    String url12 = "/api/File/GetRevoke?filename=" + cdimg2;
                    HttpFileHelper.httpGetFile(2, url12, handler);
                    break;
                case 2:
                    byte[] data1 = (byte[]) msg.obj;
                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    ds_zj20.setImageBitmap(bitmap1);
                    Context context1 = LevelingActivity.this;
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
                    String url1 = "/api/File/GetRevoke?filename=" + cdimg3;
                    HttpFileHelper.httpGetFile(3, url1, handler);
                    break;
                case 3:
                    byte[] data2 = (byte[]) msg.obj;
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                    ds_zj30.setImageBitmap(bitmap2);
                    Context context2 = LevelingActivity.this;
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
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(LevelingActivity.this, "删除成功！", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 13:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(LevelingActivity.this, "撤单成功", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 14:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            OrderType.orderType = "101";
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 15:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(LevelingActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 16:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                            Toast.makeText(LevelingActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 17:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            OrderType.orderType = "3";
                            Toast.makeText(LevelingActivity.this, "评价成功", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 18:
                    try {
                        String res = (String) msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(LevelingActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(LevelingActivity.this, AllOrdersActivity.class);
                            //startActivity(intent);
                            finish();
                        }
                        if (success == "false") {
                            Toast.makeText(LevelingActivity.this, err, Toast.LENGTH_LONG).show();
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
