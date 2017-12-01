package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 14:04.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MallOrderDetailBean {

    /**
     * code : 0
     * message : 成功
     * data : {"orderId":1,"orderNo":"2017080511122212","shippingStatus":1,"payStatus":1,"consignee":"诗诗涵","address":"河南省许昌市","mobile":"13569457589","shippingCode":"20170805112212","totalPrice":120,"shippingPrice":10,"accPointsAmount":2,"fundAmount":100,"totalAmount":130,"orderAmount":120,"createTime":"2017-08-05 08:58:00","shippingTime":"2017-08-05 09:23:23","confirmTime":"2017-08-05 08:57:49","payTime":"2017-08-05 08:59:31","orderPromAmount":null,"totalMaccount":5,"seller":1,"millOrderGoodsVoList":[{"id":1,"orderId":1,"goodsId":1,"goodsName":"好看的运动裤","goodsCount":2,"goodsPrice":10,"parameterId":1,"parameterName":"颜色：白色  尺寸：xl","promId":0,"mAccount":0}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * orderId : 1
         * orderNo : 2017080511122212
         * shippingStatus : 1
         * payStatus : 1
         * consignee : 诗诗涵
         * address : 河南省许昌市
         * mobile : 13569457589
         * shippingCode : 20170805112212
         * totalPrice : 120
         * shippingPrice : 10
         * accPointsAmount : 2
         * fundAmount : 100
         * totalAmount : 130
         * orderAmount : 120
         * createTime : 2017-08-05 08:58:00
         * shippingTime : 2017-08-05 09:23:23
         * confirmTime : 2017-08-05 08:57:49
         * payTime : 2017-08-05 08:59:31
         * orderPromAmount : null
         * totalMaccount : 5
         * seller : 1
         * millOrderGoodsVoList : [{"id":1,"orderId":1,"goodsId":1,"goodsName":"好看的运动裤","goodsCount":2,"goodsPrice":10,"parameterId":1,"parameterName":"颜色：白色  尺寸：xl","promId":0,"mAccount":0}]
         */

        @SerializedName("orderId")
        public int orderId;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("shippingStatus")
        public int shippingStatus;
        @SerializedName("payStatus")
        public int payStatus;
        @SerializedName("consignee")
        public String consignee;
        @SerializedName("address")
        public String address;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("shippingName")
        public String shippingName;
        @SerializedName("shippingCode")
        public String shippingCode;
        @SerializedName("totalPrice")
        public double totalPrice;
        @SerializedName("shippingPrice")
        public double shippingPrice;
        @SerializedName("accPointsAmount")
        public double accPointsAmount;
        @SerializedName("fundAmount")
        public double fundAmount;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("orderAmount")
        public double orderAmount;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("shippingTime")
        public String shippingTime;
        @SerializedName("confirmTime")
        public String confirmTime;
        @SerializedName("payTime")
        public String payTime;
        @SerializedName("orderPromAmount")
        public double orderPromAmount;
        @SerializedName("totalMaccount")
        public int totalMaccount;
        @SerializedName("seller")
        public int seller;
        @SerializedName("millOrderGoodsVoList")
        public ArrayList<MillOrderGoodsVoListBean> millOrderGoodsVoList;

        public static class MillOrderGoodsVoListBean {
            /**
             * id : 1
             * orderId : 1
             * goodsId : 1
             * goodsName : 好看的运动裤
             * goodsCount : 2
             * goodsPrice : 10
             * parameterId : 1
             * parameterName : 颜色：白色  尺寸：xl
             * promId : 0
             * mAccount : 0
             */

            @SerializedName("id")
            public int id;
            @SerializedName("orderId")
            public int orderId;
            @SerializedName("goodsId")
            public int goodsId;
            @SerializedName("goodsName")
            public String goodsName;
            @SerializedName("goodsCount")
            public int goodsCount;
            @SerializedName("goodsPrice")
            public double goodsPrice;
            @SerializedName("parameterId")
            public int parameterId;
            @SerializedName("parameterName")
            public String parameterName;
            @SerializedName("promId")
            public int promId;
            @SerializedName("mAccount")
            public int mAccount;
            @SerializedName("cover")
            public String cover;

        }
    }
}
