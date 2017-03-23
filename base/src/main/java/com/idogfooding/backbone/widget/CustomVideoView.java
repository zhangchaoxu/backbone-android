package com.idogfooding.backbone.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * CustomVideoView
 *
 * @author Charles
 */
public class CustomVideoView extends VideoView {

    private int measuredWidth;
    private int measuredHeight;

    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getDefaultSize(measuredWidth, widthMeasureSpec);
        measuredHeight = getDefaultSize(measuredHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

}
