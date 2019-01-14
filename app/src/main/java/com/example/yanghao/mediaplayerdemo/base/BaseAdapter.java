package com.example.yanghao.mediaplayerdemo.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseAdapter<T extends Object,VH extends BaseAdapter.BaseViewHolder> extends RecyclerView.Adapter<VH> {


    private List<T> mObject;
    private Context mContext;
    private OnClickItem mOnClickItem;

    public OnClickItem getmOnClickItem() {
        return mOnClickItem;
    }

    public void setmOnClickItem(OnClickItem mOnClickItem) {
        this.mOnClickItem = mOnClickItem;
    }

    public BaseAdapter(List<T> mObject, Context mContext) {
        this.mObject = mObject;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return (VH) BaseViewHolder.getHolder(mContext, viewGroup, createViewHolder());
    }

    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, final int i) {
        setData(viewHolder,i);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickItem.onClick(view, i);
            }
        });
    }

    protected abstract int createViewHolder();
    protected abstract void setData(@Nullable BaseViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mObject.size();
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public SparseArray<View> mViews;
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            mViews = new SparseArray<>();
        }

        public static <D extends BaseViewHolder> D getHolder(Context context,ViewGroup viewGroup,int layoutId){
            return (D) new BaseViewHolder(LayoutInflater.from(context).inflate(layoutId,viewGroup,false ));
        }

        public <D extends View> D findViewById(int viewId){
            View childView = mViews.get(viewId);
            if (childView == null){
                childView = itemView.findViewById(viewId);
                mViews.append(viewId, childView);
            }
            return (D) childView;
        }
    }

    public interface OnClickItem{
        void onClick(View v,int position);
    }
}
