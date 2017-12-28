package com.idogfooding.backbone.utils;

/**
 * DoubleClickExit
 *
 * @author Charles
 */
public class DoubleClickExit {

    public static long lastClick = 0L;
    private static final int THRESHOLD = 2000;// 1000ms

    public static boolean check() {
        return check(THRESHOLD);
    }

    public static boolean check(long threshold) {
        long now = System.currentTimeMillis();
        boolean b = now - lastClick < threshold;
        lastClick = now;
        return b;
    }
}
