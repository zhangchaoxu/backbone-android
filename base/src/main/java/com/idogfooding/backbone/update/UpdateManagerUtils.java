package com.idogfooding.backbone.update;

import android.content.Context;

import ezy.boost.update.IUpdateParser;
import ezy.boost.update.UpdateManager;

/**
 * UpdateManagerUtils
 *
 * @author Charles
 */
public class UpdateManagerUtils {

    public static void check(Context context, String checkUrl, IUpdateParser parser, boolean manual) {
        UpdateManager.setDebuggable(true);
        UpdateManager.setWifiOnly(false);

        UpdateManager.create(context).setUrl(checkUrl).setManual(manual).setNotifyId(999).setParser(parser).check();
    }
}
