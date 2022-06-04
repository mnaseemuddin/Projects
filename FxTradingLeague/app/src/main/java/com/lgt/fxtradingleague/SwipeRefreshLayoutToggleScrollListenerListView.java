package com.lgt.fxtradingleague;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

public class SwipeRefreshLayoutToggleScrollListenerListView implements AbsListView.OnScrollListener {
    private List<AbsListView.OnScrollListener> mScrollListeners = new ArrayList<AbsListView.OnScrollListener>();
    private int mExpectedVisiblePosition = 0;

    public SwipeRefreshLayoutToggleScrollListenerListView(SwipeRefreshLayout mSwipeLayout) {
        this.mSwipeLayout = mSwipeLayout;
    }

    private SwipeRefreshLayout mSwipeLayout;
    public void addScrollListener(AbsListView.OnScrollListener listener){
        mScrollListeners.add(listener);
    }
    public boolean removeScrollListener(AbsListView.OnScrollListener listener){
        return mScrollListeners.remove(listener);
    }
    public void setExpectedFirstVisiblePosition(int position){
        mExpectedVisiblePosition = position;
    }

    private void notifyOnScrolled(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
        for(AbsListView.OnScrollListener listener : mScrollListeners){
            listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
    private void notifyScrollStateChanged(AbsListView view, int scrollState){
        for(AbsListView.OnScrollListener listener : mScrollListeners){
            listener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        notifyScrollStateChanged(view, scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        notifyOnScrolled(view, firstVisibleItem, visibleItemCount, totalItemCount);
        if(firstVisibleItem != RecyclerView.NO_POSITION)
            mSwipeLayout.setEnabled(firstVisibleItem == mExpectedVisiblePosition);
    }
}