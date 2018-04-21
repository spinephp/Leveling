package com.youyudj.leveling;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyudj.leveling.entity.Url;

import java.net.URL;

public class SystemWebViewActivity extends AppCompatActivity {
    private WebView tv;
    private String iv,id;
    private LinearLayout img_title_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_web_view);
        img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv = (WebView) findViewById(R.id.webViewpp);
        Intent intent = getIntent();
        iv = intent.getStringExtra("ext");
        id = intent.getStringExtra("ID");
        String html = Url.html + id;
        Uri uri = Uri.parse(html);  //url为您要链接的地址
        Intent intent1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent1);
        finish();
    }
}
