package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.youyudj.leveling.chat.utils.FileSaveUtil;
import com.youyudj.leveling.utils.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;

/**
 * Created by myipp on 2018/3/30.
 */

public class ServiceContractActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_contract);
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(this.getAssets().open("service_contract.txt"), "UTF-8");
            @SuppressWarnings("resource")
            BufferedReader br = new BufferedReader(reader);
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
            //((WebView)findViewById(R.id.web_contract)).loadData(sb.toString(), "text/html; charset=UTF-8", null);
            ((TextView)findViewById(R.id.tv_content)).setText(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(reader != null){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(ServiceContractActivity.this,RegisterActivity.class);
//        startActivity(intent);
        finish();
    }
}
