package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建日期 2017/5/27on 15:41.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class BuyerInfoBean {
    /**
     * code : 0
     * message : 成功
     * data : {"orderId":3,"orderNo":"2017052619572355665","buyerName":"程序员之家","sellerName":"最牛逼的商铺没有之一","totalAmount":5502.5,"status":2,"type":1,"createTime":"2017-05-26 19:57:24","buyerMobile":"18864659628","sellerMobile":"15103749464","buyerOwner":"晴雪艾特","sellerOwner":"小主494","buyerAddress":"河南省许昌市许昌县尚集镇芙蓉大道电子商务中心","sellerAddress":"河南省许昌市魏都区哈哈","lpgList":[{"orderId":3,"goodId":46,"goodName":"Asd","price":600,"count":5,"cover":""},{"orderId":3,"goodId":45,"goodName":"直供商品111","price":500.5,"count":5,"cover":""}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * orderId : 3
         * orderNo : 2017052619572355665
         * buyerName : 程序员之家
         * sellerName : 最牛逼的商铺没有之一
         * totalAmount : 5502.5
         * status : 2
         * type : 1
         * createTime : 2017-05-26 19:57:24
         * buyerMobile : 18864659628
         * sellerMobile : 15103749464
         * buyerOwner : 晴雪艾特
         * sellerOwner : 小主494
         * buyerAddress : 河南省许昌市许昌县尚集镇芙蓉大道电子商务中心
         * sellerAddress : 河南省许昌市魏都区哈哈
         * lpgList : [{"orderId":3,"goodId":46,"goodName":"Asd","price":600,"count":5,"cover":""},{"orderId":3,"goodId":45,"goodName":"直供商品111","price":500.5,"count":5,"cover":""}]
         */

        @SerializedName("orderId")
        public int orderId;
        @SerializedName("orderNo")
        public String orderNo;
        @SerializedName("buyerName")
        public String buyerName;
        @SerializedName("sellerName")
        public String sellerName;
        @SerializedName("totalAmount")
        public double totalAmount;
        @SerializedName("status")
        public int status;
        @SerializedName("type")
        public int type;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("buyerMobile")
        public String buyerMobile;
        @SerializedName("sellerMobile")
        public String sellerMobile;
        @SerializedName("buyerOwner")
        public String buyerOwner;
        @SerializedName("sellerOwner")
        public String sellerOwner;
        @SerializedName("buyerAddress")
        public String buyerAddress;
        @SerializedName("sellerAddress")
        public String sellerAddress;
        @SerializedName("lpgList")
        public List<LpgListBean> lpgList;

        public static class LpgListBean {
            /**
             * orderId : 3
             * goodId : 46
             * goodName : Asd
             * price : 600.0
             * count : 5
             * cover :
             */

            @SerializedName("orderId")
            public int orderId;
            @SerializedName("goodId")
            public int goodId;
            @SerializedName("goodName")
            public String goodName;
            @SerializedName("price")
            public double price;
            @SerializedName("count")
            public int count;
            @SerializedName("cover")
            public String cover;
        }
    }

//    /**
//     * code : 0
//     * message : 成功
//     * data : {"orderId":5,"orderNo":"2017052713543019142","buyerName":"程序员之家","sellerName":"最牛逼的商铺没有之一","totalAmount":1200,"status":2,"type":1,"createTime":"2017-05-27 13:54:30","buyerMobile":"18864659628","sellerMobile":"15103749464","buyerOwner":"晴雪艾特","sellerOwner":"小主494","buyerAddress":"河南省许昌市许昌县尚集镇芙蓉大道电子商务中心","sellerAddress":"河南省许昌市魏都区哈哈","lpgList":[{"orderId":5,"goodId":46,"goodName":"Asd","price":600,"count":2,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927f16f6515fe00011157e8"}]}
//     */
//    @SerializedName("code")
//    public int code;
//    @SerializedName("message")
//    public String message;
//    @SerializedName("data")
//    public DataBean data;
//
//    public static class DataBean {
//        /**
//         * orderId : 5
//         * orderNo : 2017052713543019142
//         * buyerName : 程序员之家
//         * sellerName : 最牛逼的商铺没有之一
//         * totalAmount : 1200
//         * status : 2
//         * type : 1
//         * createTime : 2017-05-27 13:54:30
//         * buyerMobile : 18864659628
//         * sellerMobile : 15103749464
//         * buyerOwner : 晴雪艾特
//         * sellerOwner : 小主494
//         * buyerAddress : 河南省许昌市许昌县尚集镇芙蓉大道电子商务中心
//         * sellerAddress : 河南省许昌市魏都区哈哈
//         * lpgList : [{"orderId":5,"goodId":46,"goodName":"Asd","price":600,"count":2,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927f16f6515fe00011157e8"}]
//         */
//
//        @SerializedName("orderId")
//        public int orderId;
//        @SerializedName("orderNo")
//        public String orderNo;
//        @SerializedName("buyerName")
//        public String buyerName;
//        @SerializedName("sellerName")
//        public String sellerName;
//        @SerializedName("totalAmount")
//        public int totalAmount;
//        @SerializedName("status")
//        public int status;
//        @SerializedName("type")
//        public int type;
//        @SerializedName("createTime")
//        public String createTime;
//        @SerializedName("buyerMobile")
//        public String buyerMobile;
//        @SerializedName("sellerMobile")
//        public String sellerMobile;
//        @SerializedName("buyerOwner")
//        public String buyerOwner;
//        @SerializedName("sellerOwner")
//        public String sellerOwner;
//        @SerializedName("buyerAddress")
//        public String buyerAddress;
//        @SerializedName("sellerAddress")
//        public String sellerAddress;
//        @SerializedName("lpgList")
//        public List<LpgListBean> lpgList;
//
//        public static class LpgListBean {
//            /**
//             * orderId : 5
//             * goodId : 46
//             * goodName : Asd
//             * price : 600
//             * count : 2
//             * cover : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927f16f6515fe00011157e8
//             */
//
//            @SerializedName("orderId")
//            public int orderId;
//            @SerializedName("goodId")
//            public int goodId;
//            @SerializedName("goodName")
//            public String goodName;
//            @SerializedName("price")
//            public int price;
//            @SerializedName("count")
//            public int count;
//            @SerializedName("cover")
//            public String cover;
//        }
//    }
}
