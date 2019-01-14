// MusicConnect.aidl
package com.example.yanghao.mediaplayerdemo.aidl;

import com.example.yanghao.mediaplayerdemo.entity.Song;

interface MusicConnect {
    void refreshMusicList(in List<Song> musicFileList);

    void getFileList(out List<Song> musicFileList);

    boolean rePlay();

    boolean play(int position);

    boolean pause();

    boolean stop();

    boolean playNext();

    boolean playPre();

    boolean seekTo(int rate);

    int getCurPosition();

    int getDuration();

    int getPlayState();

    int getPlayMode();

    void setPlayMode(int mode);

    void sendPlayStateBrocast();

    void exit();
}
