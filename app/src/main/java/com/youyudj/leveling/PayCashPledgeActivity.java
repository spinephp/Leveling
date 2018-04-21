package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.ApplyAuthentication;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import static com.youyudj.leveling.R.id.account_left_money;
import static com.youyudj.leveling.R.id.unuseful_money;

public class PayCashPledgeActivity extends AppCompatActivity implements OnClickListener {

    private Button pay_conform;
    private TextView cashPledge;
    private String auth;
    private String Id;
    private String cash;
    private LinearLayout img_paynm_back;
    public static String money;
    public static String money1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beater_auth_pay_layout);
        img_paynm_back = (LinearLayout) findViewById(R.id.img_paynm_back);
        img_paynm_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = "/api/Pay/GetPayFreezeMoney";
        HttpGetUtils.httpGetFile(11, url, handler);
        init();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    String res = (String) msg.obj;
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            money1 = obj.getString("Data");
                        } else if (success == "false") {
                            Toast.makeText(PayCashPledgeActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };

    private void init() {
        // TODO Auto-generated method stub
        cashPledge = (TextView) findViewById(R.id.new_CashPledge);
        auth = ApplyAuthentication.id;
        Id = HttpPostUtils.getUserId();
        cash = ApplyAuthentication.money;
        money = cash;
        pay_conform = (Button) findViewById(R.id.new_pay_conform);
        cashPledge.setText("￥" + cash);
        pay_conform.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        switch (id) {
            case R.id.new_pay_conform:
                Intent intent = new Intent(PayCashPledgeActivity.this, ToPayCashPledgeActivity.class);
                startActivityForResult(intent, 0x1000);
                break;
        }
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
    }
}
