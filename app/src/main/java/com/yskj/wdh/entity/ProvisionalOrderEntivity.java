package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/5/27.
 */

public class ProvisionalOrderEntivity {

    /**
     * code : 1011
     * message : 包含库存不足的商品
     * data : {"list":[{"goodName":"测试哈哈哈","goodId":121},{"goodName":"测试哈哈哈","goodId":122}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("orderId")
        public int orderId;
        @SerializedName("list")
        public List<ListBean> list;

        public static class ListBean {
            /**
             * goodName : 测试哈哈哈
             * goodId : 121
             */

            @SerializedName("goodName")
            public String goodName;
            @SerializedName("goodId")
            public int goodId;
        }
    }
}
