package com.lh.daily.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by home on 2017/2/8.
 * DataBinding模式的ViewHold
 */

class DataBindingViewHolder<DB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    DB mDataBinding;

    DataBindingViewHolder(DB dataBinding) {
        super(dataBinding.getRoot());
        mDataBinding = dataBinding;
    }
}
