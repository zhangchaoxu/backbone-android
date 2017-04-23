package com.idogfooding.backbone.browser;

/**
 * BrowserParam
 *
 * @author Charles
*/
public class BrowserParam implements java.io.Serializable {

    private String title;
    private String url;

    public BrowserParam() {
    }

    public BrowserParam(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
