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
        return null == date ? "" : formatDate("yyyy-MM-dd HH:mm:ss", date);
    }

    public static CharSequence formatYMDHM(long millseconds) {
        return formatYM(new Date(millseconds));
    }

    public static CharSequence formatYMDHM(Date date) {
        return null == date ? "" : formatDate("yyyy-MM-dd HH:mm", date);
    }

    public static CharSequence formatYM(long millseconds) {
        return formatYM(new Date(millseconds));
    }

    public static CharSequence formatYM(Date date) {
        return null == date ? "" : formatDate("yyyy-MM", date);
    }

    public static CharSequence formatYMD(long millseconds) {
        return formatYMD(new Date(millseconds));
    }

    public static CharSequence formatYMD(Date date) {
        return null == date ? "" : formatDate("yyyy-MM-dd", date);
    }

    // base
    public static CharSequence formatDate(String format, long millseconds) {
        return millseconds <= 0 ? "" : DateFormat.format(format, millseconds);
    }

    public static CharSequence formatDate(String format, Date date) {
        return null == date ? "" : DateFormat.format(format, date);
    }

    public static String secondToTime(long time) {
        String timeStr;
        long hour;
        long minute;
        long second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Long.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
