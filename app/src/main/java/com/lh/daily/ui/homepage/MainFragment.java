package com.lh.daily.ui.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.lh.daily.R;
import com.lh.daily.adapter.MainViewPagerAdapter;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.databinding.FragmentMainBinding;

/**
 * Created by home on 2017/2/8.
 */

public class MainFragment extends BaseFragment<FragmentMainBinding> {

    public static final String TAG = "MainFragment";

    private MainViewPagerAdapter mAdapter;
    private IDrawerActivity mDrawer;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IDrawerActivity){
            mDrawer = (IDrawerActivity) context;
        }
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (mAdapter == null) {
            Fragment[] fragments = new Fragment[]{ZhihuDailyFragment.newInstance(),
                    GankListFragment.newInstance()};
            String[] strings = new String[]{"知乎日报", "Gank"};
            mAdapter = new MainViewPagerAdapter(getChildFragmentManager(), fragments, strings);
        }
        mDataBinding.toolbar.setTitle(R.string.app_name);
        mDataBinding.viewPager.setAdapter(mAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewPager);
        mDataBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawer == null){
                    return;
                }
                if (!mDrawer.isDrawerOpen()){
                    mDrawer.openDrawer();
                }else {
                    mDrawer.closeDrawer();
                }
            }
        });
    }

    @Override
    protected void fetchData() {

    }
}
