package com.example.yanghao.mediaplayerdemo.dbprocess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

import com.example.yanghao.mediaplayerdemo.entity.Song;
import com.example.yanghao.mediaplayerdemo.base.DBDao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicProcess extends DBDao<Song>{


    public MusicProcess(Context context) {
        super(context);
    }


    @Override
    public void addData(Song song) {
        //db.execSQL("create table user(username text,password text)");
        Log.e("addData: ","走了这个方法" );
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", song.getTitle());
        contentValues.put("path", song.getPath());
        contentValues.put("artist", song.getArtist());
        contentValues.put("album", song.getAlbum());
        contentValues.put("duration", song.getDuration());
        mSQLiteDatabase.insert("audio", null, contentValues);
    }

    @Override
    public void delete(String key,String str) {
        mSQLiteDatabase.delete("audio", key+"=?", new String[]{str});
    }

    @Override
    public void update(String key,String str,Song song) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", song.getTitle());
        contentValues.put("path", song.getPath());
        contentValues.put("artist", song.getArtist());
        contentValues.put("album", song.getAlbum());
        contentValues.put("duration", song.getDuration());
        mSQLiteDatabase.update("coupon", contentValues, key+"=?", new String[]{str});
    }

    //audio(title text,path text,artist tex,album text,duration integer)
    @Override
    public Song find(String key,String str) {
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from audio where "+key+" =?", new String[]{str});
        if (cursor == null || !cursor.moveToNext()) {
            return null;
        }
        return new Song(cursor.getString(cursor.getColumnIndex("title"))
                ,cursor.getString(cursor.getColumnIndex("path")),cursor.getString(cursor.getColumnIndex("artist"))
                ,cursor.getString(cursor.getColumnIndex("album")),cursor.getInt(cursor.getColumnIndex("duration")));
    }

    @Override
    public List<Song> findLike(String key,String str) {
        List<Song> list = new ArrayList();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from audio where +"+key+" like ?", new String[]{"%" + str + "%"});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new Song(cursor.getString(cursor.getColumnIndex("title"))
                        ,cursor.getString(cursor.getColumnIndex("path")),cursor.getString(cursor.getColumnIndex("artist"))
                        ,cursor.getString(cursor.getColumnIndex("album")),cursor.getInt(cursor.getColumnIndex("duration"))));
            }
        }
        return list;
    }

    @Override
    public List<Song> findAll() {
        List<Song> list = new ArrayList();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from audio", null);
        if (cursor == null) {

        } else {
            while (cursor.moveToNext()) {
                list.add(new Song(cursor.getString(cursor.getColumnIndex("title"))
                        ,cursor.getString(cursor.getColumnIndex("path")),cursor.getString(cursor.getColumnIndex("artist"))
                        ,cursor.getString(cursor.getColumnIndex("album")),cursor.getInt(cursor.getColumnIndex("duration"))));
            }
        }
        return list;
    }

    @Override
    public void deleteAll() {
        mSQLiteDatabase.execSQL("delete from audio");
    }


}
