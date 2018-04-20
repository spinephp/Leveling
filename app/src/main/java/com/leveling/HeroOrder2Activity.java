package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.OrderInfo;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class HeroOrder2Activity extends AppCompatActivity implements View.OnClickListener {
    private TextView danpai,lhzp,dqsd,dwxz,paiweisai,lhzp1;
    private EditText wcsj,yxzh,yxmm,jsmc,sjfy;
    private Button qfk;
    private JSONObject json = new JSONObject();
    private LinearLayout img_orderpp_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_order2);
        img_orderpp_back = (LinearLayout) findViewById(R.id.img_orderpppp_back);
        img_orderpp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lhzp1 = (TextView) findViewById(R.id.lhzp1);
        lhzp1.setText(HeroOrder1Activity.dapai);
        danpai = (TextView) findViewById(R.id.danpai);
        paiweisai = (TextView) findViewById(R.id.paiweisai);
        paiweisai.setText(HeroOrder1Activity.paiwei);
        danpai.setText(HeroOrder1Activity.qf);
        lhzp = (TextView) findViewById(R.id.lhzp);
        lhzp.setText(HeroOrder1Activity.area);
        dqsd = (TextView) findViewById(R.id.dqsd);
        dqsd.setText(HeroOrder1Activity.sd);
        dwxz = (TextView) findViewById(R.id.dwxz);
        dwxz.setText(HeroOrder1Activity.dw1+"-"+HeroOrder1Activity.dw2);
        sjfy = (EditText) findViewById(R.id.sjfy);
        wcsj = (EditText) findViewById(R.id.wcsj);
        yxzh = (EditText) findViewById(R.id.yxzh);
        yxmm = (EditText) findViewById(R.id.yxmm);
        jsmc = (EditText) findViewById(R.id.jsmc);
        qfk = (Button) findViewById(R.id.qfk);
        qfk.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.qfk:
                if (sjfy.getText().toString().equals("")){
                    Toast.makeText(HeroOrder2Activity.this,"请输入代练费用",Toast.LENGTH_LONG).show();
                }else if (wcsj.getText().toString().equals("")){
                    Toast.makeText(HeroOrder2Activity.this,"请输入完成时间",Toast.LENGTH_LONG).show();
                }else if (yxzh.getText().toString().equals("")){
                    Toast.makeText(HeroOrder2Activity.this,"请输入联系电话",Toast.LENGTH_LONG).show();
                }else if (yxmm.getText().toString().equals("")){
                    Toast.makeText(HeroOrder2Activity.this,"请输入微信号",Toast.LENGTH_LONG).show();
                }else{
                    qfk.setClickable(false);
                    getData(json);
                    String url = "/api/Order/PostOrderRelease";
                    HttpPostUtils.httpPostFile(8,url,json,handler);
                }

                break;
        }
    }
    private void getData(JSONObject json) {
        try {
            json.put("GameType", 2);
            if(HeroOrder1Activity.qf.equals("网通")){
                json.put("GameOS",5 );
            }else if(HeroOrder1Activity.qf.equals("电信")){
                json.put("GameOS",6 );
            }else if(HeroOrder1Activity.qf.equals("其他")){
                json.put("GameOS",7 );
            }
            json.put("GameArea", HeroOrder1Activity.area);
            json.put("Title", "");
            json.put("Type", 1);
            json.put("ReceiverNumber", "");
            json.put("ReceiverGender", "");
            json.put("ReceiverAgeMin","");
            json.put("ReceiverAgeMax", "");
            json.put("PiblisherBigRank","");
            json.put("PiblisherMediumRank","");
            json.put("PiblisherRank","");
            json.put("PiblisherGoalBigRank","");
            json.put("PiblisherGoalMediumRank","");
            json.put("PiblisherGoalRank","");
            json.put("ReceiverHero", "");
            json.put("TaskTime", Float.parseFloat(wcsj.getText().toString()));
            json.put("OrderPrice",Float.parseFloat(sjfy.getText().toString()) );
            json.put("GameAccount", yxzh.getText().toString());
            json.put("GamePwd", yxmm.getText().toString());
            json.put("GameNicekName","");
            json.put("HeroNumber", "");
            json.put("Rune", "");
            json.put("RankType", Integer.parseInt(HeroOrder1Activity.ds));
            json.put("Piblisher", HttpPostUtils.getUserId());
            json.put("LOLType", HeroOrder1Activity.LOLType);
            json.put("LOLRankType", HeroOrder1Activity.LOLRankType);
            json.put("LOLRank", HeroOrder1Activity.LOLRank);
            json.put("LOLGoalRank", HeroOrder1Activity.LOLGoalRank);
            json.put("LOLNeedToPlay", HeroOrder1Activity.LOLNeedToPlay);
            json.put("LOLWinning", HeroOrder1Activity.LOLWinning);
            json.put("LOLFail", HeroOrder1Activity.LOLFail);
            json.put("LOLWinPoint", HeroOrder1Activity.sd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 8:
                    try {
                        qfk.setClickable(true);
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
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
                            // Toast.makeText(ReleaseOrderActivity.this, success, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(HeroOrder2Activity.this,PayOrderActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (success == "false") {
                            Toast.makeText(HeroOrder2Activity.this, err, Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
