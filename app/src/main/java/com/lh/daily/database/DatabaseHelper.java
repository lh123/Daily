package com.lh.daily.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by home on 2017/2/10.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mHelper;

    public static final String TABLE_NAME = "like";
    private static final int VERSION = 2;


    public static void init(Context context) {
        if (mHelper == null) {
            mHelper = new DatabaseHelper(context.getApplicationContext(), VERSION);
        }
    }

    public static DatabaseHelper getInstance() {
        if (mHelper == null) {
            throw new RuntimeException("DatabaseHelper not init");
        }
        return mHelper;
    }

    private DatabaseHelper(Context context, int version) {
        super(context, "database", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        creatZhihuTable(sqLiteDatabase);
    }

    private void creatZhihuTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists " + TABLE_NAME + "(" +
                "id integer primary key autoincrement," +
                "content_id text not null," +
                "title text not null," +
                "type integer not null," +
                "time integer not null," +
                "content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            case 1:
                sqLiteDatabase.beginTransaction();
                sqLiteDatabase.execSQL("alter table like rename to temp");
                creatZhihuTable(sqLiteDatabase);
                sqLiteDatabase.execSQL("insert into like (content_id,type,time,content,title) select content_id,type,time,content,'empty' from temp");
                sqLiteDatabase.execSQL("drop table temp");
                sqLiteDatabase.setTransactionSuccessful();
                sqLiteDatabase.endTransaction();
        }
    }
}
