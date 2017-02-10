package com.lh.daily.mvp.contract;

import com.lh.daily.base.BasePresenter;
import com.lh.daily.base.BaseView;
import com.lh.daily.bean.zhihu.ZhihuDaily;

import java.util.Date;
import java.util.List;

/**
 * Created by home on 2017/2/8.
 */

public interface ZhihuDailyContract {
    interface View extends BaseView {
        void showError();

        void stopRefresh();

        void refreshData(List<ZhihuDaily.Story> stories);

        void addMoreData(List<ZhihuDaily.Story> stories);

        void showNoMore();

        void showClickRetry();

        void showPickDialog();
    }

    interface Presenter extends BasePresenter<View> {
        void refreshData(Date date);

        void loadMoreData(Date date);
    }
}
