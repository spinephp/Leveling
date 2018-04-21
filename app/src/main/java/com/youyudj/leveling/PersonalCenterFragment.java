package com.youyudj.leveling;

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
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.OrderType;
import com.youyudj.leveling.personcenter.AllOrdersActivity;
import com.youyudj.leveling.personcenter.PublicActivity;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.youyudj.leveling.utils.HttpFileHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 11:01
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class PersonalCenterFragment extends Fragment {
    private TextView name_under_picture;
    private static final String NICKNAME = "nickname";
    private static final String PICTURE = "picture";
    private ImageView picture;
    public static String test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_center_layout, null);
        picture = (ImageView) view.findViewById(R.id.activity_open_vip_headicon);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SetingActivity.class));
            }
        });
        name_under_picture = (TextView) view.findViewById(R.id.name_picture);
        if (HttpPostUtils.getPicture() == "null") {
            picture.setImageResource(R.drawable.userhead);
            if (HttpPostUtils.getNickname() == "null") {
                name_under_picture.setText("昵称");
            } else {
                String uu = "/api/Users/GetNickName";
                HttpGetUtils.httpGetFile(101, uu, handler);
            }
        } else {
            String url = "/api/File/GetUserhead?filename=" + HttpPostUtils.getPicture();
            HttpFileHelper.httpGetFile(6, url, handler);
        }
        view.findViewById(R.id.la_personal_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShareFriendActivity.class));
            }
        });
        view.findViewById(R.id.la_beaterAuth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "/api/Authentication/GetAuthenticationState";
                HttpGetUtils.httpGetFile(1001, url, handler);
            }
        });
        view.findViewById(R.id.la_personal_monery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ZiJinGuanLiActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_feed_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeedBackActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_im).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SimpleContactActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_all_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderType.orderType = "2";
                startActivity(new Intent(getActivity(), AllOrdersActivity.class));
            }
        });
        view.findViewById(R.id.in_dailian).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = "11";
                OrderType.orderType = "5";
                startActivity(new Intent(getActivity(), AllOrdersActivity.class));
            }
        });
        view.findViewById(R.id.be_finished).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test = "22";
                OrderType.orderType = "6";
                startActivity(new Intent(getActivity(), AllOrdersActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_seting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SetingActivity.class), 1);
            }
        });
        view.findViewById(R.id.ll_personal_center_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PublicActivity.class));
            }
        });
        view.findViewById(R.id.la_personal_mine_orders1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ShareActivity.class));
            }
        });
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what < 0) {
                return;
            }
            switch (msg.what) {
                case 101:
                    String res1 = (String) msg.obj;
                    if (res1 == null) {
                        return;
                    } else {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res1);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success == "true") {
                                name_under_picture.setText(data);
                            } else {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1001:
                    String res2 = (String) msg.obj;
                    if (res2 == null) {
                        return;
                    } else {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res2);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (data.equals("1")) {
                                startActivity(new Intent(getActivity(), BeaterAuthSuccessActivity.class));
                            } else if (data.equals("2")) {
                                Toast.makeText(getActivity(), "您已经通过认证，请重新登陆进入打手界面", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            } else if (data.equals("3")) {
                                startActivity(new Intent(getActivity(), BeaterAuthFailedActivity.class));
                            } else {
                                startActivity(new Intent(getActivity(), BeaterAuthActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 1:
                    String res12 = (String) msg.obj;
                    if (res12 == null) {
                        return;
                    } else {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res12);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success == "true") {
                                name_under_picture.setText(data);
                                String uuq = "/api/Users/GetPicture";
                                HttpGetUtils.httpGetFile(102, uuq, handler);
                            } else {
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 102:
                    String res13 = (String) msg.obj;
                    if (res13 == null) {
                        return;
                    } else {
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res13);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success == "true") {
                                if (data.equals("")) {
                                    picture.setBackgroundResource(R.drawable.userhead);
                                } else {
                                    String url = "/api/File/GetUserhead?filename=" + data;
                                    HttpFileHelper.httpGetFile(6, url, handler);
                                }
                            } else {
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 6:
                    byte[] data1 = (byte[]) msg.obj;
                    if (data1 == null) {
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    picture.setImageBitmap(bitmap);
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
        HttpGetUtils.httpGetFile(1, uu, handler);
    }
}
