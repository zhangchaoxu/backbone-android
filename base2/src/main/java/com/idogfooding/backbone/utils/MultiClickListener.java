package com.idogfooding.backbone.utils;

import android.os.SystemClock;
import android.view.View;

/**
 * 连续点击事件
 *
 * @author Charles
 */
public class MultiClickListener implements View.OnClickListener {

    private int counts = 5;//点击次数
    private long duration = 3 * 1000;//规定有效时间
    private long[] mHits = new long[counts];
    private View.OnClickListener onClickListener; // 点击事件

    public MultiClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public MultiClickListener(int counts, long duration, View.OnClickListener onClickListener) {
        this.counts = counts;
        this.duration = duration;
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        // 实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - duration)) {
            onClickListener.onClick(v);
        }
    }

    /**
     * 设置点击次数
     * @param counts
     */
    public void setCounts(int counts) {
        this.counts = counts;
        this.mHits = new long[counts];
    }

    /**
     * 设置点击间隔
     * @param duration
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }
}