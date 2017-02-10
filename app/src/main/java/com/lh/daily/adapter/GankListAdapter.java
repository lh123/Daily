package com.lh.daily.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.daily.R;
import com.lh.daily.bean.gank.Gank;
import com.lh.daily.databinding.HomeListItemWithImgBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public class GankListAdapter extends RecyclerView.Adapter<GankListAdapter.GankViewHolder> {

    private List<Gank.Result> mGankResults = new ArrayList<>();
    private OnGankItemClickListener mOnGankItemClickListener;

    public void setOnGankItemClickListener(OnGankItemClickListener listener) {
        this.mOnGankItemClickListener = listener;
    }

    public void addGankResult(Gank.Result result) {
        if (result == null) {
            return;
        }
        mGankResults.add(result);
        notifyItemInserted(mGankResults.size() - 1);
    }

    public void addGankResults(List<Gank.Result> results) {
        if (results == null || results.size() == 0) {
            return;
        }
        int preCount = mGankResults.size();
        mGankResults.addAll(results);
        notifyItemRangeInserted(preCount, results.size());
    }

    public void clear(boolean notify) {
        mGankResults.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeListItemWithImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.home_list_item_with_img, parent, false);
        return new GankViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        Gank.Result result = mGankResults.get(position);
        holder.mDataBinding.setTitle(result.getDesc());
        if (result.getImages() != null && result.getImages().length > 0) {
            holder.mDataBinding.setImg(result.getImages()[0]);
        } else {
            holder.mDataBinding.setImg(null);
        }
    }

    @Override
    public int getItemCount() {
        return mGankResults.size();
    }

    class GankViewHolder extends DataBindingViewHolder<HomeListItemWithImgBinding> implements View.OnClickListener {
        GankViewHolder(HomeListItemWithImgBinding dataBinding) {
            super(dataBinding);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnGankItemClickListener != null) {
                mOnGankItemClickListener.onGankClick(mGankResults.get(getAdapterPosition()).getUrl());
            }
        }
    }

    public interface OnGankItemClickListener {
        void onGankClick(String url);
    }
}
