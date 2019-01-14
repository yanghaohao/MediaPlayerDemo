package com.example.yanghao.mediaplayerdemo.interfaces;

import com.example.yanghao.mediaplayerdemo.base.DBDao;
import com.example.yanghao.mediaplayerdemo.model.MusicModel;

public interface MediaModel {
    void music(MusicModel.musicData musicData, DBDao dao);
}
