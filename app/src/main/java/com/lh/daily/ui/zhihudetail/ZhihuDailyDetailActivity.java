package com.lh.daily.ui.zhihudetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.lh.daily.R;
import com.lh.daily.base.BaseActivity;

/**
 * Created by home on 2017/2/9.
 */

public class ZhihuDailyDetailActivity extends BaseActivity{

    private static final String EXTRA_ID = "id";

    public static void startActivity(Context context,int id){
        Intent intent = new Intent(context,ZhihuDailyDetailActivity.class);
        intent.putExtra(EXTRA_ID,id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_container;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState==null){
            int id = getIntent().getIntExtra(EXTRA_ID,0);
            Fragment fragment = ZhihuDailyDetailFragment.newInstance(id);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment,ZhihuDailyDetailFragment.TAG)
                    .commit();
        }
    }
}
