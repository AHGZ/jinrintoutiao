package com.hgz.test.jinritoutiao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hgz.test.jinritoutiao.SQLite2;
import com.hgz.test.jinritoutiao.bean.NewsInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/18.
 */

public class SQLiteDao2 {

    private final SQLiteDatabase db;

    public SQLiteDao2(Context context){
        SQLite2 sqLite2 = new SQLite2(context);
        db = sqLite2.getWritableDatabase();
    }
    public void addNews(String title,String author_name,String date){
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("author_name",author_name);
        values.put("date",date);
        db.insert("news",null,values);
    }
    public ArrayList<NewsInfo.ResultBean.DataBean> findNews(){
        Cursor cursor = db.query(false, "news", null, null, null, null, null, null, null);
        ArrayList<NewsInfo.ResultBean.DataBean> dataBeens = new ArrayList<>();
        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String author_name = cursor.getString(cursor.getColumnIndex("author_name"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            NewsInfo.ResultBean.DataBean dataBean = new NewsInfo.ResultBean.DataBean();
            dataBean.setTitle(title);
            dataBean.setAuthor_name(author_name);
            dataBean.setDate(date);
            dataBeens.add(dataBean);
        }
        return dataBeens;
    }
    public void deleteNews(){
        db.delete("news",null,null);
    }
}
