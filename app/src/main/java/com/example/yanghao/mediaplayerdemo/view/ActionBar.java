package com.example.yanghao.mediaplayerdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.yanghao.mediaplayerdemo.R;

public class ActionBar extends RelativeLayout {

    private TextView mTvCoverMusicArt;
    private ImageView mIvImageBack;
    private TextView mTvCoverMusicName;
    private TextView mTvMenu;

    public ActionBar(Context context) {
        super(context);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.actionbar,this );
        mIvImageBack = findViewById(R.id.iv_image_back);
        mTvCoverMusicName = findViewById(R.id.tv_cover_music_name);
        mTvCoverMusicArt = findViewById(R.id.tv_cover_music_art);
        mTvMenu = findViewById(R.id.tv_menu);
    }

    public void setActionBar(int drawable,String musicName,@Nullable String musicArt,boolean haveLittleTitle,boolean isFinsh,@Nullable OnClickListener mOnClickListener) {
        mIvImageBack.setImageResource(drawable);
        mTvCoverMusicName.setText(musicName);
        if (haveLittleTitle){
            mTvCoverMusicArt.setVisibility(VISIBLE);
            mTvCoverMusicArt.setText(musicArt);
        }else {
            mTvCoverMusicArt.setVisibility(GONE);
        }
        if (isFinsh){
            mIvImageBack.setOnClickListener(mOnClickListener);
        }
    }

    public void setActionBar(Drawable drawable,String musicName,String musicArt,boolean haveMenu,@Nullable String menuNme) {
        mIvImageBack.setImageDrawable(drawable);
        mTvCoverMusicName.setText(musicName);
        mTvCoverMusicArt.setText(musicArt);
        if (haveMenu){
            mTvMenu.setText(menuNme);
        }
    }

}
