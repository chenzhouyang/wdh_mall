package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/5/31on 16:55.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ShareGoodsInfoBean {


    /**
     * code : 0
     * message : 成功
     * data : {"goodId":121,"goodName":"测试哈哈哈","price":100,"address":"河南省许昌市魏都区新东街与紫云路交叉口森林半岛","mobile":"18738120620","shopName":"西瓜专卖店","userName":"我是谁1111111","profile":"简介哈哈哈","cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=111111111"}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * goodId : 121
         * goodName : 测试哈哈哈
         * price : 100
         * address : 河南省许昌市魏都区新东街与紫云路交叉口森林半岛
         * mobile : 18738120620
         * shopName : 西瓜专卖店
         * userName : 我是谁1111111
         * profile : 简介哈哈哈
         * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=111111111
         */

        @SerializedName("goodId")
        public int goodId;
        @SerializedName("goodName")
        public String goodName;
        @SerializedName("price")
        public double price;
        @SerializedName("address")
        public String address;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("userName")
        public String userName;
        @SerializedName("profile")
        public String profile;
        @SerializedName("cover")
        public String cover;
    }
}
