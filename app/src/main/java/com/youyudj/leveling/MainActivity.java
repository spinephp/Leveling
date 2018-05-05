package com.youyudj.leveling;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.youyudj.leveling.chat.app.ChatApplication;
import com.youyudj.leveling.common.SystemLogHelper;
import com.youyudj.leveling.entity.UserInfo;
import com.youyudj.leveling.utils.HttpFileHelper;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public Fragment fg1, fg2, fg3, fg4, fg5;
    RadioButton radioButton, rb_main_beart;
    RadioButton radioButton1;
    Dialog dia;
    private UserInfo userInfo;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String AUTOLOGIN = "autoLogin";
    private static final String ID = "id";
    private String dd, name;
    int id, id1;
    Context context;
    ProgressBar progress;
    TextView text_percent;

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = ChatApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(ChatApplication.getInstance().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static int getVersionIn() {
        try {
            PackageManager manager = ChatApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(ChatApplication.getInstance().getPackageName(), 0);
            return info.versionCode;
            //return version;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EnvUtils.setEnv(EnvUtils.EnvEnum.ONLINE);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        dia = new Dialog(context, R.style.edit_AlertDialog_style);
        dia.setContentView(R.layout.download_layout);
        progress = (ProgressBar) dia.findViewById(R.id.progressBar110);
        text_percent = (TextView) dia.findViewById(R.id.text_percent10);
        id = id1 = -1;
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_main);
        userInfo = new UserInfo(this);
        radioButton = (RadioButton) findViewById(R.id.rb_main_send_orders);
        radioButton1 = (RadioButton) findViewById(R.id.rb_main_receive_orders);
        rb_main_beart = (RadioButton) findViewById(R.id.rb_main_beart);
        if (HttpPostUtils.getRole() == 0) {
            Intent intent = getIntent();
            id = intent.getIntExtra("id", -1);
            if (id == 10) {
                radioButton.setChecked(true);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fg1 = new MainFragment();
                transaction.replace(R.id.fm_main_frame, fg1);
                transaction.commit();
            } else if (id == 11) {
                rb_main_beart.setChecked(true);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fg5 = new PersonalCenterFragment();
                transaction.replace(R.id.fm_main_frame, fg5);
                transaction.commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fm_main_frame, new MainFragment()).commit();
                radioButton.setChecked(true);
            }
            radioButton1.setVisibility(View.GONE);
            radioButton.setVisibility(View.VISIBLE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    hideAllFragment(transaction);
                    switch (checkedId) {
                        case R.id.rb_main_send_orders:
                            if (fg1 == null) {
                                fg1 = new MainFragment();
                                transaction.add(R.id.fm_main_frame, fg1);
                            } else {
                                transaction.show(fg1);
                            }
                            break;
                        case R.id.rb_main_no_orders:
                            if (fg2 == null) {
                                fg2 = new GodShowFragmentNew();
                                transaction.add(R.id.fm_main_frame, fg2);
                            } else {
                                transaction.show(fg2);
                            }
                            break;
                        case R.id.rb_main_ranking:
                            if (fg3 == null) {
                                fg3 = new RankingFragment();
                                transaction.add(R.id.fm_main_frame, fg3);
                            } else {
                                transaction.show(fg3);
                            }
                            break;
                        case R.id.rb_main_message:
                            if (fg4 == null) {
                                if (userInfo.getBooleanInfo(AUTOLOGIN) == false || userInfo.getStringInfo(USER_NAME) == null || userInfo.getStringInfo(PASSWORD) == null) {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    fg4 = new MessageFragment();
                                    transaction.add(R.id.fm_main_frame, fg4);
                                }
                            } else {
                                transaction.show(fg4);
                            }
                            break;
                        case R.id.rb_main_beart:
                            if (fg5 == null) {
                                if (userInfo.getBooleanInfo(AUTOLOGIN) == false) {
                                    System.out.println(userInfo.getStringInfo(USER_NAME) + "    main0");
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    fg5 = new PersonalCenterFragment();
                                    transaction.add(R.id.fm_main_frame, fg5);
                                }
                            } else {
                                transaction.show(fg5);
                            }
                            break;
                    }
                    transaction.commit();
                }
            });
        } else if (HttpPostUtils.getRole() == 1 || HttpPostUtils.getRole() == 2 || HttpPostUtils.getRole() == 3 || HttpPostUtils.getRole() == 4 || HttpPostUtils.getRole() == 5 || HttpPostUtils.getRole() == 6 || HttpPostUtils.getRole() == 7 || HttpPostUtils.getRole() == 8) {
            Intent intent = getIntent();
            id = intent.getIntExtra("id", -1);
            if (id == 11) {
                rb_main_beart.setChecked(true);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                fg5 = new BeaterCenterFragment();
                transaction.replace(R.id.fm_main_frame, fg5);
                transaction.commit();
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.fm_main_frame, new NoOrdersFragment()).commit();
                radioButton.setChecked(true);
            }

            radioButton1.setChecked(true);
            radioButton1.setVisibility(View.VISIBLE);
            radioButton.setVisibility(View.GONE);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    hideAllFragment(transaction);
                    switch (checkedId) {
                        case R.id.rb_main_send_orders:
                            if (fg1 == null) {
                                fg1 = new NoOrdersFragment();
                                transaction.add(R.id.fm_main_frame, fg1);
                            } else {
                                transaction.show(fg1);
                            }
                            break;
                        case R.id.rb_main_no_orders:
                            if (fg2 == null) {
                                fg2 = new GodShowFragmentNew();
                                transaction.add(R.id.fm_main_frame, fg2);
                            } else {
                                transaction.show(fg2);
                            }
                            break;
                        case R.id.rb_main_ranking:
                            if (fg3 == null) {
                                fg3 = new RankingFragment();
                                transaction.add(R.id.fm_main_frame, fg3);
                            } else {
                                transaction.show(fg3);
                            }
                            break;
                        case R.id.rb_main_message:
                            if (fg4 == null) {
                                if (userInfo.getBooleanInfo(AUTOLOGIN) == false || userInfo.getStringInfo(USER_NAME) == null || userInfo.getStringInfo(PASSWORD) == null) {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    fg4 = new MessageFragment();
                                    transaction.add(R.id.fm_main_frame, fg4);
                                }
                            } else {
                                transaction.show(fg4);
                            }
                            break;
                        case R.id.rb_main_beart:
                            if (fg5 == null) {
                                if (userInfo.getBooleanInfo(AUTOLOGIN) == false || userInfo.getStringInfo(USER_NAME) == null || userInfo.getStringInfo(PASSWORD) == null) {
                                    System.out.println(userInfo.getStringInfo(USER_NAME) + "    main0");
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    fg5 = new BeaterCenterFragment();
                                    transaction.add(R.id.fm_main_frame, fg5);
                                }
                            } else {
                                transaction.show(fg5);
                            }
                            break;
                    }
                    transaction.commit();
                }
            });
        }
        XGPushConfig.enableDebug(this, true);
        XGPushManager.registerPush(this, "test", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                System.out.println("onSuccess");
            }

            @Override
            public void onFail(Object o, int i, String s) {
                System.out.println("onFail");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUpdate();
    }

    void checkUpdate() {
        // 调用api，查询最新版本，如果本地保存的版本和api返回的版本不一致，则开始更新，更新完毕后，保存最新您的版本号
        String url = "/api/SystemInfor/GetAppVersion";
        HttpGetUtils.httpGetFile(1011, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1011:
                    String res11 = (String) msg.obj;
                    if (res11 == null) {
                        return;
                    } else {
                        try {
                            JSONObject result = new JSONObject(res11);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            if (success == "true") {
                                try {
                                    JSONObject jj = new JSONObject(data);
                                    dd = jj.getString("VersionNumber");
                                    int idd = Integer.parseInt(dd);
                                    name = jj.getString("FileName");
                                    int version = getVersionIn();
                                    if (idd > version) {
                                        new AlertDialog.Builder(MainActivity.this).setTitle("提示")//设置对话框标题
                                                .setMessage("发现新版本，是否立刻更新?")//设置显示的内容
                                                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {//添加确定按钮
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                                        dia.show();
                                                        text_percent.setVisibility(View.VISIBLE);
                                                        progress.setVisibility(View.VISIBLE);
                                                        // TODO Auto-generated method stub
                                                        String uuq = "/api/File/GetAppFile?filename=" + name;
                                                        HttpFileHelper.httpGetFileWithProgress(uuq, handlerDownloadNewVersionProgress);
                                                    }
                                                }).setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {//添加返回按钮
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                                // TODO Auto-generated method stub
                                            }
                                        }).show();//在按键响应事件中显示此对话框
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
                            } else {
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    };

    Handler handlerDownloadNewVersionProgress = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // System.out.println(msg.what);
            int per = msg.what;
            if (msg.what == 101) {
                dia.dismiss();
                text_percent.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                String str = "/" + "Leveling.apk";
                //banben.setText(dd);
                String fileName = Environment.getExternalStorageDirectory() + str;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(fileName)), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (msg.what < 0) {
                dia.dismiss();
                Exception ex = (Exception) msg.obj;
                SystemLogHelper.Error(ex);
                text_percent.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "更新失败", Toast.LENGTH_LONG).show();
            } else if (msg.what <= 100 && msg.what >= 0) {
                text_percent.setText("" + per + "%");
                //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
                dia.setCanceledOnTouchOutside(false); // Sets whether this dialog is
                Window w = dia.getWindow();
                WindowManager.LayoutParams lp = w.getAttributes();
                lp.x = 0;
                lp.y = 40;
                dia.onWindowAttributesChanged(lp);
            }
        }
    };

    public void hideAllFragment(FragmentTransaction transaction) {
        if (fg1 != null) {
            transaction.hide(fg1);
        }
        if (fg2 != null) {
            transaction.hide(fg2);
        }
        if (fg3 != null) {
            transaction.hide(fg3);
        }
        if (fg4 != null) {
            transaction.hide(fg4);
        }
        if (fg5 != null) {
            transaction.hide(fg5);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
