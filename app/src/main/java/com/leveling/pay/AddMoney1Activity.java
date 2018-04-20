package com.youyudj.leveling.pay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.youyudj.leveling.R;
import com.youyudj.leveling.entity.Type;

public class AddMoney1Activity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout img_back_from_tixian;
    private EditText input_money_new;
    private Button to_add_money_button;
    private String money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money1);
        img_back_from_tixian = (LinearLayout) findViewById(R.id.img_back_from_tixian);
        to_add_money_button = (Button) findViewById(R.id.to_add_money_button);
        input_money_new = (EditText) findViewById(R.id.input_money_new);
        img_back_from_tixian.setOnClickListener(this);
        to_add_money_button.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_back_from_tixian:
                finish();
                break;
            case R.id.to_add_money_button:
                money = input_money_new.getText().toString();
                if(money.isEmpty()){
                    Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT);
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("money", money);
                setResult(1, intent);
                finish();
                break;
        }
    }
}
