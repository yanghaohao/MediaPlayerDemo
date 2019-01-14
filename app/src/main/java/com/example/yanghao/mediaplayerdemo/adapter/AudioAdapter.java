package com.example.yanghao.mediaplayerdemo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yanghao.mediaplayerdemo.R;
import com.example.yanghao.mediaplayerdemo.base.BaseAdapter;
import com.example.yanghao.mediaplayerdemo.entity.Song;

import java.util.List;

public class AudioAdapter extends BaseAdapter<Song,BaseAdapter.BaseViewHolder> {


    private List<Song> mSong;
    private OnClickMore mOnClickMore;

    public OnClickMore getmOnClickMore() {
        return mOnClickMore;
    }

    public void setmOnClickMore(OnClickMore mOnClickMore) {
        this.mOnClickMore = mOnClickMore;
    }

    public AudioAdapter(List<Song> mObject, Context mContext) {
        super(mObject, mContext);
        this.mSong = mObject;
    }

    @Override
    protected int createViewHolder() {
        return R.layout.audio_item;
    }

    @Override
    protected void setData(BaseViewHolder viewHolder, final int position) {
        TextView tvAudioName = viewHolder.findViewById(R.id.tv_audio_name);
        TextView tvSuccessionSpecies = viewHolder.findViewById(R.id.tv_succession_species);
        ImageView ivAudioMore = viewHolder.findViewById(R.id.iv_audio_more);

        tvAudioName.setText(mSong.get(position).getTitle());
        tvSuccessionSpecies.setText(mSong.get(position).getArtist());
        ivAudioMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickMore.onClickMore(view, position);
            }
        });
    }

    public interface OnClickMore{
        void onClickMore(View view,int position);
    }
}
