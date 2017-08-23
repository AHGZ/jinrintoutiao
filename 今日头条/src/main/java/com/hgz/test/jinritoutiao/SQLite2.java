package com.hgz.test.jinritoutiao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SQLite2 extends SQLiteOpenHelper {
    public SQLite2(Context context) {
        super(context,"news.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table news(_id Integer primary key autoincrement,title varchar(20),author_name varchar(20),date varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
