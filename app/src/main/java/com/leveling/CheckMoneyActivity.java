package com.youyudj.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CheckMoneyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_money);
        Button supp = (Button) findViewById(R.id.supp);
        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckMoneyActivity.this,ZiJinGuanLiActivity.class);
                startActivity(intent);
            }
        });
    }
}
