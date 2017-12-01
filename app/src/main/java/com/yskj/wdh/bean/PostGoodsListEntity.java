package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 创建日期 2017/8/11on 10:23.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PostGoodsListEntity {

    /**
     * millId : 1
     * strStatus : 1
     * orderParam : status
     * index : 0
     * max : 10
     */

    @SerializedName("millId")
    public String millId;
    @SerializedName("strStatus")
    public int status;
    @SerializedName("orderParam")
    public String orderParam;
    @SerializedName("index")
    public int index;
    @SerializedName("max")
    public int max;
    @SerializedName("orderType")
    public int orderType;
}
