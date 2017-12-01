package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11 0011.
 */
public class ProjectBean {


    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":10,"list":[{"lifeId":1,"name":"卖个球","teamBuyPrice":15,"onlineTime":"2016-11-18","count":19,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd"}],"statistics":{"lifeItemCount":6,"couponCount":29}}
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
         * list : [{"lifeId":1,"name":"卖个球","teamBuyPrice":15,"onlineTime":"2016-11-18","count":19,"cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd"}]
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
             * lifeId : 1
             * name : 卖个球
             * teamBuyPrice : 15
             * onlineTime : 2016-11-18
             * count : 19
             * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=582e56a1f676fd69b5657ddd
             */

            @SerializedName("lifeId")
            private int lifeId;
            @SerializedName("name")
            private String name;
            @SerializedName("teamBuyPrice")
            private double teamBuyPrice;
            @SerializedName("onlineTime")
            private String onlineTime;
            @SerializedName("count")
            private int count;
            @SerializedName("cover")
            private String cover;

            public void setLifeId(int lifeId) {
                this.lifeId = lifeId;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setTeamBuyPrice(int teamBuyPrice) {
                this.teamBuyPrice = teamBuyPrice;
            }

            public void setOnlineTime(String onlineTime) {
                this.onlineTime = onlineTime;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getLifeId() {
                return lifeId;
            }

            public String getName() {
                return name;
            }

            public double getTeamBuyPrice() {
                return teamBuyPrice;
            }

            public String getOnlineTime() {
                return onlineTime;
            }

            public int getCount() {
                return count;
            }

            public String getCover() {
                return cover;
            }
        }
    }
}
