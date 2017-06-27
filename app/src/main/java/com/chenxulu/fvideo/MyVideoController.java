package com.chenxulu.fvideo;

import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.chenxulu.fvideo.widget.BaseVideoController;
import com.chenxulu.fvideo.widget.MyVideoView;

/**
 * Created by xulu on 2017/6/27.
 */

public class MyVideoController extends BaseVideoController implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout mScrollView;

    public MyVideoController(MyVideoView videoLayout, View hideView, AppBarLayout scrollView) {
        super(videoLayout, hideView);
        this.mScrollView = scrollView;

        mScrollView.addOnOffsetChangedListener(this);
    }

    @Override
    protected int getTopMargin() {
        return ((View) mScrollView.getParent()).getTop();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        System.out.println(verticalOffset);
        onScroll();
    }


}
