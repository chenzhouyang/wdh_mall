package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期 2017/8/23on 11:37.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class FindLogisticsDetailBean {
    /**
     * code : 0
     * message : 成功
     * data : {"count":10,"cursor":0,"id":44,"millId":15,"type":3,"name":"编辑运费模板名称","areaUntransportList":[{"id":104,"transportId":44,"areaId":3102},{"id":105,"transportId":44,"areaId":4670}],"customTransportList":[{"id":25,"transport_id":null,"number":1,"price":1,"addNumber":1,"addPrice":1,"shopCustomTransportList":null,"customTransportAreaList":[{"id":64,"customTransportId":25,"areaId":7531},{"id":65,"customTransportId":25,"areaId":5827}],"areaUntransportList":null}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * count : 10
         * cursor : 0
         * id : 44
         * millId : 15
         * type : 3
         * name : 编辑运费模板名称
         * areaUntransportList : [{"id":104,"transportId":44,"areaId":3102},{"id":105,"transportId":44,"areaId":4670}]
         * customTransportList : [{"id":25,"transport_id":null,"number":1,"price":1,"addNumber":1,"addPrice":1,"shopCustomTransportList":null,"customTransportAreaList":[{"id":64,"customTransportId":25,"areaId":7531},{"id":65,"customTransportId":25,"areaId":5827}],"areaUntransportList":null}]
         */

        @SerializedName("count")
        public int count;
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("id")
        public String id;
        @SerializedName("millId")
        public String millId;
        @SerializedName("type")
        public String type;
        @SerializedName("name")
        public String name;
        @SerializedName("areaUntransportList")
        public ArrayList<AreaUntransportListBean> areaUntransportList;
        @SerializedName("customTransportList")
        public ArrayList<CustomTransportListBean> customTransportList;

        public static class AreaUntransportListBean {
            /**
             * id : 104
             * transportId : 44
             * areaId : 3102
             */

            @SerializedName("id")
            public String id;
            @SerializedName("transportId")
            public int transportId;
            @SerializedName("areaId")
            public String areaId;
            @SerializedName("areaName")
            public String areaName;
        }

        public static class CustomTransportListBean {
            /**
             * id : 25
             * transport_id : null
             * number : 1
             * price : 1.0
             * addNumber : 1
             * addPrice : 1.0
             * shopCustomTransportList : null
             * customTransportAreaList : [{"id":64,"customTransportId":25,"areaId":7531},{"id":65,"customTransportId":25,"areaId":5827}]
             * areaUntransportList : null
             */

            @SerializedName("id")
            public String id;
            @SerializedName("transport_id")
            public String transportId;
            @SerializedName("number")
            public String number;
            @SerializedName("price")
            public String price;
            @SerializedName("addNumber")
            public String addNumber;
            @SerializedName("addPrice")
            public String addPrice;
            @SerializedName("shopCustomTransportList")
            public Object shopCustomTransportList;
            @SerializedName("areaUntransportList")
            public Object areaUntransportList;
            @SerializedName("customTransportAreaList")
            public List<CustomTransportAreaListBean> customTransportAreaList;

            public static class CustomTransportAreaListBean {
                /**
                 * id : 64
                 * customTransportId : 25
                 * areaId : 7531
                 */

                @SerializedName("id")
                public int id;
                @SerializedName("areaId")
                public String areaId;
                @SerializedName("areaName")
                public String areaName;
            }
        }
    }
}
