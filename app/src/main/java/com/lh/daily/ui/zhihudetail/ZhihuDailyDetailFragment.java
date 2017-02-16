package com.lh.daily.ui.zhihudetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lh.daily.R;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.databinding.FragmentZhihuDailyDetailBinding;
import com.lh.daily.mvp.contract.ZhihuDailyDetailContract;
import com.lh.daily.mvp.presenter.ZhihuDailyDetailPresenter;

/**
 * Created by home on 2017/2/10.
 */

public class ZhihuDailyDetailFragment extends BaseFragment<FragmentZhihuDailyDetailBinding> implements ZhihuDailyDetailContract.View {

    private static final String EXTRA_ID = "id";

    private ZhihuDailyDetailContract.Presenter mPresenter;
    private int mId;

    public static ZhihuDailyDetailFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, id);
        ZhihuDailyDetailFragment fragment = new ZhihuDailyDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_zhihu_daily_detail;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mId = getArguments().getInt(EXTRA_ID, 0);
        mDataBinding.toolbar.inflateMenu(R.menu.detail_menu);
        mDataBinding.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (!mPresenter.isLike()) {
                    mPresenter.likeStory();
                } else {
                    mPresenter.unlikeStory();
                }
                return false;
            }
        });
        mDataBinding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        mPresenter = new ZhihuDailyDetailPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    protected void fetchData() {
        mPresenter.loadStoryDetail(mId);
    }

    @Override
    public void showTitle(String title) {
        mDataBinding.toolbar.setTitle(title);
    }

    @Override
    public void showImge(String url) {
        mDataBinding.setImg(url);
    }

    @Override
    public void showContent(String content) {
        mDataBinding.webView.loadDataWithBaseURL("file:///android_asset/", content, "text/html;charset=utf-8", "utf-8", null);
    }

    @Override
    public void setLikeState(boolean favoriteState) {
        MenuItem item = mDataBinding.toolbar.getMenu().findItem(R.id.like);
        item.setTitle(favoriteState ? R.string.unlike : R.string.like);
    }

    @Override
    public void showError() {
        Snackbar.make(mDataBinding.getRoot(), R.string.load_fail, Snackbar.LENGTH_SHORT).show();
    }
}
