package com.idogfooding.backbone.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;

/**
 * 系统分享工具
 *
 * @author Charles
 */
public class ShareUtils {

    public static void shareText(Context context, String text) {
        shareText(context, null, null, text);
    }

    /**
     * 分享文字
     */
    public static void shareText(Context context, String title, String subject, String text) {
        if (context == null || StringUtils.isEmpty(text)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (!StringUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        context.startActivity(StringUtils.isEmpty(title) ? intent : Intent.createChooser(intent, title));
    }

    public static void shareImage(Context context, Uri uri) {
        shareImage(context, null, null, null, uri);
    }

    /**
     * 分享图片
     */
    public static void shareImage(Context context, String title, String subject, String text, Uri uri) {
        if (context == null || ObjectUtils.isEmpty(uri)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        if (!StringUtils.isEmpty(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (!StringUtils.isEmpty(text)) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
        }
        context.startActivity(StringUtils.isEmpty(title) ? intent : Intent.createChooser(intent, title));
    }

    public void shareMultiImage(Context context, ArrayList<Uri> uriList) {
        shareMultiImage(context, null, uriList);
    }

    /**
     * 分享多图片
     */
    public void shareMultiImage(Context context, String title, ArrayList<Uri> uriList) {
        if (context == null || ObjectUtils.isEmpty(uriList)) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        intent.setType("image/*");
        context.startActivity(StringUtils.isEmpty(title) ? intent : Intent.createChooser(intent, title));
    }

}
