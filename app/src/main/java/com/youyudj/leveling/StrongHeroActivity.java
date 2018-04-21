package com.youyudj.leveling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.adapter.HeroRecyclerViewAdapter;
import com.youyudj.leveling.entity.Hero;
import com.youyudj.leveling.entity.HeroChoose;
import com.youyudj.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 21:47
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class StrongHeroActivity extends AppCompatActivity {

    Map<Integer, Integer> category2HeroType = new HashMap<>();
    Map<Integer, TextView> category2TitleView = new HashMap<>();
    RecyclerView lvHero;
    private TextView selected_hero_number;
    private Button btnConfirm;
    private int i = 3;
    private TextView color1, color2, color3, color4, color5, color6;
    HeroRecyclerViewAdapter<String> adapter;
    private boolean click = true;
    private int maxSelectCount = 0;

    void bindCategoryWithIndex(int resID, int index, int resTitleID) {
        findViewById(resID).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory(v.getId());
            }
        });
        category2HeroType.put(resID, index);
        category2TitleView.put(resID, (TextView) findViewById(resTitleID));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.strong_hero_layout);
        bindCategoryWithIndex(R.id.li_tank, 1, R.id.color1);
        bindCategoryWithIndex(R.id.li_cike, 2, R.id.color2);
        bindCategoryWithIndex(R.id.li_fashi, 3, R.id.color3);
        bindCategoryWithIndex(R.id.li_sheshou, 4, R.id.color4);
        bindCategoryWithIndex(R.id.li_zhanshi, 5, R.id.color5);
        bindCategoryWithIndex(R.id.li_fuzhu, 6, R.id.color6);
        btnConfirm = (Button) findViewById(R.id.select_hero_sure111);
        adapter = new HeroRecyclerViewAdapter<String>(StrongHeroActivity.this);
        selected_hero_number = (TextView) findViewById(R.id.selected_hero_number);
        lvHero = (RecyclerView) findViewById(R.id.lv_hero);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              setResult(100);
                                              finish();
                                          }
                                      }
        );
        findViewById(R.id.img_sh_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvMaxCanSelect = (TextView) findViewById(R.id.tv_max_can_select);
        Intent intent = getIntent();
        maxSelectCount = intent.getIntExtra("maxSelectCount", 0);
        tvMaxCanSelect.setText("最多可选择" + maxSelectCount + "个强势英雄");
        adapter.setMyItemClickListener(new HeroRecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                HeroRecyclerViewAdapter.ViewHolder holder = HeroRecyclerViewAdapter.holders.get(postion);
                String name = holder.hero_name.getText().toString();
                String pic = holder.pic_name.getText().toString();
                if (view.isSelected()) {
                    // 取消选择
                    Hero.heros.remove(name);
                    view.setSelected(false);
                } else {
                    if (Hero.heros.size() >= maxSelectCount)
                        Toast.makeText(StrongHeroActivity.this, "最多可选择" + maxSelectCount + "个强势英雄", Toast.LENGTH_LONG).show();
                    else {
                        Hero.heros.put(name, pic);
                        view.setSelected(true);
                    }
                }
            }
        });
        selectCategory(R.id.li_tank);
    }

    void selectCategory(int resCatID) {
        for (TextView tv : category2TitleView.values()) {
            tv.setTextColor(this.getResources().getColor(R.color.black));
        }
        category2TitleView.get(resCatID).setTextColor(this.getResources().getColor(R.color.colorPrimary));
        String url = "/api/Hero/GetHero?GameType=" + 1 + "&HeroType=" + category2HeroType.get(resCatID);
        HttpGetUtils.httpGetFile(1, url, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                String res = (String) msg.obj;
                if (res == null) {
                    return;
                }
                JSONObject result = new JSONObject(res);
                String success = result.getString("Success");
                String error = result.getString("ErrMsg");
                String data = result.getString("Data");
                JSONArray arr = new JSONArray(data);
                if (success == "true") {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject inresult = (JSONObject) arr.get(i);
                        list.add(inresult.toString());
                        Log.d("TAG", inresult.toString());
                    }
                    adapter.setData(list);
                    lvHero.setAdapter(adapter);
                    lvHero.setLayoutManager(new GridLayoutManager(StrongHeroActivity.this, 4));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
