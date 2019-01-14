package com.example.yanghao.mediaplayerdemo.util;

import android.content.Context;
import android.util.Log;

import com.example.yanghao.mediaplayerdemo.entity.Song;

import java.util.ArrayList;
import java.util.List;

public class AudioToMusicUtil {

    public static List<Song> audioToMusic(Context context){
        List<Song> mSong = AudioUtil.readAudio(context);
        if (mSong==null){
            return null;
        }
        int corrut = 0;
        List<Song> mMusic = new ArrayList<>();
        do {
            if (mSong.get(corrut).getDuration()>60){
                mMusic.add(mSong.get(corrut));
            }
            if (mSong.get(corrut).getArtist()!=null&&mSong.get(corrut).getArtist().equals("<unknown>")){
                mSong.get(corrut).setArtist("未知艺术家");
            }
            corrut++;
        } while (corrut<mSong.size());
        return mMusic;
    }

}
