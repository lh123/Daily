package com.lh.daily.ui.favorite;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lh.daily.R;
import com.lh.daily.adapter.LikeAdapter;
import com.lh.daily.base.BaseFragment;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;
import com.lh.daily.databinding.FragmentLikeBinding;
import com.lh.daily.mvp.contract.LikeContract;
import com.lh.daily.mvp.presenter.LikePresenter;
import com.lh.daily.ui.homepage.IDrawerActivity;
import com.lh.daily.ui.zhihudetail.ZhihuDailyDetailActivity;

import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public class LikeFragment extends BaseFragment<FragmentLikeBinding> implements LikeContract.View {

    public static final String TAG = "LikeFragment";

    private LikeContract.Presenter mPresenter;

    private LikeAdapter mAdapter;

    public static LikeFragment newInstance(){
        return new LikeFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_like;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter = new LikePresenter();
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
                switch (type){
                    case 1:
                        ZhihuDailyDetailActivity.startActivity(getContext(),id);
                        break;
                }
            }
        });
        mDataBinding.recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        mDataBinding.recyclerView.setLayoutManager(manager);
        mDataBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        mPresenter.attachView(this);
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
        mDataBinding.swipeRefreshLayout.setVisibility(View.GONE);
        mDataBinding.emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResult(List<ZhihuDailyDetail> details) {
        mDataBinding.swipeRefreshLayout.setVisibility(View.VISIBLE);
        mDataBinding.emptyView.setVisibility(View.GONE);
        mAdapter.clear(true);
        mAdapter.addStories(details);
    }
}
