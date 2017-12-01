package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/8/14on 8:56.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MallOrderListCountBean {

    /**
     * code : 0
     * message : 成功
     * data : {"toSendCount":1,"confirmCount":2,"shippedCount":1}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * toSendCount : 1
         * confirmCount : 2
         * shippedCount : 1
         */

        @SerializedName("toSendCount")
        public int toSendCount;
        @SerializedName("confirmCount")
        public int confirmCount;
        @SerializedName("shippedCount")
        public int shippedCount;
    }
}
