package com.youyudj.leveling;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
public class TixianHistoryActivity extends AppCompatActivity {

    private ListView tixian_lv;
    private List<HashMap<String, Object>> list;
    private LinearLayout img_tixian_back;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdrawals_histroy_layout);
        img_tixian_back = (LinearLayout) findViewById(R.id.img_tixian_back);
        img_tixian_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tixian_lv = (ListView) findViewById(R.id.tixian_lv);
        String url = "/api/Pay/GetWithdrawalRecord";
        HttpGetUtils.httpGetFile(12, url, handler);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 12:
                    String res = (String) msg.obj;
                    if (res==null){
                        return;
                    }
                    JSONObject result = null;
                    try {
                        result = new JSONObject(res);
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
                            hashMap.put("Type",inresult.getString("Type"));
                            hashMap.put("Time1", inresult.getString("Time1"));
                            hashMap.put("Time2", inresult.getString("Time2"));
                            hashMap.put("Money", inresult.getString("Money"));
                            hashMap.put("State", inresult.getString("State"));
                            list.add(hashMap);
                        }
                        if (success == "true") {
                            //Toast.makeText(TixianHistoryActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(TixianHistoryActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        MyAdapter adapter = new MyAdapter(TixianHistoryActivity.this);
                        tixian_lv.setAdapter(adapter);
                        tixian_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:break;
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
            convertView = inflater.inflate(R.layout.withdrawals_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            viewHold.zhifubao_tixian = (TextView) convertView
                    .findViewById(R.id.zhifubao_tixian);
            viewHold.zhifubao_time = (TextView) convertView.findViewById(R.id.zhifubao_time);
            viewHold.zhifubao_tixian_money = (TextView) convertView
                    .findViewById(R.id.zhifubao_tixian_money);
            viewHold.zhifu_progress = (TextView) convertView.findViewById(R.id.zhifu_progress);
            if (hashMap.get("Type").equals("0")){
                viewHold.zhifubao_tixian.setText("钱包提现");
            }else if (hashMap.get("Type").equals("1")){
                viewHold.zhifubao_tixian.setText("代练押金提现");
            }else if (hashMap.get("Type").equals("2")){
                viewHold.zhifubao_tixian.setText("陪练押金提现");
            }
            if (hashMap.get("State").equals("1")){
                String time = hashMap.get("Time1").toString();
                String nian = time.substring(0,10);
                String qi = time.substring(11,19);
                String riqi = nian+" "+qi;
                viewHold.zhifubao_time.setText(riqi);
                viewHold.zhifu_progress.setTextColor(Color.WHITE);
                viewHold.zhifu_progress.setText("待确认");
            }else if (hashMap.get("State").equals("2")){
                String time = hashMap.get("Time1").toString();
                String nian = time.substring(0,10);
                String qi = time.substring(11,19);
                String riqi = nian+" "+qi;
                viewHold.zhifubao_time.setText(riqi);
                viewHold.zhifu_progress.setText("提现成功");
                viewHold.zhifu_progress.setTextColor(Color.WHITE);
                viewHold.zhifu_progress.setBackgroundResource(R.drawable.shape_order_green_btn);
            }else if(hashMap.get("State").equals("3")){
                String time = hashMap.get("Time1").toString();
                String nian = time.substring(0,10);
                String qi = time.substring(11,19);
                String riqi = nian+" "+qi;
                viewHold.zhifubao_time.setText(riqi);
                viewHold.zhifu_progress.setText("提现失败");
                viewHold.zhifu_progress.setTextColor(Color.BLACK);
                viewHold.zhifu_progress.setBackgroundResource(R.drawable.shape_tixian_fail);
            }
            viewHold.zhifubao_tixian_money.setText(hashMap.get("Money").toString());
            return convertView;
        }
        class ViewHold {
            private TextView zhifubao_tixian;
            private TextView zhifubao_time;
            private TextView zhifubao_tixian_money;
            private TextView zhifu_progress;
        }

    }
}
