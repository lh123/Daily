package com.lh.daily.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.daily.R;
import com.lh.daily.bean.zhihu.ZhihuDailyDetail;
import com.lh.daily.databinding.HomeListItemWithImgBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2017/2/10.
 */

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.LikeHolder> {

    private List<ZhihuDailyDetail> mZhihuDailyDetails = new ArrayList<>();

    private OnLikeItemClickListener mOnLikeItemClickListener;

    public void setOnLikeItemClickListener(OnLikeItemClickListener listener) {
        this.mOnLikeItemClickListener = listener;
    }

    public void addStories(List<ZhihuDailyDetail> details) {
        if (details == null) {
            return;
        }
        int preSize = mZhihuDailyDetails.size();
        mZhihuDailyDetails.addAll(details);
        notifyItemRangeInserted(preSize, details.size());
    }

    public void clear(boolean notify) {
        mZhihuDailyDetails.clear();
        if (notify) {
            notifyDataSetChanged();
        }
    }

    @Override
    public LikeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeListItemWithImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.home_list_item_with_img, parent, false);
        return new LikeHolder(binding);
    }

    @Override
    public void onBindViewHolder(LikeHolder holder, int position) {
        ZhihuDailyDetail dailyDetail = mZhihuDailyDetails.get(position);
        if (TextUtils.isEmpty(dailyDetail.getImage())) {
            holder.mDataBinding.setImg(null);
        } else {
            holder.mDataBinding.setImg(dailyDetail.getImage());
        }
        holder.mDataBinding.setTitle(dailyDetail.getTitle());
    }

    @Override
    public int getItemCount() {
        return mZhihuDailyDetails.size();
    }

    class LikeHolder extends DataBindingViewHolder<HomeListItemWithImgBinding> implements View.OnClickListener {

        LikeHolder(HomeListItemWithImgBinding dataBinding) {
            super(dataBinding);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnLikeItemClickListener != null) {
                mOnLikeItemClickListener.onLikeItemClick(1, mZhihuDailyDetails.get(getAdapterPosition()).getId());
            }
        }
    }

    public interface OnLikeItemClickListener {
        void onLikeItemClick(int type, int id);
    }
}
