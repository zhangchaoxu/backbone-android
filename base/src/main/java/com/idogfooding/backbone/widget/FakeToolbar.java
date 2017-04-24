package com.idogfooding.backbone.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idogfooding.backbone.R;
import com.idogfooding.backbone.utils.StrKit;

/**
 * FakeToolbar
 * 伪造的仿ios toolbar, 标题居中，有layout构建
 *
 * @author Charles
 */
public class FakeToolbar extends LinearLayout {

    Context mContext;

    TextView titleLeftTextView;
    TextView titleRightTextView;
    TextView titleTextView;
    ImageView iconLeftImageView;
    ImageView iconRightImageView;
    View shadowView;

    Drawable iconLeftDrawable;
    Drawable iconRightDrawable;
    CharSequence title;
    CharSequence titleLeft;
    CharSequence titleRight;
    boolean shadow;
    int layoutId;

    public FakeToolbar(Context context) {
        this(context, null);
    }

    public FakeToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FakeToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        getAttr(attrs, defStyleAttr);
        init();
    }

    private void getAttr(AttributeSet attrs,int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FakeToolbar, defStyleAttr, 0);
        layoutId = ta.getResourceId(R.styleable.FakeToolbar_fb_layout, R.layout.fake_toolbar);
        title = ta.getString(R.styleable.FakeToolbar_fb_title);
        titleLeft = ta.getString(R.styleable.FakeToolbar_fb_title_left);
        titleRight = ta.getString(R.styleable.FakeToolbar_fb_title_right);
        iconLeftDrawable = ta.getDrawable(R.styleable.FakeToolbar_fb_icon_left);
        iconRightDrawable = ta.getDrawable(R.styleable.FakeToolbar_fb_icon_right);
        shadow = ta.getBoolean(R.styleable.FakeToolbar_fb_shadow, false);
        ta.recycle();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(layoutId, this, true);
        titleTextView = (TextView) findViewById(R.id.toolbar_title);
        iconLeftImageView = (ImageView) findViewById(R.id.toolbar_left);
        iconRightImageView = (ImageView) findViewById(R.id.toolbar_right);
        shadowView = findViewById(R.id.toolbar_shadow);

        // title
        if (StrKit.isEmpty(title)) {
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
        // left icon
        if (null == iconLeftDrawable) {
            iconLeftImageView.setVisibility(View.GONE);
        } else {
            iconLeftImageView.setVisibility(View.VISIBLE);
            iconLeftImageView.setImageDrawable(iconLeftDrawable);
        }
        // right icon
        if (null == iconRightDrawable) {
            iconRightImageView.setVisibility(View.GONE);
        } else {
            iconRightImageView.setVisibility(View.VISIBLE);
            iconRightImageView.setImageDrawable(iconRightDrawable);
        }
        // shadow
        shadowView.setVisibility(shadow ? View.VISIBLE : View.GONE);
    }

    public FakeToolbar setTitle(CharSequence string) {
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(string);
        return this;
    }

}
