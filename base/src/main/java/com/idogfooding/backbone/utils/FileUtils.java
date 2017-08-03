package com.idogfooding.backbone.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * FileUtils
 *
 * @author Charles
 */
public class FileUtils {

    /**
     * format file size
     *
     * @param size size
     * @return size
     */
    public static String formatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static boolean existInAssets(Context context, String filename) {
        return existInAssets(context, "", filename);
    }

    public static boolean existInAssets(Context context, String path, String filename) {
        AssetManager am = context.getAssets();
        try {
            String[] names = am.list(path);
            for (String name : names) {
                if (name.equals(filename.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
