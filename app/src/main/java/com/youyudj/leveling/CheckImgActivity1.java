package com.youyudj.leveling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youyudj.leveling.utils.HttpFileHelper;

public class CheckImgActivity1 extends AppCompatActivity {

    ImageView img_show;
    private LinearLayout img_heronub_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_img1);
        img_show = (ImageView) findViewById(R.id.img_show1212);
        img_heronub_back = (LinearLayout) findViewById(R.id.img_heronub_back);
        img_heronub_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = "/api/File/GetOrder?filename="+Sure_Order_Activity.img2;
        HttpFileHelper.httpGetFile(100,url,handler);

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    byte[] data = (byte[])msg.obj;
                    if (data==null){
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    img_show.setImageBitmap(bitmap);
                    break;
            }
        }
    };
}
