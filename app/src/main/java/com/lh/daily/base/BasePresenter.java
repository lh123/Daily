package com.lh.daily.base;

/**
 * Created by home on 2017/2/8.
 */

public interface BasePresenter<V extends BaseView> {

    //绑定View
    void attachView(V view);

    //解除View的绑定
    void detachView();
}
