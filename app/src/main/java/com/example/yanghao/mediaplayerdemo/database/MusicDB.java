package com.example.yanghao.mediaplayerdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDB extends SQLiteOpenHelper {

    public MusicDB(Context context) {
        super(context, "media.db", null, 1);
    }

    /**
     *  private String title , path , artist , album ;
        private long duration ;
        private Bitmap albumCover ;
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table audio(title text,path text,artist tex,album text,duration integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
