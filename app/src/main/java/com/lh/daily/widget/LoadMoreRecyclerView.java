package com.lh.daily.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lh.daily.R;

/**
 * Created by liuhui on 2016/10/6.
 * 加载更多的RecyclerView
 */

public class LoadMoreRecyclerView extends RecyclerView {

    public static final int TYPE_LOAD_MORE = -1;

    public static final int STATUS_PREPARE = 0;
    public static final int STATUS_LOADING = 1;
    public static final int STATUS_LOADING_ERROR = 2;
    public static final int STATUS_LOADING_RETRY = 3;
    public static final int STATUS_NO_MORE = 4;

    private int mCurrentStatus;

    private boolean mEnableLoadMore = true;

    private WrappedAdapter mAdapter;

    private OnLoadMoreLinstener mOnLoadMoreLinstener;

    private AdapterDataObserver mObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };


    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        addOnScrollListener(new LoadMoreScrollLinstener());
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            return;
        }
        if (mAdapter != null) {
            mAdapter.mInternalAdapter.unregisterAdapterDataObserver(mObserver);
        }
        mAdapter = new WrappedAdapter(adapter);
        mAdapter.mInternalAdapter.registerAdapterDataObserver(mObserver);
        super.setAdapter(mAdapter);
    }

    public void setEnableLoadMore(boolean enable) {
        if (mEnableLoadMore == enable) {
            return;
        }
        mEnableLoadMore = enable;
        if (mAdapter == null) {
            return;
        }
        if (mEnableLoadMore) {
            mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        } else {
            mAdapter.notifyItemRemoved(mAdapter.getItemCount());
        }
    }

    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            setRefreshStatus(STATUS_LOADING);
        } else {
            setRefreshStatus(STATUS_PREPARE);
        }
    }

    public void setRefreshStatus(int status) {
        mCurrentStatus = status;
        if (mAdapter.getItemCount() > 0 && mEnableLoadMore) {
            mAdapter.notifyItemChanged(mAdapter.getItemCount() - 1);
        }
    }

    public void setOnLoadMoreLinstener(OnLoadMoreLinstener linstener) {
        mOnLoadMoreLinstener = linstener;
    }

    public class WrappedAdapter extends Adapter {

        private Adapter<? super ViewHolder> mInternalAdapter;

        WrappedAdapter(Adapter<? super ViewHolder> innerAdapter) {
            mInternalAdapter = innerAdapter;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_LOAD_MORE) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.load_more_view, parent, false);
                return new LoadMoreViewHolder(view);
            } else {
                return mInternalAdapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_LOAD_MORE) {
                LoadMoreViewHolder loadMoreViewHolder = (LoadMoreViewHolder) holder;
                switch (mCurrentStatus) {
                    case STATUS_LOADING:
                        loadMoreViewHolder.textView.setText(R.string.loading);
                        loadMoreViewHolder.progressBar.setVisibility(VISIBLE);
                        loadMoreViewHolder.removeClickListener();
                        break;
                    case STATUS_PREPARE:
                        loadMoreViewHolder.textView.setText(R.string.loading);
                        loadMoreViewHolder.progressBar.setVisibility(VISIBLE);
                        loadMoreViewHolder.removeClickListener();
                        break;
                    case STATUS_LOADING_ERROR:
                        loadMoreViewHolder.textView.setText(R.string.load_fail);
                        loadMoreViewHolder.progressBar.setVisibility(GONE);
                        loadMoreViewHolder.removeClickListener();
                        break;
                    case STATUS_LOADING_RETRY:
                        loadMoreViewHolder.textView.setText(R.string.click_retry);
                        loadMoreViewHolder.progressBar.setVisibility(GONE);
                        loadMoreViewHolder.setUpClickListener();
                        break;
                    case STATUS_NO_MORE:
                        loadMoreViewHolder.textView.setText(R.string.loading_no_more);
                        loadMoreViewHolder.progressBar.setVisibility(GONE);
                        loadMoreViewHolder.removeClickListener();
                        break;
                }
            } else {
                mInternalAdapter.onBindViewHolder(holder, position);
            }
        }

        @Override
        public int getItemCount() {
            if (mEnableLoadMore) {
                return mInternalAdapter.getItemCount() + 1;
            } else {
                return mInternalAdapter.getItemCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mEnableLoadMore && position == getItemCount() - 1) {
                return TYPE_LOAD_MORE;
            } else {
                return mInternalAdapter.getItemViewType(position);
            }
        }

        class LoadMoreViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

            ProgressBar progressBar;
            TextView textView;

            LoadMoreViewHolder(View itemView) {
                super(itemView);
                progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
                textView = (TextView) itemView.findViewById(R.id.text_view);
            }

            void setUpClickListener() {
                itemView.setOnClickListener(this);
            }

            void removeClickListener() {
                itemView.setOnClickListener(null);
            }

            @Override
            public void onClick(View view) {
                setRefreshStatus(STATUS_LOADING);
                if (mOnLoadMoreLinstener != null) {
                    mOnLoadMoreLinstener.onLoadMore();
                }
            }
        }
    }

    public interface OnLoadMoreLinstener {
        void onLoadMore();
    }

    public class LoadMoreScrollLinstener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager.getChildCount() <= 0) {
                return;
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                    mCurrentStatus == STATUS_PREPARE && mOnLoadMoreLinstener != null &&
                    mEnableLoadMore && !canScrollVertically(1) &&
                    mAdapter != null && mAdapter.getItemCount() > 0) {
                mCurrentStatus = STATUS_LOADING;
                mOnLoadMoreLinstener.onLoadMore();
            }
        }
    }
}
