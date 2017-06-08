package com.chenxulu.video.demo1;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chenxulu.video.widget.MyVideoLayout;
import com.chenxulu.video.widget.MyVideoLayoutListener;

/**
 * Created by xulu on 16/5/10.
 */
public class MyVideoController1 implements AbsListView.OnScrollListener, MyVideoLayoutListener, View.OnTouchListener {
    private static final int SCREEN_DEFAULT = 0;
    private static final int SCREEN_SMALL = 1;
    private static final int SCREEN_FULL = 2;

    private static final float VIDEO_SCALE = 1.77f;

    private MyVideoLayout myVideoLayout;
    private View hideView;
    private ListView listView;

    private int screenType;

    private VideoControllerListener mListener;

    private int displayWidth;
    private int displayHeight;
    private float lastX;
    private float lastY;

    /**
     * @param videoLayout
     * @param listView
     * @param hideView
     */
    public MyVideoController1(MyVideoLayout videoLayout, ListView listView, View hideView) {
        this.myVideoLayout = videoLayout;
        this.listView = listView;
        this.hideView = hideView;
        this.initView();
    }

    private void initView() {
        displayWidth = myVideoLayout.getResources().getDisplayMetrics().widthPixels;
        displayHeight = myVideoLayout.getResources().getDisplayMetrics().heightPixels;

        myVideoLayout.setVisibility(View.GONE);
        myVideoLayout.setOnTouchListener(this);
        myVideoLayout.setMyVideoLayoutListener(this);

        listView.setOnScrollListener(this);
    }

    /**
     * set video path
     *
     * @param videoPath
     */
    public void setVideoPath(String videoPath) {
        myVideoLayout.setVideoPath(videoPath);
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
     */
    public void startPlay() {
        setDefaultScreen();
        myVideoLayout.setVisibility(View.VISIBLE);
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
     * set video view to small screen
     */
    public void setSmallScreen() {
        screenType = SCREEN_SMALL;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myVideoLayout.getLayoutParams();
        layoutParams.width = displayWidth / 2;
        layoutParams.height = (int) (layoutParams.width / VIDEO_SCALE);
        layoutParams.topMargin = listView.getTop();
        layoutParams.leftMargin = displayWidth - layoutParams.width;
        myVideoLayout.setLayoutParams(layoutParams);
        myVideoLayout.setScreenType(MyVideoLayout.SCREEN_SMALL);
    }

    /**
     * if video view to small screen
     */
    public boolean isSmallScreen() {
        if (listView.getFirstVisiblePosition() > 0) {
            return true;
        }
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
        if (screenType != SCREEN_FULL && myVideoLayout.getVisibility() == View.VISIBLE) {
            if (isSmallScreen()) {
                if (screenType != SCREEN_SMALL) {
                    setSmallScreen();
                }
            } else {
                setDefaultScreen();
            }
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (screenType != SCREEN_SMALL) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = event.getRawX() - lastX;
                float dy = event.getRawY() - lastY;
                lastX = event.getRawX();
                lastY = event.getRawY();

                int left = (int) (v.getLeft() + dx);
                int top = (int) (v.getTop() + dy);
                int right = (int) (v.getRight() + dx);
                int bottom = (int) (v.getBottom() + dy);

                if (left > 0 && right < displayWidth && top > listView.getTop() && bottom < displayHeight) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myVideoLayout.getLayoutParams();
                    layoutParams.topMargin += dy;
                    layoutParams.leftMargin += dx;
                    myVideoLayout.setLayoutParams(layoutParams);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void fullScreenChange() {
        if (screenType == SCREEN_FULL) {
            if (isSmallScreen()) {
                setSmallScreen();
            } else {
                setDefaultScreen();
            }
            if (mListener != null)
                mListener.fullScreen(false);
        } else {
            setFullScreen();
            if (mListener != null)
                mListener.fullScreen(true);
        }
    }

    /**
     * if screen rotate,refresh view location
     */
    public void onConfigurationChanged() {
        displayWidth = myVideoLayout.getResources().getDisplayMetrics().widthPixels;
        displayHeight = myVideoLayout.getResources().getDisplayMetrics().heightPixels;
        if (screenType != SCREEN_FULL) {
            if (isSmallScreen()) {
                setSmallScreen();
            } else {
                setDefaultScreen();
            }
        }
    }

    @Override
    public void onError() {
        Toast.makeText(myVideoLayout.getContext(), "play error", Toast.LENGTH_SHORT).show();
    }

    public interface VideoControllerListener {
        void fullScreen(boolean fullScreen);
    }
}
