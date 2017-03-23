package com.idogfooding.backbone.ui.component;

import android.view.View;

/**
 * UIComponent
 */

public interface UIComponent {

    /**
     * @param containerView base container of user layout, usually use $(BASE_CONTAINER_ID)
     */
    void inflate(View containerView);

    String getTag();

    <T extends UIComponent> void loadConfig(T component);
}
