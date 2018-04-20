package com.youyudj.leveling;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.youyudj.leveling.utils.HttpFileHelper;
import com.youyudj.leveling.utils.HttpGetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/8 10:41
 * Update:2017/11/8
 * Version:
 * *******************************************************
 */
public class RankingFragment extends Fragment implements View.OnClickListener {

    View view;
    //private TextView rank_rank1,rank_rank2,rank_rank3,rank_rank4,rank_rank5,rank_rank6,rank_rank7,rank_rank8,rank_rank9,rank_rank10;
    private ImageView rank_picture1,rank_picture2,rank_picture3,rank_picture4,rank_picture5,rank_picture6,rank_picture7,rank_picture8,rank_picture9,rank_picture10;
    private TextView rank_name1,rank_name2,rank_name3,rank_name4,rank_name5,rank_name6,rank_name7,rank_name8,rank_name9,rank_name10;
    private TextView rank_money1,rank_money2,rank_money3,rank_money4,rank_money5,rank_money6,rank_money7,rank_money8,rank_money9,rank_money10;
    //private TextView rank_number_4,rank_number_5,rank_number_6,rank_number_7,rank_number_8,rank_number_9,rank_number_10;
    private RadioButton dianzan1,dianzan2,dianzan3,dianzan4,dianzan5,dianzan6,dianzan7,dianzan8,dianzan9,dianzan10;

    private String rank_rank11,rank_rank12,rank_rank13,rank_rank14,rank_rank15,rank_rank16,rank_rank17,rank_rank18,rank_rank19,rank_rank110;
    private String rank_picture11,rank_picture12,rank_picture13,rank_picture14,rank_picture15,rank_picture16,rank_picture17,rank_picture18,rank_picture19,rank_picture110;
    private String rank_name11,rank_name12,rank_name13,rank_name14,rank_name15,rank_name16,rank_name17,rank_name18,rank_name19,rank_name110;
    private String rank_money11,rank_money12,rank_money13,rank_money14,rank_money15,rank_money16,rank_money17,rank_money18,rank_money19,rank_money110;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_monery_ranking_layout,null);
//        rank_rank1 = (TextView) view.findViewById(R.id.rank_rank1);
//        rank_rank2 = (TextView) view.findViewById(R.id.rank_rank2);
//        rank_rank3 = (TextView) view.findViewById(R.id.rank_rank3);
//        rank_rank4 = (TextView) view.findViewById(R.id.rank_rank4);
//        rank_rank5 = (TextView) view.findViewById(R.id.rank_rank5);
//        rank_rank6 = (TextView) view.findViewById(R.id.rank_rank6);
//        rank_rank7 = (TextView) view.findViewById(R.id.rank_rank7);
//        rank_rank8 = (TextView) view.findViewById(R.id.rank_rank8);

        rank_picture1 = (ImageView) view.findViewById(R.id.rank_picture1);
        rank_picture2 = (ImageView) view.findViewById(R.id.rank_picture2);
        rank_picture3 = (ImageView) view.findViewById(R.id.rank_picture3);
        rank_picture4 = (ImageView) view.findViewById(R.id.rank_picture4);
        rank_picture5 = (ImageView) view.findViewById(R.id.rank_picture5);
        rank_picture6 = (ImageView) view.findViewById(R.id.rank_picture6);
        rank_picture7 = (ImageView) view.findViewById(R.id.rank_picture7);
        rank_picture8 = (ImageView) view.findViewById(R.id.rank_picture8);

        rank_name1 = (TextView) view.findViewById(R.id.rank_name1);
        rank_name2 = (TextView) view.findViewById(R.id.rank_name2);
        rank_name3 = (TextView) view.findViewById(R.id.rank_name3);
        rank_name4 = (TextView) view.findViewById(R.id.rank_name4);
        rank_name5 = (TextView) view.findViewById(R.id.rank_name5);
        rank_name6 = (TextView) view.findViewById(R.id.rank_name6);
        rank_name7 = (TextView) view.findViewById(R.id.rank_name7);
        rank_name8 = (TextView) view.findViewById(R.id.rank_name8);

        rank_money1 = (TextView) view.findViewById(R.id.rank_money1);
        rank_money2 = (TextView) view.findViewById(R.id.rank_money2);
        rank_money3 = (TextView) view.findViewById(R.id.rank_money3);
        rank_money4 = (TextView) view.findViewById(R.id.rank_money4);
        rank_money5 = (TextView) view.findViewById(R.id.rank_money5);
        rank_money6 = (TextView) view.findViewById(R.id.rank_money6);
        rank_money7 = (TextView) view.findViewById(R.id.rank_money7);
        rank_money8 = (TextView) view.findViewById(R.id.rank_money8);


        dianzan4 = (RadioButton) view.findViewById(R.id.dianzan4);
        dianzan5 = (RadioButton) view.findViewById(R.id.dianzan5);
        dianzan6 = (RadioButton) view.findViewById(R.id.dianzan6);
        dianzan7 = (RadioButton) view.findViewById(R.id.dianzan7);
        dianzan8= (RadioButton) view.findViewById(R.id.dianzan8);

        dianzan4.setOnClickListener(this);
        dianzan5.setOnClickListener(this);
        dianzan6.setOnClickListener(this);
        dianzan7.setOnClickListener(this);
        dianzan8.setOnClickListener(this);
        String url = "/api/Pay/GetIncomeList";
        HttpGetUtils.httpGetFile(111,url, handler);
        return view;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 111:
                    String res = (String) msg.obj;
                    try {
                        JSONObject result = new JSONObject(res);
                        String success = result.getString("Success");
                        String err = result.getString("ErrMsg");
                        String date = result.getString("Data");
                        if (success=="true"){
                            JSONArray data = new JSONArray(date);
                            if(data.getJSONObject(0)!=null){
                                JSONObject oj1 = data.getJSONObject(0);
                                Log.d("DDDD",oj1.toString());
                                rank_rank11 = oj1.getString("BigRankMax1");
                                rank_picture11 = oj1.getString("Picture");
                                rank_name11 = oj1.getString("NickName");
                                rank_money11 = oj1.getString("Historucalncome");
                                rank_name1.setText(rank_name11);
                                rank_money1.setText(rank_money11);
                                if (rank_picture11.equals("null")){
                                    rank_picture1.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture11;
                                    HttpFileHelper.httpGetFile(11,url,handler);
                                }
                            }
                            if(data.getJSONObject(1)!=null){
                                JSONObject oj2 = data.getJSONObject(1);
                                rank_rank12 = oj2.getString("BigRankMax1");
                                rank_picture12 = oj2.getString("Picture");
                                rank_name12 = oj2.getString("NickName");
                                rank_money12 = oj2.getString("Historucalncome");
                                rank_name2.setText(rank_name12);
                                rank_money2.setText(rank_money12);
                                if (rank_picture12.equals("null")){
                                    rank_picture2.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture12;
                                    HttpFileHelper.httpGetFile(12,url,handler);
                                }

                            }
                            if(data.getJSONObject(2)!=null){
                                JSONObject oj3 = data.getJSONObject(2);
                                rank_rank13 = oj3.getString("BigRankMax1");
                                rank_picture13 = oj3.getString("Picture");
                                rank_name13 = oj3.getString("NickName");
                                rank_money13 = oj3.getString("Historucalncome");
                                rank_name3.setText(rank_name13);
                                rank_money3.setText(rank_money13);
                                if (rank_picture13.equals("null")){
                                    rank_picture3.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture13;
                                    HttpFileHelper.httpGetFile(13,url,handler);
                                }
                            }
                            if(data.getJSONObject(3)!=null){
                                JSONObject oj4= data.getJSONObject(3);
                                rank_rank14 = oj4.getString("BigRankMax1");
                                rank_picture14 = oj4.getString("Picture");
                                rank_name14 = oj4.getString("NickName");
                                rank_money14 = oj4.getString("Historucalncome");
                                rank_name4.setText(rank_name14);
                                rank_money4.setText(rank_money14);
                                if (rank_picture14.equals("null")){
                                    rank_picture4.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture14;
                                    HttpFileHelper.httpGetFile(14,url,handler);
                                }

                            }
                            if(data.getJSONObject(4)!=null){
                                JSONObject oj5 = data.getJSONObject(4);
                                rank_rank15 = oj5.getString("BigRankMax1");
                                rank_picture15 = oj5.getString("Picture");
                                rank_name15 = oj5.getString("NickName");
                                rank_money15 = oj5.getString("Historucalncome");
                               // rank_rank5.setText(rank_rank15);
                                rank_name5.setText(rank_name15);
                                rank_money5.setText(rank_money15);
                                if (rank_picture15.equals("null")){
                                    rank_picture5.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture15;
                                    HttpFileHelper.httpGetFile(15,url,handler);
                                }
                            }
                            if(data.getJSONObject(5)!=null){
                                JSONObject oj6 = data.getJSONObject(5);
                                rank_rank16 = oj6.getString("BigRankMax1");
                                rank_picture16 = oj6.getString("Picture");
                                rank_name16 = oj6.getString("NickName");
                                rank_money16 = oj6.getString("Historucalncome");
                                //rank_rank6.setText(rank_rank16);
                                rank_name6.setText(rank_name16);
                                rank_money6.setText(rank_money16);
                                if (rank_picture16.equals("null")){
                                    rank_picture6.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture16;
                                    HttpFileHelper.httpGetFile(16,url,handler);
                                }
                            }
                            if(data.getJSONObject(6)!=null){
                                JSONObject oj7 = data.getJSONObject(6);
                                rank_rank17 = oj7.getString("BigRankMax1");
                                rank_picture17 = oj7.getString("Picture");
                                rank_name17 = oj7.getString("NickName");
                                rank_money17= oj7.getString("Historucalncome");
                                //rank_rank7.setText(rank_rank17);
                                rank_name7.setText(rank_name17);
                                rank_money7.setText(rank_money17);
                                if (rank_picture17.equals("null")){
                                    rank_picture7.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture17;
                                    HttpFileHelper.httpGetFile(17,url,handler);
                                }
                            }
                            if(data.getJSONObject(7)!=null){
                                JSONObject oj8 = data.getJSONObject(7);
                                rank_rank18 = oj8.getString("BigRankMax1");
                                rank_picture18 = oj8.getString("Picture");
                                rank_name18 = oj8.getString("NickName");
                                rank_money18 = oj8.getString("Historucalncome");
                               // rank_rank8.setText(rank_rank18);
                                rank_name8.setText(rank_name18);
                                rank_money8.setText(rank_money18);
                                if (rank_picture18.equals("null")){
                                    rank_picture8.setImageResource(R.drawable.userhead);
                                }else{
                                    String url = "/api/File/GetUserhead?filename="+rank_picture18;
                                    HttpFileHelper.httpGetFile(18,url,handler);
                                }
                            }
                        }
                        else if (success=="false"){
                            Toast.makeText(getActivity(),err,Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 11:
                    byte[] data = (byte[])msg.obj;
                    if (data==null){
                        return;
                    }
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    rank_picture1.setImageBitmap(bitmap);
                    break;
                case 12:
                    byte[] data2 = (byte[])msg.obj;
                    if (data2==null){
                        return;
                    }
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
                    rank_picture2.setImageBitmap(bitmap2);
                    break;
                case 13:
                    byte[] data3 = (byte[])msg.obj;
                    if (data3==null){
                        return;
                    }
                    Bitmap bitmap3 = BitmapFactory.decodeByteArray(data3, 0, data3.length);
                    rank_picture3.setImageBitmap(bitmap3);
                    break;
                case 14:
                    byte[] data4 = (byte[])msg.obj;
                    if (data4==null){
                        return;
                    }
                    Bitmap bitmap4 = BitmapFactory.decodeByteArray(data4, 0, data4.length);
                    rank_picture1.setImageBitmap(bitmap4);
                    break;
                case 15:
                    byte[] data5 = (byte[])msg.obj;
                    if (data5==null){
                        return;
                    }
                    Bitmap bitmap5 = BitmapFactory.decodeByteArray(data5, 0, data5.length);
                    rank_picture5.setImageBitmap(bitmap5);
                    break;
                case 16:
                    byte[] data6 = (byte[])msg.obj;
                    if (data6==null){
                        return;
                    }
                    Bitmap bitmap6 = BitmapFactory.decodeByteArray(data6, 0, data6.length);
                    rank_picture6.setImageBitmap(bitmap6);
                    break;
                case 17:
                    byte[] data7 = (byte[])msg.obj;
                    if (data7==null){
                        return;
                    }
                    Bitmap bitmap7 = BitmapFactory.decodeByteArray(data7, 0, data7.length);
                    rank_picture7.setImageBitmap(bitmap7);
                    break;
                case 18:
                    byte[] data8 = (byte[])msg.obj;
                    if (data8==null){
                        return;
                    }
                    Bitmap bitmap8 = BitmapFactory.decodeByteArray(data8, 0, data8.length);
                    rank_picture8.setImageBitmap(bitmap8);
                    break;
                case 19:
                    byte[] data9 = (byte[])msg.obj;
                    if (data9==null){
                        return;
                    }
                    Bitmap bitmap9 = BitmapFactory.decodeByteArray(data9, 0, data9.length);
                    rank_picture9.setImageBitmap(bitmap9);
                    break;
                case 110:
                    byte[] data10 = (byte[])msg.obj;
                    if (data10==null){
                        return;
                    }
                    Bitmap bitmap10 = BitmapFactory.decodeByteArray(data10, 0, data10.length);
                    rank_picture10.setImageBitmap(bitmap10);
                    break;

            }

        }
    };

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        String url = "/api/Pay/GetIncomeList";
        HttpGetUtils.httpGetFile(111,url, handler);
    }
}
