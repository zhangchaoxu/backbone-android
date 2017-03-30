package com.idogfooding.backbone.utils;

import android.text.TextUtils;

/**
 * StrKit
 *
 * @author Charles
 */
public class StrKit {

    /**
     * Returns true if the string is null or 0-length after trim
     */
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.toString().trim());
    }

    /**
     * Compares this string to the specified object.
     */
    public static boolean equals(String source, String key) {
        return source != null && source.equals(key);
    }

}
