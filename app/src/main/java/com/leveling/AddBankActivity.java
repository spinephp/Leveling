package com.leveling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leveling.common.CheckVerifyCodeActivity;
import com.leveling.ui.EasyLoading;
import com.leveling.ui.StatusControl;
import com.leveling.utils.HttpGetUtils;
import com.leveling.utils.HttpPostUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
public class AddBankActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout img_title_back;
    private EditText card_person, card_number;
    private Button add_card;
    private LinearLayout card_type;
    private TextView wherecard, cardbnc;
    private String[] bankList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_bank_layout);
        img_title_back = (LinearLayout) findViewById(R.id.img_title_back);
        cardbnc = (TextView) findViewById(R.id.cardbnc);
        cardbnc.setText(HttpPostUtils.getNickname());
        img_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpPostUtils.getRole() == 0) {
                    Intent intent = new Intent(AddBankActivity.this, TixianActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (MyWalletActivity.message == "1") {
                        Intent intent = new Intent(AddBankActivity.this, TixianPackageActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (MyWalletActivity.message == "2") {
                        Intent intent = new Intent(AddBankActivity.this, TixianDailianActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (MyWalletActivity.message == "3") {
                        Intent intent = new Intent(AddBankActivity.this, TixianPeilianActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        card_number = (EditText) findViewById(R.id.card_number);
        card_person = (EditText) findViewById(R.id.card_person);
        card_type = (LinearLayout) findViewById(R.id.card_type);
        wherecard = (TextView) findViewById(R.id.wherecard);
        add_card = (Button) findViewById(R.id.add_card);
        add_card.setOnClickListener(this);
        card_type.setOnClickListener(this);
        String url = "/api/Pay/GetBackList";
        HttpGetUtils.httpGetFile(0x1000, url, handler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (HttpPostUtils.getRole() == 0) {
            Intent intent = new Intent(AddBankActivity.this, TixianActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (MyWalletActivity.message == "1") {
                Intent intent = new Intent(AddBankActivity.this, TixianPackageActivity.class);
                startActivity(intent);
                finish();
            } else if (MyWalletActivity.message == "2") {
                Intent intent = new Intent(AddBankActivity.this, TixianDailianActivity.class);
                startActivity(intent);
                finish();
            } else if (MyWalletActivity.message == "3") {
                Intent intent = new Intent(AddBankActivity.this, TixianPeilianActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x1000: {
                    String res = (String) msg.obj;
                    if (res == null) {
                        Toast.makeText(AddBankActivity.this, "获取银行列表失败", Toast.LENGTH_LONG).show();
                    } else {
                        res = res.substring(1, res.length() - 1);
                        bankList = res.split(",");
                    }
                    break;
                }
                case 1:
                    try {
                        String res = (String) msg.obj;
                        if (res == null) {
                            return;
                        }
                        JSONObject obj = new JSONObject(res);
                        String success = obj.getString("Success");
                        String err = obj.getString("ErrMsg");
                        if (success == "true") {
                            Toast.makeText(AddBankActivity.this, "操作成功", Toast.LENGTH_LONG).show();
                            if (HttpPostUtils.getRole() == 0) {
                                Intent intent = new Intent(AddBankActivity.this, TixianActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (MyWalletActivity.message == "1") {
                                    Intent intent = new Intent(AddBankActivity.this, TixianPackageActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (MyWalletActivity.message == "2") {
                                    Intent intent = new Intent(AddBankActivity.this, TixianDailianActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (MyWalletActivity.message == "3") {
                                    Intent intent = new Intent(AddBankActivity.this, TixianPeilianActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        } else if (success == "false") {
                            Toast.makeText(AddBankActivity.this, err, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.card_type:
                ChooseCard();
                break;
            case R.id.add_card:
                String num = card_number.getText().toString();
                if (card_number.getText().toString().equals("")) {
                    Toast.makeText(AddBankActivity.this, "请输入银行卡号", Toast.LENGTH_LONG).show();
                } else if (card_person.getText().toString().equals("")) {
                    Toast.makeText(AddBankActivity.this, "请输入开户人姓名", Toast.LENGTH_LONG).show();
                } else if (wherecard.getText().toString().equals("请选银行卡类型")) {
                    Toast.makeText(AddBankActivity.this, "请选择银行卡类型", Toast.LENGTH_LONG).show();
                } else {
                    Intent it = new Intent(AddBankActivity.this, CheckVerifyCodeActivity.class);
                    startActivityForResult(it, 0x1000);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0x1000){
            if(resultCode == 0x1000){
                EasyLoading.doWork(AddBankActivity.this, "正在添加", new EasyLoading.WorkerListener() {
                    @Override
                    public void run(StatusControl statusControl) {
                        String url = "/api/Users/GetUpdatePayInfor?type=" + 1 + "&Name=" + card_person.getText().toString() + "&Number=" + card_number.getText().toString() + "&CardType=" + wherecard.getText().toString();
                        HttpGetUtils.httpGetFile(1, url, handler);
                    }
                });
            }
        }
    }

    private void ChooseCard() {
        DataPickerDialog.Builder builder = new DataPickerDialog.Builder(this);
        if (bankList == null) {
            Toast.makeText(this, "未能正确获取银行列表，请重试。", Toast.LENGTH_LONG).show();
            return;
        }
        List<String> data = Arrays.asList(bankList);
        DataPickerDialog dialog = builder.setUnit("").setData(data).setSelection(1).setTitle("取消")
                .setOnDataSelectedListener(new DataPickerDialog.OnDataSelectedListener() {
                    @Override
                    public void onDataSelected(String itemValue) {
                        wherecard.setText(itemValue);
                    }
                }).create();
        dialog.show();
    }
}
