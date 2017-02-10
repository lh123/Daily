package com.lh.daily.api.service;

import com.lh.daily.bean.gank.Gank;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by home on 2017/2/10.
 */

public interface GankService {
    String HOST = "http://gank.io";

    @GET("/api/data/all/{size}/{page}")
    Observable<Gank> loadGanksData(@Path("size")int size, @Path("page")int page);
}
