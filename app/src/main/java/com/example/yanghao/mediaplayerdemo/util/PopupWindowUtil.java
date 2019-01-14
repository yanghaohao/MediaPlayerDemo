package com.example.yanghao.mediaplayerdemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.yanghao.mediaplayerdemo.R;

public class PopupWindowUtil {


    public static void setAddPopupWindow(View rootView, final Context context, final Activity activity){
        View popView = LayoutInflater.from(context).inflate(
                R.layout.pupwindow_bottom, null);
        final PopupWindow popupWindow = new PopupWindow(popView,        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundAlpha(activity,0.5f);//设置屏幕透明度

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
        // 顯示在根佈局的底部
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.LEFT, 0,
                0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(activity,1.0f);
            }
        });

        TextView tvScanFast = popView.findViewById(R.id.tv_scan_fast);
        TextView tvScanAll = popView.findViewById(R.id.tv_scan_all);
        Button btnCancle = popView.findViewById(R.id.btn_cancle);

        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_scan_fast:
                        break;
                    case R.id.tv_scan_all:
                        break;
                    case R.id.btn_cancle:
                        popupWindow.dismiss();
                        break;
                }
            }
        };

        tvScanFast.setOnClickListener(mOnClickListener);
        tvScanAll.setOnClickListener(mOnClickListener);
        btnCancle.setOnClickListener(mOnClickListener);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     *            屏幕透明度0.0-1.0 1表示完全不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    public static void setDetailPopupWindow(View rootView, final Context context, final Activity activity){
        View popView = LayoutInflater.from(context).inflate(
                R.layout.pupopwindow_detail, null);
        final PopupWindow popupWindow = new PopupWindow(popView,        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setBackgroundAlpha(activity,0.5f);//设置屏幕透明度

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);// 点击空白处时，隐藏掉pop窗口
        // 顯示在根佈局的底部
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.LEFT, 0,
                0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                // popupWindow隐藏时恢复屏幕正常透明度
                setBackgroundAlpha(activity,1.0f);
            }
        });

        TextView tvDetailPop = popView.findViewById(R.id.tv_detail_pop);
        TextView tvLastPop = popView.findViewById(R.id.tv_last_pop);
        Button btnCancel = popView.findViewById(R.id.btn_cancel);


        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_detail_pop:
                        break;
                    case R.id.tv_last_pop:
                        break;
                    case R.id.btn_cancel:
                        popupWindow.dismiss();
                        break;
                }
            }
        };
        tvDetailPop.setOnClickListener(mOnClickListener);
        tvLastPop.setOnClickListener(mOnClickListener);
        btnCancel.setOnClickListener(mOnClickListener);
    }


}
