package com.youyudj.leveling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class ShareActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layoout);
        LinearLayout img_ppppp_back = (LinearLayout) findViewById(R.id.img_ppppp_back);
        img_ppppp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
