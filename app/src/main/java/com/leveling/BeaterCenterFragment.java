package com.leveling;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leveling.entity.BeaterOrder;
import com.leveling.utils.HttpFileHelper;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 11:39
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class BeaterCenterFragment extends Fragment implements View.OnClickListener {
    public static String money;
    private ImageView activity_open_vip_headicon;
    private TextView namePP;
    private LinearLayout tv_beater_center_mine_monery,tv_beater_center_mine_order,tv_beater_center_zijin_detial,tv_beater_center_order_detial,
            tv_beater_center_vip,tv_beater_center_kf,tv_beater_center_yqhy,tv_beater_center_seting,tv_beater_center_upload_video,yijieorder,daiquerenOrder,kejieorder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.beater_center_layout,null);
//        yijieorder = (LinearLayout) view.findViewById(R.id.yijieorder);
//        daiquerenOrder = (LinearLayout) view.findViewById(R.id.daiquerenOrder);
//        kejieorder = (LinearLayout) view.findViewById(R.id.kejieorder);
        tv_beater_center_mine_monery = (LinearLayout) view.findViewById(R.id.tv_beater_center_mine_monery);
        tv_beater_center_mine_order = (LinearLayout) view.findViewById(R.id.tv_beater_center_mine_order);
        tv_beater_center_zijin_detial = (LinearLayout) view.findViewById(R.id.tv_beater_center_zijin_detial);
        tv_beater_center_order_detial = (LinearLayout) view.findViewById(R.id.tv_beater_center_order_detial);
        tv_beater_center_vip = (LinearLayout) view.findViewById(R.id.tv_beater_center_vip);
        tv_beater_center_kf = (LinearLayout) view.findViewById(R.id.tv_beater_center_kf);
        tv_beater_center_yqhy = (LinearLayout) view.findViewById(R.id.tv_beater_center_yqhy);
        tv_beater_center_seting = (LinearLayout) view.findViewById(R.id.tv_beater_center_seting);
        tv_beater_center_upload_video = (LinearLayout) view.findViewById(R.id.tv_beater_center_upload_video);
//        yijieorder.setOnClickListener(this);
//        daiquerenOrder.setOnClickListener(this);
//        kejieorder.setOnClickListener(this);
        tv_beater_center_mine_monery.setOnClickListener(this);
        tv_beater_center_mine_order.setOnClickListener(this);
        tv_beater_center_zijin_detial.setOnClickListener(this);
        tv_beater_center_order_detial.setOnClickListener(this);
        tv_beater_center_vip.setOnClickListener(this);
        tv_beater_center_kf.setOnClickListener(this);
        tv_beater_center_yqhy.setOnClickListener(this);
        tv_beater_center_seting.setOnClickListener(this);
        tv_beater_center_upload_video.setOnClickListener(this);
        activity_open_vip_headicon = (ImageView) view.findViewById(R.id.activity_open_vip_headicon);
        activity_open_vip_headicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SetingActivity.class));
            }
        });
        namePP = (TextView) view.findViewById(R.id.namePP);
        if (HttpPostUtils.getNickname()=="null"){
            namePP.setText("昵称");
            if (HttpPostUtils.getPicture()=="null"){
                activity_open_vip_headicon.setImageResource(R.drawable.userhead);
            }else{
                String uuq = "/api/Users/GetPicture";
                HttpGetUtils.httpGetFile(102,uuq,handler);
            }
        }else{
            String uu = "/api/Users/GetNickName";
            HttpGetUtils.httpGetFile(101,uu,handler);
        }
        return view;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
//            case R.id.yijieorder:
//                startActivity(new Intent(getActivity(),Beater_Order_Activity.class));
//                break;
//            case R.id.daiquerenOrder:
//                BeaterOrder.orderType="2";
//                startActivity(new Intent(getActivity(),Beater_Order_Activity.class));
//                break;
//            case R.id.kejieorder:
//                startActivity(new Intent(getActivity(),MainActivity.class));
//                break;
            case R.id.tv_beater_center_mine_monery:
                startActivity(new Intent(getActivity(),MyWalletActivity.class));
                break;
            case R.id.tv_beater_center_mine_order:
                startActivity(new Intent(getActivity(),Beater_Order_Activity.class));
                break;
            case R.id.tv_beater_center_zijin_detial:
                startActivity(new Intent(getActivity(),ZiJinGuanLiActivity.class));
                break;
            case R.id.tv_beater_center_order_detial:
                BeaterOrder.orderType = "10";
                startActivity(new Intent(getActivity(),Beater_Order_Activity.class));
                break;
            case R.id.tv_beater_center_vip:
                startActivity(new Intent(getActivity(),VIPActivity.class));
                break;
            case R.id.tv_beater_center_kf:
                startActivity(new Intent(getActivity(),SimpleContactActivity.class));
                break;
            case R.id.tv_beater_center_yqhy:
                startActivity(new Intent(getActivity(),ShareActivity.class));
                break;
            case R.id.tv_beater_center_seting:
                startActivityForResult(new Intent(getActivity(),SetingActivity.class),21);
                break;
            case R.id.tv_beater_center_upload_video:
                startActivity(new Intent(getActivity(),UpLoadVideoActivity.class));
                break;
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
                case 101:
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
                                namePP.setText(data);
                                String uuq = "/api/Users/GetPicture";
                                HttpGetUtils.httpGetFile(102,uuq,handler);
                            }else{
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 102:
                    String res2 = (String) msg.obj;
                    if (res2 == null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res2);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success=="true"){
                                if (data.equals("")){
                                    activity_open_vip_headicon.setBackgroundResource(R.drawable.userhead);
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
                case 6:
                    byte[] data1 = (byte[])msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    activity_open_vip_headicon.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        String uu = "/api/Users/GetNickName";
        HttpGetUtils.httpGetFile(101,uu,handler);
    }
}
