package com.leveling;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leveling.personcenter.LevelingActivity;
import com.leveling.utils.HttpFileHelper;

public class ShowImageActivity extends AppCompatActivity {
    ImageView img_show;
    LinearLayout img_heronub_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        img_show = (ImageView) findViewById(R.id.img_show);
        img_heronub_back = (LinearLayout) findViewById(R.id.img_heronub_back);
        img_heronub_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = "/api/File/GetOrder?filename="+ LevelingActivity.img1;
        HttpFileHelper.httpGetFile(1,url,handler);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    byte[] data = (byte[])msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    img_show.setImageBitmap(bitmap);
                    break;
            }
        }
    };
}
