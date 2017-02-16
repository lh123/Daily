package com.lh.daily.ui.like;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.lh.daily.R;
import com.lh.daily.adapter.LikeAdapter;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;
import com.lh.daily.databinding.FragmentLikeBinding;
import com.lh.daily.mvp.contract.LikeContract;
import com.lh.daily.mvp.contract.SearchContract;
import com.lh.daily.mvp.presenter.LikePresenter;
import com.lh.daily.mvp.presenter.SearchPresenter;
import com.lh.daily.ui.homepage.IDrawerActivity;
import com.lh.daily.ui.zhihudetail.ZhihuDailyDetailActivity;
import com.lh.daily.widget.SearchView;

import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public class LikeFragment extends BaseFragment<FragmentLikeBinding> implements LikeContract.View, SearchContract.View {

    private LikeContract.Presenter mPresenter;
    private SearchContract.Presenter mSearchPresenter;

    private LikeAdapter mAdapter;
    private SearchView mSearchView;

    public static LikeFragment newInstance() {
        return new LikeFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_like;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter = new LikePresenter();
        mSearchPresenter = new SearchPresenter();
        mDataBinding.views.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mDataBinding.views.toolbar.setTitle(R.string.my_like);
        mDataBinding.views.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof IDrawerActivity) {
                    IDrawerActivity activity = (IDrawerActivity) getActivity();
                    if (activity.isDrawerOpen()) {
                        activity.closeDrawer();
                    } else {
                        activity.openDrawer();
                    }
                }
            }
        });
        MenuItem item = mDataBinding.views.toolbar.getMenu().add(R.string.search);
        item.setIcon(R.drawable.ic_search_white_24dp);
        item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showSearchView();
                return false;
            }
        });
        mDataBinding.swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mDataBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadLikeData();
            }
        });
        if (mAdapter == null) {
            mAdapter = new LikeAdapter();
        }
        mAdapter.setOnLikeItemClickListener(new LikeAdapter.OnLikeItemClickListener() {
            @Override
            public void onLikeItemClick(int type, int id) {
                switch (type) {
                    case 1:
                        ZhihuDailyDetailActivity.startActivity(getContext(), id);
                        break;
                }
            }
        });
        mDataBinding.recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mDataBinding.recyclerView.setLayoutManager(manager);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mPresenter.attachView(this);
        mSearchPresenter.attachView(this);
    }

    private void showSearchView() {
        mSearchView = SearchView.newInstance();
        mSearchView.show(getFragmentManager(), SearchView.class.getName());
        mSearchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void onSuggestClick(int index) {

            }

            @Override
            public void onSearch(String key) {

            }

            @Override
            public void onTextChanged(String after) {
                mSearchPresenter.loadSuggest(after);
            }
        });
    }

    @Override
    protected void fetchData() {
        mPresenter.loadLikeData();
    }

    @Override
    public void showLoading() {
        mDataBinding.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        mDataBinding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showEmpty() {
        mDataBinding.recyclerView.setVisibility(View.GONE);
        mDataBinding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult(List<ZhihuDailyDetail> details) {
        mDataBinding.recyclerView.setVisibility(View.VISIBLE);
        mDataBinding.emptyView.setVisibility(View.GONE);
        mAdapter.clear(true);
        mAdapter.addStories(details);
    }

    @Override
    public void showSuggest(List<String> suggest) {
        if (mSearchView != null) {
            mSearchView.setSuggest(suggest);
        }
    }

    @Override
    public void showResult() {

    }
}
