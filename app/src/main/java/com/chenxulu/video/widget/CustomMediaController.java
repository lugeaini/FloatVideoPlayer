package com.chenxulu.video.widget;

import android.content.Context;
import android.widget.MediaController;

public class CustomMediaController extends MediaController {
    private HideCallBack callBack;

    public CustomMediaController(Context context) {
        super(context);
    }

    @Override
    public void hide() {
        super.hide();
        if (callBack != null) {
            callBack.hide();
        }
    }

    @Override
    public void show(int timeout) {
        super.show(timeout);
        if (callBack != null) {
            callBack.show();
        }
    }

    public void setHideCallBack(HideCallBack hideCallBack) {
        callBack = hideCallBack;
    }

    public interface HideCallBack {
        void show();

        void hide();
    }
}
