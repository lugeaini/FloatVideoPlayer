package com.chenxulu.fvideo.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by xulu on 2017/6/26.
 */

public abstract class BaseVideoController implements MyVideoViewListener, View.OnTouchListener {
    private static final int SCREEN_DEFAULT = 0;
    private static final int SCREEN_SMALL = 1;
    private static final int SCREEN_FULL = 2;

    private static final float VIDEO_SCALE = 1.77f;

    private MyVideoView myVideoLayout;
    private View hideView;

    private int screenType;

    private VideoControllerListener mListener;

    private int displayWidth;
    private int displayHeight;
    private float lastX;
    private float lastY;

    /**
     * @param videoLayout
     * @param hideView
     */
    public BaseVideoController(MyVideoView videoLayout, View hideView) {
        this.myVideoLayout = videoLayout;
        this.hideView = hideView;
        initView();
    }

    private void initView() {
        displayWidth = myVideoLayout.getResources().getDisplayMetrics().widthPixels;
        displayHeight = myVideoLayout.getResources().getDisplayMetrics().heightPixels;

        myVideoLayout.setVisibility(View.GONE);
        myVideoLayout.setOnTouchListener(this);
        myVideoLayout.setMyVideoLayoutListener(this);
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
        myVideoLayout.setScreenType(MyVideoView.SCREEN_FULL);
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

        layoutParams.leftMargin = hideViewLocation[0];
        layoutParams.topMargin = hideViewLocation[1] - getParentTopMargin();

        myVideoLayout.setLayoutParams(layoutParams);
        myVideoLayout.setScreenType(MyVideoView.SCREEN_DEFAULT);
    }

    /**
     * set video view to small screen
     */
    public void setSmallScreen() {
        screenType = SCREEN_SMALL;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) myVideoLayout.getLayoutParams();
        layoutParams.width = displayWidth / 2;
        layoutParams.height = (int) (layoutParams.width / VIDEO_SCALE);
        layoutParams.topMargin = getTopMargin();
        layoutParams.leftMargin = displayWidth - layoutParams.width;
        myVideoLayout.setLayoutParams(layoutParams);
        myVideoLayout.setScreenType(MyVideoView.SCREEN_SMALL);
    }

    /**
     * if video view to small screen
     */
    public boolean isInSmallRect() {
        int[] hideViewLocation = new int[2];
        hideView.getLocationInWindow(hideViewLocation);

        int topMargin = getTopMargin() + getParentTopMargin();

        return hideViewLocation[1] > displayHeight || hideViewLocation[1] + hideView.getHeight() < topMargin;
    }

    /**
     * 根视图的居上距离，大部分情况等同于状态栏的高度
     *
     * @return
     */
    protected int getParentTopMargin() {
        int[] parentLocation = new int[2];
        ((View) myVideoLayout.getParent()).getLocationInWindow(parentLocation);
        return parentLocation[1];
    }

    /**
     * @return
     */
    protected abstract int getTopMargin();


    public void onScroll() {
        if (screenType != SCREEN_FULL && myVideoLayout.getVisibility() == View.VISIBLE) {
            if (isInSmallRect()) {
                if (screenType != SCREEN_SMALL) {
                    setSmallScreen();
                }
            } else {
                setDefaultScreen();
            }
        }
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

                if (left > 0 && right < displayWidth && top > getTopMargin() && bottom < displayHeight) {
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
            if (isInSmallRect()) {
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
            if (isInSmallRect()) {
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
