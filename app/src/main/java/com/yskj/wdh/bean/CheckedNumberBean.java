package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class CheckedNumberBean {

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"orderNo":"2016122110124180126","fundAmount":44,"accPointsAmount":0,"count":2,"lifeItemName":"武汉热干面","mobile":"18864659628","validityDate":"2016-12-21 10:13:45","price":22,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584b6a47d232d62cacd74b52"}],"statistics":{"lifeItemCount":6,"couponCount":29}}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public DataBean getData() {
        return data;
    }

    public static class DataBean {
        /**
         * cursor : 0
         * count : 10
         * list : [{"orderNo":"2016122110124180126","fundAmount":44,"accPointsAmount":0,"count":2,"lifeItemName":"武汉热干面","mobile":"18864659628","validityDate":"2016-12-21 10:13:45","price":22,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584b6a47d232d62cacd74b52"}]
         * statistics : {"lifeItemCount":6,"couponCount":29}
         */

        @SerializedName("cursor")
        private int cursor;
        @SerializedName("count")
        private int count;
        @SerializedName("statistics")
        private StatisticsBean statistics;
        @SerializedName("list")
        private List<ListBean> list;

        public void setCursor(int cursor) {
            this.cursor = cursor;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setStatistics(StatisticsBean statistics) {
            this.statistics = statistics;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public int getCursor() {
            return cursor;
        }

        public int getCount() {
            return count;
        }

        public StatisticsBean getStatistics() {
            return statistics;
        }

        public List<ListBean> getList() {
            return list;
        }

        public static class StatisticsBean {
            /**
             * lifeItemCount : 6
             * couponCount : 29
             */

            @SerializedName("lifeItemCount")
            private int lifeItemCount;
            @SerializedName("couponCount")
            private int couponCount;

            public void setLifeItemCount(int lifeItemCount) {
                this.lifeItemCount = lifeItemCount;
            }

            public void setCouponCount(int couponCount) {
                this.couponCount = couponCount;
            }

            public int getLifeItemCount() {
                return lifeItemCount;
            }

            public int getCouponCount() {
                return couponCount;
            }
        }

        public static class ListBean {
            /**
             * orderNo : 2016122110124180126
             * fundAmount : 44
             * accPointsAmount : 0
             * count : 2
             * lifeItemName : 武汉热干面
             * mobile : 18864659628
             * validityDate : 2016-12-21 10:13:45
             * price : 22
             * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=584b6a47d232d62cacd74b52
             */

            @SerializedName("orderNo")
            private String orderNo;
            @SerializedName("fundAmount")
            private double fundAmount;
            @SerializedName("accPointsAmount")
            private double accPointsAmount;
            @SerializedName("count")
            private int count;
            @SerializedName("lifeItemName")
            private String lifeItemName;
            @SerializedName("mobile")
            private String mobile;
            @SerializedName("validityDate")
            private String validityDate;
            @SerializedName("price")
            private double price;
            @SerializedName("cover")
            private String cover;

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public void setFundAmount(int fundAmount) {
                this.fundAmount = fundAmount;
            }

            public void setAccPointsAmount(int accPointsAmount) {
                this.accPointsAmount = accPointsAmount;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public void setLifeItemName(String lifeItemName) {
                this.lifeItemName = lifeItemName;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public void setValidityDate(String validityDate) {
                this.validityDate = validityDate;
            }

            public void setPrice(int price) {
                this.price = price;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public double getFundAmount() {
                return fundAmount;
            }

            public double getAccPointsAmount() {
                return accPointsAmount;
            }

            public int getCount() {
                return count;
            }

            public String getLifeItemName() {
                return lifeItemName;
            }

            public String getMobile() {
                return mobile;
            }

            public String getValidityDate() {
                return validityDate;
            }

            public double getPrice() {
                return price;
            }

            public String getCover() {
                return cover;
            }
        }
    }
}
