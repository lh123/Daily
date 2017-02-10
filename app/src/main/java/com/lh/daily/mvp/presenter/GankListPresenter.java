package com.lh.daily.mvp.presenter;

import com.lh.daily.api.ApiHelper;
import com.lh.daily.bean.gank.Gank;
import com.lh.daily.mvp.contract.GankListContract;
import com.lh.daily.utils.RxUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by home on 2017/2/10.
 */

public class GankListPresenter implements GankListContract.Presenter {

    private static final int MODE_REFRESH = 1;
    private static final int MODE_LOAD_MORE = 2;

    private CompositeDisposable mCompositeDisposable;

    private GankListContract.View mView;

    @Override
    public void refreshData(int page) {
        loadData(page, MODE_REFRESH);
    }

    @Override
    public void loadMoreData(int page) {
        loadData(page,MODE_LOAD_MORE);
    }

    private void loadData(int page, final int mode) {
        Disposable disposable = ApiHelper.getInstance().getGankService()
                .loadGanksData(20, page)
                .flatMap(new Function<Gank, ObservableSource<List<Gank.Result>>>() {
                    @Override
                    public ObservableSource<List<Gank.Result>> apply(Gank gank) throws Exception {
                        if (gank.isError()) {
                            return Observable.error(new RuntimeException("gank api error"));
                        } else {
                            return Observable.just(gank.getResults());
                        }
                    }
                })
                .compose(RxUtils.<List<Gank.Result>>io_main())
                .singleOrError()
                .subscribeWith(new DisposableSingleObserver<List<Gank.Result>>() {
                    @Override
                    public void onSuccess(List<Gank.Result> results) {
                        if (mode == MODE_REFRESH) {
                            mView.refreshData(results);
                            mView.stopRefresh();
                        } else if (mode == MODE_LOAD_MORE) {
                            if (results.size() > 0) {
                                mView.addMoreData(results);
                            } else {
                                mView.showNoMore();
                            }
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
    public void attachView(GankListContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mCompositeDisposable.clear();
    }
}
