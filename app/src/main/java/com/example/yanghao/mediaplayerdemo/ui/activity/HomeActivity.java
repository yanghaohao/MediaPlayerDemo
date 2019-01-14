package com.example.yanghao.mediaplayerdemo.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanghao.mediaplayerdemo.R;
import com.example.yanghao.mediaplayerdemo.adapter.AudioAdapter;
import com.example.yanghao.mediaplayerdemo.base.BaseActivity;
import com.example.yanghao.mediaplayerdemo.base.BaseAdapter;
import com.example.yanghao.mediaplayerdemo.dbprocess.MusicProcess;
import com.example.yanghao.mediaplayerdemo.entity.Song;
import com.example.yanghao.mediaplayerdemo.interfaces.MusicData;
import com.example.yanghao.mediaplayerdemo.preserter.MediaDataPreserter;
import com.example.yanghao.mediaplayerdemo.util.AudioToMusicUtil;
import com.example.yanghao.mediaplayerdemo.util.PopupWindowUtil;
import com.example.yanghao.mediaplayerdemo.util.ViewUtil;

import java.util.List;

public class HomeActivity extends BaseActivity {
    private RecyclerView rvMediaList;
    private Thread run;
    private List<Song> mMusic;
    private MusicProcess mMusicProcess;
    private MediaDataPreserter mMediaDataPreserter;
    private View rootView;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            setRecyclerViewData(mMusic);
            return false;
        }
    });


    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_home);
    }

    MusicData mMusicData = new MusicData() {
        @Override
        public void musicData(List<Song> list) {
            mMusic = list;
            handler.sendEmptyMessage(1);
        }

        @Override
        public void fail(String string) {
            Toast.makeText(HomeActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void setView() {
        rvMediaList = findViewById(R.id.rv_media_list);
        rvMediaList.setLayoutManager(new LinearLayoutManager(this));
        mMusicProcess = new MusicProcess(this);
        if ( mMusicProcess.haveData()){
            mMediaDataPreserter = new MediaDataPreserter(mMusicProcess,mMusicData);
            mMediaDataPreserter.loadData(0);
        }else {
            requestPermission(rvMediaList);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_home);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.dl_home);
        TextView t= (TextView) findViewById(R.id.t);
        ViewUtil.initView(this,toolbar,drawer ,t );
        rootView = findViewById(R.id.rl_home);
        findViewById(R.id.tv_home_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupWindowUtil.setAddPopupWindow(rootView,HomeActivity.this,HomeActivity.this);
            }
        });

    }

    private void setRecyclerViewAndData(RecyclerView rvMediaList){
        run = new Thread(){
            @Override
            public void run() {
                super.run();
                mMusic = AudioToMusicUtil.audioToMusic(HomeActivity.this);
                handler.sendEmptyMessage(1);
                if (!mMusicProcess.haveData()){
                    for (int i = 0;i < mMusic.size();i++){
                        mMusicProcess.addData(mMusic.get(i));
                    }
                }
            }
        };
        run.start();
    }


    public void requestPermission(RecyclerView rvMediaList){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }else{
            setRecyclerViewAndData(rvMediaList);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    setRecyclerViewAndData(rvMediaList);
                } else {
                    // 没有获取到权限，做特殊处理
                    Toast.makeText(this, "请不要拒绝我", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setRecyclerViewData(final List<Song> mMusic){
        AudioAdapter audioAdapter = new AudioAdapter(mMusic,HomeActivity.this);
        rvMediaList.setAdapter(audioAdapter);
        audioAdapter.setmOnClickItem(new BaseAdapter.OnClickItem() {
            @Override
            public void onClick(View v, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("音乐", mMusic.get(position));
                bundle.putInt("条目", position);
                openActivity(PlayActivity.class,bundle);
            }
        });
        audioAdapter.setmOnClickMore(new AudioAdapter.OnClickMore() {
            @Override
            public void onClickMore(View view, int position) {
                PopupWindowUtil.setDetailPopupWindow(rootView, HomeActivity.this, HomeActivity.this);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        run.interrupt();
    }
}
