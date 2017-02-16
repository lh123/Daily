package com.lh.daily.mvp.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.lh.daily.api.ApiHelper;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;
import com.lh.daily.database.DatabaseHelper;
import com.lh.daily.mvp.contract.ZhihuDailyDetailContract;
import com.lh.daily.utils.RxUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Created by home on 2017/2/10.
 */

public class ZhihuDailyDetailPresenter implements ZhihuDailyDetailContract.Presenter {

    private CompositeDisposable mCompositeDisposable;

    private ZhihuDailyDetailContract.View mView;
    private ZhihuDailyDetail mDailyDetail;

    @Override
    public void loadStoryDetail(int id) {
        Disposable disposable = ApiHelper.getInstance().getZhihuDailyService().loadDetail(id)
                .compose(RxUtils.<ZhihuDailyDetail>io_main())
                .singleOrError()
                .subscribeWith(new DisposableSingleObserver<ZhihuDailyDetail>() {
                    @Override
                    public void onSuccess(ZhihuDailyDetail dailyDetail) {
                        mDailyDetail = dailyDetail;
                        mView.showTitle(dailyDetail.getTitle());
                        mView.showImge(dailyDetail.getImage());
                        mView.showContent(buildHtml(dailyDetail));
                        mView.setLikeState(isLike());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void likeStory() {
        if (mDailyDetail == null){
            return;
        }
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> e) throws Exception {
                        SQLiteDatabase database = DatabaseHelper.getInstance().getWritableDatabase();
                        String str = new Gson().toJson(mDailyDetail);
                        database.execSQL("insert into like (content_id,title,type,time,content) values (?,?,?,?,?)",
                                new String[]{mDailyDetail.getId() + "",mDailyDetail.getTitle(), "1", System.currentTimeMillis() + "", str});
                        e.onComplete();
                    }
                })
                .compose(RxUtils.io_main())
                .ignoreElements()
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        mView.setLikeState(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showError();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void unlikeStory() {
        if (mDailyDetail == null){
            return;
        }
        SQLiteDatabase sqLiteDatabase = DatabaseHelper.getInstance().getWritableDatabase();
        sqLiteDatabase.execSQL("delete from like where (content_id == ? and type == 1)",new String[]{mDailyDetail.getId()+""});
        mView.setLikeState(false);
    }

    @Override
    public boolean isLike() {
        if (mDailyDetail == null){
            return false;
        }
        SQLiteDatabase database = DatabaseHelper.getInstance().getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from like where content_id = ?", new String[]{mDailyDetail.getId() + ""});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    private String buildHtml(ZhihuDailyDetail dailyDetail) {
        return "<head><meta charset=\"utf-8\">" +
                "<link type=\"text/css\" rel=\"stylesheet\" href=\"" +
                "file:///android_asset/zhuhi.css\"" +
                "</head>" +
                dailyDetail.getBody();
    }

    @Override
    public void attachView(ZhihuDailyDetailContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mCompositeDisposable.clear();
    }
}
