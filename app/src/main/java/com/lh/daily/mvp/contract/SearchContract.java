package com.lh.daily.mvp.contract;

import com.lh.daily.base.BasePresenter;
import com.lh.daily.base.BaseView;

import java.util.List;

/**
 * Created by home on 2017/2/11.
 */

public interface SearchContract {
    interface View extends BaseView {
        void showSuggest(List<String> suggest);

        void showResult();
    }

    interface Presenter extends BasePresenter<View> {
        void loadSuggest(String key);

        void loadResult(String key);
    }
}
