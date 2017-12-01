package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/26on 9:50.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class StraightForBean {
    /**
     * code : 0
     * message : 成功
     * data : [{"shopName":"最牛逼的商铺没有之一","shopId":21,"goodId":46,"name":"Asd","profile":"Asd","price":600,"goodsInTrade":4988,"frozenCount":0,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927f16f6515fe00011157e8"},{"shopName":"最牛逼的商铺没有之一","shopId":21,"goodId":45,"name":"直供商品111","profile":"直供商品003的简介","price":500.5,"goodsInTrade":995,"frozenCount":0,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=123456789"},{"shopName":"最牛逼的商铺没有之一","shopId":21,"goodId":44,"name":"直供商品111","profile":"直供商品003的简介","price":500.5,"goodsInTrade":1000,"frozenCount":0,"cover":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=123456789"}]
     */
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * shopName : 最牛逼的商铺没有之一
         * shopId : 21
         * goodId : 46
         * name : Asd
         * profile : Asd
         * price : 600
         * goodsInTrade : 4988
         * frozenCount : 0
         * cover : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927f16f6515fe00011157e8
         */

        @SerializedName("shopName")
        public String shopName;
        @SerializedName("shopId")
        public int shopId;
        @SerializedName("goodId")
        public String goodId;
        @SerializedName("name")
        public String name;
        @SerializedName("profile")
        public String profile;
        @SerializedName("price")
        public double price;
        @SerializedName("goodsInTrade")
        public int goodsInTrade;
        @SerializedName("frozenCount")
        public int frozenCount;
        @SerializedName("cover")
        public String cover;

        public int currentCount = 1;
        public int getCurrentCount() {
            return currentCount;
        }
        public void setCurrentCount(int currentCount) {
            this.currentCount = currentCount;
        }



        public boolean isChoosed;
        public boolean isCheck = false;
        public boolean isCheck() {
            return isCheck;
        }
        public void setCheck(boolean check) {
            isCheck = check;
        }
        public boolean isChoosed() {
            return isChoosed;
        }
        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }
    }
}
