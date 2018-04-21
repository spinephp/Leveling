package com.youyudj.leveling;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class HelpActivity extends AppCompatActivity {
    public static String str1;
    private List<HashMap<String, Object>> list;
    private int type = 2;
    private ListView lv_help_listview;
    private int i;
    public static String cont;
    private View v_help_banner;
    private LinearLayout img_hep_back;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 20:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        //String res = msg.getData().getString("res");
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                        JSONArray arr = new JSONArray(data);
                        list = new ArrayList<HashMap<String, Object>>();
                        HashMap<String, Object> hashMap;
                        for (int i = 0; i < arr.length(); i++) {
                            hashMap = new HashMap<String, Object>();
                            JSONObject inresult = (JSONObject) arr.get(i);
                            hashMap.put("ID", inresult.getString("ID"));
                            hashMap.put("title", inresult.getString("Title"));
                            hashMap.put("urlUploadFile", inresult.getString("URL"));
                            hashMap.put("time", inresult.getString("Time"));
                            hashMap.put("contents", inresult.getString("Contents"));
                            list.add(hashMap);
                        }
                        MyAdapter adapter = new MyAdapter(HelpActivity.this);
                        lv_help_listview.setAdapter(adapter);
                        lv_help_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);

        String url = "/api/Announcement/GetAnnouncement?type=" + 2;
        HttpGetUtils.httpGetFile(20, url, handler);
        v_help_banner = findViewById(R.id.v_help_banner);
        img_hep_back = (LinearLayout) findViewById(R.id.img_hep_back);
        img_hep_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_help_listview = (ListView) findViewById(R.id.lv_help_listview);
        v_help_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this, SimpleContactActivity.class);
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MyAdapter(Context context) {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.help_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            i = position;
            Log.d("WWWWWWWWWWWWWWW", hashMap.toString());
            ViewHold viewHold = new ViewHold();
            viewHold.help_title = (TextView) convertView
                    .findViewById(R.id.help_title);
            viewHold.help_url = (ImageButton) convertView
                    .findViewById(R.id.help_url);
            viewHold.qnm = (RelativeLayout) convertView.findViewById(R.id.qnm);
            viewHold.help_title.setText(hashMap.get("title").toString());
            Log.d("111", hashMap.get("contents").toString());
            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.qnm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(HelpActivity.this, WebVIew.class);
                    intent.putExtra("cont", finalHashMap.get("contents").toString());
                    intent.putExtra("ID", finalHashMap.get("ID").toString());
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHold {
            private TextView help_title;
            private ImageButton help_url;
            private RelativeLayout qnm;
        }

    }
}
