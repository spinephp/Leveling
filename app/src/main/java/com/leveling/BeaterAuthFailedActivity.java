package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BeaterAuthFailedActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beater_auth_failed);
        init();
    }
    private void init() {
        btn1 = (Button) findViewById(R.id.suqqqq);
        btn1.setOnClickListener(this);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.suqqqq:
                Intent intent = new Intent(BeaterAuthFailedActivity.this,BeaterAuthActivity.class);
                startActivity(intent);
                finish();
                break;
            default:break;
        }
    }
}
