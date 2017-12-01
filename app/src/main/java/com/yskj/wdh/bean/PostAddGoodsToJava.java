package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/17on 9:16.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PostAddGoodsToJava {

    /**
     * goodId : 1
     * operaType : 1
     * imageIds : 5,6,7,8
     * contentIds : 9,10,
     * name : 测试商品20170808
     * profile : 我是20170808测试的一个商品
     * barCode : 201708080001
     * mAccount : 200
     * approvalOpinion : 我就是没啥意见
     * millId : 1
     * transport_id : 3
     * keyWord : 测试，解暑
     * marketPrice : 1000
     * shopPrice : 800
     * costPrice : 500
     * sequence : 1
     * prameterList : [{"name":"规格1","price":1000,"stock":88,"mAccount":200,"orders":1},{"name":"规格2","price":1010,"stock":68,"mAccount":200,"orders":1},{"name":"规格3","price":900,"stock":90,"mAccount":200,"orders":1}]
     * goodsCategoryId : 3
     */

    @SerializedName("goodId")
    public String goodId;
    @SerializedName("operaType")
    public int operaType;
    @SerializedName("imageIds")
    public String imageIds;
    @SerializedName("contentIds")
    public String contentIds;
    @SerializedName("name")
    public String name;
    @SerializedName("profile")
    public String profile;
    @SerializedName("keyWord")
    public String keyWord;
    @SerializedName("millId")
    public String millId;
    @SerializedName("transport_id")
    public String transportId;
    @SerializedName("goodsCategoryId")
    public String goodsCategoryId;
    @SerializedName("prameterList")
    public ArrayList<PrameterListBean> prameterList;

    public static class PrameterListBean {
        /**
         * name : 规格1
         * price : 1000
         * stock : 88
         * mAccount : 200
         * orders : 1
         */

        @SerializedName("name")
        public String name;
        @SerializedName("price")
        public String price;
        @SerializedName("stock")
        public String stock;
        @SerializedName("mAccount")
        public String mAccount;
        @SerializedName("orders")
        public int orders;
    }
}
