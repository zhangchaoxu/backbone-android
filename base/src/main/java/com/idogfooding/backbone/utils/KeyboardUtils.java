package com.idogfooding.backbone.utils;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Keyboard utilities
 *
 * @author Kevin Sawicki <kevinsawicki@gmail.com>
 * @author Charles <zhangchaoxu@gmail.com>
 */
public class KeyboardUtils {

    /**
     * Hide soft input method manager
     *
     * @param view
     * @return view
     */
    public static View hideSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        return view;
    }

    /**
     * Show soft input method manager
     *
     * @param view
     * @return view
     */
    public static View showSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null)
            manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        view.requestFocus();
        return view;
    }
}
