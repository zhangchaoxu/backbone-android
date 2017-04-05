
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
    public static boolean isEmpty(CharSequence str) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str.toString().trim());
    }

    /**
     * Compares this string to the specified object.
     */
    public static boolean equals(String source, String key) {
        return source != null && source.equals(key);
    }

    /**
     * 是否大陆手机号码
     * 大陆手机号码11位数，匹配格式：前2位固定格式+后9位任意数
     */
    public static boolean isChinaMobilePhoneNumber(String number) {
        String regExp = "^(1[3-8])\\d{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(number);
        return m.matches();
    }

}
