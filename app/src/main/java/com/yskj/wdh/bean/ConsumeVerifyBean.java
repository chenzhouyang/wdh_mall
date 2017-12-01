package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class ConsumeVerifyBean {


    /**
     * code : 0
     * message : 成功
     * data : {"orderId":18,"orderNo":"lf_1478670931950250","shopId":1,"shopName":"name","couponId":2,"consumePassword":"PZ6GS19IHRY8","status":1,"nickName":"","payAmount":0,"fundAmount":0,"accPointsAmount":0,"poundage":0,"createTime":"2016-11-09 13:55:32","timeoutTime":"2017-05-09 13:55:32","validityTime":"2016-11-10 14:30:51","refundTime":"","goods":[{"orderId":18,"shopId":1,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd","goodsName":"卖个球","price":15,"deduPoint":0,"count":1}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * orderId : 18
         * orderNo : lf_1478670931950250
         * shopId : 1
         * shopName : name
         * couponId : 2
         * consumePassword : PZ6GS19IHRY8
         * status : 1
         * nickName :
         * payAmount : 0.0
         * fundAmount : 0.0
         * accPointsAmount : 0.0
         * poundage : 0.0
         * createTime : 2016-11-09 13:55:32
         * timeoutTime : 2017-05-09 13:55:32
         * validityTime : 2016-11-10 14:30:51
         * refundTime :
         * goods : [{"orderId":18,"shopId":1,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd","goodsName":"卖个球","price":15,"deduPoint":0,"count":1}]
         */

        @SerializedName("orderId")
        public int orderId;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("shopId")
        public int shopId;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("couponId")
        public int couponId;
        @SerializedName("consumePassword")
        public String consumePassword;
        @SerializedName("status")
        public int status;
        @SerializedName("nickName")
        public String nickName;
        @SerializedName("payAmount")
        public double payAmount;
        @SerializedName("fundAmount")
        public double fundAmount;
        @SerializedName("accPointsAmount")
        public double accPointsAmount;
        @SerializedName("poundage")
        public double poundage;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("timeoutTime")
        public String timeoutTime;
        @SerializedName("validityTime")
        public String validityTime;
        @SerializedName("refundTime")
        public String refundTime;
        @SerializedName("goods")
        public ArrayList<GoodsBean> goods;

        public static class GoodsBean {
            /**
             * orderId : 18
             * shopId : 1
             * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd
             * goodsName : 卖个球
             * price : 15.0
             * deduPoint : 0.0
             * count : 1
             */

            @SerializedName("orderId")
            public int orderId;
            @SerializedName("shopId")
            public int shopId;
            @SerializedName("cover")
            public String cover;
            @SerializedName("goodsName")
            public String goodsName;
            @SerializedName("price")
            public double price;
            @SerializedName("deduPoint")
            public double deduPoint;
            @SerializedName("count")
            public int count;
        }
    }
}
