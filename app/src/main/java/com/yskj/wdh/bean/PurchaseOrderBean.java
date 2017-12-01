package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/26on 9:19.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PurchaseOrderBean {
    /**
     * code : 0
     * message : 成功
     * data : [{"orderId":3,"orderNo":"2017052619572355665","buyerName":"程序员之家","sellerName":"最牛逼的商铺没有之一","totalAmount":5502.5,"status":2,"type":1,"createTime":"2017-05-26 19:57:24","buyerMobile":"","sellerMobile":"","buyerOwner":"","sellerOwner":"","buyerAddress":"","sellerAddress":"","lpgList":[{"orderId":3,"goodId":3,"goodName":"Asd","price":600,"count":5},{"orderId":3,"goodId":4,"goodName":"直供商品111","price":500.5,"count":5}]},{"orderId":2,"orderNo":"2017052619475099027","buyerName":"程序员之家","sellerName":"最牛逼的商铺没有之一","totalAmount":3000,"status":2,"type":1,"createTime":"2017-05-26 19:47:50","buyerMobile":"","sellerMobile":"","buyerOwner":"","sellerOwner":"","buyerAddress":"","sellerAddress":"","lpgList":[{"orderId":2,"goodId":2,"goodName":"Asd","price":600,"count":5}]},{"orderId":1,"orderNo":"2017052619425971669","buyerName":"程序员之家","sellerName":"最牛逼的商铺没有之一","totalAmount":1200,"status":2,"type":1,"createTime":"2017-05-26 19:43:00","buyerMobile":"","sellerMobile":"","buyerOwner":"","sellerOwner":"","buyerAddress":"","sellerAddress":"","lpgList":[{"orderId":1,"goodId":1,"goodName":"Asd","price":600,"count":2}]}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * orderId : 3
         * orderNo : 2017052619572355665
         * buyerName : 程序员之家
         * sellerName : 最牛逼的商铺没有之一
         * totalAmount : 5502.5
         * status : 2  //0：草稿；1：待审批；2：审批通过；3：审批不通过；4：取消订单；5：废弃
         * type : 1
         * createTime : 2017-05-26 19:57:24
         * buyerMobile :
         * sellerMobile :
         * buyerOwner :
         * sellerOwner :
         * buyerAddress :
         * sellerAddress :
         * lpgList : [{"orderId":3,"goodId":3,"goodName":"Asd","price":600,"count":5},{"orderId":3,"goodId":4,"goodName":"直供商品111","price":500.5,"count":5}]
         */

        @SerializedName("orderId")
        public String orderId;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("buyerName")
        public String buyerName;
        @SerializedName("sellerName")
        public String sellerName;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("status")
        public int status;
        @SerializedName("type")
        public int type;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("buyerMobile")
        public String buyerMobile;
        @SerializedName("sellerMobile")
        public String sellerMobile;
        @SerializedName("buyerOwner")
        public String buyerOwner;
        @SerializedName("sellerOwner")
        public String sellerOwner;
        @SerializedName("buyerAddress")
        public String buyerAddress;
        @SerializedName("sellerAddress")
        public String sellerAddress;
        @SerializedName("lpgList")
        public ArrayList<LpgListBean> lpgList;

        public static class LpgListBean {
            /**
             * orderId : 3
             * goodId : 3
             * goodName : Asd
             * price : 600
             * count : 5
             */

            @SerializedName("orderId")
            public int orderId;
            @SerializedName("goodId")
            public int goodId;
            @SerializedName("goodName")
            public String goodName;
            @SerializedName("price")
            public double price;
            @SerializedName("count")
            public int count;
            @SerializedName("cover")
            public String cover;
        }
    }
}
