package com.lh.daily;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.lh.daily.database.DatabaseHelper;

/**
 * Created by home on 2017/2/10.
 */

public class DailyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this);
        Stetho.initializeWithDefaults(this);
    }
}
