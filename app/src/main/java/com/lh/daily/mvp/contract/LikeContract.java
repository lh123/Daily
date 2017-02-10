package com.lh.daily.mvp.contract;

import com.lh.daily.base.BasePresenter;
import com.lh.daily.base.BaseView;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;

import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public interface LikeContract {
    interface View extends BaseView {
        void showLoading();

        void stopLoading();

        void showEmpty();

        void showResult(List<ZhihuDailyDetail> details);
    }

    interface Presenter extends BasePresenter<View> {
        void loadLikeData();
    }
}
