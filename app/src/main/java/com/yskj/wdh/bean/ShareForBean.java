package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建日期 2017/5/26on 14:42.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ShareForBean {


    /**
     * code : 0
     * message : 成功
     * data : {"list":[{"goodsId":50,"goodsName":"直供商品111","cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=123456789","price":500.5,"address":"河南省许昌市许昌县尚集镇芙蓉大道电子商务中心"}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("list")
        public List<ListBean> list;

        public static class ListBean {
            /**
             * goodsId : 50
             * goodsName : 直供商品111
             * cover : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=123456789
             * price : 500.5
             * address : 河南省许昌市许昌县尚集镇芙蓉大道电子商务中心
             */

            @SerializedName("goodsId")
            public String goodsId;
            @SerializedName("goodsName")
            public String goodsName;
            @SerializedName("cover")
            public String cover;
            @SerializedName("price")
            public double price;
            @SerializedName("address")
            public String address;
        }
    }
}
