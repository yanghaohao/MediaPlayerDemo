package com.example.yanghao.mediaplayerdemo.util;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.yanghao.mediaplayerdemo.R;

public class ViewUtil {

    public static void initView(AppCompatActivity appCompatActivity,Toolbar toolbar, DrawerLayout drawer, TextView t){
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                appCompatActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "侧拉页面", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


}
