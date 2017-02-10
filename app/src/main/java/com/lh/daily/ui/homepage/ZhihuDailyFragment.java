package com.lh.daily.ui.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.lh.daily.R;
import com.lh.daily.adapter.ZhihuDailyListAdapter;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.bean.zhihu.ZhihuDaily;
import com.lh.daily.databinding.ListLayoutBinding;
import com.lh.daily.mvp.contract.ZhihuDailyContract;
import com.lh.daily.mvp.presenter.ZhihuDailyPresenter;
import com.lh.daily.ui.zhihudetail.ZhihuDailyDetailActivity;
import com.lh.daily.widget.LoadMoreRecyclerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by home on 2017/2/8.
 * 知乎日报列表界面
 */

public class ZhihuDailyFragment extends BaseFragment<ListLayoutBinding> implements ZhihuDailyContract.View {

    private ZhihuDailyContract.Presenter mPresenter;
    private ZhihuDailyListAdapter mAdapter;
    private Date mCurrentDate;

    public static ZhihuDailyFragment newInstance() {
        return new ZhihuDailyFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list_layout;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter = new ZhihuDailyPresenter();
        if (mAdapter == null) {
            mAdapter = new ZhihuDailyListAdapter();
        }
        mDataBinding.recyclerView.setEnableLoadMore(false);
        mDataBinding.recyclerView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDataBinding.recyclerView.setLayoutManager(linearLayoutManager);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mDataBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mDataBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentDate = new Date();
                mPresenter.refreshData(mCurrentDate);
            }
        });
        mDataBinding.recyclerView.setOnLoadMoreLinstener(new LoadMoreRecyclerView.OnLoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData(mCurrentDate);
            }
        });
        mAdapter.setOnStoryClickListener(new ZhihuDailyListAdapter.OnStoryClickListener() {
            @Override
            public void onStoryClick(int id) {
                ZhihuDailyDetailActivity.startActivity(getContext(),id);
            }
        });
        mPresenter.attachView(this);
    }

    @Override
    protected void fetchData() {
        mDataBinding.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mDataBinding.swipeRefreshLayout.setRefreshing(true);
            }
        });
        mCurrentDate = new Date();
        mPresenter.refreshData(mCurrentDate);
    }

    @Override
    public void showError() {
        Snackbar.make(mDataBinding.getRoot(), R.string.load_fail, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void stopRefresh() {
        mDataBinding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void refreshData(List<ZhihuDaily.Story> stories) {
        mDataBinding.recyclerView.setEnableLoadMore(true);
        mDataBinding.recyclerView.setRefreshing(false);
        mAdapter.clear();
        mAdapter.addStories(stories);
        Calendar ca = Calendar.getInstance();
        ca.setTime(mCurrentDate);
        ca.add(Calendar.DAY_OF_YEAR, -1);
        mCurrentDate = ca.getTime();
    }

    @Override
    public void addMoreData(List<ZhihuDaily.Story> stories) {
        mAdapter.addStories(stories);
        mDataBinding.recyclerView.setRefreshing(false);
        Calendar ca = Calendar.getInstance();
        ca.setTime(mCurrentDate);
        ca.add(Calendar.DAY_OF_YEAR, -1);
        mCurrentDate = ca.getTime();
    }

    @Override
    public void showNoMore() {
        mDataBinding.recyclerView.setRefreshStatus(LoadMoreRecyclerView.STATUS_LOADING_RETRY);
    }

    @Override
    public void showClickRetry() {
        mDataBinding.recyclerView.setRefreshStatus(LoadMoreRecyclerView.STATUS_LOADING_RETRY);
    }

    @Override
    public void showPickDialog() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }
}
