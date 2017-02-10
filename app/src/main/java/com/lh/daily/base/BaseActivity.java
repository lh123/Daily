package com.lh.daily.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by home on 2017/2/8.
 */

public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    protected DB mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this,getLayoutResId());
        initView(savedInstanceState);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initView(@Nullable Bundle savedInstanceState);
}
