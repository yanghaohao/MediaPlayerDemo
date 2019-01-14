package com.example.yanghao.mediaplayerdemo.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.yanghao.mediaplayerdemo.R;
import com.example.yanghao.mediaplayerdemo.base.BaseFragment;
import com.example.yanghao.mediaplayerdemo.entity.Song;
import com.example.yanghao.mediaplayerdemo.util.AudioUtil;
import com.example.yanghao.mediaplayerdemo.view.ActionBar;
import com.example.yanghao.mediaplayerdemo.view.MusicControl;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CoverFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoverFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER 
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Control control;

    private MusicControl mMusicControl;
    private Song song;
    public CoverFragment.Control getControl() {
        return control;
    }

    public void setControl(CoverFragment.Control control) {
        this.control = control;
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;  

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    mMusicControl.setControl(new MusicControl.Control() {
                        @Override
                        public void moveSeek(int progress) {
                            control.moveProgress(progress);
                            mMusicControl.progressToTime(progress);
                        }

                        @Override
                        public void playMusic() {
                            control.play();
                        }

                        @Override
                        public void nextMusic() {
                            control.next();
                            control.complete();
                            mMusicControl.setSeekProgress(0);
                        }

                        @Override
                        public void promousMusic() {
                            control.promous();
                            control.complete();
                            mMusicControl.setSeekProgress(0);
                        }

                        @Override
                        public void changeMode() {
                            control.changeMode();
                        }
                    });
                    Log.e("handleMessage: ",control.progress()+"" );

                    if (control.progress()>0.01){
                        mMusicControl.setSeekProgress((int) (control.progress()*100));
                    }
                    if (control.progress() == 100){
                        song = control.complete();
                        mMusicControl.setEndTime((int) song.getDuration());
                    }
                    handler.sendEmptyMessageDelayed(0,200 );
                    break;
            }
            return false;
        }
    });
    public CoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoverFragment newInstance(String param1, String param2) {
        CoverFragment fragment = new CoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_cover;
    }

    @Override
    protected void initView(View view) {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable("音乐");

        ImageView circleImageView = view.findViewById(R.id.iv_cover_background);
        Bitmap bitmap = AudioUtil.getImage(getContext(), song.getPath());
        circleImageView.setImageBitmap(bitmap);
        if (bitmap == null){
            circleImageView.setImageResource(R.mipmap.aoq);
        }

        mMusicControl = view.findViewById(R.id.mc_music);
        mMusicControl.setEndTime((int) song.getDuration());
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.music);
        LinearInterpolator lin = new LinearInterpolator();//设置动画匀速运动
        animation.setInterpolator(lin);
        circleImageView.startAnimation(animation);

        handler.sendEmptyMessageDelayed(0,200 );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public interface Control{
        void next();
        void promous();
        void play();
        double progress();
        void changeMode();
        void moveProgress(int progress);
        Song complete();
    }
}
