package com.lh.daily.ui.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.lh.daily.R;
import com.lh.daily.base.BaseActivity;
import com.lh.daily.databinding.ActivityMainBinding;
import com.lh.daily.ui.favorite.LikeFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IDrawerActivity {

    private Fragment mCurrentFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        Fragment fragment = getFragment(MainFragment.TAG);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, MainFragment.TAG)
                .commit();
        mCurrentFragment = fragment;
        mDataBinding.drawer.setCheckedItem(R.id.home);
        mDataBinding.drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        showFragment(MainFragment.TAG);
                        break;
                    case R.id.like:
                        showFragment(LikeFragment.TAG);
                        break;
                }
                closeDrawer();
                return true;
            }
        });
    }

    private Fragment getFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            switch (tag) {
                case MainFragment.TAG:
                    fragment = MainFragment.newInstance();
                    break;
                case LikeFragment.TAG:
                    fragment = LikeFragment.newInstance();
                    break;
            }
        }
        return fragment;
    }

    public void showFragment(String tag) {
        Fragment fragment = getFragment(tag);
        if (mCurrentFragment == fragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,fragment,tag)
                .commit();
        mCurrentFragment = fragment;
    }

    @Override
    public void openDrawer() {
        mDataBinding.drawerLayout.openDrawer(mDataBinding.drawer);
    }

    @Override
    public void closeDrawer() {
        mDataBinding.drawerLayout.closeDrawer(mDataBinding.drawer);
    }

    @Override
    public boolean isDrawerOpen() {
        return mDataBinding.drawerLayout.isDrawerOpen(mDataBinding.drawer);
    }
}
