package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leveling.entity.OrderType;
import com.leveling.personcenter.AllOrdersActivity;

public class TousuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tousu);
        Button tousu = (Button) findViewById(R.id.tousu);
        tousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OrderType.orderType = "7";
                //Intent intent = new Intent(TousuActivity.this,AllOrdersActivity.class);
                //startActivity(intent);
                finish();
            }
        });
    }
}
