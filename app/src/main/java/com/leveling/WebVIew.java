package com.leveling;

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

import com.leveling.entity.Url;

import java.net.URL;

public class WebVIew extends AppCompatActivity {
    private WebView tv;
    private LinearLayout img;
    private String iv,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);
        tv = (WebView) findViewById(R.id.webView12);
        img = (LinearLayout) findViewById(R.id.img_help_back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        iv = intent.getStringExtra("cont");
        id = intent.getStringExtra("ID");
        String html = Url.html + id;
        Uri uri = Uri.parse(html);  //url为您要链接的地址
        Intent intent1 = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent1);
        finish();
    }
}
