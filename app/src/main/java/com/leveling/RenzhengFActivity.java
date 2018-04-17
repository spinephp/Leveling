package com.leveling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RenzhengFActivity extends AppCompatActivity {
    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzheng_f);
        init();
    }

    private void init() {
        btn1 = (Button) findViewById(R.id.renzheng_from_failed);
    }
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.renzheng_from_failed:
                Intent intent = new Intent(RenzhengFActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:break;
        }
    }
}
