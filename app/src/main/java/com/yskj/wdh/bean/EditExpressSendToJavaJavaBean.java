package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/15on 9:52.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class EditExpressSendToJavaJavaBean {

    /**
     * id : 2
     * operaType : 1
     * mill_id : 1
     * type : 2
     * name : 不发货地区模板
     * areaUntransportList : [{"transport_id":1,"area_id":51},{"transport_id":1,"area_id":52},{"transport_id":1,"area_id":53},{"transport_id":1,"area_id":54}]
     * customTransportList : [{"transport_id":1,"number":5,"price":10,"addNumber":1,"addPrice":3,"customTransportAreaList":[{"area_id":61},{"area_id":62},{"area_id":63},{"area_id":64}]}]
     */

    @SerializedName("id")
    public String id;
    @SerializedName("operaType")
    public String operaType;
    @SerializedName("mill_id")
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
         * transport_id : 1
         * area_id : 51
         */
        @SerializedName("areaId")
        public String areaId;
        @SerializedName("areaName")
        public String areaName;
    }

    public static class CustomTransportListBean {
        /**
         * transport_id : 1
         * number : 5
         * price : 10
         * addNumber : 1
         * addPrice : 3
         * customTransportAreaList : [{"area_id":61},{"area_id":62},{"area_id":63},{"area_id":64}]
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
        @SerializedName("customTransportAreaList")
        public ArrayList<CustomTransportAreaListBean> customTransportAreaList;

        public static class CustomTransportAreaListBean {
            /**
             * area_id : 61
             */

            @SerializedName("areaId")
            public String areaId;
            @SerializedName("areaName")
            public String areaName;

        }
    }
}
