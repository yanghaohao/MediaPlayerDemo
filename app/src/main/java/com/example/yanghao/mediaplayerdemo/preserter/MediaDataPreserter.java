package com.example.yanghao.mediaplayerdemo.preserter;

import com.example.yanghao.mediaplayerdemo.base.DBDao;
import com.example.yanghao.mediaplayerdemo.interfaces.MediaDataPresenter;
import com.example.yanghao.mediaplayerdemo.interfaces.MusicData;
import com.example.yanghao.mediaplayerdemo.model.MusicModel;

import java.util.List;

public class MediaDataPreserter implements MediaDataPresenter,MusicModel.musicData{

    private DBDao mDBDao;
    private MusicData mMusicData;
    private MusicModel musicModel;
    private int control;
    public MediaDataPreserter(DBDao dbDao,MusicData musicData) {
        this.mDBDao = dbDao;
        this.mMusicData = musicData;
        musicModel = new MusicModel();
    }

    @Override
    public void loadData(int control) {
        this.control = control;
        musicModel.music(this, mDBDao);
    }

    @Override
    public void fail() {
        switch (control){
            case 0:
                mMusicData.fail("音乐列表加载失败");
                break;
        }
    }

    @Override
    public void music(List list) {
        switch (control){
            case 0:
                mMusicData.musicData(list);
                break;
        }
    }
}
