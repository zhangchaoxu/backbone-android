package com.idogfooding.backbone.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * BrowserUtils
 *
 * @author Charles
 */
public class BrowserUtils {

    public static void openUri(Context context, String uri) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(uri));
        context.startActivity(intent);
    }
}
