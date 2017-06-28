package com.chenxulu.fvideo;

import android.support.design.widget.AppBarLayout;
import android.view.View;

import com.chenxulu.fvideo.widget.BaseVideoController;
import com.chenxulu.fvideo.widget.MyVideoView;

/**
 * Created by xulu on 2017/6/27.
 */

public class MyVideoController extends BaseVideoController implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout mAppBarLayout;

    public MyVideoController(MyVideoView videoLayout, View hideView, AppBarLayout scrollView) {
        super(videoLayout, hideView);
        this.mAppBarLayout = scrollView;

        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected int getTopMargin() {
        return ((View) mAppBarLayout.getParent()).getTop();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        System.out.println(verticalOffset);
        onScroll();
    }


}
