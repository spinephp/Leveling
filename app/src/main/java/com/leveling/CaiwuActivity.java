package com.youyudj.leveling;

import android.content.Context;
import android.content.Intent;
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
public class CaiwuActivity extends AppCompatActivity {

    private ListView money_details;
    private List<HashMap<String, Object>> list;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caiwu_detial_layout);
        LinearLayout img_money_details_back = (LinearLayout) findViewById(R.id.img_money_details_back);
        img_money_details_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        money_details = (ListView) findViewById(R.id.money_details);
        String url = "/api/Pay/GetPayRecord";
        HttpGetUtils.httpGetFile(10,url,handler);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:

                    try {
                        String res = (String) msg.obj;
                        if (res==null){
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
                            hashMap.put("Time", inresult.getString("Time"));
                            hashMap.put("Money", inresult.getString("Money"));
                            hashMap.put("Type", inresult.getInt("Type"));
                            list.add(hashMap);
                        }
                        if (success == "true") {
                            //Toast.makeText(CaiwuActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                            Toast.makeText(CaiwuActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        final MyAdapter adapter = new MyAdapter(CaiwuActivity.this);
                        money_details.setAdapter(adapter);
                        money_details.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
            convertView = inflater.inflate(R.layout.caiwu_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
//            viewHold.money_number = (TextView) convertView
//                    .findViewById(R.id.money_number);
            viewHold.money_time = (TextView) convertView.findViewById(R.id.money_time);
            viewHold.money_amount = (TextView) convertView
                    .findViewById(R.id.money_amount);
            viewHold.money_type = (TextView) convertView.findViewById(R.id.money_type);
 //           viewHold.money_number.setText(hashMap.get("ID").toString());
            String time = hashMap.get("Time").toString();
            String nian = time.substring(0,10);
            String qi = time.substring(11,19);
            String riqi = nian+" "+qi;
            viewHold.money_time.setText(riqi);
            viewHold.money_amount.setText(hashMap.get("Money").toString());
            if (hashMap.get("Type").equals(0)){
                viewHold.money_type.setBackgroundResource(R.drawable.shape_green_btn);
                viewHold.money_type.setText("收入");
            }
            else if (hashMap.get("Type").equals(1)){
                viewHold.money_type.setBackgroundResource(R.drawable.shape_green_btn);
                viewHold.money_type.setText("收入");
            }else if (hashMap.get("Type").equals(2)){
                viewHold.money_type.setText("收入");
                viewHold.money_type.setBackgroundResource(R.drawable.shape_green_btn);
            }else if (hashMap.get("Type").equals(3)){
                viewHold.money_type.setText("支出");
                viewHold.money_type.setBackgroundResource(R.drawable.shape_tixian_process);
            }else if (hashMap.get("Type").equals(4)){
                viewHold.money_type.setText("支出");
                viewHold.money_type.setBackgroundResource(R.drawable.shape_tixian_process);
            }else if (hashMap.get("Type").equals(5)){
                viewHold.money_type.setText("支出");
                viewHold.money_type.setBackgroundResource(R.drawable.shape_tixian_process);
            }else if (hashMap.get("Type").equals(6)){
                viewHold.money_type.setBackgroundResource(R.drawable.shape_green_btn);
                viewHold.money_type.setText("收入");
            }
            return convertView;
        }
        class ViewHold {
         //   private TextView money_number;
            private TextView money_time;
            private TextView money_amount;
            private TextView money_type;
        }

    }
}
