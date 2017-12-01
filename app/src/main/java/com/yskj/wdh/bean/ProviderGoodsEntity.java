package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/11on 11:14.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ProviderGoodsEntity {

    /**
     * code : 0
     * message : 成功
     * data : [{"count":10,"cursor":0,"millId":1,"orderParam":null,"status":1,"index":null,"max":null,"goodid":13,"thumbnail":"C:/Users/July/Pictures/201708021752423364734.jpg","goodName":"测试商品20170804","shopPrice":800,"volume":0,"stock":246,"createTime":"2017-08-05 14:19:46"}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * count : 10
         * cursor : 0
         * millId : 1
         * orderParam : null
         * status : 1
         * index : null
         * max : null
         * goodid : 13
         * thumbnail : C:/Users/July/Pictures/201708021752423364734.jpg
         * goodName : 测试商品20170804
         * shopPrice : 800
         * volume : 0
         * stock : 246
         * createTime : 2017-08-05 14:19:46
         */

        @SerializedName("count")
        public int count;
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("millId")
        public String millId;
        @SerializedName("orderParam")
        public String orderParam;
        @SerializedName("status")
        public int status;
        @SerializedName("index")
        public int index;
        @SerializedName("max")
        public int max;
        @SerializedName("goodId")
        public String goodId;
        @SerializedName("thumbnail")
        public String thumbnail;
        @SerializedName("goodName")
        public String goodName;
        @SerializedName("shopPrice")
        public double shopPrice;
        @SerializedName("volume")
        public int volume;
        @SerializedName("stock")
        public int stock;
        @SerializedName("createTime")
        public String createTime;
    }
}
