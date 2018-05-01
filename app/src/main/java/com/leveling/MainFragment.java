package com.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.leveling.common.CheckVerifyCodeActivity;
import com.leveling.entity.OrderInfo;
import com.leveling.entity.ReleaseOrder;
import com.leveling.entity.UserInfo;
import com.leveling.entity.gametype;
import com.leveling.utils.AnimationUtil;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/8 14:44
 * Update:2017/11/8
 * Version:
 * *******************************************************
 */
public class MainFragment extends Fragment {
    public static int choose_game;
    public static int play_type;
    public static String mm;
    public static String kaishi;
    public static String jieshu;
    private ViewFlipper viewfipper;
    private Toast toast;
    private String[] category1;
    private String[][] category2;
    private TextView begin, true_money, true_time;
    private TextView end;
    private int tag;
    private int tag1;
    private int t;
    public static int j;
    public static int m = 0;
    public static int n;
    public static String nima;
    public static String niba;
    public static String price;
    public static String time;
    private UserInfo userInfo;
    private static final String AUTOLOGIN = "autoLogin";
    PopupWindow pop;
    private JSONObject json = new JSONObject();
    public static String big, middle, small;
    public static String big1, middle1, small1;
    private static final String FIRSTAUTOLOGIN = "firstautoLogin";
    private static final String BEENAUTH = "BEENAUTH";
    private static final String LOGIN = "login";
    private View peiwan, dailian, hero, main_bg, conmit;
    private LinearLayout la_duanwei, pi_duanwei;
    private Button to_release_order;
    private String game_type;
    View king;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().replace(R.id.bannerFragment, new BannerFragment()).commitAllowingStateLoss();
    }

    @Override
    public void onStart() {
        super.onStart();
        king.callOnClick();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_layout, null);
        king = view.findViewById(R.id.ll_main_king);
        king.setSelected(true);
        choose_game = 1;
        play_type = 2;
        game_type = "1";
        ReleaseOrder.levelingId1 = 0;
        ReleaseOrder.levelingId2 = 0;
        to_release_order = (Button) view.findViewById(R.id.to_release_order);
        la_duanwei = (LinearLayout) view.findViewById(R.id.la_duanwei);
        pi_duanwei = (LinearLayout) view.findViewById(R.id.pi_duanwei);
        peiwan = view.findViewById(R.id.daiwofei);
        dailian = view.findViewById(R.id.bangwoda);
        hero = view.findViewById(R.id.ll_main_hero);
        main_bg = view.findViewById(R.id.la_main_bg);
        conmit = view.findViewById(R.id.la_main_conmit);
        true_money = (TextView) view.findViewById(R.id.true_money);
        true_time = (TextView) view.findViewById(R.id.true_time);
        final View view1 = view.findViewById(R.id.view1);
        final View view2 = view.findViewById(R.id.view2);
        begin = (TextView) view.findViewById(R.id.begin_level);
        end = (TextView) view.findViewById(R.id.goal_level);
        userInfo = new UserInfo(getActivity());
        main_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conmit.setClickable(false);
                if (userInfo.getBooleanInfo(LOGIN) == true) {
                    String url = "/api/Authentication/GetAuthenticationState";
                    HttpGetUtils.httpGetFile(1001, url, handler);
                } else if (userInfo.getBooleanInfo(LOGIN) == false) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        king.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gametype.game = "1";
                choose_game = 1;
                game_type = "1";
                begin.setText("选择初始段位");
                end.setText("选择目标段位");
                king.setSelected(true);
                la_duanwei.setClickable(true);
                pi_duanwei.setClickable(true);
                main_bg.setVisibility(View.VISIBLE);
                conmit.setVisibility(View.GONE);
                conmit.setAnimation(AnimationUtil.moveToViewBottom());
                main_bg.setAnimation(AnimationUtil.moveToViewLocation());
            }
        });
        hero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gametype.game = "2";
                game_type = "2";
                ReleaseOrder.levelingId1 = -1;
                ReleaseOrder.levelingId2 = -1;
                choose_game = 2;
                //begin.setText("");
                //end.setText("");
                //true_money.setText("0");
                //true_time.setText("0");
                //king.setSelected(false);
                main_bg.setVisibility(View.VISIBLE);
                conmit.setVisibility(View.GONE);
                startActivityForResult(new Intent(getActivity(), HeroOrder1Activity.class), 1);
            }
        });
        peiwan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_type = 2;
                view1.setBackgroundResource(R.color.colorPrimary);
                view2.setBackgroundResource(0);
                ChooseType();
            }
        });

        dailian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_type = 1;
                view1.setBackgroundResource(0);
                view2.setBackgroundResource(R.color.colorPrimary);
                ChooseType();
            }
        });
        to_release_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ReleaseOrderActivity.class), 1);
            }
        });
        view.findViewById(R.id.la_duanwei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mm = "1";
                if (userInfo.getBooleanInfo(LOGIN) == false) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(getActivity());
                    RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnRegionSelectedListener() {
                        @Override
                        public void onRegionSelected(String[] cityAndArea) {

                            String msg = cityAndArea[0] + cityAndArea[1];
                            ReleaseOrder.message1 = msg;
                            begin.setText(msg);
                            nima = msg;
                            if (cityAndArea[0].equals("最强王者")) {
                                big = cityAndArea[0].substring(cityAndArea[0].length() - 2, cityAndArea[0].length());
                                middle = "";
                                small = cityAndArea[1];
                            } else {
                                big = cityAndArea[0].substring(cityAndArea[0].length() - 3, cityAndArea[0].length() - 1);
                                middle = cityAndArea[0].substring(cityAndArea[0].length() - 1, cityAndArea[0].length());
                                small = cityAndArea[1];
                            }
                            //判断段位
                            int i = 0;
                            String level1 = null;
                            char level2 = ' ';
                            int star = 0;
                            if (cityAndArea[0].length() == 5) {
                                level1 = msg.substring(0, 4);
                                level2 = msg.charAt(4);
                                star = Integer.parseInt(msg.substring(5, 6));
                                if (level1.equals("倔强青铜")) {
                                    i = 0;
                                    switch (level2) {
                                        case '一':
                                            i += 6;
                                            break;
                                        case '二':
                                            i += 3;
                                            break;
                                        case '三':
                                            i += 0;
                                            break;
                                    }
                                } else if (level1.equals("秩序白银")) {
                                    i = 9;
                                    switch (level2) {
                                        case '一':
                                            i += 6;
                                            break;
                                        case '二':
                                            i += 3;
                                            break;
                                        case '三':
                                            i += 0;
                                            break;
                                    }
                                } else if (level1.equals("荣耀黄金")) {
                                    i = 18;
                                    switch (level2) {
                                        case '一':
                                            i += 12;
                                            break;
                                        case '二':
                                            i += 8;
                                            break;
                                        case '三':
                                            i += 4;
                                            break;
                                        case '四':
                                            i += 0;
                                            break;
                                    }
                                } else if (level1.equals("尊贵铂金")) {
                                    i = 34;
                                    switch (level2) {
                                        case '一':
                                            i += 12;
                                            break;
                                        case '二':
                                            i += 8;
                                            break;
                                        case '三':
                                            i += 4;
                                            break;
                                        case '四':
                                            i += 0;
                                            break;
                                    }
                                } else if (level1.equals("永恒钻石")) {
                                    i = 50;
                                    switch (level2) {
                                        case '一':
                                            i += 20;
                                            break;
                                        case '二':
                                            i += 15;
                                            break;
                                        case '三':
                                            i += 10;
                                            break;
                                        case '四':
                                            i += 5;
                                            break;
                                        case '五':
                                            i += 0;
                                            break;
                                    }
                                } else if (level1.equals("至尊星耀")) {
                                    i = 75;
                                    switch (level2) {
                                        case '一':
                                            i += 20;
                                            break;
                                        case '二':
                                            i += 15;
                                            break;
                                        case '三':
                                            i += 10;
                                            break;
                                        case '四':
                                            i += 5;
                                            break;
                                        case '五':
                                            i += 0;
                                            break;
                                    }
                                }
                            } else if (cityAndArea[0].length() == 4) {
                                star = Integer.parseInt(msg.substring(4, msg.length() - 1));
                                i = 100;
                            }
                            i += star;
                            ReleaseOrder.levelingId1 = i;
                            chooseLeveling();
                        }
                    }).create();

                    dialog.show();

                }
            }
        });

        view.findViewById(R.id.pi_duanwei).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mm = "2";
                if (userInfo.getBooleanInfo(LOGIN) == false) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    if (nima == null) {
                        Toast.makeText(getActivity(), "请选择初始段位", Toast.LENGTH_SHORT).show();
                    } else {

                        RegionPickerDialog.Builder builder = new RegionPickerDialog.Builder(getActivity());
                        RegionPickerDialog dialog = builder.setOnRegionSelectedListener(new RegionPickerDialog.OnRegionSelectedListener() {
                            @Override
                            public void onRegionSelected(String[] cityAndArea) {
                                String msg = cityAndArea[0] + cityAndArea[1];
                                ReleaseOrder.message2 = msg;
                                niba = msg;
                                if (cityAndArea[0].equals("最强王者")) {
                                    big1 = cityAndArea[0].substring(cityAndArea[0].length() - 2, cityAndArea[0].length());
                                    middle1 = "";
                                    small1 = cityAndArea[1];
                                } else {
                                    big1 = cityAndArea[0].substring(cityAndArea[0].length() - 3, cityAndArea[0].length() - 1);
                                    middle1 = cityAndArea[0].substring(cityAndArea[0].length() - 1, cityAndArea[0].length());
                                    small1 = cityAndArea[1];
                                }
                                end.setText(msg);
                                //判断段位
                                int i = 0;
                                String level1 = null;
                                char level2 = ' ';
                                int star = 0;
                                if (cityAndArea[0].length() == 5) {
                                    level1 = msg.substring(0, 4);
                                    level2 = msg.charAt(4);
                                    star = Integer.parseInt(msg.substring(5, 6));
                                    if (level1.equals("倔强青铜")) {
                                        i = 0;
                                        switch (level2) {
                                            case '一':
                                                i += 6;
                                                break;
                                            case '二':
                                                i += 3;
                                                break;
                                            case '三':
                                                i += 0;
                                                break;
                                        }
                                    } else if (level1.equals("秩序白银")) {
                                        i = 9;
                                        switch (level2) {
                                            case '一':
                                                i += 6;
                                                break;
                                            case '二':
                                                i += 3;
                                                break;
                                            case '三':
                                                i += 0;
                                                break;
                                        }
                                    } else if (level1.equals("荣耀黄金")) {
                                        i = 18;
                                        switch (level2) {
                                            case '一':
                                                i += 12;
                                                break;
                                            case '二':
                                                i += 8;
                                                break;
                                            case '三':
                                                i += 4;
                                                break;
                                            case '四':
                                                i += 0;
                                                break;
                                        }
                                    } else if (level1.equals("尊贵铂金")) {
                                        i = 34;
                                        switch (level2) {
                                            case '一':
                                                i += 12;
                                                break;
                                            case '二':
                                                i += 8;
                                                break;
                                            case '三':
                                                i += 4;
                                                break;
                                            case '四':
                                                i += 0;
                                                break;
                                        }
                                    } else if (level1.equals("永恒钻石")) {
                                        i = 50;
                                        switch (level2) {
                                            case '一':
                                                i += 20;
                                                break;
                                            case '二':
                                                i += 15;
                                                break;
                                            case '三':
                                                i += 10;
                                                break;
                                            case '四':
                                                i += 5;
                                                break;
                                            case '五':
                                                i += 0;
                                                break;
                                        }
                                    } else if (level1.equals("至尊星耀")) {
                                        i = 75;
                                        switch (level2) {
                                            case '一':
                                                i += 20;
                                                break;
                                            case '二':
                                                i += 15;
                                                break;
                                            case '三':
                                                i += 10;
                                                break;
                                            case '四':
                                                i += 5;
                                                break;
                                            case '五':
                                                i += 0;
                                                break;
                                        }
                                    }
                                } else if (cityAndArea[0].length() == 4) {
                                    star = Integer.parseInt(msg.substring(4, msg.length() - 1));
                                    i = 100;
                                }
                                i += star;
                                ReleaseOrder.levelingId2 = i;
                                chooseLeveling();
                            }
                        }).create();
                        dialog.show();
                    }
                }
            }
        });

        return view;
    }

    private void ChooseType() {
        if (ReleaseOrder.levelingId1 >= ReleaseOrder.levelingId2) {
            return;
        } else {
            String url1 = "/api/Order/GetWKGOrderPriceTime?type=" + play_type + "&bigrank=" + big + "&mediumrank=" + middle + "&rank=" + small + "&goalbigrank=" + big1 + "&goalmediumrank=" + middle1 + "&goalrank=" + small1;
            HttpGetUtils.httpGetFile(17, url1, handler);
        }
    }

    private void chooseLeveling() {
        if (ReleaseOrder.levelingId1 >= ReleaseOrder.levelingId2) {
            Toast.makeText(getActivity(), "请选择正确的段位", Toast.LENGTH_LONG).show();
            main_bg.setVisibility(View.VISIBLE);
            conmit.setVisibility(View.GONE);
            conmit.setAnimation(AnimationUtil.moveToViewBottom());
            main_bg.setAnimation(AnimationUtil.moveToViewLocation());
        } else {
            main_bg.setVisibility(View.GONE);
            conmit.setVisibility(View.VISIBLE);
            main_bg.setAnimation(AnimationUtil.moveToViewBottom());
            conmit.setAnimation(AnimationUtil.moveToViewLocation());
            String url1 = "/api/Order/GetWKGOrderPriceTime?type=" + play_type + "&bigrank=" + big + "&mediumrank=" + middle + "&rank=" + small + "&goalbigrank=" + big1 + "&goalmediumrank=" + middle1 + "&goalrank=" + small1;
            HttpGetUtils.httpGetFile(17, url1, handler);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 17:
                    try {
                        // String res = msg.getData().getString("res");
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        String data = obj.getString("Data");
                        if (success == "true") {
                            //Toast.makeText(getActivity(),success,Toast.LENGTH_LONG).show();
                            JSONObject result = new JSONObject(data);
                            String price1 = result.getString("Price");
                            String time1 = result.getString("Time");
                            true_money.setText(price1);
                            true_time.setText(time1);
                            price = price1;
                            time = time1;
                        } else {
                            Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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
                default:
                    break;
            }
        }

    };
}
