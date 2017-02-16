package com.lh.daily.ui.homepage;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.lh.daily.R;
import com.lh.daily.base.BaseActivity;
import com.lh.daily.databinding.ActivityMainBinding;
import com.lh.daily.ui.like.LikeFragment;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements IDrawerActivity {

    private Fragment mCurrentFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Fragment fragment = getFragment(MainFragment.class.getName());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, MainFragment.class.getName())
                    .show(fragment)
                    .commit();
            mCurrentFragment = fragment;
        }
        mDataBinding.drawer.setCheckedItem(R.id.home);
        mDataBinding.drawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        showFragment(MainFragment.class.getName());
                        break;
                    case R.id.like:
                        showFragment(LikeFragment.class.getName());
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
            if (MainFragment.class.getName().equals(tag)) {
                fragment = MainFragment.newInstance();
            } else if (LikeFragment.class.getName().equals(tag)) {
                fragment = LikeFragment.newInstance();
            }
        }
        return fragment;
    }

    public void showFragment(String tag) {
        Fragment toFragment = getFragment(tag);
        if (mCurrentFragment == toFragment) {
            return;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,toFragment,tag)
                .commit();
        mCurrentFragment = toFragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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
