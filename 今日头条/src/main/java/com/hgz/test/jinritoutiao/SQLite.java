package com.hgz.test.jinritoutiao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SQLite extends SQLiteOpenHelper {
    private static final String DBName="pindao.db";
    private static final int VERSION=1;
    public SQLite(Context context) {
        super(context, DBName, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table pindao(name varchar(20),selected integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
