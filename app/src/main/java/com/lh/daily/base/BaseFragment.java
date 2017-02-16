package com.lh.daily.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.daily.DailyApp;

/**
 * Created by home on 2017/2/8.
 */

public abstract class BaseFragment<DB extends ViewDataBinding> extends Fragment {

    private boolean isViewPrepared = false;
    private boolean isDataLoaded = false;

    protected DB mDataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater,getLayoutResId(),container,false);
        initView(savedInstanceState);
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepared = true;
        if (getUserVisibleHint() && isViewPrepared && !isDataLoaded) {
            isDataLoaded = true;
            fetchData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isViewPrepared && !isDataLoaded) {
            isDataLoaded = true;
            fetchData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DailyApp.getWatcher(getContext()).watch(this);
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void fetchData();
}
