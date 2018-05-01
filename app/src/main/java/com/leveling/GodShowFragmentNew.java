package com.leveling;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leveling.adapter.RecyclerViewAdapter;
import com.leveling.utils.HttpGetUtils;

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
 * Date: 2017/11/8 10:15
 * Update:2017/11/8
 * Version:
 * *******************************************************
 */
public class GodShowFragmentNew extends Fragment implements View.OnClickListener {
  //  private List<HashMap<String,Object>> list;
    RecyclerView recyclerView;
    RecyclerViewAdapter<String> adapter;
    View view;
    private View view00,view01,view02,view03,view04,view05,view06,view07,view08,view09;
    private ImageView video_time, video_tank,video_cike ,video_fashi ,video_sheshou,video_zhanshi,video_fuzhu,video_god,video_five,video_four;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.god_show_one_layout_new,null);
        video_time = (ImageView) view.findViewById(R.id.video_time);
        video_tank = (ImageView) view.findViewById(R.id.video_tank);
        video_cike = (ImageView) view.findViewById(R.id.video_cike);
        video_fashi = (ImageView) view.findViewById(R.id.video_fashi);
        video_sheshou = (ImageView) view.findViewById(R.id.video_sheshou);
        video_zhanshi = (ImageView) view.findViewById(R.id.video_zhanshi);
        video_fuzhu = (ImageView) view.findViewById(R.id.video_fuzhu);
        video_god = (ImageView) view.findViewById(R.id.video_god);
        video_five = (ImageView) view.findViewById(R.id.video_five);
        video_four = (ImageView) view.findViewById(R.id.video_four);
        view00 = view.findViewById(R.id.view00);
        view01 = view.findViewById(R.id.view01);
        view02 = view.findViewById(R.id.view02);
        view03 = view.findViewById(R.id.view03);
        view04 = view.findViewById(R.id.view04);
        view05 = view.findViewById(R.id.view05);
        view06 = view.findViewById(R.id.view06);
        view07 = view.findViewById(R.id.view07);
        view08 = view.findViewById(R.id.view08);
        view09 = view.findViewById(R.id.view09);
        video_time.setOnClickListener(this);
        video_tank.setOnClickListener(this);
        video_cike.setOnClickListener(this);
        video_fashi.setOnClickListener(this);
        video_sheshou.setOnClickListener(this);
        video_zhanshi.setOnClickListener(this);
        video_fuzhu.setOnClickListener(this);
        video_five.setOnClickListener(this);
        video_four.setOnClickListener(this);
        video_god.setOnClickListener(this);
        recyclerView=(RecyclerView) view.findViewById(R.id.rv_god_show);
        view00.setBackgroundResource(R.color.colorPrimary);
        view01.setBackgroundResource(0);
        view02.setBackgroundResource(0);
        view03.setBackgroundResource(0);
        view04.setBackgroundResource(0);
        view05.setBackgroundResource(0);
        view06.setBackgroundResource(0);
        view07.setBackgroundResource(0);
        view08.setBackgroundResource(0);
        view09.setBackgroundResource(0);
        String URL= "/api/Video/GetVideos?lable="+"";
        HttpGetUtils.httpGetFile(100,URL,handler);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        view00.setBackgroundResource(R.color.colorPrimary);
        view01.setBackgroundResource(0);
        view02.setBackgroundResource(0);
        view03.setBackgroundResource(0);
        view04.setBackgroundResource(0);
        view05.setBackgroundResource(0);
        view06.setBackgroundResource(0);
        view07.setBackgroundResource(0);
        view08.setBackgroundResource(0);
        view09.setBackgroundResource(0);
        String URL= "/api/Video/GetVideos?lable="+"";
        HttpGetUtils.httpGetFile(100,URL,handler);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.video_time:
                view00.setBackgroundResource(R.color.colorPrimary);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URLv= "/api/Video/GetVideos?lable="+"";
                HttpGetUtils.httpGetFile(100,URLv,handler);
                break;
            case R.id.video_tank:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(R.color.colorPrimary);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL= "/api/Video/GetVideos?lable="+"坦克";
                HttpGetUtils.httpGetFile(99,URL,handler);
                break;
            case R.id.video_cike:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(R.color.colorPrimary);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL1= "/api/Video/GetVideos?lable="+"刺客";
                HttpGetUtils.httpGetFile(98,URL1,handler);
                break;
            case R.id.video_fashi:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(R.color.colorPrimary);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL2= "/api/Video/GetVideos?lable="+"法师";
                HttpGetUtils.httpGetFile(97,URL2,handler);
                break;
            case R.id.video_sheshou:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(R.color.colorPrimary);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL3= "/api/Video/GetVideos?lable="+"射手";
                HttpGetUtils.httpGetFile(96,URL3,handler);
                break;
            case R.id.video_zhanshi:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(R.color.colorPrimary);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL4= "/api/Video/GetVideos?lable="+"战士";
                HttpGetUtils.httpGetFile(95,URL4,handler);
                break;
            case R.id.video_fuzhu:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(R.color.colorPrimary);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL5= "/api/Video/GetVideos?lable="+"辅助";
                HttpGetUtils.httpGetFile(94,URL5,handler);
                break;
            case R.id.video_god:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(R.color.colorPrimary);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(0);
                String URL6= "/api/Video/GetVideos?lable="+"超神";
                HttpGetUtils.httpGetFile(93,URL6,handler);
                break;

            case R.id.video_five:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(R.color.colorPrimary);
                view09.setBackgroundResource(0);
                String URL7= "/api/Video/GetVideos?lable="+"五杀";
                HttpGetUtils.httpGetFile(92,URL7,handler);
                break;
            case R.id.video_four:
                view00.setBackgroundResource(0);
                view01.setBackgroundResource(0);
                view02.setBackgroundResource(0);
                view03.setBackgroundResource(0);
                view04.setBackgroundResource(0);
                view05.setBackgroundResource(0);
                view06.setBackgroundResource(0);
                view07.setBackgroundResource(0);
                view08.setBackgroundResource(0);
                view09.setBackgroundResource(R.color.colorPrimary);
                String URL8= "/api/Video/GetVideos?lable="+"四杀";
                HttpGetUtils.httpGetFile(91,URL8,handler);
                break;
        }
    }
    private Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what<0){
                return;
            }
            switch (msg.what){
                case 100:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 99:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 98:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 97:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 96:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 95:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 94:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 93:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 92:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 91:
                    try {
                        String res = (String)msg.obj;
                        JSONObject result = null;
                        result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        JSONArray arr = new JSONArray(data);
                        if (success=="true"){
                            adapter =new RecyclerViewAdapter<String>(getActivity());
                            List<String>list=new ArrayList<>();
                            for(int i=0;i<arr.length();i++){
                                JSONObject inresult = (JSONObject) arr.get(i);
                                list.add(inresult.toString());
                            }
                            adapter.setData(list);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
