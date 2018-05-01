package com.leveling.new_chat;

import android.renderscript.Element;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lin on 2018/3/29.
 */

public abstract class BaseRecyclerHolder<DataType> extends RecyclerView.ViewHolder {

    public BaseRecyclerHolder(View view){
        super(view);
    }


    public abstract void displayData(DataType data);

}
