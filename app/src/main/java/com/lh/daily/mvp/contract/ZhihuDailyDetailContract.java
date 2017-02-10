package com.lh.daily.mvp.contract;

import com.lh.daily.base.BasePresenter;
import com.lh.daily.base.BaseView;

/**
 * Created by home on 2017/2/8.
 */

public interface ZhihuDailyDetailContract {

    interface View extends BaseView {

        void showTitle(String title);

        void showImge(String url);

        void showContent(String content);

        void setLikeState(boolean favoriteState);

        void showError();
    }

    interface Presenter extends BasePresenter<View> {

        void loadStoryDetail(int id);

        void likeStory();

        void unlikeStory();

        boolean isLike();
    }
}
