package com.leveling;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by lin on 2018/4/6.
 */

public class TestFragment extends Fragment {

    private static String TAG = TestFragment.class.getSimpleName();


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.layout_test, null);



        final LinearLayout la_duanwei = (LinearLayout)view.findViewById(R.id.la_duanwei);

        la_duanwei.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "la_duanwei宽度："+view.getWidth());
                Log.d(TAG,"la_duanwei高度"+view.getHeight());
            }
        });


        return view;
    }

}
