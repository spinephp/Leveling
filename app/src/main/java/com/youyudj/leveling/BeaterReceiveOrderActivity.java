package com.youyudj.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.entity.BeaterOrder;
import com.youyudj.leveling.utils.HttpGetUtils;
import com.youyudj.leveling.utils.HttpFileHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class BeaterReceiveOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Button receive_order;
    private TextView publisher_game_name,dailian_content,game_area,dailian_time,dailian_price,jiangli,zuizhongshouyi,create_time,dingdan_id;
    private ImageView civ_order_detial_header;
    private LinearLayout img_jiedanxq_back;
    private String BigRank1,GoalBigRank1,GameType,number,orderid,picture,name,BigRank,middleRank,Rank,GoalBigRank,GoalMediumRank,GoalRank,GameOS,GameArea,TaskTime,OrderPrice,PiblishDateTime;
    private List<HashMap<String, Object>> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);
        Intent intent = getIntent();
        GameType = intent.getStringExtra("GameType");
        picture = intent.getStringExtra("picture");
        name = intent.getStringExtra("name");
        BigRank = intent.getStringExtra("BigRank");
        middleRank = intent.getStringExtra("middleRank");
        Rank = intent.getStringExtra("Rank");
        GoalBigRank = intent.getStringExtra("GoalBigRank");
        GoalMediumRank = intent.getStringExtra("GoalMediumRank");
        GoalRank = intent.getStringExtra("GoalRank");
        GameOS = intent.getStringExtra("GameOS");
        GameArea = intent.getStringExtra("GameArea");
        TaskTime = intent.getStringExtra("TaskTime");
        OrderPrice = intent.getStringExtra("OrderPrice");
        PiblishDateTime = intent.getStringExtra("PiblishDateTime");
        number = intent.getStringExtra("number");
        orderid = intent.getStringExtra("orderid");
        BigRank1 = intent.getStringExtra("LOLRank");
        GoalBigRank1 = intent.getStringExtra("LOLGoalRank");
        init();
    }
    private void init() {
        receive_order = (Button) findViewById(R.id.rec_order_btn);
        civ_order_detial_header = (ImageView) findViewById(R.id.civ_order_detial_header);
        String url = "/api/File/GetUserhead?filename="+picture;
        HttpFileHelper.httpGetFile(111,url,handler);
        publisher_game_name  = (TextView)findViewById(R.id.publisher_game_name);
        publisher_game_name.setText(name);
        dailian_content = (TextView) findViewById(R.id.dailian_content);
        if (GameType.equals("1")){
            dailian_content.setText(BigRank+middleRank+Rank+" "+"到 "+" "+GoalBigRank+GoalMediumRank+GoalRank);
        }else if (GameType.equals("2")){
                dailian_content.setText(BigRank1+" "+"到 "+" "+GoalBigRank1);
        }
        game_area = (TextView)findViewById(R.id.game_areas);
        if (GameOS.equals("1")){
            game_area.setText("手Q安卓"+"/"+GameArea);
        }else if (GameOS.equals("2")){
            game_area.setText("手Q苹果"+"/"+GameArea);
        }else if (GameOS.equals("3")){
            game_area.setText("微信安卓"+"/"+GameArea);
        }else if (GameOS.equals("4")){
            game_area.setText("微信苹果"+"/"+GameArea);
        }else if (GameOS.equals("5")){
            game_area.setText("网通"+"/"+GameArea);
        }else if (GameOS.equals("6")){
            game_area.setText("电信"+"/"+GameArea);
        }else {
            game_area.setText("其他"+"/"+GameArea);
        }
        dailian_time = (TextView)findViewById(R.id.dailian_time);
        dailian_time.setText(TaskTime+"小时");
        dailian_price= (TextView)findViewById(R.id.dailian_price);
        dailian_price.setText("￥"+OrderPrice);
        jiangli = (TextView)findViewById(R.id.jiangli);
        zuizhongshouyi = (TextView)findViewById(R.id.zuizhongshouyi);
        zuizhongshouyi.setText("￥"+OrderPrice);
        create_time = (TextView) findViewById(R.id.create_time);
        create_time.setText(PiblishDateTime);
        dingdan_id = (TextView) findViewById(R.id.dingdan_id);
        dingdan_id.setText(number);
        img_jiedanxq_back = (LinearLayout) findViewById(R.id.img_jiedanxq_back);
        img_jiedanxq_back.setOnClickListener(this);
        receive_order.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.img_jiedanxq_back:
                finish();
                break;
            case R.id.rec_order_btn:
                new AlertDialog.Builder(BeaterReceiveOrderActivity.this).setTitle("提示")//设置对话框标题
                        .setMessage("确认接单？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                // TODO Auto-generated method stub
                                String url = "/api/Order/GetReceiveOrder?Orderid="+orderid;
                                HttpGetUtils.httpGetFile(23,url, handler);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        // TODO Auto-generated method stub
                    }
                }).show();//在按键响应事件中显示此对话框
                break;
            default:break;
        }
    }
    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 111:
                    byte[] data1 = (byte[])msg.obj;
                    if (data1==null){
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    civ_order_detial_header.setImageBitmap(bitmap);
                    break;
                case 23:
                    try {
                        String res = (String) msg.obj;
                        if (res==null){
                            return;
                        }
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String data = result.getString("Data");
                        if (success == "true") {
                            //Toast.makeText(BeaterReceiveOrderActivity.this, success, Toast.LENGTH_LONG).show();
                            BeaterOrder.orderType = "1";
                            Intent intent = new Intent(BeaterReceiveOrderActivity.this,Beater_Order_Activity.class);
                            startActivity(intent);
                            finish();
                        } else if (success == "false") {
                            Toast.makeText(BeaterReceiveOrderActivity.this, err, Toast.LENGTH_LONG).show();
                        }
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
}
