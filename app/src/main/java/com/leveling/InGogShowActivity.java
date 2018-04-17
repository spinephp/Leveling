package com.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import com.leveling.entity.Url;
import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class InGogShowActivity extends AppCompatActivity {
    private String video_user, video_bg, video_time, video_url, video_lable, video_title;
    private VideoView videoView;
    private TextView release_person,video_release_time,video_lables;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_gog_show);
        LinearLayout img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        release_person = (TextView) findViewById(R.id.release_person);
        video_release_time = (TextView) findViewById(R.id.video_release_time);
        video_lables = (TextView) findViewById(R.id.video_lable);
        video_user = intent.getStringExtra("video_user");
        video_bg = intent.getStringExtra("video_bg");
        video_time = intent.getStringExtra("video_time");
        video_url = intent.getStringExtra("video_url");
        video_lable = intent.getStringExtra("video_lable");
        video_title = intent.getStringExtra("video_title");
        release_person.setText(video_user);
        video_release_time.setText(video_time);
        video_lables.setText(video_lable);
       // String videoUrl2 = "http://192.168.1.254:8001/Uploads/video/" + video_url;
        String videoUrl2 = Url.video_url + video_url;

        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(videoUrl2, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, video_title);
        //jzVideoPlayerStandard.setUp("http://192.168.1.254:8001/Uploads/video/5a8015ff0c798.mp4", JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, video_title);
        Picasso.with(this)
                .load("/api/File/GetvideoIMG?filename="+video_bg)
                .into(jzVideoPlayerStandard.thumbImageView);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}