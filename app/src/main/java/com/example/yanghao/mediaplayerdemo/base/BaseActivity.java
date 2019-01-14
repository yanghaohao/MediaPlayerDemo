package com.example.yanghao.mediaplayerdemo.base;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.yanghao.mediaplayerdemo.service.MusicService;
import com.example.yanghao.mediaplayerdemo.ui.activity.HomeActivity;

import java.io.IOException;

public abstract class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setLayout();
        setView();

    }

    protected abstract void setLayout();
    protected abstract void setView();

    public void hideActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    protected void openActivity(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }


    protected void openActivity(Class cls,Bundle bundle){
        Intent intent = new Intent(this,cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    protected void sendBorad(String action){
        Intent intent = new Intent();
        intent.setAction(action);
        this.sendBroadcast(intent);

    }

}
