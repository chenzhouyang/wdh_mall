package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 9:08.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MallOrderListBean {


    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":1,"orderList":[{"orderId":5,"orderNo":"zx2017081016232844824","shippingCode":"123456789","orderAmount":100,"shippingPrice":0,"accPointsAmount":null,"fundAmount":null,"millId":1,"millOrderGoodsVoList":[{"id":5,"orderId":5,"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"goodsPrice":100,"parameterId":1,"parameterName":"颜色：白色  尺寸：xl","promId":0,"mAccount":50}]}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * cursor : 0
         * count : 1
         * orderList : [{"orderId":5,"orderNo":"zx2017081016232844824","shippingCode":"123456789","orderAmount":100,"shippingPrice":0,"accPointsAmount":null,"fundAmount":null,"millId":1,"millOrderGoodsVoList":[{"id":5,"orderId":5,"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"goodsPrice":100,"parameterId":1,"parameterName":"颜色：白色  尺寸：xl","promId":0,"mAccount":50}]}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("orderList")
        public ArrayList<OrderListBean> orderList;

        public static class OrderListBean {
            /**
             * orderId : 5
             * orderNo : zx2017081016232844824
             * shippingCode : 123456789
             * orderAmount : 100
             * shippingPrice : 0
             * accPointsAmount : null
             * fundAmount : null
             * millId : 1
             * millOrderGoodsVoList : [{"id":5,"orderId":5,"goodsId":1,"goodsName":"好看的运动裤","goodsCount":1,"goodsPrice":100,"parameterId":1,"parameterName":"颜色：白色  尺寸：xl","promId":0,"mAccount":50}]
             */

            @SerializedName("orderId")
            public String orderId;
            @SerializedName("orderNo")
            public String orderNo;
            @SerializedName("shippingCode")
            public String shippingCode;
            @SerializedName("orderAmount")
            public double orderAmount;
            @SerializedName("shippingPrice")
            public double shippingPrice;
            @SerializedName("accPointsAmount")
            public double accPointsAmount;
            @SerializedName("fundAmount")
            public double fundAmount;
            @SerializedName("millId")
            public int millId;
            @SerializedName("millOrderGoodsVoList")
            public ArrayList<MillOrderGoodsVoListBean> millOrderGoodsVoList;

            public static class MillOrderGoodsVoListBean {
                /**
                 * id : 5
                 * orderId : 5
                 * goodsId : 1
                 * goodsName : 好看的运动裤
                 * goodsCount : 1
                 * goodsPrice : 100
                 * parameterId : 1
                 * parameterName : 颜色：白色  尺寸：xl
                 * promId : 0
                 * mAccount : 50
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
                public double mAccount;
                @SerializedName("cover")
                public String cover;

            }
        }
    }
}
