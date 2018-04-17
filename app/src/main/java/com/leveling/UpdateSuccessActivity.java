package com.leveling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leveling.personcenter.UpdateNickNameActivity;

public class UpdateSuccessActivity extends AppCompatActivity {
    private Button update_pwd_success;
    LinearLayout img_nbna_back;
    private TextView tx_ni_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_success);
        tx_ni_name = (TextView) findViewById(R.id.tx_ni_name);
        if (UpdateNickNameActivity.str=="1"){
            tx_ni_name.setText("修改昵称");
        }else{
            tx_ni_name.setText("修改密码");
        }
        img_nbna_back = (LinearLayout) findViewById(R.id.img_nbna_back);
        img_nbna_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
