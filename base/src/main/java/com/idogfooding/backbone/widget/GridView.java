package com.idogfooding.backbone.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * GridView
 *
 * @author Charles
 */
public class GridView extends android.widget.GridView {

    public GridView(Context context) {
        super(context);
    }

    public GridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    @Override
    public int getNumColumns() {
        return super.getNumColumns();
    }
}