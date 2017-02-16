package com.lh.daily.ui.homepage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lh.daily.R;
import com.lh.daily.adapter.GankListAdapter;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.bean.gank.Gank;
import com.lh.daily.databinding.ListLayoutBinding;
import com.lh.daily.mvp.contract.GankListContract;
import com.lh.daily.mvp.presenter.GankListPresenter;
import com.lh.daily.ui.webview.WebViewActivity;
import com.lh.daily.widget.LoadMoreRecyclerView;

import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public class GankListFragment extends BaseFragment<ListLayoutBinding> implements GankListContract.View {

    private GankListContract.Presenter mPresenter;

    private GankListAdapter mAdapter;
    private int mCurrentPage;

    public static GankListFragment newInstance() {
        return new GankListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.list_layout;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mCurrentPage = 1;
        mPresenter = new GankListPresenter();
        mDataBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mDataBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                mPresenter.refreshData(mCurrentPage);
            }
        });
        if (mAdapter == null) {
            mAdapter = new GankListAdapter();
        }
        mDataBinding.recyclerView.setAdapter(mAdapter);
        mDataBinding.recyclerView.setEnableLoadMore(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDataBinding.recyclerView.setLayoutManager(layoutManager);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mDataBinding.recyclerView.setOnLoadMoreLinstener(new LoadMoreRecyclerView.OnLoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                mPresenter.loadMoreData(mCurrentPage);
            }
        });
        mAdapter.setOnGankItemClickListener(new GankListAdapter.OnGankItemClickListener() {
            @Override
            public void onGankClick(String url) {
                WebViewActivity.startActivity(getContext(),url);
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
        mPresenter.refreshData(mCurrentPage);
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
    public void refreshData(List<Gank.Result> results) {
        mCurrentPage = 1;
        mAdapter.clear(false);
        mAdapter.addGankResults(results);
        mAdapter.notifyDataSetChanged();
        mDataBinding.recyclerView.setEnableLoadMore(true);
        mCurrentPage++;
    }

    @Override
    public void addMoreData(List<Gank.Result> results) {
        mCurrentPage++;
        mAdapter.addGankResults(results);
        mDataBinding.recyclerView.setRefreshing(false);
    }

    @Override
    public void showNoMore() {
        mDataBinding.recyclerView.setRefreshStatus(LoadMoreRecyclerView.STATUS_NO_MORE);
    }

    @Override
    public void showClickRetry() {
        mDataBinding.recyclerView.setRefreshStatus(LoadMoreRecyclerView.STATUS_LOADING_RETRY);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }
}
