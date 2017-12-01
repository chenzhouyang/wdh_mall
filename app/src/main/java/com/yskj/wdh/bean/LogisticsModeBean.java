package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/23on 10:38.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class LogisticsModeBean {

    /**
     * code : 0
     * message : 成功
     * data : [{"count":10,"cursor":0,"id":2,"millId":1,"type":2,"name":"不发货地区模板2","areaUntransportList":null,"customTransportList":null},{"count":10,"cursor":0,"id":3,"millId":1,"type":3,"name":"自定义地区模板","areaUntransportList":null,"customTransportList":null},{"count":10,"cursor":0,"id":11,"millId":1,"type":1,"name":"全国包邮","areaUntransportList":null,"customTransportList":null},{"count":10,"cursor":0,"id":12,"millId":1,"type":2,"name":"不发货地区模板","areaUntransportList":null,"customTransportList":null},{"count":10,"cursor":0,"id":13,"millId":1,"type":3,"name":"自定义地区模板","areaUntransportList":null,"customTransportList":null}]
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
         * id : 2
         * millId : 1
         * type : 2
         * name : 不发货地区模板2
         * areaUntransportList : null
         * customTransportList : null
         */
        @SerializedName("count")
        public int count;
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("id")
        public String id;
        @SerializedName("millId")
        public int millId;
        @SerializedName("type")
        public String type;
        @SerializedName("name")
        public String name;
        public boolean isSclected;
        public boolean isSclected() {
            return isSclected;
        }

        public void setSclected(boolean sclected) {
            isSclected = sclected;
        }
    }
}
