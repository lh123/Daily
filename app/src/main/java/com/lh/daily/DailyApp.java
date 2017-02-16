package com.lh.daily;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.lh.daily.database.DatabaseHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by home on 2017/2/10.
 */

public class DailyApp extends Application {
    private RefWatcher mRefWatcher;

    public static RefWatcher getWatcher(Context context){
        DailyApp app = (DailyApp) context.getApplicationContext();
        return app.mRefWatcher;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseHelper.init(this);
        Stetho.initializeWithDefaults(this);
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);
    }
}
