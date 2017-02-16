package com.lh.daily.mvp.presenter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.lh.daily.database.DatabaseHelper;
import com.lh.daily.mvp.contract.SearchContract;
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
 * Created by home on 2017/2/11.
 */

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void loadSuggest(final String key) {
        if (TextUtils.isEmpty(key)){
            mView.showSuggest(null);
            return;
        }
        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<List<String>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<String>> e) throws Exception {
                        SQLiteDatabase database = DatabaseHelper.getInstance().getReadableDatabase();
                        Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_NAME + " where title like ?", new String[]{"%"+key+"%"});
                        List<String> strings = new ArrayList<>();
                        while (cursor.moveToNext()){
                            String str = cursor.getString(cursor.getColumnIndex("title"));
                            strings.add(str);
                        }
                        cursor.close();
                        e.onNext(strings);
                        e.onComplete();
                    }
                })
                .compose(RxUtils.<List<String>>io_main())
                .singleOrError()
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) {
                        mView.showSuggest(strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void loadResult(String key) {

    }

    @Override
    public void attachView(SearchContract.View view) {
        mView = view;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mCompositeDisposable.clear();
    }
}
