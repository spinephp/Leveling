package com.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class BeaterAuthSuccessActivity extends AppCompatActivity {
private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beater_auth_wait_layout);
        btn1 = (Button) findViewById(R.id.su);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeaterAuthSuccessActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
