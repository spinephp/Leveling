package com.youyudj.leveling;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpFileHelper;
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
 * Date: 2017/11/7 11:41
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class NoOrdersFragment extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    LinearLayout king,hero,yxlm;
    private ListView beater_all_order;
    private RelativeLayout all_order_qq,dailian_order_qq,peilain_oder_qq;
    private View all_order_qq1,dailian_order_qq1,peilain_oder_qq1;
    private List<HashMap<String, Object>> list;
    public static String nickname,userid,orderid,publisherId;
    public static String  begin;
    public static String area;
    public static String price;
    public static String time,create_time;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_orders_layout, null);
        king=(LinearLayout) view.findViewById(R.id.ll_no_orders_king);
        yxlm = (LinearLayout) view.findViewById(R.id.yxlm);
        hero=(LinearLayout) view.findViewById(R.id.ll_no_orders_hero);
        all_order_qq = (RelativeLayout) view.findViewById(R.id.all_order_qq);
        dailian_order_qq = (RelativeLayout) view.findViewById(R.id.dailian_order_qq);
        peilain_oder_qq = (RelativeLayout) view.findViewById(R.id.peilain_oder_qq);
        all_order_qq1 = (View) view.findViewById(R.id.all_order_qq1);
        dailian_order_qq1 = (View) view.findViewById(R.id.dailian_order_qq1);
        peilain_oder_qq1 = (View) view.findViewById(R.id.peilain_oder_qq1);
        beater_all_order = (ListView) view.findViewById(R.id.beater_all_order);
        all_order_qq.setOnClickListener(this);
        dailian_order_qq.setOnClickListener(this);
        peilain_oder_qq.setOnClickListener(this);
        king.setOnClickListener(this);
        hero.setOnClickListener(this);
        if (HttpPostUtils.getRole() == 1||HttpPostUtils.getRole() == 2||HttpPostUtils.getRole() == 3||HttpPostUtils.getRole() == 4){
            king.setSelected(true);
            hero.setSelected(false);
            yxlm.setVisibility(View.VISIBLE);
            String url = "/api/Order/GetOrder1?Page="+1+"&type="+0;
            HttpGetUtils.httpGetFile(15,url, handler);
        }else if (HttpPostUtils.getRole() == 5||HttpPostUtils.getRole() == 6||HttpPostUtils.getRole() == 7||HttpPostUtils.getRole() == 8){
            king.setSelected(false);
            hero.setSelected(true);
            String url = "/api/Order/GetOrder1?Page="+1+"&type="+0;
            HttpGetUtils.httpGetFile(15,url, handler);
        }
        return view;
    }
    @Override
    public void onClick(View v) {
        if (HttpPostUtils.getRole() == 1||HttpPostUtils.getRole() == 2||HttpPostUtils.getRole() == 3||HttpPostUtils.getRole() == 4){
            int id = v.getId();
            switch (id){
                case R.id.ll_no_orders_king:
                    String url = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(15,url, handler);
                    break;
                case R.id.ll_no_orders_hero:
                    String url12 = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(115,url12, handler);
                    break;
                case R.id.all_order_qq:
                    all_order_qq1.setVisibility(View.VISIBLE);
                    dailian_order_qq1.setVisibility(View.GONE);
                    peilain_oder_qq1.setVisibility(View.GONE);
                    String url11 = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(15,url11, handler);
                    break;
                case R.id.peilain_oder_qq:
                    all_order_qq1.setVisibility(View.GONE);
                    dailian_order_qq1.setVisibility(View.GONE);
                    peilain_oder_qq1.setVisibility(View.VISIBLE);
                    String url2 = "/api/Order/GetOrder1?Page="+1+"&type="+2;
                    HttpGetUtils.httpGetFile(15,url2, handler);
                    break;
                case R.id.dailian_order_qq:
                    all_order_qq1.setVisibility(View.GONE);
                    dailian_order_qq1.setVisibility(View.VISIBLE);
                    peilain_oder_qq1.setVisibility(View.GONE);
                    String url1 = "/api/Order/GetOrder1?Page="+1+"&type="+1;
                    HttpGetUtils.httpGetFile(15,url1, handler);
                    break;
            }
        }else if (HttpPostUtils.getRole() == 5||HttpPostUtils.getRole() == 6||HttpPostUtils.getRole() == 7||HttpPostUtils.getRole() == 8){
            int id = v.getId();
            switch (id){
                case R.id.ll_no_orders_king:
                    String url = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(115,url, handler);
                    break;
                case R.id.ll_no_orders_hero:
                    String url12 = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(15,url12, handler);
                    break;
                case R.id.all_order_qq:
                    all_order_qq1.setVisibility(View.VISIBLE);
                    dailian_order_qq1.setVisibility(View.GONE);
                    peilain_oder_qq1.setVisibility(View.GONE);
                    String url11 = "/api/Order/GetOrder1?Page="+1+"&type="+0;
                    HttpGetUtils.httpGetFile(15,url11, handler);
                    break;
                case R.id.peilain_oder_qq:
                    all_order_qq1.setVisibility(View.GONE);
                    dailian_order_qq1.setVisibility(View.GONE);
                    peilain_oder_qq1.setVisibility(View.VISIBLE);
                    String url2 = "/api/Order/GetOrder1?Page="+1+"&type="+2;
                    HttpGetUtils.httpGetFile(15,url2, handler);
                    break;
                case R.id.dailian_order_qq:
                    all_order_qq1.setVisibility(View.GONE);
                    dailian_order_qq1.setVisibility(View.VISIBLE);
                    peilain_oder_qq1.setVisibility(View.GONE);
                    String url1 = "/api/Order/GetOrder1?Page="+1+"&type="+1;
                    HttpGetUtils.httpGetFile(15,url1, handler);
                    break;
            }
        }

    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 15:
                    String res = (String) msg.obj;
                    if (res==null){
                        return;
                    }else{
                        JSONObject result = null;
                        try {
                            result = new JSONObject(res);
                            Log.d("WEEWEEE",res);
                            String success = result.getString("Success");
                            String err = result.getString("ErrMsg");
                            String data = result.getString("Data");
                            JSONArray arr = new JSONArray(data);
                            list = new ArrayList<HashMap<String, Object>>();
                            HashMap<String, Object> hashMap;
                            for(int i=0;i<arr.length();i++){
                                hashMap = new HashMap<String, Object>();
                                //arr.getJSONObject()
                                JSONObject inresult = (JSONObject) arr.get(i);
                                hashMap.put("OrderID",inresult.getString("OrderID"));
                                hashMap.put("OrderNumber",inresult.getString("OrderNumber"));
                                hashMap.put("GameType",inresult.getString("GameType"));
                                hashMap.put("GameOS",inresult.getString("GameOS"));
                                hashMap.put("GameArea",inresult.getString("GameArea"));
                                hashMap.put("Title",inresult.getString("Title"));
                                hashMap.put("Type",inresult.getString("Type"));
                                hashMap.put("ReceiverNumber",inresult.getString("ReceiverNumber"));
                                hashMap.put("ReceiverGender",inresult.getString("ReceiverGender"));
                                hashMap.put("ReceiverAgeMin",inresult.getString("ReceiverAgeMin"));
                                hashMap.put("ReceiverAgeMax",inresult.getString("ReceiverAgeMax"));
                                hashMap.put("ReceiverRank",inresult.getString("ReceiverRank"));
                                hashMap.put("ReceiverHero",inresult.getString("ReceiverHero"));
                                hashMap.put("PiblisherID",inresult.getString("PiblisherID"));
                                hashMap.put("PiblisherBigRank",inresult.getString("PiblisherBigRank"));
                                hashMap.put("PiblisherMediumRank",inresult.getString("PiblisherMediumRank"));
                                hashMap.put("PiblisherRank",inresult.getString("PiblisherRank"));
                                hashMap.put("PiblisherGoalBigRank",inresult.getString("PiblisherGoalBigRank"));
                                hashMap.put("PiblisherGoalMediumRank",inresult.getString("PiblisherGoalMediumRank"));
                                hashMap.put("PiblisherGoalRank",inresult.getString("PiblisherGoalRank"));
                                hashMap.put("TaskTime",inresult.getString("TaskTime"));
                                hashMap.put("OrderPrice",inresult.getString("OrderPrice"));
                                hashMap.put("GameAccount",inresult.getString("GameAccount"));
                                hashMap.put("GamePwd",inresult.getString("GamePwd"));
                                hashMap.put("ReceiverNickName",inresult.getString("ReceiverNickName"));
                                hashMap.put("GameNicekName",inresult.getString("GameNicekName"));
                                hashMap.put("PiblishDateTime",inresult.getString("PiblishDateTime"));
                                hashMap.put("Piblisher",inresult.getString("Piblisher"));
                                hashMap.put("PiblisherPicture",inresult.getString("PiblisherPicture"));
                                hashMap.put("LOLRank",inresult.getString("LOLRank"));
                                hashMap.put("LOLGoalRank",inresult.getString("LOLGoalRank"));
                                list.add(hashMap);
                            }
                            if (success == "true") {
                                // Toast.makeText(getActivity(), success, Toast.LENGTH_LONG).show();
                            } else if (success == "false") {
                                Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                            }
                            final MyAdapter adapter = new MyAdapter(getActivity());
                            beater_all_order.setAdapter(adapter);
                            beater_all_order.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ViewHold viewHold = new ViewHold();
        HashMap<Integer, ViewHold> holders;
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
            convertView = inflater.inflate(R.layout.no_orders_item, null, false);
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap = list.get(position);
            holders = new HashMap<Integer, ViewHold>();
            holders.put(position, viewHold);
            viewHold.publisher_picture = (ImageView) convertView
                    .findViewById(R.id.publisher_picture);
            viewHold.publisher_name = (TextView) convertView
                    .findViewById(R.id.publisher_name);
            viewHold.beater_required_time = (TextView) convertView
                    .findViewById(R.id.beater_required_time);
            viewHold.beater_receive_order_time = (TextView) convertView
                    .findViewById(R.id.beater_receive_order_time);
            viewHold.beater_receive_begin_level = (TextView) convertView
                    .findViewById(R.id.beater_receive_begin_level);
            viewHold.beater_receive_end_level = (TextView) convertView
                    .findViewById(R.id.beater_receive_end_level);
            viewHold.beater_receive_money = (TextView) convertView
                    .findViewById(R.id.beater_receive_money);
            viewHold.receive_order_btn = (Button) convertView
                    .findViewById(R.id.receive_order_btn);
            viewHold.publisher_name.setText(hashMap.get("ReceiverNickName").toString());
            viewHold.beater_required_time.setText(hashMap.get("TaskTime").toString()+"小时");
            viewHold.beater_receive_order_time.setText(hashMap.get("PiblishDateTime").toString());
            if (hashMap.get("GameType").toString().equals("1")){
                viewHold.beater_receive_begin_level.setText(hashMap.get("PiblisherBigRank").toString()+hashMap.get("PiblisherMediumRank").toString()+hashMap.get("PiblisherRank").toString());
                viewHold.beater_receive_end_level.setText(hashMap.get("PiblisherGoalBigRank").toString()+hashMap.get("PiblisherGoalMediumRank").toString()+hashMap.get("PiblisherGoalRank").toString());
            }else if (hashMap.get("GameType").toString().equals("2")){
                viewHold.beater_receive_begin_level.setText(hashMap.get("LOLRank").toString());
                viewHold.beater_receive_end_level.setText(hashMap.get("LOLGoalRank").toString());
            }{

            }

            viewHold.beater_receive_money.setText("￥"+hashMap.get("OrderPrice").toString());
            if (hashMap.get("PiblisherPicture").toString().equals("null")){
                viewHold.publisher_picture.setImageResource(R.drawable.userhead);
            }else{
                String url = "/api/File/GetUserhead?filename="+hashMap.get("PiblisherPicture").toString();
                HttpFileHelper.httpGetFile(position,url,handlerPic);
            }
            final HashMap<String, Object> finalHashMap = hashMap;
            viewHold.receive_order_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(getActivity(),BeaterReceiveOrderActivity.class);
                    intent.putExtra("GameType", finalHashMap.get("GameType").toString());
                    intent.putExtra("picture", finalHashMap.get("PiblisherPicture").toString());
                    intent.putExtra("name", finalHashMap.get("ReceiverNickName").toString());

                    intent.putExtra("BigRank", finalHashMap.get("PiblisherBigRank").toString());
                    intent.putExtra("middleRank", finalHashMap.get("PiblisherMediumRank").toString());
                    intent.putExtra("Rank", finalHashMap.get("PiblisherRank").toString());

                    intent.putExtra("GoalBigRank", finalHashMap.get("PiblisherGoalBigRank").toString());
                    intent.putExtra("GoalMediumRank", finalHashMap.get("PiblisherGoalMediumRank").toString());
                    intent.putExtra("GoalRank", finalHashMap.get("PiblisherGoalRank").toString());

                    intent.putExtra("GameOS", finalHashMap.get("GameOS").toString());
                    intent.putExtra("GameArea", finalHashMap.get("GameArea").toString());

                    intent.putExtra("LOLRank", finalHashMap.get("LOLRank").toString());
                    intent.putExtra("LOLGoalRank", finalHashMap.get("LOLGoalRank").toString());

                    intent.putExtra("TaskTime", finalHashMap.get("TaskTime").toString());
                    intent.putExtra("OrderPrice", finalHashMap.get("OrderPrice").toString());

                    intent.putExtra("PiblishDateTime", finalHashMap.get("PiblishDateTime").toString());
                    intent.putExtra("orderid", finalHashMap.get("OrderID").toString());
                    intent.putExtra("number", finalHashMap.get("OrderNumber").toString());
                    startActivity(intent);
                }
            });
            return convertView;
        }
        class ViewHold {
            private ImageView publisher_picture;
            private TextView publisher_name;
            private TextView beater_required_time;
            private TextView beater_receive_order_time;
            private TextView beater_receive_begin_level;
            private TextView beater_receive_end_level;
            private TextView beater_receive_money;
            private Button receive_order_btn;
        }
        private Handler handlerPic = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what < 0) {
                    return;
                }
                int pos = msg.what;
                if ( MyAdapter.this.holders.containsKey(pos)){
                    byte[] data = (byte[])msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    ViewHold holder = MyAdapter.this.holders.get(pos);
                    holder.publisher_picture.setImageBitmap(bitmap);
                }
            }
        };
    }
    @Override
    public void onResume() {
        super.onResume();
        String url = "/api/Order/GetOrder1?Page="+1+"&type="+0;
        HttpGetUtils.httpGetFile(15,url, handler);
    }

}
