package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RenZhengSuccActivity extends AppCompatActivity {

    private Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_success_layout);
        init();
    }

    private void init() {
       // btn1 = (Button) findViewById(R.id.back_from_renzheng);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.back_from_renzheng:
//                Intent intent = new Intent(RenZhengSuccActivity.this,MainActivity.class);
//                startActivity(intent);
//                break;

            default:break;
        }
    }
}
