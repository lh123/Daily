package com.lh.daily.mvp.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;
import com.lh.daily.database.DatabaseHelper;
import com.lh.daily.mvp.contract.LikeContract;
import com.lh.daily.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by home on 2017/2/10.
 */

public class LikePresenter implements LikeContract.Presenter {

    private LikeContract.View mView;
    private CompositeDisposable mCompositeDisposable;

    @Override
    public void loadLikeData() {
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<List<ZhihuDailyDetail>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<ZhihuDailyDetail>> e) throws Exception {
                        SQLiteDatabase database = DatabaseHelper.getInstance().getReadableDatabase();
                        Cursor cursor = database.rawQuery("select * from favorite where type == ?", new String[]{"1"});
                        Gson gson = new Gson();
                        List<ZhihuDailyDetail> stories = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            String str = cursor.getString(cursor.getColumnIndex("content"));
                            ZhihuDailyDetail story = gson.fromJson(str, ZhihuDailyDetail.class);
                            cursor.moveToNext();
                            stories.add(story);
                        }
                        cursor.close();
                        e.onNext(stories);
                        e.onComplete();
                    }
                })
                .compose(RxUtils.<List<ZhihuDailyDetail>>io_main())
                .singleOrError()
                .subscribeWith(new DisposableSingleObserver<List<ZhihuDailyDetail>>() {
                    @Override
                    public void onSuccess(List<ZhihuDailyDetail> stories) {
                        mView.stopLoading();
                        if (stories.size() > 0) {
                            mView.showResult(stories);
                        } else {
                            mView.showEmpty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showEmpty();
                        mView.stopLoading();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void attachView(LikeContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mCompositeDisposable.clear();
    }
}
