package com.yskj.wdh.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 创建日期 2017/8/3on 14:23.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ProviderGoodsInfoBean implements Serializable{
    private int type;//判断添加类型  1、插入文字  2、插入图片
    private String content;
    private Bitmap bitmap;
    private String data;//上传图片后返回的图片id

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
