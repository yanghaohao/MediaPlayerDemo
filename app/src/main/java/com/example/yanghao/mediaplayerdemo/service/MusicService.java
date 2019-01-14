package com.example.yanghao.mediaplayerdemo.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.yanghao.mediaplayerdemo.entity.Song;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MusicService extends Service {
    //列表循环
    public static final int MENU_RECICLE=1;
    //单曲循环
    public static final int SINGLE_RECICLE=2;
    //随机播放
    public static final int RADOM=3;
    //播放下一首
    public static final int OPER_NEXT=1;
    //播放上一首
    public static final int OPER_PREVIOUS=-1;

    /*当前歌曲播放状态——播放列表刚刚加载或没有加载，并未播放任何歌曲*/
    private static final int UNPLAY=-1;
    /*当前歌曲播放状态——当前歌曲被暂停*/
    private static final int PAUSE=1;
    /*当前歌曲播放状态——当前歌曲被停止*/
    private static final int STOP=2;
    /*当前歌曲播放状态——正在播放*/
    private static final int PLAYING=3;
    /*当前歌曲播放状态——播放完成*/
    private static final int PLAYCOM=4;

    //保存当前播放状态
    private int currMode=MENU_RECICLE;
    //用于显示播放列表的数据源
    private List<Map<String,Object>> musicList=new ArrayList<Map<String,Object>>();

    public MediaPlayer mPlayer;
    //当前播放的歌曲索引
    private int currIndex=-1;
    //当前歌曲的播放状态
    private int currStatus=UNPLAY;

    private Complete complete;

    private int momentPlayName;

    public Complete getComplete() {
        return complete;
    }

    public int getMomentPlayName() {
        return momentPlayName;
    }

    public void setMomentPlayName(int momentPlayName) {
        this.momentPlayName = momentPlayName;
        
    }

    public void setComplete(Complete complete) {
        this.complete = complete;
    }

    public MusicService() {
        super();
        // TODO Auto-generated constructor stub
        mPlayer=new MediaPlayer();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                try {
                    complete.complete();
                    playNew(OPER_NEXT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return new MainBinder();

    }


    public boolean isComplete(){
        return currStatus==PLAYCOM;
    }

    /**
     * 获得当前切换策略的内容
     * @return
     */
    public String getMode(){
        switch(currMode){
            case MENU_RECICLE:
                return "列表循环";
            case SINGLE_RECICLE:
                return "单曲循环";
            case RADOM:
                return "随机播放";
            default:
                return "";
        }
    }


    /**
     * 改变切换策略
     * @return
     */
    public String changeMode(){
        switch(currMode){
            case MENU_RECICLE:
                currMode=SINGLE_RECICLE;
                return "单曲循环";
            case SINGLE_RECICLE:
                currMode=RADOM;
                return "随机播放";
            case RADOM:
                currMode=MENU_RECICLE;
                return "列表循环";
            default:
                return "";
        }
    }

    /**
     * 找当前目录下的音乐文件
     * @param musicUrl 要找寻音乐文件的目录路径
     * @return
     */
    public List<Map<String,Object>> refeshMusicList(String musicUrl){
        File musicDir=new File(musicUrl);
        if(musicDir!=null&&musicDir.isDirectory()){
            File[] musicFile=musicDir.listFiles(new MusicFilter());
            if(musicFile!=null){
                musicList=new ArrayList<Map<String,Object>>();
                for(int i=0;i<musicFile.length;i++){
                    File currMusic=musicFile[i];
                    //获取当前目录的名称和绝对路径
                    String abPath=currMusic.getAbsolutePath();
                    String musicName=currMusic.getName();
                    Map<String,Object> currMusicMap=new HashMap<String,Object>();
                    currMusicMap.put("musicName", musicName);
                    currMusicMap.put("musicAbPath", abPath);
                    musicList.add(currMusicMap);
                }

            }else{
                musicList = new ArrayList<Map<String,Object>>();
            }
        }else{
            musicList = new ArrayList<Map<String,Object>>();
        }
        return musicList;
    }

    /**
     * 找当前目录下的音乐文件
     *
     * @return
     */
    public List<Map<String,Object>> getMusicList(List<Song> mSongs){
        Log.e("getMusicList: ", mSongs.get(0).getTitle());
        musicList = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<mSongs.size();i++){
            Map<String,Object> currMusicMap=new HashMap<String,Object>();
            currMusicMap.put("musicName", mSongs.get(i).getTitle());
            Log.e("getMusicList: ",mSongs.get(i).getPath() );
            currMusicMap.put("musicAbPath", mSongs.get(i).getPath());
            musicList.add(currMusicMap);
        }
        Log.e("playMusic11: ", musicList+"");
        return musicList;
    }

    /**
     * 播放歌曲
     * @param musicPo 要播放的歌曲在歌曲列表中的索引
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws IOException
     */
    public void playMusic(int musicPo) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException {
        Map<String,Object> currMusic=musicList.get(musicPo);
        Log.e("playMusic: ", musicList+"");
        String musicUrl=(String)currMusic.get("musicAbPath");
        mPlayer.reset();
        mPlayer.setDataSource(musicUrl);
        mPlayer.prepare();
        mPlayer.start();
        currIndex=musicPo;
        currStatus=PLAYING;
        setMomentPlayName(currIndex);
    }

    /**
     * 停止播放
     */
    public void stopPlay(){
        mPlayer.stop();
        currStatus=STOP;
    }



    /**
     * 播放一首新的歌曲
     * @param operCode 播放操作（是上一首还是下一首）
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     * @throws IOException
     */
    public void playNew(int operCode) throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
        if(musicList.size()>0){
            switch(currMode){
                case MENU_RECICLE:
                    int newIndex=0;
                    switch(operCode){
                        case OPER_NEXT:
                            newIndex=currIndex+1;
                            if(newIndex>=musicList.size()){
                                newIndex=0;
                            }
                            setMomentPlayName(newIndex);
                            break;
                        case OPER_PREVIOUS:
                            newIndex=currIndex-1;
                            if(newIndex<0){
                                newIndex=musicList.size()-1;
                            }
                            setMomentPlayName(newIndex);
                            break;
                    }
                    playMusic(newIndex);
                    break;
                case SINGLE_RECICLE:
                    playMusic(currIndex);
                    setMomentPlayName(currIndex);
                    break;
                case RADOM:
                    Random rd=new Random();
                    int randomIndex=rd.nextInt(musicList.size());
                    playMusic(randomIndex);
                    setMomentPlayName(randomIndex);
                    break;
            }
        }
    }
    /**
     * 暂停当前歌曲的播放
     */
    public void pause(){
        mPlayer.pause();
        currStatus=PAUSE;
    }

    /**
     * 继续当前歌曲的播放
     */
    public void resum(){
        mPlayer.start();
        currStatus=PLAYING;
    }
    /*
     * 释放当前播放的音乐资源
     */
    public void release(){
        mPlayer.release();
        currStatus=UNPLAY;
    }

    /**
     * 播放按钮要执行播放操作时调用
     * @throws IOException
     * @throws IllegalStateException
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    public void play() throws IllegalArgumentException, SecurityException, IllegalStateException, IOException{
        if(musicList.size()>0){
            switch(currStatus){
                case UNPLAY:
                    playMusic(0);
                    break;
                case STOP:
                    mPlayer.prepare();
                    mPlayer.seekTo(0);
                case PAUSE:
                    mPlayer.start();
                    currStatus=PLAYING;
                    break;
            }
        }

    }
    /**
     * 改变当前播放进度
     * @param progress 新的播放进度
     */
    public void changePro(int progress){
        if(mPlayer.isPlaying()){
            mPlayer.seekTo(progress);
        }
    }

    /**
     * 获取当前音乐播放进度
     * @return 当前音乐的播放进度
     */
    public int getMusicPro(){
        if(mPlayer.isPlaying()){
            return mPlayer.getCurrentPosition();
        }else{
            return 0;
        }
    }
    /**
     * 获取当前音乐的总时长
     * @return
     */
    public int getMusicDur(){
        return mPlayer.getDuration();
    }

    private class MusicFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            // TODO Auto-generated method stub
            if(pathname.isFile()==true){
                String fileName=pathname.getName();
                String lowerName=fileName.toLowerCase();
                return lowerName.endsWith(".mp3");
            }else{
                return false;
            }

        }

    }

    public class MainBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }
    }


    public class MusicRecevier extends BroadcastReceiver{
        public static final String NOTIFICATION_ITEM_BUTTON_LAST
                = "com.example.notification.ServiceReceiver.last";
        public static final String NOTIFICATION_ITEM_BUTTON_PLAY
                = "com.example.notification.ServiceReceiver.play";
        public static final String NOTIFICATION_ITEM_BUTTON_NEXT
                = "com.example.notification.ServiceReceiver.next";
        public static final String NOTIFICATION_ITEM_BUTTON_CHANGE_MODE
                = "com.example.notification.ServiceReceiver.complete";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            try {
                if (action.equals(NOTIFICATION_ITEM_BUTTON_LAST)) {
                    playNew(OPER_PREVIOUS);
                }
                else  if (action.equals(NOTIFICATION_ITEM_BUTTON_PLAY)) {
                    if (mPlayer.isPlaying()){
                        pause();
                    }else {
                        resum();
                    }
                }
                else if (action.equals(NOTIFICATION_ITEM_BUTTON_NEXT)) {
                    playNew(OPER_NEXT);
                }
                else if (action.equals(NOTIFICATION_ITEM_BUTTON_CHANGE_MODE)){
                    changeMode();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public interface Complete{
        void complete();
    }
}
