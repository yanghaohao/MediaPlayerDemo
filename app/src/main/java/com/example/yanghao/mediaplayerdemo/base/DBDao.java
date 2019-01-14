package com.example.yanghao.mediaplayerdemo.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.yanghao.mediaplayerdemo.database.MusicDB;

import java.util.List;

public abstract class DBDao<T> {

    private Context mContext;
    protected SQLiteDatabase mSQLiteDatabase;

    public DBDao(Context context) {
        this.mContext = context;
        mSQLiteDatabase = new MusicDB(context).getWritableDatabase();
    }

    public abstract void addData(T t);

    public abstract void delete(String key, String str);

    public abstract void update(String key, String str, T t);

    public abstract T find(String key, String str);

    public abstract List<T> findLike(String key, String str);

    public abstract List<T> findAll();

    public abstract void deleteAll();

    public void close() {
        mSQLiteDatabase.close();
    }

    public boolean haveData() {
        if (findLike("title", "").size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
