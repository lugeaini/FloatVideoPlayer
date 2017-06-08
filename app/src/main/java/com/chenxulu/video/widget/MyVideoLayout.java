package com.chenxulu.video.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.chenxulu.video.R;
import com.chenxulu.video.util.TimeUtil;

/**
 * Created by xulu on 16/5/6.
 */
public class MyVideoLayout extends FrameLayout implements View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, CustomMediaController.HideCallBack {
    private static final int VIDEO_STATE_IDLE = 0;
    private static final int VIDEO_STATE_PREPARE = 1;
    private static final int VIDEO_STATE_PLAYING = 2;
    private static final int VIDEO_STATE_PAUSED = 3;

    public static final int SCREEN_DEFAULT = 0;
    public static final int SCREEN_SMALL = 1;
    public static final int SCREEN_FULL = 2;

    private ImageView closeView;
    private ImageView playView;
    private VideoView videoView;

    private View bottomLayout;
    private SeekBar seekBar;
    private TextView timeTxt;
    private ImageView fullScreenView;

    private View prepareLayout;

    private String videoPath;
    private int videoState;
    private int screenType;

    private MyVideoLayoutListener mListener;
    private CustomMediaController mediaController;

    public MyVideoLayout(Context context) {
        this(context, null);
    }

    public MyVideoLayout(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public MyVideoLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_video, this);

        closeView = (ImageView) findViewById(R.id.close_view);
        closeView.setOnClickListener(this);

        playView = (ImageView) findViewById(R.id.play_view);
        playView.setOnClickListener(this);

        fullScreenView = (ImageView) findViewById(R.id.full_screen_view);
        fullScreenView.setOnClickListener(this);

        bottomLayout = findViewById(R.id.bottom_layout);
        seekBar = (SeekBar) findViewById(R.id.progress_view);
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
        timeTxt = (TextView) findViewById(R.id.time);

        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnErrorListener(this);

        mediaController = new CustomMediaController(getContext());
        mediaController.setHideCallBack(this);
        mediaController.setVisibility(GONE);
        videoView.setMediaController(mediaController);

        prepareLayout = findViewById(R.id.prepare_layout);

        videoState = VIDEO_STATE_IDLE;
    }

    /**
     * set screen style
     *
     * @param screenType
     */
    public void setScreenType(int screenType) {
        if (this.screenType != screenType) {
            this.screenType = screenType;
            show();
        }
    }

    @Override
    public void hide() {
        closeView.setVisibility(INVISIBLE);
        playView.setVisibility(INVISIBLE);
        bottomLayout.setVisibility(INVISIBLE);
        fullScreenView.setVisibility(INVISIBLE);
    }

    @Override
    public void show() {
        playView.setVisibility(VISIBLE);
        fullScreenView.setVisibility(VISIBLE);

        closeView.setVisibility(screenType == SCREEN_DEFAULT ? INVISIBLE : VISIBLE);
        bottomLayout.setVisibility(screenType == SCREEN_SMALL ? INVISIBLE : VISIBLE);
        fullScreenView.setImageResource(screenType == SCREEN_FULL ?
                R.drawable.btn_video_to_window_selector : R.drawable.btn_video_to_screen_selector);
        setClickable(screenType != SCREEN_SMALL);
    }

    /**
     * video start play
     */
    public void startPlay() {
        if (videoState == VIDEO_STATE_IDLE) {
            videoView.stopPlayback();
            videoView.setVideoPath(videoPath);
            videoView.requestFocus();
            videoView.start();
            videoState = VIDEO_STATE_PREPARE;

            prepareLayout.setVisibility(VISIBLE);
            playView.setImageResource(R.drawable.btn_video_pause_selector);
        }
    }

    /**
     * video pause
     */
    public void pause() {
        if (videoState == VIDEO_STATE_PLAYING) {
            videoView.pause();
            videoState = VIDEO_STATE_PAUSED;
            playView.setImageResource(R.drawable.btn_video_start_selector);
        }
    }

    /**
     * video stop
     */
    public void stop() {
        videoView.stopPlayback();

        seekBar.setProgress(0);
        timeTxt.setText(TimeUtil.intToStr(0) + "/" + TimeUtil.intToStr(0));

        closeView.setVisibility(INVISIBLE);
        playView.setVisibility(INVISIBLE);
        bottomLayout.setVisibility(INVISIBLE);
        prepareLayout.setVisibility(VISIBLE);

        videoState = VIDEO_STATE_IDLE;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        System.out.println("onPrepared()");
        if (videoState == VIDEO_STATE_PREPARE) {
            videoState = VIDEO_STATE_PLAYING;
            prepareLayout.setVisibility(GONE);
            startTime();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        System.out.println("onCompletion()");
        stop();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        System.out.println("onError()" + "-" + what + "-" + extra);
        stop();
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        System.out.println("onBufferingUpdate:" + percent);
    }

    /**
     * play or pause on click
     */
    private void playOnClick() {
        if (videoState == VIDEO_STATE_PAUSED) {
            videoView.start();
            videoState = VIDEO_STATE_PLAYING;
            playView.setImageResource(R.drawable.btn_video_pause_selector);
        } else if (videoState == VIDEO_STATE_PLAYING) {
            videoView.pause();
            videoState = VIDEO_STATE_PAUSED;
            playView.setImageResource(R.drawable.btn_video_start_selector);
        }
    }

    /**
     * close on click
     */
    private void closeOnClick() {
        stop();
    }

    /**
     * full screen on click
     */
    private void fullScreenClick() {
        if (mListener != null)
            mListener.fullScreenChange();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_view:
                closeOnClick();
                break;
            case R.id.full_screen_view:
                fullScreenClick();
                break;
            case R.id.play_view:
                playOnClick();
                break;
            default:
                break;
        }
    }

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int seekIndex = 0;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            seekIndex = progress;
            String progressStr = TimeUtil.intToStr(videoView.getDuration() * progress / 100);
            String durationStr = TimeUtil.intToStr(videoView.getDuration());
            timeTxt.setText(progressStr + "/" + durationStr);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (seekIndex != 0) {
                videoView.seekTo(videoView.getDuration() * seekIndex / 100);
            }
        }
    };

    /**
     * start record time
     */
    private void startTime() {
        handler.post(timeRunnable);
    }

    private Handler handler = new Handler();
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            if (videoState != VIDEO_STATE_IDLE) {
                int duration = videoView.getDuration();
                int progress = videoView.getCurrentPosition();
                if (duration != -1 && duration != 0 && screenType != SCREEN_SMALL) {
                    seekBar.setProgress(progress * 100 / duration);
                    timeTxt.setText(TimeUtil.intToStr(progress) + "/" + TimeUtil.intToStr(duration));
                }
                handler.postDelayed(timeRunnable, 1000);
            }
        }
    };

    /**
     * set video path
     *
     * @param videoPath
     */
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    /**
     * set listener
     */
    public void setMyVideoLayoutListener(MyVideoLayoutListener listener) {
        this.mListener = listener;
    }
}
