package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TixianSuccessActivity extends AppCompatActivity {

    private Button suctt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tixian_success);
        suctt = (Button) findViewById(R.id.suctt);
        suctt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(TixianSuccessActivity.this,ZiJinGuanLiActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }
}
