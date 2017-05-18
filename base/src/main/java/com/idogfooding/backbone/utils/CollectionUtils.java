package com.idogfooding.backbone.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * CollectionUtils
 *
 * @author Charles
 */
public class CollectionUtils {

    /**
     * 将字符串集合转化为字符串，以分隔符间隔.
     *
     * @param list      需要处理的List.
     * @param separator 分隔符.
     * @return 转化后的字符串
     */
    public static String toString(Collection list, String separator) {
        if (null == list || list.isEmpty()) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object str : list) {
                sb.append(separator + str.toString());
            }
            return sb.deleteCharAt(0).toString();
        }
    }

    /**
     * 将string按separator分割成array
     * @param str
     * @param separator
     * @return
     */
    public static List<String> stringToArray(String str, String separator) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(str.split(separator, -1)));
        }
    }

}
