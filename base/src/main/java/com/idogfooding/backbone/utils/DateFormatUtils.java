package com.idogfooding.backbone.utils;

import android.text.format.DateFormat;

import java.util.Date;

/**
 * DateFormatUtils
 *
 * @author Charles
 */
public class DateFormatUtils {

    public static CharSequence formatYMDHMS(long millseconds) {
        return formatYMDHMS(new Date(millseconds));
    }

    public static CharSequence formatYMDHMS(Date date) {
        return null == date ? "" : formatDate("yyyy.MM.dd kk:mm:ss", date);
    }

    public static CharSequence formatYMDHM(long millseconds) {
        return formatYM(new Date(millseconds));
    }

    public static CharSequence formatYMDHM(Date date) {
        return null == date ? "" : formatDate("yyyy.MM.dd kk:mm", date);
    }

    public static CharSequence formatYM(long millseconds) {
        return formatYM(new Date(millseconds));
    }

    public static CharSequence formatYM(Date date) {
        return null == date ? "" : formatDate("yyyy.MM", date);
    }

    public static CharSequence formatYMD(long millseconds) {
        return formatYMD(new Date(millseconds));
    }

    public static CharSequence formatYMD(Date date) {
        return null == date ? "" : formatDate("yyyy.MM.dd", date);
    }

    // base
    public static CharSequence formatDate(String format, long millseconds) {
        return millseconds <= 0 ? "" : DateFormat.format(format, millseconds);
    }

    public static CharSequence formatDate(String format, Date date) {
        return null == date ? "" : DateFormat.format(format, date);
    }
}
