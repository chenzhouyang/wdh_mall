package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class RevenueEntiity {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"id":"58fdd652369d680001e1b4c7","amount":100000,"profitType":1041,"createTime":"2017-04-06 16:03:50","userId":3,"fromId":0,"note":""},{"id":"58fdd652369d680001e1b475","amount":12.18,"profitType":1017,"createTime":"2017-04-06 09:54:57","userId":3,"fromId":232,"note":""}]}
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
         * list : [{"id":"58fdd652369d680001e1b4c7","amount":100000,"profitType":1041,"createTime":"2017-04-06 16:03:50","userId":3,"fromId":0,"note":""},{"id":"58fdd652369d680001e1b475","amount":12.18,"profitType":1017,"createTime":"2017-04-06 09:54:57","userId":3,"fromId":232,"note":""}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public ArrayList<ListBean> list;

        public static class ListBean {
            /**
             * id : 58fdd652369d680001e1b4c7
             * amount : 100000.0
             * profitType : 1041
             * createTime : 2017-04-06 16:03:50
             * userId : 3
             * fromId : 0
             * note :
             */

            @SerializedName("id")
            public String id;
            @SerializedName("amount")
            public double amount;
            @SerializedName("profitType")
            public int profitType;
            @SerializedName("createTime")
            public String createTime;
            @SerializedName("userId")
            public int userId;
            @SerializedName("fromId")
            public int fromId;
            @SerializedName("note")
            public String note;
        }
    }
}
