package com.idogfooding.backbone.update;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ezy.boost.update.UpdateInfo;

/**
 * UpdateInfoTransUtils
 *
 * @author Charles
 */

public class UpdateInfoTransUtils {

    public static UpdateInfo transFirUpdateInfoToUpdateInfo(String firUpdateInfoStr) {
        UpdateInfo info = new UpdateInfo();
        if (TextUtils.isEmpty(firUpdateInfoStr))
            return info;

        Gson gson = new Gson();
        FirUpdateInfo firUpdateInfo;
        try {
            firUpdateInfo = gson.fromJson(firUpdateInfoStr, FirUpdateInfo.class);
        } catch (JsonSyntaxException jse) {
            return info;
        }
        info = transFirUpdateInfoToUpdateInfo(firUpdateInfo);
        return info;
    }

    public static UpdateInfo transFirUpdateInfoToUpdateInfo(FirUpdateInfo firUpdateInfo) {
        UpdateInfo info = new UpdateInfo();
        if (null == firUpdateInfo)
            return info;

        info.hasUpdate = true;
        info.url = firUpdateInfo.getInstall_url();
        info.md5 = "609fed2379377735f45eed5b324ee933";
        info.versionCode = firUpdateInfo.getBuild();
        info.versionName = firUpdateInfo.getVersion();
        info.updateContent = firUpdateInfo.getChangelog();
        info.size = firUpdateInfo.getBinary().getFsize();
        return info;
    }

}
