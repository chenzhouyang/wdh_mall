package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/5/27.
 */

public class CaptureCollectEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"goodName":"直供商品111","price":500.5,"goodId":44}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * goodName : 直供商品111
         * price : 500.5
         * goodId : 44
         */

        @SerializedName("goodName")
        public String goodName;
        @SerializedName("price")
        public double price;
        @SerializedName("goodId")
        public int goodId;
    }
}
