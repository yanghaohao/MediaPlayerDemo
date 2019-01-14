package com.example.yanghao.mediaplayerdemo.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yanghao.mediaplayerdemo.R;

public class MusicControl extends RelativeLayout implements View.OnClickListener {


    private SeekBar mSbMusic;
    private TextView mTvMomentTime,mTvCompleteTime;
    private ImageView mIvMusicNext,mIvMusicPlay,mIvMusicPrimous,mIvChangeMode;
    private Control mControl;
    private int endTime;
    private int a;
    private int b;
    private Context context;

    public Control getControl() {
        return mControl;
    }

    public void setControl(final Control control) {
        this.mControl = control;

    }

    public MusicControl(Context context) {
        super(context);
        this.context  = context;
        initView(context);
    }

    public MusicControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context  = context;
        initView(context);
    }

    public MusicControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context  = context;
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.control_music,this );
        mSbMusic = findViewById(R.id.sb_music);
        mSbMusic.setMax(100);
        mTvMomentTime = findViewById(R.id.tv_moment_time);
        mTvCompleteTime = findViewById(R.id.tv_complete_time);
        mIvMusicNext = findViewById(R.id.iv_music_next);
        mIvMusicPlay = findViewById(R.id.iv_music_play);
        mIvMusicPrimous = findViewById(R.id.iv_music_primous);
        mIvChangeMode = findViewById(R.id.iv_change_mode);
        setListerner();
    }

    public void setSeekProgress(int progress){
        mSbMusic.setProgress(progress);
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
        mTvCompleteTime.setText(millisecondToMinite(endTime));
    }

    public int getEndTime() {
        return endTime;
    }

    public String setStartTime(int startTime){
        return millisecondToMinite(startTime);
    }

    public String progressToTime(int progress){
        int startTime = (int) (endTime*((double)progress/mSbMusic.getMax()));
        return millisecondToMinite(startTime);
    }

    private int getStartTime(int progress){
        return progress*endTime/100;
    }

    private String  millisecondToMinite(int time){
        int minite = (time/1000)/60;
        int second = (time/1000)%60;
        String min = minite<10?"0"+minite:minite+"";
        String sec = second<10?"0"+second:second+"";
        return min+":"+sec;
    }

    private void setListerner(){
        mSbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvMomentTime.setText(progressToTime(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mControl.moveSeek(getStartTime(seekBar.getProgress()));
                mTvMomentTime.setText(progressToTime(seekBar.getProgress()));
            }
        });
        mIvMusicNext.setOnClickListener(this);
        mIvMusicPlay.setOnClickListener(this);
        mIvChangeMode.setOnClickListener(this);
        mIvMusicPrimous.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_music_next:
                mControl.nextMusic();
                break;
            case R.id.iv_music_play:
                if (a == 0){
                    mIvMusicPlay.setImageResource(R.mipmap.ai_);
                    a = 1;
                }else {
                    mIvMusicPlay.setImageResource(R.mipmap.ai8);
                    a = 0;
                }
                mControl.playMusic();
                break;
            case R.id.iv_music_primous:
                mControl.promousMusic();
                break;
            case R.id.iv_change_mode:
                if (b == 0){
                    mIvChangeMode.setImageResource(R.mipmap.aqj);
                    Toast.makeText(context, "单曲循环", Toast.LENGTH_SHORT).show();
                    b = 1;
                }else if (b == 1){
                    mIvChangeMode.setImageResource(R.mipmap.ar5);
                    Toast.makeText(context, "随机播放", Toast.LENGTH_SHORT).show();
                    b = 2;
                }else if (b == 2){
                    mIvChangeMode.setImageResource(R.mipmap.aqj);
                    Toast.makeText(context, "列表循环", Toast.LENGTH_SHORT).show();
                    b = 0;
                }
                mControl.changeMode();
                break;
        }
    }

    public interface Control{
        void moveSeek(int progress);
        void playMusic();
        void nextMusic();
        void promousMusic();
        void changeMode();
    }
}
