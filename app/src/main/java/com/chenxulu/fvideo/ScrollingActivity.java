package com.chenxulu.fvideo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chenxulu.fvideo.widget.BaseVideoController;
import com.chenxulu.fvideo.widget.MyVideoView;

public class ScrollingActivity extends AppCompatActivity implements BaseVideoController.VideoControllerListener {
    String videoPath = "http://cdn.xnwimg.com/down/f:%7B26F3FBCF-1718-7B81-3791-D6AF9C06BD63%7D/ct:0/testmpg.mpg";

    View topLayout;
    CoordinatorLayout coordinatorLayout;

    MyVideoView myVideoView;
    MyVideoController mVideoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        topLayout = findViewById(R.id.top_layout);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);

        myVideoView = (MyVideoView) findViewById(R.id.my_video_view);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        View hideView = findViewById(R.id.hide_view);
        hideView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoController.startPlay();
            }
        });

        mVideoController = new MyVideoController(myVideoView, hideView, appBarLayout);
        mVideoController.setVideoPath(videoPath);

    }

    @Override
    public void fullScreen(boolean fullScreen) {
        if (fullScreen) {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attr);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            coordinatorLayout.setVisibility(View.GONE);
            topLayout.setVisibility(View.GONE);
        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            coordinatorLayout.setVisibility(View.VISIBLE);
            topLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoController != null) {
            mVideoController.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mVideoController != null) {
            mVideoController.stop();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mVideoController.onConfigurationChanged();
    }

    @Override
    public void onBackPressed() {
        if (mVideoController != null && mVideoController.isFullScreen()) {
            mVideoController.stop();
            fullScreen(false);
        } else {
            finish();
        }
    }
}
