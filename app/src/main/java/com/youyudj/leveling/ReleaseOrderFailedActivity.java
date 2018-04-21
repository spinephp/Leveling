package com.youyudj.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.youyudj.leveling.entity.OrderType;

public class ReleaseOrderFailedActivity extends AppCompatActivity {

    public static String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_fail_layout);
        Button btn1 = (Button) findViewById(R.id.back_from_failed);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderType.orderType = "2";
                Intent intent = new Intent(ReleaseOrderFailedActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
