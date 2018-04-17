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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.R;
import com.leveling.SystemWebViewActivity;
import com.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemInfoActivity extends AppCompatActivity {
    public static String str1;
    private List<HashMap<String, Object>> list;
    private Handler handler;
    private int type;
    private int type1 = 3;
    private ListView lv_ann;
    private LinearLayout img_Systeminfo_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        img_Systeminfo_back = (LinearLayout) findViewById(R.id.img_Systeminfo_back);
        img_Systeminfo_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 11:
                        if ((String) msg.obj==null){
                            return;
                        }
                        try {
                            //String res = msg.getData().getString("res");
                            JSONObject result = new JSONObject((String) msg.obj);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            //List<AnnouncementInfo> items = new ArrayList<AnnouncementInfo>();
                            JSONArray arr = new JSONArray(data);
                            list = new ArrayList<HashMap<String, Object>>();
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
                               // Toast.makeText(SystemInfoActivity.this, success, Toast.LENGTH_LONG).show();
                            } else if (success == "false") {
                                Toast.makeText(SystemInfoActivity.this, err, Toast.LENGTH_LONG).show();
                            }
                            final MyAdapter adapter = new MyAdapter(SystemInfoActivity.this);
                            lv_ann.setAdapter(adapter);
                            lv_ann.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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

        type = type1;
        String url = "/api/Announcement/GetAnnouncement?type="+3;
        HttpGetUtils.httpGetFile(11,url, handler);
        lv_ann = (ListView) findViewById(R.id.ann_lv);

    }

    private void sendReadSystemInfoByID(String id)
    {
        String url = "/api/Announcement/SetAnnouncementReadState?annID="+id;
        HttpGetUtils.httpGetFile(12,url, handler);
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
            convertView = inflater.inflate(R.layout.system_info_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            viewHold.system_info = (RelativeLayout) convertView.findViewById(R.id.system_info);
            viewHold.title = (TextView) convertView
                    .findViewById(R.id.ann_title);
            viewHold.url = (TextView) convertView
                    .findViewById(R.id.ann_url);
            viewHold.time = (TextView) convertView
                    .findViewById(R.id.ann_time);
            viewHold.title.setText(hashMap.get("title").toString());
            String time = hashMap.get("time").toString();
            String nian = time.substring(0,10);
            String qi = time.substring(11,19);
            String riqi = nian+" "+qi;
            viewHold.time.setText(riqi);
            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.system_info.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    /**
                     * 2018.04.09 add by lin
                     * 点击触发请求已读取订单消息的请求
                     * 开始
                     * */

                    //发送已读取信息请求
                    sendReadSystemInfoByID(finalHashMap.get("ID").toString());

                    /**
                     * 2018.04.09 add by lin
                     * 点击触发请求已读取订单消息的请求
                     * 结束
                     * */

                    Intent intent = new Intent(SystemInfoActivity.this,SystemWebViewActivity.class);
                    intent.putExtra("ext", finalHashMap.get("contents").toString());
                    intent.putExtra("ID", finalHashMap.get("ID").toString());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHold {
            private TextView title;
            private TextView url;
            private TextView time;
            private RelativeLayout system_info;
        }
    }

}
