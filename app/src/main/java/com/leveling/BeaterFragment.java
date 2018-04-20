package com.youyudj.leveling;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * *******************************************************
 * Description:
 * Autour: 3W攻城狮
 * Date: 2017/11/7 11:39
 * Update:2017/11/7
 * Version:
 * *******************************************************
 */
public class BeaterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_need_order_layout,null);
    }
}
