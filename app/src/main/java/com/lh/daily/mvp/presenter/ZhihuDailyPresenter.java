package com.lh.daily.mvp.presenter;

import com.lh.daily.api.ApiHelper;
import com.lh.daily.bean.zhihu.ZhihuDaily;
import com.lh.daily.mvp.contract.ZhihuDailyContract;
import com.lh.daily.utils.RxUtils;
import com.lh.daily.utils.ZhihuDataUtils;

import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by home on 2017/2/8.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {

    private static final int MODE_REFRESH = 1;
    private static final int MODE_LOAD_MORE = 2;

    private ZhihuDailyContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void attachView(ZhihuDailyContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mCompositeDisposable.clear();
    }

    public void loadData(long date, final int mode) {
        Disposable disposable = ApiHelper.getInstance().getZhihuDailyService()
                .loadDaily(date)
                .compose(RxUtils.<ZhihuDaily>io_main())
                .singleOrError()
                .subscribeWith(new DisposableSingleObserver<ZhihuDaily>() {
                    @Override
                    public void onSuccess(ZhihuDaily zhihuDaily) {
                        if (mode == MODE_LOAD_MORE) {
                            if (zhihuDaily.getStories().size() > 0) {
                                mView.addMoreData(zhihuDaily.getStories());
                            } else {
                                mView.showNoMore();
                            }
                        } else if (mode == MODE_REFRESH) {
                            mView.refreshData(zhihuDaily.getStories());
                            mView.stopRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mode == MODE_REFRESH) {
                            mView.showError();
                            mView.stopRefresh();
                        } else if (mode == MODE_LOAD_MORE) {
                            mView.showClickRetry();
                        }
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void refreshData(Date date) {
        loadData(ZhihuDataUtils.long2date(date.getTime()),MODE_REFRESH);
    }

    @Override
    public void loadMoreData(Date date) {
        loadData(ZhihuDataUtils.long2date(date.getTime()),MODE_LOAD_MORE);
    }
}
