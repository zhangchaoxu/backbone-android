package com.idogfooding.backbone.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.blankj.utilcode.util.ToastUtils;

/**
 * 地图工具
 *
 * @author Charles
 */
public class MapUtils {

    /**
     * 调用原生地图工具导航到某个地址
     * @param context
     * @param name
     * @param lat
     * @param lng
     */
    public static void goNaviMap(Context context, String name, double lat, double lng) {
        new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                .setTitle("导航")
                .setCancelable(true)
                .setItems(new String[]{"百度地图", "高德地图"}, (dialog, which) -> {
                    if (0 == which) {
                        // 百度地图
                        try {
                            Intent intent = new Intent();
                            intent.setData(Uri.parse("baidumap://map/direction?coord_type=gcj02"
                                    + "&src=idogfooding|exchange"
                                    + "&origin="
                                    + "&destination=name:" + name
                                    + "|latlng:" + lat + "," + lng));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showLong("请确认是否安装百度地图");
                        }
                    } else {
                        // 高德地图
                        try {
                            Intent intent = new Intent("android.intent.action.VIEW",
                                    Uri.parse("androidamap://navi?sourceApplication=exchange&poiname="
                                            + name
                                            + "&lat=" + lat
                                            + "&lon=" + lng + "&dev=1&style=0"));
                            intent.setPackage("com.autonavi.minimap");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ToastUtils.showLong("请确认是否安装高德地图");
                        }
                    }
                }).show();
    }

}
