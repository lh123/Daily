package com.lh.daily.api.service;

import com.lh.daily.bean.zhihu.ZhihuDaily;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by home on 2017/2/8.
 */

public interface ZhihuDailyService {

    String HOST = "http://news-at.zhihu.com";

    @GET("/api/4/news/before/{time}")
    Observable<ZhihuDaily> loadDaily(@Path("time")long time);

    @GET("/api/4/news/{id}")
    Observable<ZhihuDailyDetail> loadDetail(@Path("id")int id);

}
