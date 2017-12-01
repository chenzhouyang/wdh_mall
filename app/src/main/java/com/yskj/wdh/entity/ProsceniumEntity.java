package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class ProsceniumEntity {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":2,"orderName":"T01","orderNo":"ZG_2017052512423356542","consumeTime":"","totalAmount":20300.5,"extAmount":0,"payType":0,"payTime":"","refundTime":"","status":0,"poundage":0,"userId":0,"userNickName":"","userMobile":"","createTime":"2017-05-25 12:42:34","updatedTime":""}]}
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
         * count : 10
         * list : [{"id":2,"orderName":"T01","orderNo":"ZG_2017052512423356542","consumeTime":"","totalAmount":20300.5,"extAmount":0,"payType":0,"payTime":"","refundTime":"","status":0,"poundage":0,"userId":0,"userNickName":"","userMobile":"","createTime":"2017-05-25 12:42:34","updatedTime":""}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 2
             * orderName : T01
             * orderNo : ZG_2017052512423356542
             * consumeTime :
             * totalAmount : 20300.5
             * extAmount : 0.0
             * payType : 0
             * payTime :
             * refundTime :
             * status : 0
             * poundage : 0.0
             * userId : 0
             * userNickName :
             * userMobile :
             * createTime : 2017-05-25 12:42:34
             * updatedTime :
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
        }
    }
}
