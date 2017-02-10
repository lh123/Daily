package com.lh.daily.mvp.contract;

import com.lh.daily.base.BasePresenter;
import com.lh.daily.base.BaseView;
import com.lh.daily.bean.gank.Gank;

import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public interface GankListContract {
    interface View extends BaseView{
        void showError();

        void stopRefresh();

        void refreshData(List<Gank.Result> results);

        void addMoreData(List<Gank.Result> results);

        void showNoMore();

        void showClickRetry();
    }

    interface Presenter extends BasePresenter<View>{
        void refreshData(int page);

        void loadMoreData(int page);
    }
}
