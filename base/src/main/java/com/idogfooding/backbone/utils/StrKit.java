
package com.idogfooding.backbone.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StrKit
 *
 * @author Charles
 */
public class StrKit {

    /**
     * Returns true if the string is null or 0-length after trim
     */
    public static boolean isEmpty(CharSequence source) {
        return TextUtils.isEmpty(source) || TextUtils.isEmpty(source.toString().trim());
    }

    /**
     * Compares this string to the specified object.
     */
    public static boolean equals(CharSequence source, CharSequence key) {
        return source != null && source.equals(key);
    }

    /**
     * is china mobile phone number
     */
    public static boolean isChinaMobilePhoneNumber(CharSequence source) {
        String regExp = "^(1[3-8])\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * match length
     * @param source
     * @param minLength
     * @param maxLength
     * @return
     */
    public static boolean matchLength(CharSequence source, int minLength, int maxLength) {
        if (null == source) {
            return false;
        } else {
            return source.length() >= minLength && source.length() <= maxLength;
        }
    }



}
