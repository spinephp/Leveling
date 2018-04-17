package com.leveling.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.leveling.R;
import com.leveling.SetingActivity;

public class ChangeSuccessActivity extends AppCompatActivity {
    private Button update_pwd_success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_success);
        update_pwd_success = (Button) findViewById(R.id.update_pwd_success);
        update_pwd_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeSuccessActivity.this,SetingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
