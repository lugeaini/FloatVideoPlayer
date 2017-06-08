package com.chenxulu.video.demo2;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chenxulu.video.widget.MyVideoLayout;
import com.chenxulu.video.widget.MyVideoLayoutListener;

/**
 * Created by xulu on 16/5/10.
 */
public class MyVideoController2 implements AbsListView.OnScrollListener, MyVideoLayoutListener {
    private static final int SCREEN_DEFAULT = 0;
    private static final int SCREEN_FULL = 1;

    private MyVideoLayout myVideoLayout;
    private View hideView;
    private ListView listView;

    private int screenType;

    private VideoControllerListener mListener;

    private int displayWidth;
    private int displayHeight;

    private int position = -1;

    /**
     * @param videoLayout
     * @param listView
     */
    public MyVideoController2(MyVideoLayout videoLayout, ListView listView) {
        this.myVideoLayout = videoLayout;
        this.listView = listView;
        this.initView();
    }

    private void initView() {
        displayWidth = myVideoLayout.getResources().getDisplayMetrics().widthPixels;
        displayHeight = myVideoLayout.getResources().getDisplayMetrics().heightPixels;

        myVideoLayout.setVisibility(View.GONE);
        myVideoLayout.setMyVideoLayoutListener(this);

        listView.setOnScrollListener(this);
    }

    /**
     * set listener
     *
     * @param listener
     */
    public void setMyVideoLayoutListener(VideoControllerListener listener) {
        this.mListener = listener;
    }

    /**
     * is full screen
     *
     * @return
     */
    public boolean isFullScreen() {
        return screenType == SCREEN_FULL;
    }

    /**
     * start play video
     *
     * @param hideView
     * @param videoPath
     * @param position
     */
    public void startPlay(View hideView, String videoPath, int position) {
        this.hideView = hideView;
        this.position = position;
        myVideoLayout.setVideoPath(videoPath);

        myVideoLayout.setVisibility(View.VISIBLE);
        setDefaultScreen();
        myVideoLayout.startPlay();
    }

    /**
     * video pause
     */
    public void pause() {
        myVideoLayout.pause();
    }

    /**
     * video stop
     */
    public void stop() {
        myVideoLayout.stop();
        myVideoLayout.setVisibility(View.GONE);
        screenType = SCREEN_DEFAULT;
        hideView = null;
        position = -1;
    }

    /**
     * set video view to full screen
     */
    public void setFullScreen() {
        screenType = SCREEN_FULL;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myVideoLayout.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(0, 0, 0, 0);
        myVideoLayout.setLayoutParams(layoutParams);
        myVideoLayout.setScreenType(MyVideoLayout.SCREEN_FULL);
    }

    /**
     * set video view to default screen
     */
    public void setDefaultScreen() {
        screenType = SCREEN_DEFAULT;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myVideoLayout.getLayoutParams();
        layoutParams.width = hideView.getWidth();
        layoutParams.height = hideView.getHeight();

        int[] hideViewLocation = new int[2];
        hideView.getLocationInWindow(hideViewLocation);

        int[] parentLocation = new int[2];
        ((View) myVideoLayout.getParent()).getLocationInWindow(parentLocation);

        layoutParams.leftMargin = hideViewLocation[0];
        layoutParams.topMargin = hideViewLocation[1] - parentLocation[1];

        myVideoLayout.setLayoutParams(layoutParams);
        myVideoLayout.setScreenType(MyVideoLayout.SCREEN_DEFAULT);
    }

    /**
     * if video view leave screen
     */
    public boolean isLeaveScreen() {
        int[] hideViewLocation = new int[2];
        hideView.getLocationInWindow(hideViewLocation);

        int[] listViewLocation = new int[2];
        listView.getLocationInWindow(listViewLocation);

        System.out.println(hideViewLocation[1] + ":" + hideView.getHeight() + ":" + listViewLocation[1]);

        if (hideViewLocation[1] > displayHeight || hideViewLocation[1] - listViewLocation[1] + hideView.getHeight() < 0) {
            return true;
        }
        return false;
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (hideView == null) {
            return;
        }
        if (screenType == SCREEN_FULL) {
            return;
        }
        if (position < firstVisibleItem || position > firstVisibleItem + visibleItemCount) {
            return;
        }
        if (isLeaveScreen()) {
            myVideoLayout.pause();
        }else {
            setDefaultScreen();
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public void fullScreenChange() {
        if (screenType == SCREEN_FULL) {
            setDefaultScreen();
            if (mListener != null)
                mListener.fullScreen(false);
        } else {
            setFullScreen();
            if (mListener != null)
                mListener.fullScreen(true);
        }
    }

    @Override
    public void onError() {
        if (mListener != null) {
            mListener.playOnError();
        }
    }

    /**
     * if screen rotate,refresh view location
     */
    public void onConfigurationChanged() {
        displayWidth = myVideoLayout.getResources().getDisplayMetrics().widthPixels;
        displayHeight = myVideoLayout.getResources().getDisplayMetrics().heightPixels;
        if (screenType != SCREEN_FULL) {
            setDefaultScreen();
        }
    }

    public interface VideoControllerListener {
        void fullScreen(boolean fullScreen);

        void playOnError();
    }
}
