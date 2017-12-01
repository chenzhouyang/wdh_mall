package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class OrderDetailsEntity {
    /**
     * code : 0
     * message : 成功
     * data : {"id":2,"orderName":"T01","orderNo":"ZG_2017052512423356542","consumeTime":"","totalAmount":20300.5,"dispatchAmount":0,"fundAmount":0,"payAmount":0,"accPointsAmount":0,"extAmount":0,"payType":0,"payTime":"","refundTime":"","status":0,"poundage":0,"totalMaccount":2500.5,"userId":0,"userNickName":"","userMobile":"","createTime":"2017-05-25 12:42:34","updatedTime":"","relations":[{"goodId":121,"goodName":"测试哈哈哈","price":10000.5,"count":2},{"goodId":122,"goodName":"测试哈哈哈","price":1000.5,"count":3}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * id : 2
         * orderName : T01
         * orderNo : ZG_2017052512423356542
         * consumeTime :
         * totalAmount : 20300.5
         * dispatchAmount : 0.0
         * fundAmount : 0.0
         * payAmount : 0.0
         * accPointsAmount : 0.0
         * extAmount : 0.0
         * payType : 0
         * payTime :
         * refundTime :
         * status : 0
         * poundage : 0.0
         * totalMaccount : 2500.5
         * userId : 0
         * userNickName :
         * userMobile :
         * createTime : 2017-05-25 12:42:34
         * updatedTime :
         * relations : [{"goodId":121,"goodName":"测试哈哈哈","price":10000.5,"count":2},{"goodId":122,"goodName":"测试哈哈哈","price":1000.5,"count":3}]
         */

        @SerializedName("id")
        public int id;
        @SerializedName("orderName")
        public String orderName;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("consumeTime")
        public String consumeTime;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("dispatchAmount")
        public double dispatchAmount;
        @SerializedName("fundAmount")
        public double fundAmount;
        @SerializedName("payAmount")
        public double payAmount;
        @SerializedName("accPointsAmount")
        public double accPointsAmount;
        @SerializedName("extAmount")
        public double extAmount;
        @SerializedName("payType")
        public int payType;
        @SerializedName("payTime")
        public String payTime;
        @SerializedName("refundTime")
        public String refundTime;
        @SerializedName("status")
        public int status;
        @SerializedName("poundage")
        public double poundage;
        @SerializedName("totalMaccount")
        public double totalMaccount;
        @SerializedName("userId")
        public int userId;
        @SerializedName("userNickName")
        public String userNickName;
        @SerializedName("userMobile")
        public String userMobile;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("updatedTime")
        public String updatedTime;
        @SerializedName("relations")
        public List<RelationsBean> relations;

        public static class RelationsBean {
            /**
             * goodId : 121
             * goodName : 测试哈哈哈
             * price : 10000.5
             * count : 2
             */

            @SerializedName("goodId")
            public int goodId;
            @SerializedName("goodName")
            public String goodName;
            @SerializedName("price")
            public double price;
            @SerializedName("count")
            public int count;
        }
    }
}
