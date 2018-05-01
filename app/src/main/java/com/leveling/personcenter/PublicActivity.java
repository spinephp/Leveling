package com.leveling.personcenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.PublicWebViewActivity;
import com.leveling.R;
import com.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicActivity extends AppCompatActivity {
    public static String str1;
    private List<HashMap<String, Object>> list;
    private int type1 = 1;
    private ListView activity_list;
    private LinearLayout img_public_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public);
        activity_list = (ListView) findViewById(R.id.ann_lv);
        img_public_back = (LinearLayout) findViewById(R.id.img_public_back);
        img_public_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String url = "/api/Announcement/GetAnnouncement?type="+type1;
        HttpGetUtils.httpGetFile(13,url, handler);
    }
    private Handler   handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 13:
                    try {
                        String res = (String) msg.obj;
                        if (res == null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        list = new ArrayList<HashMap<String, Object>>();
                        JSONArray arr = new JSONArray(data);
                        HashMap<String, Object> hashMap;
                        for(int i=0;i<arr.length();i++){
                            hashMap = new HashMap<String, Object>();
                            JSONObject inresult = (JSONObject) arr.get(i);
                            hashMap.put("ID", inresult.getString("ID"));
                            hashMap.put("title", inresult.getString("Title"));
                            hashMap.put("urlUploadFile", inresult.getString("URL"));
                            hashMap.put("time", inresult.getString("Time"));
                            hashMap.put("contents", inresult.getString("Contents"));
                            list.add(hashMap);
                        }
                        if (success == "true") {
                           // Toast.makeText(PublicActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(PublicActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(PublicActivity.this);
                        activity_list.setAdapter(adapter);
                        activity_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
            convertView = inflater.inflate(R.layout.public_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            viewHold.huodong = (LinearLayout) convertView.findViewById(R.id.huodong);
            viewHold.activity_title = (TextView) convertView
                    .findViewById(R.id.activity_title);
            viewHold.activity_time = (TextView) convertView.findViewById(R.id.activity_time);
            viewHold.activity_url = (TextView) convertView
                    .findViewById(R.id.activity_url);
            viewHold.activity_title.setText(hashMap.get("title").toString());
            String time = hashMap.get("time").toString();
            String nian = time.substring(0,10);
            String qi = time.substring(11,19);
            String riqi = nian+" "+qi;
            viewHold.activity_time.setText(riqi);
            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.huodong.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(PublicActivity.this,PublicWebViewActivity.class);
                    intent.putExtra("con", finalHashMap.get("contents").toString());
                    intent.putExtra("ID",finalHashMap.get("ID").toString());
                    startActivity(intent);
                }
            });
            //String con = hashMap.get("content").toString();
            //viewHold.ann_content.loadData(hashMap.get("content").toString(), "text/html", "utf-8");
            return convertView;
        }
        class ViewHold {
            private TextView activity_title;
            private TextView activity_url;
            private TextView activity_time;
            private LinearLayout huodong;
        }

    }

}

