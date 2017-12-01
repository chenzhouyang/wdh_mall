package com.yskj.wdh.bean;

import java.io.Serializable;

/**
 * Created by liuchaoya on 2016/11/14.
 * For yskj
 * Project Name : LSK
 */

public class PurchaseNoteBean implements Serializable{

    /**
     * key : 1000
     * type : 0
     * val :
     * title : 是否支持极速退款
     * content : 否
     */

    private int key;
    private int type;
    private String val;
    private String title;
    private String content;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
