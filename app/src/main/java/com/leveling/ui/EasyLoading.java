package com.leveling.ui;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.dyhdyh.widget.loading.factory.LoadingFactory;
import com.leveling.R;

import org.w3c.dom.Text;

/**
 * Created by myipp on 2018/3/17.
 */
public class EasyLoading implements LoadingFactory, StatusControl {

    View parent;
    TextView tvContent;
    String status;

    @Override
    public View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_process_dialog_color, parent, false);
        tvContent = (TextView)view.findViewById(R.id.text_content);
        setStatus(status);
        return view;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1)
                LoadingBar.cancel(parent);
            else {
                String text = (String) msg.obj;
                tvContent.setText(text);
            }
        }
    };

    public void setStatus(String status){
        this.status = status;
        if(tvContent != null){
            tvContent.setText(status);
        }
    }

    @Override
    public void update(String newStatus) {
        Message msg = new Message();
        msg.what = 2;
        msg.obj = newStatus;
        handler.sendMessage(msg);
    }

    public interface WorkerListener {
        void run(StatusControl statusControl);
    }

    public static void doWork(Activity act, String status, final WorkerListener worker) {
        doWork(act.getWindow().getDecorView(), status, worker);
    }

    public static void doWork(View parView, String status, final WorkerListener worker) {
        final EasyLoading ld = new EasyLoading();
        ld.parent = parView;
        ld.setStatus(status);
        LoadingBar.make(parView, ld).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    worker.run(ld);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                ld.handler.sendEmptyMessage(1);
            }
        }).start();
    }
}
