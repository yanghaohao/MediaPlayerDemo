package com.example.yanghao.mediaplayerdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.util.Log;

import com.example.yanghao.mediaplayerdemo.entity.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioUtil {

    public static List<Song> readAudio(Context context) {
        ContentResolver resolver = context.getContentResolver() ;
        Uri audioUri = Audio.Media.EXTERNAL_CONTENT_URI ;
        String[] columns = {
                Audio.Media.TITLE , 	// 歌名
                Audio.Media.DATA ,   	// 路径
                Audio.Media.ARTIST , 	// 演唱者
                Audio.Media.ALBUM	,	// 专辑名
                Audio.Media.DURATION , 	// 音频文件长度，毫秒
                Audio.Media.ALBUM_KEY  	// 用来读取专辑图片时使用

        };
        List<Song> data = null ;
        Cursor cursor = resolver.query(
                audioUri,
                columns,
                //null, null,
                Audio.Media.DATA + " like ? or " + Audio.Media.DATA + " like ?" , new String[]{"%.mp3" , "%.wma"} ,
                null) ;
        if(null != cursor && cursor.getCount() > 0) {
            data = new ArrayList<Song>() ;
            while(cursor.moveToNext()) {
                // 读到一个音频
//                String a

                data.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3) , cursor.getLong(4) )) ;
            }
        }
        if(null != cursor) {
            cursor.close() ;
        }
        return data ;
    }

    public static void readImage(Context context) {
        ContentResolver resolver = context.getContentResolver() ;
        Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI ;
        String[] columns = new String[]{
                MediaStore.Images.Media.TITLE ,
                MediaStore.Images.Media.DATA ,
                MediaStore.Images.Media.SIZE ,
                MediaStore.Images.Media.MIME_TYPE	// 类型
        };
        Cursor cursor = resolver.query(imgUri, columns, null, null, null) ;
        if(null != cursor && cursor.getCount() > 0) {
            while(cursor.moveToNext()) {
                Log.e("imgs" , cursor.getString(0) + "\t" + cursor.getString(3)) ;
            }
        }
        if(null != cursor) {
            cursor.close() ;
        }
    }

    private static Cursor getCursor(Context context,String filePath) {
        String path = null;
        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        // System.out.println(c.getString(c.getColumnIndex("_data")));
        if (c.moveToFirst()) {
            do {
                // 通过Cursor 获取路径，如果路径相同则break；
                System.out.println("////////"+filePath);
                path = c.getString(c
                        .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                System.out.println("?????????"+path);
                // 查找到相同的路径则返回，此时cursorPosition 便是指向路径所指向的Cursor 便可以返回了
                if (path.equals(filePath)) {
                // System.out.println("audioPath = " + path);
                // System.out.println("filePath = " + filePath);
                // cursorPosition = c.getPosition();
                    break;
                }
            } while (c.moveToNext());
        }
                // 这两个没有什么作用，调试的时候用
                // String audioPath = c.getString(c
                // .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                //
                // System.out.println("audioPath = " + audioPath);
        return c;
    }

    private static String getAlbumArt(Context context,int album_id) {
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = context.getContentResolver().query(
                Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
                projection, null, null, null);
        String album_art = null;
        if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
            cur.moveToNext();
            album_art = cur.getString(0);
        }
        cur.close();
        cur = null;
        return album_art;
    }

    public static Bitmap getImage(Context context,String filename){
        Cursor currentCursor = getCursor(context,filename);
        int album_id = currentCursor.getInt(currentCursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        String albumArt = getAlbumArt(context,album_id);
        Bitmap bm = null;
        if (albumArt == null) {
            return null;
        } else {
            bm = BitmapFactory.decodeFile(albumArt);
            return bm;
        }
    }
}
