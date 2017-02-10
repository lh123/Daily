package com.lh.daily.api;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lh.daily.api.service.GankService;
import com.lh.daily.api.service.ZhihuDailyService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by home on 2017/2/8.
 */

public class ApiHelper {

    private static volatile ApiHelper mHelper;
    private OkHttpClient mOkhttpClient;

    private ZhihuDailyService mZhihuDailyService;
    private GankService mGankService;

    public static ApiHelper getInstance() {
        if (mHelper == null) {
            synchronized (ApiHelper.class) {
                if (mHelper == null) {
                    mHelper = new ApiHelper();
                }
            }
        }
        return mHelper;
    }

    private ApiHelper() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        mOkhttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

    }

    private <T> T creatService(Class<T> tClass, String host) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkhttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(host)
                .build();
        return retrofit.create(tClass);
    }


    public ZhihuDailyService getZhihuDailyService() {
        if (mZhihuDailyService == null) {
            mZhihuDailyService = creatService(ZhihuDailyService.class, ZhihuDailyService.HOST);
        }
        return mZhihuDailyService;
    }

    public GankService getGankService() {
        if (mGankService == null) {
            mGankService = creatService(GankService.class, GankService.HOST);
        }
        return mGankService;
    }

}
