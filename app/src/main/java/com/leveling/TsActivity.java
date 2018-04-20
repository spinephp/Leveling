package com.youyudj.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.youyudj.leveling.entity.BeaterOrder;
import com.youyudj.leveling.entity.OrderType;

public class TsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ts);
        Button tousu = (Button) findViewById(R.id.tousu12);
        tousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeaterOrder.orderType = "123";
                Intent intent = new Intent(TsActivity.this, Beater_Order_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}