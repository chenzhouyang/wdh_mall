package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/8/18on 15:49.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MillInfoBean {

    /**
     * code : 0
     * message : 成功
     * data : {"millId":1,"name":"ceshi","url":"KKKKK","content":"cesh","createTime":"2017-08-04 12:22:59","largeOrderLimit":1,"shopId":1,"shopName":"程序员之家","shopDetailAddress":"河南省许昌市许昌县芙蓉大道电子商务中心","totalProfit":11}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * millId : 1
         * name : ceshi
         * url : KKKKK
         * content : cesh
         * createTime : 2017-08-04 12:22:59
         * largeOrderLimit : 1
         * shopId : 1
         * shopName : 程序员之家
         * shopDetailAddress : 河南省许昌市许昌县芙蓉大道电子商务中心
         * totalProfit : 11
         */

        @SerializedName("millId")
        public int millId;
        @SerializedName("name")
        public String name;
        @SerializedName("url")
        public String url;
        @SerializedName("content")
        public String content;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("largeOrderLimit")
        public int largeOrderLimit;
        @SerializedName("shopId")
        public int shopId;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("shopDetailAddress")
        public String shopDetailAddress;
        @SerializedName("totalProfit")
        public double totalProfit;
    }
}
