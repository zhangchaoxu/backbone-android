package com.idogfooding.backbone.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.idogfooding.backbone.R;
import com.idogfooding.backbone.statusbar.StatusBarUtils;
import com.idogfooding.backbone.utils.StrKit;

/**
 * FakeToolbar
 * Toolbar of Android is so hard to use, especially in fragment
 * fake ios style toolbar
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
    View statusBar;
    View toolbar;

    Drawable iconLeftDrawable;
    Drawable iconRightDrawable;
    CharSequence title;
    CharSequence titleLeft;
    CharSequence titleRight;
    boolean shadow;
    int shadowColor;
    int toolbarColor;
    int titleColor;
    int statusBarColor;
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
        setup();
    }

    private void getAttr(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.FakeToolbar, defStyleAttr, 0);
        layoutId = ta.getResourceId(R.styleable.FakeToolbar_fb_layout, R.layout.fake_toolbar);
        title = ta.getString(R.styleable.FakeToolbar_fb_title);
        titleLeft = ta.getString(R.styleable.FakeToolbar_fb_title_left);
        titleRight = ta.getString(R.styleable.FakeToolbar_fb_title_right);
        iconLeftDrawable = ta.getDrawable(R.styleable.FakeToolbar_fb_icon_left);
        iconRightDrawable = ta.getDrawable(R.styleable.FakeToolbar_fb_icon_right);
        shadow = ta.getBoolean(R.styleable.FakeToolbar_fb_shadow, false);
        // color
        shadowColor = ta.getColor(R.styleable.FakeToolbar_fb_shadow_color, getResources().getColor(R.color.divider));
        toolbarColor = ta.getColor(R.styleable.FakeToolbar_fb_toolbar_color, getResources().getColor(R.color.primary));
        statusBarColor = ta.getColor(R.styleable.FakeToolbar_fb_status_bar_color, getResources().getColor(R.color.primary_dark));
        titleColor = ta.getColor(R.styleable.FakeToolbar_fb_title_color, getResources().getColor(R.color.text));
        ta.recycle();
    }

    private void setup() {
        LayoutInflater.from(mContext).inflate(layoutId, this, true);
        titleTextView = (TextView) findViewById(R.id.toolbar_title);
        iconLeftImageView = (ImageView) findViewById(R.id.toolbar_left);
        iconRightImageView = (ImageView) findViewById(R.id.toolbar_right);
        toolbar = findViewById(R.id.toolbar);
        shadowView = findViewById(R.id.toolbar_shadow);
        statusBar = findViewById(R.id.toolbar_status_bar);

        // toolbar
        if (toolbar != null) {
            toolbar.setBackgroundColor(toolbarColor);
        }

        // title
        if (titleTextView != null) {
            if (StrKit.isEmpty(title)) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(title);
                titleTextView.setTextColor(titleColor);
            }
        }

        // left icon
        if (iconLeftImageView != null) {
            if (null == iconLeftDrawable) {
                iconLeftImageView.setVisibility(View.GONE);
            } else {
                iconLeftImageView.setVisibility(View.VISIBLE);
                iconLeftImageView.setImageDrawable(iconLeftDrawable);
            }
        }

        // right icon
        if (null != iconRightImageView) {
            if (null == iconRightDrawable) {
                iconRightImageView.setVisibility(View.GONE);
            } else {
                iconRightImageView.setVisibility(View.VISIBLE);
                iconRightImageView.setImageDrawable(iconRightDrawable);
            }
        }

        // shadow
        if (null != shadowView) {
            shadowView.setVisibility(shadow ? View.VISIBLE : View.GONE);
            shadowView.setBackgroundColor(shadowColor);
        }

        // status bar
        if (null != statusBar) {
            ViewGroup.LayoutParams params = statusBar.getLayoutParams();
            params.height = StatusBarUtils.getStatusBarHeight(getContext());
            statusBar.setLayoutParams(params);
            statusBar.setBackgroundColor(statusBarColor);
        }
    }

    public FakeToolbar setTitle(CharSequence string) {
        if (null != titleTextView && !TextUtils.isEmpty(string)) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(string);
        }
        return this;
    }

}
