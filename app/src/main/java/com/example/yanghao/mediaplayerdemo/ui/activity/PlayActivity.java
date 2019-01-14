package com.example.yanghao.mediaplayerdemo.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.yanghao.mediaplayerdemo.R;
import com.example.yanghao.mediaplayerdemo.adapter.LcyAndDetcilAdapter;
import com.example.yanghao.mediaplayerdemo.base.BaseActivity;
import com.example.yanghao.mediaplayerdemo.dbprocess.MusicProcess;
import com.example.yanghao.mediaplayerdemo.entity.Song;
import com.example.yanghao.mediaplayerdemo.interfaces.MusicData;
import com.example.yanghao.mediaplayerdemo.preserter.MediaDataPreserter;
import com.example.yanghao.mediaplayerdemo.service.MusicService;
import com.example.yanghao.mediaplayerdemo.view.ActionBar;
import com.example.yanghao.mediaplayerdemo.ui.fragment.CoverFragment;
import com.example.yanghao.mediaplayerdemo.ui.fragment.DetailFragment;
import com.example.yanghao.mediaplayerdemo.ui.fragment.LcyFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseActivity {

    protected MusicService musicService;
    private Bundle bundle;
    private ActionBar abCoverPlayer;
    private int a = 0;
    private int b = 0;
    private int c = 0;
    private Song song;
    private List<Song> mMusic;
    private MusicProcess mMusicProcess;
    private MediaDataPreserter mMediaDataPreserter;
    private int position;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.iv_image_back:
                    finish();
                    break;
            }
        }
    };
    private MusicService.Complete complete = new MusicService.Complete() {
        @Override
        public void complete() {
            song = mMusic.get(musicService.getMomentPlayName());
            abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, song.getTitle(), song.getArtist(),true ,true,mOnClickListener);
        }
    };

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 1:
                    b = message.what;
                    break;
                case 2:
                    a = message.what;
            }
            if (a==2&&b==1){
                musicService.getMusicList(mMusic);
                musicService.setComplete(complete);
                try {
                    musicService.playMusic(position);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

    MusicData mMusicData = new MusicData() {
        @Override
        public void musicData(List<Song> list) {
            mMusic = list;
            handler.sendEmptyMessage(1);
        }

        @Override
        public void fail(String string) {
            Toast.makeText(PlayActivity.this, string, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void setLayout() {
        setContentView(R.layout.activity_play);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setView() {

        Log.e("setView: ",musicService+"" );
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.status_background));
        bundle = getIntent().getExtras();
        song = (Song) bundle.getSerializable("音乐");
        position = bundle.getInt("条目");

        mMusicProcess = new MusicProcess(this);

        mMediaDataPreserter = new MediaDataPreserter(mMusicProcess,mMusicData);
        mMediaDataPreserter.loadData(0);


        ViewPager vpLcyAndDetail = findViewById(R.id.vp_lcy_and_detail);
        abCoverPlayer = findViewById(R.id.ab_cover_player);

        LcyAndDetcilAdapter lcyAndDetcilAdapter = new LcyAndDetcilAdapter(getSupportFragmentManager(),getFragmentArray());
        vpLcyAndDetail.setAdapter(lcyAndDetcilAdapter);

        vpLcyAndDetail.setCurrentItem(1);
        abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, song.getTitle(), song.getArtist(),true ,true,mOnClickListener);
        vpLcyAndDetail.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
               switch (i){
                   case 0:
                       abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, "歌曲详情",null,false ,true,mOnClickListener);
                       break;
                   case 1:
                       abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, song.getTitle(), song.getArtist(),true ,true,mOnClickListener);
                       break;
                   case 2:
                       abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, "歌词", null,false ,true,mOnClickListener);
                       break;
               }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent,serviceConnection , BIND_AUTO_CREATE);


    }

    protected ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MainBinder mainBinder = (MusicService.MainBinder) iBinder;
            musicService = mainBinder.getService();

            handler.sendEmptyMessage(2);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private List<Fragment> getFragmentArray(){
        LcyFragment lcyFragment = new LcyFragment();
        DetailFragment detailFragment = new DetailFragment();
        CoverFragment coverFragment = new CoverFragment();
        coverFragment.setControl(control);
        lcyFragment.setArguments(bundle);
        detailFragment.setArguments(bundle);
        coverFragment.setArguments(bundle);
        List<Fragment> list = new ArrayList<>();
        list.add(detailFragment);
        list.add(coverFragment);
        list.add(lcyFragment);
        return list;
    }

    CoverFragment.Control control = new CoverFragment.Control() {
        @Override
        public void next() {
//            sendBorad(MusicService.MusicRecevier.NOTIFICATION_ITEM_BUTTON_NEXT);
            try {
                musicService.playNew(MusicService.OPER_PREVIOUS);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void promous() {
//            sendBorad(MusicService.MusicRecevier.NOTIFICATION_ITEM_BUTTON_LAST);
            try {
                musicService.playNew(MusicService.OPER_NEXT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void play() {
//            sendBorad(MusicService.MusicRecevier.NOTIFICATION_ITEM_BUTTON_PLAY);
            if (musicService.mPlayer.isPlaying()){
                musicService.pause();
            }else {
                musicService.resum();
            }
        }

        @Override
        public double progress() {
            Log.e("progress: ", musicService.getMusicPro()+"\t"+song.getDuration());
            return (double) musicService.getMusicPro()/song.getDuration();
        }

        @Override
        public void changeMode() {
//            sendBorad(MusicService.MusicRecevier.NOTIFICATION_ITEM_BUTTON_CHANGE_MODE);
            musicService.changeMode();
        }

        @Override
        public void moveProgress(int progress) {
            musicService.changePro(progress);
        }

        @Override
        public Song complete() {
            song = mMusic.get(musicService.getMomentPlayName());
            abCoverPlayer.setActionBar(R.mipmap.abc_spinner_mtrl_am_alpha, song.getTitle(), song.getArtist(),true ,true,mOnClickListener);
            return song;
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy: ","我在这里" );

    }
}
