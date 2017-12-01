package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yzw on 2016/11/9.
 */
public class PurchaseNoteVo {
    @SerializedName("key")
    public int key;
    @SerializedName("type")
    public int type;
    @SerializedName("val")
    public String val;
    @SerializedName("title")
    public String title;
    @SerializedName("content")
    public String content;

    public PurchaseNoteVo() {
    }

    public PurchaseNoteVo(Integer key, Integer type, String val, String title, String content) {
        this.key = key;
        this.type = type;
        this.val = val;
        this.title = title;
        this.content = content;
    }


}
