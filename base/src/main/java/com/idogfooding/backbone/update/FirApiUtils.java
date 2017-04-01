package com.idogfooding.backbone.update;

/**
 * FirApiUtils
 * see {https://fir.im/docs/version_detection}
 *
 * @author Charles
 */

public class FirApiUtils {

    public static String getLatestApi() {
        String id = "";
        String token = "";
        return getLatestApi(id, token);
    }

    public static String getLatestApi(String id, String token) {
        return String.format("http://api.fir.im/apps/latest/%s?api_token=%s", id, token);
    }

}
