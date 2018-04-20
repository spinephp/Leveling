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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youyudj.leveling.entity.Url;

import java.net.URL;

public class PublicWebViewActivity extends AppCompatActivity {
    private WebView tv;
    private LinearLayout img;
    private String iv,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_web_view);
        tv = (WebView) findViewById(R.id.webView1211);
        img = (LinearLayout) findViewById(R.id.img_details_back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        iv = intent.getStringExtra("con");
        id = intent.getStringExtra("ID");
        String html = Url.html + id;
        Uri uri = Uri.parse(html);  //url为您要链接的地址
        Intent intent1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent1);
        finish();
    }
}
