package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/5/27on 16:55.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class JavaBaseBean {
    /**
     * code : 0
     * message : 成功
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
}
