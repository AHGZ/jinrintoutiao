package com.hgz.test.jinritoutiao.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.andy.library.ChannelBean;
import com.hgz.test.jinritoutiao.SQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class SQLiteDao {

    private final SQLiteDatabase db;
    private static  final String TABLE_NAME="pindao";
    public SQLiteDao(Context context){
        SQLite sqLite = new SQLite(context);
        db = sqLite.getWritableDatabase();
    }
    //添加数据
    public void addPindao(List<ChannelBean> channelBeanList){
        if (channelBeanList==null||channelBeanList.size()<1){
            return;
        }
        for (ChannelBean channelBeanLists:channelBeanList) {
            ContentValues values = new ContentValues();
            values.put("name", channelBeanLists.getName());
            values.put("selected",channelBeanLists.isSelect());
            db.insert(TABLE_NAME, null, values);
        }
    }
    //查询数据
    public ArrayList<ChannelBean> findAllPindao(){
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<ChannelBean> channelBeens = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("selected"));
            ChannelBean channelBean = new ChannelBean(name, selected == 0 ? false : true);
            channelBeens.add(channelBean);
        }
        return channelBeens;
    }
    //条件查询数据
    public ArrayList<ChannelBean> findUserPindao(){
        Cursor cursor = db.query(TABLE_NAME, null,"selected=?",new String[]{"1"}, null, null, null);
        ArrayList<ChannelBean> channelBeens = new ArrayList<>();
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int selected = cursor.getInt(cursor.getColumnIndex("selected"));
            ChannelBean channelBean = new ChannelBean(name, selected == 0 ? false : true);
            channelBeens.add(channelBean);
        }
        return channelBeens;
    }
    //删除数据的内容
    public void deletePindao(){
        db.delete(TABLE_NAME,null,null);
    }
}
