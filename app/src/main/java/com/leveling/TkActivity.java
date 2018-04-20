package com.youyudj.leveling;

import android.support.v7.app.AppCompatActivity;

public class TkActivity extends AppCompatActivity {
//    ImageView img_show01,img_show02,img_show03,img_heronub_back;
//    Dialog dia,dia1,dia2;
//    LinearLayout ssss1,ssss2,ssss3;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tk);
//        img_show01 = (ImageView) findViewById(R.id.img_show001);
//        img_show02 = (ImageView) findViewById(R.id.img_show002);
//        img_show03 = (ImageView) findViewById(R.id.img_show003);
//        ssss1 = (LinearLayout) findViewById(R.id.ssss1);
//        ssss2 = (LinearLayout) findViewById(R.id.ssss2);
//        ssss3 = (LinearLayout) findViewById(R.id.ssss3);
//        if (Sure_Order_Activity.zjimg1.equals("null")){
//            ssss1.setVisibility(View.GONE);
//        }
//        if (Sure_Order_Activity.zjimg2.equals("null")){
//            ssss2.setVisibility(View.GONE);
//        }
//        if (Sure_Order_Activity.zjimg3.equals("null")){
//            ssss3.setVisibility(View.GONE);
//        }
//        img_heronub_back = (ImageView) findViewById(R.id.img_heronub_back);
//        img_heronub_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        img_show01.setOnClickListener(this);
//        img_show02.setOnClickListener(this);
//        img_show03.setOnClickListener(this);
//        String urlUploadFile = "/api/File/GetRefund?filename="+Sure_Order_Activity.zjimg1;
//        HttpFileHelper.httpGetFile(1,urlUploadFile,handler);
//    }
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 1:
//                    byte[] data = (byte[])msg.obj;
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    img_show01.setImageBitmap(bitmap);
//                    Context context = TkActivity.this;
//                    dia = new Dialog(context, R.style.edit_AlertDialog_style);
//                    dia.setContentView(R.layout.activity_start_dialog);
//                    ImageView imageView = (ImageView) dia.findViewById(R.id.start_img);
//                    //imageView.setBackgroundResource(R.drawable.imggg1);
//                    imageView.setImageBitmap(bitmap);
//                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
//                    dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
//                    Window w = dia.getWindow();
//                    WindowManager.LayoutParams lp = w.getAttributes();
//                    lp.x = 0;
//                    lp.y = 40;
//                    dia.onWindowAttributesChanged(lp);
//                    imageView.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dia.dismiss();
//                                }
//                            });
//                    String urlUploadFile = "/api/File/GetRefund?filename="+Sure_Order_Activity.zjimg2;
//                    HttpFileHelper.httpGetFile(2,urlUploadFile,handler);
//                    break;
//                case 2:
//                    byte[] data1 = (byte[])msg.obj;
//                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
//                    img_show02.setImageBitmap(bitmap1);
//                    Context context1 = TkActivity.this;
//                    dia1 = new Dialog(context1, R.style.edit_AlertDialog_style);
//                    dia1.setContentView(R.layout.activity_start_dialog);
//                    ImageView imageView1 = (ImageView) dia1.findViewById(R.id.start_img);
//                    //imageView.setBackgroundResource(R.drawable.imggg1);
//                    imageView1.setImageBitmap(bitmap1);
//                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
//                    dia1.setCanceledOnTouchOutside(true); // Sets whether this dialog is
//                    Window w1 = dia1.getWindow();
//                    WindowManager.LayoutParams lp1 = w1.getAttributes();
//                    lp1.x = 0;
//                    lp1.y = 40;
//                    dia.onWindowAttributesChanged(lp1);
//                    imageView1.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dia1.dismiss();
//                                }
//                            });
//                    String url1 = "/api/File/GetRefund?filename="+Sure_Order_Activity.zjimg3;
//                    HttpFileHelper.httpGetFile(3,url1,handler);
//                    break;
//                case 3:
//                    byte[] data2 = (byte[])msg.obj;
//                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
//                    img_show03.setImageBitmap(bitmap2);
//                    Context context2 = TkActivity.this;
//                    dia2 = new Dialog(context2, R.style.edit_AlertDialog_style);
//                    dia2.setContentView(R.layout.activity_start_dialog);
//                    ImageView imageView2 = (ImageView) dia2.findViewById(R.id.start_img);
//                    //imageView.setBackgroundResource(R.drawable.imggg1);
//                    imageView2.setImageBitmap(bitmap2);
//                    //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
//                    dia2.setCanceledOnTouchOutside(true); // Sets whether this dialog is
//                    Window w2 = dia2.getWindow();
//                    WindowManager.LayoutParams lp2 = w2.getAttributes();
//                    lp2.x = 0;
//                    lp2.y = 40;
//                    dia2.onWindowAttributesChanged(lp2);
//                    imageView2.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    dia2.dismiss();
//                                }
//                            });
//                    break;
//            }
//        }
//    };
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        switch (id){
//            case R.id.img_show001:
//                dia.show();
//                break;
//            case R.id.img_show002:
//                dia1.show();
//                break;
//            case R.id.img_show003:
//                dia2.show();
//                break;
//        }
//    }
}
