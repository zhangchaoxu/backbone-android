package com.idogfooding.backbone.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
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
        ta.recycle();
    }

    private void setup() {
        LayoutInflater.from(mContext).inflate(layoutId, this, true);
        titleTextView = (TextView) findViewById(R.id.toolbar_title);
        iconLeftImageView = (ImageView) findViewById(R.id.toolbar_left);
        iconRightImageView = (ImageView) findViewById(R.id.toolbar_right);
        shadowView = findViewById(R.id.toolbar_shadow);
        statusBar = findViewById(R.id.toolbar_status_bar);

        // title
        if (titleTextView != null) {
            if (StrKit.isEmpty(title)) {
                titleTextView.setVisibility(View.GONE);
            } else {
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(title);
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
        }

        // status bar
        if (null != statusBar) {
            ViewGroup.LayoutParams params = statusBar.getLayoutParams();
            params.height = StatusBarUtils.getStatusBarHeight(getContext());
            statusBar.setLayoutParams(params);
        }
    }

    public FakeToolbar setTitle(CharSequence string) {
        titleTextView.setVisibility(View.VISIBLE);
        titleTextView.setText(string);
        return this;
    }

}
