package com.lh.daily.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lh.daily.R;
import com.lh.daily.bean.zhihu.ZhihuDaily;
import com.lh.daily.databinding.HomeListItemWithImgBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by home on 2017/2/8.
 * 知乎日报列表适配器
 */

public class ZhihuDailyListAdapter extends RecyclerView.Adapter<ZhihuDailyListAdapter.StoryViewHolder> {

    private List<ZhihuDaily.Story> mStories = new ArrayList<>();

    private OnStoryClickListener mOnStoryClickListener;

    public void addStory(ZhihuDaily.Story story) {
        if (story == null) {
            return;
        }
        mStories.add(story);
        notifyItemInserted(mStories.size() - 1);
    }

    public void addStories(List<ZhihuDaily.Story> stories) {
        if (stories == null) {
            return;
        }
        int preSize = mStories.size();
        mStories.addAll(stories);
        notifyItemRangeInserted(preSize,stories.size());
    }

    public void clear() {
        mStories.clear();
        notifyDataSetChanged();
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        mOnStoryClickListener = listener;
    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeListItemWithImgBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.home_list_item_with_img, parent, false);
        return new StoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(StoryViewHolder holder, int position) {
        ZhihuDaily.Story story = mStories.get(position);
        if (story.getImages()==null||story.getImages().size() == 0) {
            holder.mDataBinding.setImg(null);
        } else {
            holder.mDataBinding.setImg(story.getImages().get(0));
        }
        holder.mDataBinding.setTitle(story.getTitle());

    }

    @Override
    public int getItemCount() {
        return mStories.size();
    }

    class StoryViewHolder extends DataBindingViewHolder<HomeListItemWithImgBinding> implements View.OnClickListener {
        StoryViewHolder(HomeListItemWithImgBinding dataBinding) {
            super(dataBinding);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnStoryClickListener != null) {
                mOnStoryClickListener.onStoryClick(mStories.get(getAdapterPosition()).getId());
            }
        }
    }

    public interface OnStoryClickListener {
        void onStoryClick(int id);
    }
}
