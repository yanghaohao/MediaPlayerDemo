package com.example.yanghao.mediaplayerdemo.model;

import com.example.yanghao.mediaplayerdemo.base.DBDao;
import com.example.yanghao.mediaplayerdemo.dbprocess.MusicProcess;
import com.example.yanghao.mediaplayerdemo.interfaces.MediaModel;

import java.util.List;

public class MusicModel implements MediaModel {

    @Override
    public void music(musicData musicData,DBDao dao) {
        musicData.music(dao.findAll());
    }

    public interface musicData<T>{
        void music(List<T> ts);
    }






}
