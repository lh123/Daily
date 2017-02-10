package com.lh.daily.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by home on 2017/2/10.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mHelper;


    public static void init(Context context){
        if (mHelper == null){
            mHelper = new DatabaseHelper(context.getApplicationContext(),1);
        }
    }

    public static DatabaseHelper getInstance(){
        if (mHelper == null){
            throw new RuntimeException("DatabaseHelper not init");
        }
        return mHelper;
    }

    private DatabaseHelper(Context context, int version) {
        super(context, "favorite", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        creatZhihuTable(sqLiteDatabase);
    }

    private void creatZhihuTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists favorite(" +
                "id integer primary key autoincrement," +
                "content_id text not null," +
                "type integer not null," +
                "time integer not null," +
                "content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
