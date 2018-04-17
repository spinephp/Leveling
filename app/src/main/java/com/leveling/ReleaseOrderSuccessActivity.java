package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leveling.entity.OrderType;
import com.leveling.personcenter.AllOrdersActivity;

public class ReleaseOrderSuccessActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1,btn2;
    public static String test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success_layout);
        init();
    }

    private void init() {
        btn1 = (Button) findViewById(R.id.back_from_order);
       btn2 = (Button) findViewById(R.id.order_details_to_see);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.back_from_order:
                Intent intent = new Intent(ReleaseOrderSuccessActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.order_details_to_see:
                OrderType.orderType = "1";
                Intent intent1 = new Intent(ReleaseOrderSuccessActivity.this, AllOrdersActivity.class);
                startActivity(intent1);
                finish();
                break;
            default:break;
        }
    }
}
