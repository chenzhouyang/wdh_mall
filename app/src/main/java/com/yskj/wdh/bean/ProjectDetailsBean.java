package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gtz on 2016/11/15 0015.
 */
public class ProjectDetailsBean {


    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"orderNo":"2017020914450686619","fundAmount":15,"accPointsAmount":0,"count":1,"lifeItemName":"卖个球","mobile":"13569457585","validityDate":"2017-02-09 17:47:01","price":15,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd"}],"statistics":{"lifeItemCount":1,"couponCount":1}}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("data")
    private DataBean data;

    public void setCode(int code) {
        this.code = code;
    }


    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }


    public DataBean getData() {
        return data;
    }

    public static class DataBean {
        /**
         * cursor : 0
         * count : 10
         * list : [{"orderNo":"2017020914450686619","fundAmount":15,"accPointsAmount":0,"count":1,"lifeItemName":"卖个球","mobile":"13569457585","validityDate":"2017-02-09 17:47:01","price":15,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd"}]
         * statistics : {"lifeItemCount":1,"couponCount":1}
         */

        @SerializedName("cursor")
        private int cursor;
        @SerializedName("count")
        private int count;
        @SerializedName("statistics")
        private StatisticsBean statistics;
        @SerializedName("list")
        private ArrayList<ListBean> list;

        public void setCursor(int cursor) {
            this.cursor = cursor;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setStatistics(StatisticsBean statistics) {
            this.statistics = statistics;
        }

        public void setList(ArrayList<ListBean> list) {
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

        public ArrayList<ListBean> getList() {
            return list;
        }

        public static class StatisticsBean {
            /**
             * lifeItemCount : 1
             * couponCount : 1
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
             * orderNo : 2017020914450686619
             * fundAmount : 15.0
             * accPointsAmount : 0.0
             * count : 1
             * lifeItemName : 卖个球
             * mobile : 13569457585
             * validityDate : 2017-02-09 17:47:01
             * price : 15.0
             * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd
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

            public void setFundAmount(double fundAmount) {
                this.fundAmount = fundAmount;
            }

            public void setAccPointsAmount(double accPointsAmount) {
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

            public void setPrice(double price) {
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
