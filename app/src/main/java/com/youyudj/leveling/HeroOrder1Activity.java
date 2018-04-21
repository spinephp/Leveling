package com.youyudj.leveling;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpPostUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeroOrder1Activity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout choose_zupai_type,choose_gamearea,choose_type_play,choose_begin_level,choose_end_level,choose_shengdian,choose_gamefuwu;
    private LinearLayout yincang;
    private TextView zupai_type,gamearea,type_play,begin_level,end_level,gamefuwu,advice_money,about_time;
    private EditText shengdian,need_to_play,been_win,been_loss;
    private Button suanprice,suanpricenext;
    private String gameos;
    private String[] arr;
    List<String> data1,data2;
    private LinearLayout img_orderpp_back;
    private String ms;
    public static String qf,area,play_type,ds,sd,cs,cs1,mb,mb1,dapai,dw1,dw2,paiwei,dsp,LOLType,LOLRankType,LOLRank, LOLGoalRank,LOLNeedToPlay,LOLWinning,LOLFail;
    private JSONObject json = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_order1);
        img_orderpp_back = (LinearLayout) findViewById(R.id.img_orderpp_back12);
        img_orderpp_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        choose_zupai_type = (RelativeLayout) findViewById(R.id.choose_zupai_type);
        need_to_play = (EditText) findViewById(R.id.need_to_play);
        been_win = (EditText) findViewById(R.id.been_win);
        been_loss = (EditText) findViewById(R.id.been_loss);
        choose_gamefuwu = (RelativeLayout) findViewById(R.id.choose_gamefuwu);
        choose_gamearea = (RelativeLayout) findViewById(R.id.choose_gamearea);
        choose_type_play = (RelativeLayout) findViewById(R.id.choose_type_play);
        choose_begin_level = (RelativeLayout) findViewById(R.id.choose_begin_level);
        choose_end_level = (RelativeLayout) findViewById(R.id.choose_end_level);
        choose_shengdian = (RelativeLayout) findViewById(R.id.choose_shengdian);
        yincang = (LinearLayout) findViewById(R.id.yincang);
        zupai_type = (TextView) findViewById(R.id.zupai_type);
        gamefuwu = (TextView) findViewById(R.id.gamefuwu);
        gamearea = (TextView) findViewById(R.id.gamearea);
        type_play = (TextView) findViewById(R.id.type_play);
        begin_level = (TextView) findViewById(R.id.begin_level);
        end_level = (TextView) findViewById(R.id.end_level);
        advice_money = (TextView) findViewById(R.id.advice_money);
        about_time = (TextView) findViewById(R.id.about_time);
        shengdian = (EditText) findViewById(R.id.shengdian);
        suanprice = (Button) findViewById(R.id.suanprice);
        suanpricenext = (Button) findViewById(R.id.suanpricenext);
        choose_zupai_type.setOnClickListener(this);
        choose_gamefuwu.setOnClickListener(this);
        choose_gamearea.setOnClickListener(this);
        choose_type_play.setOnClickListener(this);
        choose_begin_level.setOnClickListener(this);
        choose_end_level.setOnClickListener(this);
        suanprice.setOnClickListener(this);
        yincang.setOnClickListener(this);
        suanpricenext.setOnClickListener(this);
    }

    void resetPrice(){
        suanprice.setVisibility(View.VISIBLE);
        suanpricenext.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.choose_zupai_type:
                Choosezupailevel();
                resetPrice();
                break;
            case R.id.choose_gamefuwu:
                Choosegamefuwulevel();
                resetPrice();
                break;
            case R.id.choose_gamearea:
                Choosegamearealevel();
                resetPrice();
                break;
            case R.id.choose_type_play:
                Choosegametype();
                resetPrice();
                break;
            case R.id.choose_begin_level:
                Choosebeginlevel();
                resetPrice();
                break;
            case R.id.choose_end_level:
                Chooseendlevel();
                resetPrice();
                break;
            case R.id.suanprice:
                if (type_play.getText().toString().equals("排位")){
                    if(zupai_type.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择组排类型",Toast.LENGTH_LONG).show();
                    }else if(gamefuwu.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在服务",Toast.LENGTH_LONG).show();
                    }else if(gamearea.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在区服",Toast.LENGTH_LONG).show();
                    }else if(type_play.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择代练类型",Toast.LENGTH_LONG).show();
                    }else if(begin_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择初始段位",Toast.LENGTH_LONG).show();
                    }else if(end_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择目标段位",Toast.LENGTH_LONG).show();
                    }else if(shengdian.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请填写胜点数",Toast.LENGTH_LONG).show();
                    }else if(Integer.parseInt(shengdian.getText().toString())<0||Integer.parseInt(shengdian.getText().toString())>100){
                        Toast.makeText(HeroOrder1Activity.this,"请填写正确胜点数",Toast.LENGTH_LONG).show();
                        shengdian.setText("");
                    }else{
                        getData(json);
                        String url = "/api/Order/PostLoLPrice";
                        HttpPostUtils.httpPostFile(111,url,json,handler);
                        sd = shengdian.getText().toString().trim();
                        yincang.setVisibility(View.VISIBLE);
                        suanprice.setVisibility(View.GONE);
                        suanpricenext.setVisibility(View.VISIBLE);
                    }
                }else if (type_play.getText().toString().equals("定位")){
                    if(zupai_type.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择组排类型",Toast.LENGTH_LONG).show();
                    }else if(gamefuwu.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在服务",Toast.LENGTH_LONG).show();
                    }else if(gamearea.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在区服",Toast.LENGTH_LONG).show();
                    }else if(type_play.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择代练类型",Toast.LENGTH_LONG).show();
                    }else if(begin_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择初始段位",Toast.LENGTH_LONG).show();
                    }else if(end_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择目标段位",Toast.LENGTH_LONG).show();
                    }else if(need_to_play.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请填写需打场数",Toast.LENGTH_LONG).show();
                    }else{
                        getData(json);
                        String url = "/api/Order/PostLoLPrice";
                        HttpPostUtils.httpPostFile(111,url,json,handler);
                        sd = shengdian.getText().toString().trim();
                        yincang.setVisibility(View.VISIBLE);
                        suanprice.setVisibility(View.GONE);
                        suanpricenext.setVisibility(View.VISIBLE);
                    }
                }else if (type_play.getText().toString().equals("晋级")){
                    if(zupai_type.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择组排类型",Toast.LENGTH_LONG).show();
                    }else if(gamefuwu.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在服务",Toast.LENGTH_LONG).show();
                    }else if(gamearea.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择所在区服",Toast.LENGTH_LONG).show();
                    }else if(type_play.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择代练类型",Toast.LENGTH_LONG).show();
                    }else if(begin_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择初始段位",Toast.LENGTH_LONG).show();
                    }else if(end_level.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请选择目标段位",Toast.LENGTH_LONG).show();
                    }else if(been_win.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请填写已胜场数",Toast.LENGTH_LONG).show();
                    }else if(been_loss.getText().toString().equals("")){
                        Toast.makeText(HeroOrder1Activity.this,"请填写已负场数",Toast.LENGTH_LONG).show();
                    }else{
                        getData(json);
                        String url = "/api/Order/PostLoLPrice";
                        HttpPostUtils.httpPostFile(111,url,json,handler);
                        sd = shengdian.getText().toString().trim();
                        yincang.setVisibility(View.VISIBLE);
                        suanprice.setVisibility(View.GONE);
                        suanpricenext.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.suanpricenext:
                LOLType = zupai_type.getText().toString();
                LOLRankType = type_play.getText().toString();
                LOLRank = begin_level.getText().toString();
                LOLGoalRank = end_level.getText().toString();
                LOLNeedToPlay = need_to_play.getText().toString();
                LOLWinning = been_win.getText().toString();
                LOLFail = been_loss.getText().toString();
                Intent intent = new Intent(HeroOrder1Activity.this,HeroOrder2Activity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void getData(JSONObject json) {
        try {
            json.put("ID","");
            json.put("Type",zupai_type.getText().toString());
            json.put("Area",gamefuwu.getText().toString()+gamearea.getText().toString());
            json.put("RankType",type_play.getText().toString());
            json.put("Rank",begin_level.getText().toString());
            json.put("GoalRank",end_level.getText().toString());
            json.put("NeedToPlay",need_to_play.getText().toString());
            json.put("Winning",been_win.getText().toString());
            json.put("Fail",been_loss.getText().toString());
            json.put("Price","");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Choosezupailevel() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);

        List<String> data = Arrays.asList(new String[]{ "单双排", "灵活组排"});
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        zupai_type.setText(itemValue);
                        dapai = itemValue;
                        if (itemValue=="单双排"){
                            ds = "1";
                        }else if (itemValue=="灵活组排"){
                            ds = "2";
                        }
                    }
                }).create();
        dialog.show();
    }
    private void Choosegamefuwulevel() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        List<String> data = Arrays.asList(new String[]{ "网通", "电信","其他"});
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        gamefuwu.setText(itemValue);
                        qf = gamefuwu.getText().toString().trim();
                        if(itemValue=="网通"){
                            gameos = "5";
                            gamearea.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType="+MainFragment.choose_game+"&GameOS="+gameos;
                            HttpGetUtils.httpGetFile(12,url,handler);
                        }else if(itemValue=="电信"){
                            gameos = "6";
                            gamearea.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType="+MainFragment.choose_game+"&GameOS="+gameos;
                            HttpGetUtils.httpGetFile(12,url,handler);
                        }else if(itemValue=="其他"){
                            gameos = "7";
                            gamearea.setText("");
                            String url = "/api/GameArea/GetGameArea?GameType="+MainFragment.choose_game+"&GameOS="+gameos;
                            HttpGetUtils.httpGetFile(12,url,handler);
                        }
                    }
                }).create();
        dialog.show();
    }
    private void Choosegamearealevel() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        if (arr == null){
            return;
        }
        if(gameos == "5"){
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            gamearea.setText(itemValue);
                            area = gamearea.getText().toString().trim();
                        }
                    }).create();
            dialog.show();
        }else if(gameos =="6"){
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            gamearea.setText(itemValue);
                            area = gamearea.getText().toString().trim();
                        }
                    }).create();
            dialog.show();
        }else if(gameos =="7"){
            List<String> data = Arrays.asList(arr);
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                            gamearea.setText(itemValue);
                            area = gamearea.getText().toString().trim();
                        }
                    }).create();
            dialog.show();
        }else if(gameos==""){
            List<String> data = Arrays.asList(new String[]{ "请选择游戏区服"});
            DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                    .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                        @Override
                        public void onDataSelected(String itemValue) {
                        }
                    }).create();
            dialog.show();
        }
    }
    private void  Choosegametype() {
        List<String> data = null;
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        data = Arrays.asList(new String[]{ "排位","定位","晋级"});
//        if (zupai_type.getText().toString().equals("单双排")){
//
//        }else if (zupai_type.getText().toString().equals("灵活组排")){
//            data = Arrays.asList(new String[]{ "代练", "陪玩"});
//        }
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        type_play.setText(itemValue);
                        paiwei = itemValue+"赛";
                        if (itemValue=="排位"){
                            play_type = "1";
                        }else if (itemValue=="定位"){
                            play_type = "2";
                        }else if (itemValue=="晋级"){
                            play_type = "2";
                        }
                    }
                }).create();
        dialog.show();
    }
    private void Choosebeginlevel() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        data1 = Arrays.asList(new String[]{ "黄铜五", "黄铜四","黄铜三", "黄铜二","黄铜一","白银五","白银四","白银三","白银二","白银一","黄金五","黄金四","黄金三","黄金二","黄金一","铂金五","铂金四","铂金三","铂金二","铂金一","钻石五","钻石四","钻石三","钻石二","钻石一","大师","王者"});
        DataPickerDialog dialog = builder.setUnit("").setData(data1).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        begin_level.setText(itemValue);
                        end_level.setText("");
                        yincang.setVisibility(View.GONE);
                        suanprice.setVisibility(View.VISIBLE);
                        suanpricenext.setVisibility(View.GONE);
                        ms = itemValue;
                        dw1 = itemValue;
                        cs = ms.substring(0,2);
                        cs1 = ms.substring(ms.length()-1,ms.length());
                    }
                }).create();
        dialog.show();
    }
    private void Chooseendlevel() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        data2 = Arrays.asList(new String[]{ "黄铜五", "黄铜四","黄铜三", "黄铜二","黄铜一","白银五","白银四","白银三","白银二","白银一","黄金五","黄金四","黄金三","黄金二","黄金一","铂金五","铂金四","铂金三","铂金二","铂金一","钻石五","钻石四","钻石三","钻石二","钻石一","大师","王者"});
        DataPickerDialog dialog = builder.setUnit("").setData(data2).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        if(data1.indexOf(ms)>=data2.indexOf(itemValue)){
                            Toast.makeText(HeroOrder1Activity.this,"请选择正确的段位！",Toast.LENGTH_LONG).show();
                        }else{
                            end_level.setText(itemValue);
                            yincang.setVisibility(View.GONE);
                            suanprice.setVisibility(View.VISIBLE);
                            suanpricenext.setVisibility(View.GONE);
                            dw2 = itemValue;
                            mb = itemValue.substring(0,2);
                            mb1 = itemValue.substring(itemValue.length()-1,itemValue.length());
                        }
                    }
                }).create();
        dialog.show();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 12:
                    try {
                        String oj = (String) msg.obj;
                        if(oj == null){
                            return;
                        }
                        JSONObject result = new JSONObject(oj);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        if(success=="true"){
                            JSONArray ar = result.getJSONArray("Data");
                            ArrayList<String> stringArrayList = new ArrayList<String>();
                            for(int i = 0;i<ar.length();i++){
                                JSONObject json_data = ar.getJSONObject(i);
                                stringArrayList.add(json_data.getString("GameArea"));
                            }
                            arr = stringArrayList.toArray(new String[stringArrayList.size()]);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 111:
                    try {
                        String oj = (String) msg.obj;
                        if(oj == null){
                            return;
                        }
                        String res = (String)msg.obj;
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if(success == "true"){
                            String data = obj.getString("Data");
                            String price1 = data;
                            if (price1.equals("null")){
                                advice_money.setText(0.0+"");
                            }else{
                                advice_money.setText(price1);
                            }
                        }else{
                            Toast.makeText(HeroOrder1Activity.this,err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
}
