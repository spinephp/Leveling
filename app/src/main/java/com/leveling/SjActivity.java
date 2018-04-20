package com.youyudj.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SjActivity extends AppCompatActivity {
    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sj);
        init();
    }
    private void init() {
        btn1 = (Button) findViewById(R.id.su112);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.su112:
                Intent intent = new Intent(SjActivity.this,SetingActivity.class);
                startActivity(intent);
                finish();
                break;
            default:break;
        }
    }
}
