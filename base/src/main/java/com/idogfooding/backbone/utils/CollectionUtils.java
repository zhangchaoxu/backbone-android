package com.idogfooding.backbone.utils;

import java.util.Collection;

/**
 * CollectionUtils
 *
 * @author Charles
 */
public class CollectionUtils {

    /**
     * 将字符串List转化为字符串，以分隔符间隔.
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

}
