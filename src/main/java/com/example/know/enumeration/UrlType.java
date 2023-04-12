package com.example.know.enumeration;

import org.apache.ibatis.annotations.Case;

public enum UrlType {


    USERIMG(0, "know\\src\\static\\author/"),
    POSTIMG(1, "know\\src\\static\\img\\post/"),
    PLATEIMG(2, "know\\src\\static\\img\\plate/"),
    ROTATIONCHART(3, "know\\src\\static\\img\\rotation/");

    private int type;
    private String url;

    UrlType(int type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}