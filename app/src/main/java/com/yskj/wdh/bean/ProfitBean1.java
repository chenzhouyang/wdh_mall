package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wdx on 2016/11/12 0012.
 */
public class ProfitBean1 {


    /**
     * code : 0
     * message : 成功
     * data : {"list":[{"payAmount":12.5,"count":1,"consumeDate":"2016-11-10"},{"payAmount":45,"count":3,"consumeDate":"2016-11-14"}],"statistics":{"payAmount":57.5,"count":4}}
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
         * list : [{"payAmount":12.5,"count":1,"consumeDate":"2016-11-10"},{"payAmount":45,"count":3,"consumeDate":"2016-11-14"}]
         * statistics : {"payAmount":57.5,"count":4}
         */

        @SerializedName("statistics")
        private StatisticsBean statistics;
        @SerializedName("list")
        private List<ListBean> list;

        public void setStatistics(StatisticsBean statistics) {
            this.statistics = statistics;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public StatisticsBean getStatistics() {
            return statistics;
        }

        public List<ListBean> getList() {
            return list;
        }

        public static class StatisticsBean {
            /**
             * payAmount : 57.5
             * count : 4
             */

            @SerializedName("payAmount")
            private double payAmount;
            @SerializedName("count")
            private int count;

            public void setPayAmount(double payAmount) {
                this.payAmount = payAmount;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public double getPayAmount() {
                return payAmount;
            }

            public int getCount() {
                return count;
            }
        }

        public static class ListBean {
            /**
             * payAmount : 12.5
             * count : 1
             * consumeDate : 2016-11-10
             */

            @SerializedName("payAmount")
            private double payAmount;
            @SerializedName("count")
            private int count;
            @SerializedName("consumeDate")
            private String consumeDate;

            public void setPayAmount(double payAmount) {
                this.payAmount = payAmount;
            }

            public void setCount(int count) {
                this.count = count;
            }

            public void setConsumeDate(String consumeDate) {
                this.consumeDate = consumeDate;
            }

            public double getPayAmount() {
                return payAmount;
            }

            public int getCount() {
                return count;
            }

            public String getConsumeDate() {
                return consumeDate;
            }
        }
    }
}
