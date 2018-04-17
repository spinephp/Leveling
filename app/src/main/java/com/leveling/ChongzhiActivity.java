package com.leveling;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

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
public class ChongzhiActivity extends AppCompatActivity {

    private LinearLayout img_cz_back;
    private ListView chongzhi_lv;
    private List<HashMap<String, Object>> list;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chongzhi_histroy_layout);
        img_cz_back = (LinearLayout) findViewById(R.id.img_cz_back);
        img_cz_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chongzhi_lv = (ListView) findViewById(R.id.chongzhi_lv);
        String url = "/api/Pay/GetDepositRecord";
        HttpGetUtils.httpGetFile(15,url,handler);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 15:
                    String res = (String) msg.obj;
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
                            hashMap.put("Type",inresult.getInt("Type"));
                            hashMap.put("Time1", inresult.getString("Time1"));
                            hashMap.put("Time2", inresult.getString("Time2"));
                            hashMap.put("Money", inresult.getString("Money"));
                            hashMap.put("State", inresult.getString("State"));
                            list.add(hashMap);
                        }
                        if (success == "true") {
                            //Toast.makeText(ChongzhiActivity.this, success, Toast.LENGTH_LONG).show();
                        } else if (success == "false") {
                           // Toast.makeText(ChongzhiActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                        MyAdapter adapter = new MyAdapter(ChongzhiActivity.this);
                        chongzhi_lv.setAdapter(adapter);
                        chongzhi_lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
            convertView = inflater.inflate(R.layout.chongzhi_item_layout, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            ViewHold viewHold = new ViewHold();
            viewHold.zhifubao_pay = (TextView) convertView
                    .findViewById(R.id.zhifubao_pay);
            viewHold.zhifubao_time = (TextView) convertView.findViewById(R.id.zhifubao_time);
            viewHold.zhifubao_money = (TextView) convertView
                    .findViewById(R.id.zhifubao_money);
            viewHold.chongzhi_result = (TextView) convertView.findViewById(R.id.chongzhi_result);
            if (hashMap.get("Type").equals(0)){
                viewHold.zhifubao_pay.setText("钱包充值");
            }else if (hashMap.get("Type").equals(1)){
                viewHold.zhifubao_pay.setText("代练押金充值");
            }else if (hashMap.get("Type").equals(2)){
                viewHold.zhifubao_pay.setText("陪练押金充值");
            }else if(hashMap.get("Type").equals(3)){
                viewHold.zhifubao_pay.setText("后台充值");
            }
            if (hashMap.get("State").equals(1)){
                String time = hashMap.get("Time1").toString();
                String nian = time.substring(0,10);
                String qi = time.substring(11,19);
                String riqi = nian+" "+qi;
                viewHold.zhifubao_time.setText(riqi);
                viewHold.chongzhi_result.setText("待确认");
            }else if (hashMap.get("State").equals("2")){
                String time = hashMap.get("Time2").toString();
                String nian = time.substring(0,10);
                String qi = time.substring(11,19);
                String riqi = nian+" "+qi;
                viewHold.zhifubao_time.setText(riqi);
                viewHold.chongzhi_result.setText("充值成功");
                viewHold.chongzhi_result.setBackgroundResource(R.drawable.shape_order_green_btn);
            }
            viewHold.zhifubao_money.setText(hashMap.get("Money").toString());
            return convertView;
        }
        class ViewHold {
            private TextView zhifubao_pay;
            private TextView zhifubao_time;
            private TextView zhifubao_money;
            private TextView chongzhi_result;
        }

    }
}
