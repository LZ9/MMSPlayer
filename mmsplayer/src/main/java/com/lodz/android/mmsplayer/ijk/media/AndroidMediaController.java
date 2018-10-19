package com.lodz.android.mmsplayer.ijk.media;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.MediaController;

/**
 * 原生控制器
 * Created by zhouL on 2016/12/1.
 */
public class AndroidMediaController extends MediaController implements IMediaController {

    public AndroidMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AndroidMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
    }

    public AndroidMediaController(Context context) {
        super(context);
    }
}