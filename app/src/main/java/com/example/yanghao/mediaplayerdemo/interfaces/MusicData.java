package com.example.yanghao.mediaplayerdemo.interfaces;

import com.example.yanghao.mediaplayerdemo.entity.Song;
import com.example.yanghao.mediaplayerdemo.util.ViewUtil;

import java.util.List;

public interface MusicData {

    void musicData(List<Song> list);
    void fail(String string);
}
