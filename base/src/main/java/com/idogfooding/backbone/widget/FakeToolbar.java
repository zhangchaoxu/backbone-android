package com.idogfooding.backbone.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * FakeToolbar
 * 伪造的仿ios toolbar, 标题居中，有layout构建
 *
 * @author Charles
 */
public class FakeToolbar extends RelativeLayout {

    Drawable iconLeft;
    Drawable iconRight;

    TextView row_title;
    String title;

    TextView row_subtitle;
    String subtitle;

    View row_arrow;
    boolean arrowVisible;

    CheckBox row_checkbox;
    boolean checkboxVisible;

    int layoutId;

    public FakeToolbar(Context context) {
        this(context, null);
    }

    public FakeToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

       /* TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FakeToolbar, defStyleAttr, 0);
        layoutId = ta.getResourceId(R.styleable.FakeToolbar_fb_layout, R.layout.fake_toolbar);
        title = ta.getString(R.styleable.FakeToolbar_fb_title);
        subtitle = ta.getString(R.styleable.FakeToolbar_fb_subtitle);
        iconLeft = ta.getDrawable(R.styleable.FakeToolbar_fb_icon_left);
        ta.recycle();

        LayoutInflater.from(context).inflate(layoutId, this, true);
        row_icon = findViewById(R.id.row_icon);
        row_title = (TextView) findViewById(R.id.row_title);
        row_subtitle = (TextView) findViewById(R.id.row_subtitle);
        row_arrow = findViewById(R.id.row_arrow);
        row_checkbox = (CheckBox) findViewById(R.id.row_checkbox);
        initViews();*/
    }

}
