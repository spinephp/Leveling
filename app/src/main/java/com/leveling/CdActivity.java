package com.leveling;

import android.support.v7.app.AppCompatActivity;

public class CdActivity extends AppCompatActivity {
//    ImageView img_show01,img_show02,img_show03,img_heronub_back;
//    Dialog dia,dia1,dia2;
//    TextView ssss1,ssss2,ssss3;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cd);
//        img_show01 = (ImageView) findViewById(R.id.img_show01);
//        img_show02 = (ImageView) findViewById(R.id.img_show02);
//        img_show03 = (ImageView) findViewById(R.id.img_show03);
//        ssss1 = (TextView) findViewById(R.id.ssss1);
//        ssss2 = (TextView) findViewById(R.id.ssss2);
//        ssss3 = (TextView) findViewById(R.id.ssss3);
//
//        img_show01.setOnClickListener(this);
//        img_show02.setOnClickListener(this);
//        img_show03.setOnClickListener(this);
//        img_heronub_back = (ImageView) findViewById(R.id.img_heronub_back);
//        img_heronub_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        if (LevelingActivity.cdimg1.equals("null")&&LevelingActivity.cdimg3.equals("null")){
//            ssss1.setText("点击图片查看大图");
//            String urlUploadFile = "/api/File/GetRevoke?filename="+LevelingActivity.cdimg2;
//            HttpFileHelper.httpGetFile(1,urlUploadFile,handler);
//        }else if (LevelingActivity.cdimg1.equals("null")&&LevelingActivity.cdimg2.equals("null")){
//            ssss1.setText("点击图片查看大图");
//            String urlUploadFile = "/api/File/GetRevoke?filename="+LevelingActivity.cdimg3;
//            HttpFileHelper.httpGetFile(1,urlUploadFile,handler);
//        }else if (LevelingActivity.cdimg2.equals("null")){
//
//        }
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
//                    Context context = CdActivity.this;
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
//                    String urlUploadFile = "/api/File/GetRevoke?filename="+LevelingActivity.cdimg2;
//                    HttpFileHelper.httpGetFile(2,urlUploadFile,handler);
//                    break;
//                case 2:
//                    byte[] data1 = (byte[])msg.obj;
//                    Bitmap bitmap1 = BitmapFactory.decodeByteArray(data1, 0, data1.length);
//                    img_show02.setImageBitmap(bitmap1);
//                    Context context1 = CdActivity.this;
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
//                    String url1 = "/api/File/GetRevoke?filename="+LevelingActivity.cdimg3;
//                    HttpFileHelper.httpGetFile(3,url1,handler);
//                    break;
//                case 3:
//                    byte[] data2 = (byte[])msg.obj;
//                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(data2, 0, data2.length);
//                    img_show03.setImageBitmap(bitmap2);
//                    Context context2 = CdActivity.this;
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
//            case R.id.img_show01:
//                dia.show();
//                break;
//            case R.id.img_show02:
//                dia1.show();
//                break;
//            case R.id.img_show03:
//                dia2.show();
//                break;
//        }
//    }
}
