package com.appublisher.huaxiaotest;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * SwipeRefreshLayout add auto load more
 */
public class ExtendedSwipeRefreshLayout extends SwipeRefreshLayout{

    private RecyclerView mRecyclerView;
    private Context mContext;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mIsLoading;

    public ExtendedSwipeRefreshLayout(Context context) {
        super(context);
        mContext = context;
    }

    public ExtendedSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setChildRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        mLayoutManager = mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        if (isBottom() && !mIsLoading) setLoading(true);
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private boolean isBottom() {
        if (mLayoutManager == null) return false;
        int last = getLastVisibleItemPosition();
        int total = mLayoutManager.getItemCount();
        return last != 0 && last >= total;
    }

    private int getLastVisibleItemPosition() {
        if (mLayoutManager == null) return 0;

        if (mLayoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastPositions = new int[((StaggeredGridLayoutManager) mLayoutManager)
                    .getSpanCount()];
            ((StaggeredGridLayoutManager) mLayoutManager)
                    .findLastVisibleItemPositions(lastPositions);
            return findMaxPosition(lastPositions);
        }

        return 0;
    }

    private int findMaxPosition(int[] positions) {
        if (positions == null || positions.length == 0) return 0;
        int max = positions[0];
        for (int value : positions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public void setLoading(boolean loading) {
        if (mRecyclerView == null) return;
        mIsLoading = loading;
        if (loading) {
            if (isRefreshing()) {
                setRefreshing(false);
            }
//            mListView.setSelection(mListView.getAdapter().getCount() - 1);
            if (mOnLoadMoreListener != null) mOnLoadMoreListener.onLoadMore();
        }
    }

}
