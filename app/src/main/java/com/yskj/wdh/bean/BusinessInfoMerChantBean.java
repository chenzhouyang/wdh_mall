package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class BusinessInfoMerChantBean implements Serializable {


    /**
     * code : 0
     * message : 成功
     * data : {"name":"小主494","shopName":"最牛逼的商铺没有之一","areaId":22657,"areaArr":[21387,22655,22657],"areaName":"魏都区","categoryId":1,"categoryName":"美食","areaString":"河南省许昌市魏都区哈哈","mobile":"15103749464","cover":"58ca3aaca340c40001a2703b","coverUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ca3aaca340c40001a2703b","idCardFront":"","idCardFrontUrl":"","idCardBack":"","idCardBackUrl":"","licence":"58ca3aa1a340c40001a27039","licenceUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ca3aa1a340c40001a27039","latitude":34.022765,"longitude":113.883016,"detailAddress":"哈哈","approvalOpinion":"tongg","createTime":"2017-03-16 15:11:41","updateTime":"2017-06-01 11:57:36","industry":"","profile":"呵呵","vipLevel":0,"level":1,"status":1,"totalSendRed":0,"checkStatus":0,"totalProfit":14440.3,"fundAccount":13440.3,"machineStatus":1,"agent":{"name":"Sdf","profile":"Dfsdfsdf","level":3,"createTime":"2017-05-26 14:23:46","status":1,"remark":"","shopName":"最牛逼的商铺没有之一","areaId":["22657,魏都区","22671,许昌县"],"parentId":["21387,河南省","22655,许昌市"]}}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * name : 小主494
         * shopName : 最牛逼的商铺没有之一
         * areaId : 22657
         * areaArr : [21387,22655,22657]
         * areaName : 魏都区
         * categoryId : 1
         * categoryName : 美食
         * areaString : 河南省许昌市魏都区哈哈
         * mobile : 15103749464
         * cover : 58ca3aaca340c40001a2703b
         * coverUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ca3aaca340c40001a2703b
         * idCardFront :
         * idCardFrontUrl :
         * idCardBack :
         * idCardBackUrl :
         * licence : 58ca3aa1a340c40001a27039
         * licenceUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=58ca3aa1a340c40001a27039
         * latitude : 34.022765
         * longitude : 113.883016
         * detailAddress : 哈哈
         * approvalOpinion : tongg
         * createTime : 2017-03-16 15:11:41
         * updateTime : 2017-06-01 11:57:36
         * industry :
         * profile : 呵呵
         * vipLevel : 0
         * level : 1
         * status : 1
         * totalSendRed : 0
         * checkStatus : 0
         * totalProfit : 14440.3
         * fundAccount : 13440.3
         * machineStatus : 1
         * agent : {"name":"Sdf","profile":"Dfsdfsdf","level":3,"createTime":"2017-05-26 14:23:46","status":1,"remark":"","shopName":"最牛逼的商铺没有之一","areaId":["22657,魏都区","22671,许昌县"],"parentId":["21387,河南省","22655,许昌市"]}
         */

        @SerializedName("name")
        public String name;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("areaId")
        public int areaId;
        @SerializedName("areaName")
        public String areaName;
        @SerializedName("categoryId")
        public int categoryId;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("areaString")
        public String areaString;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("cover")
        public String cover;
        @SerializedName("coverUrl")
        public String coverUrl;
        @SerializedName("idCardFront")
        public String idCardFront;
        @SerializedName("idCardFrontUrl")
        public String idCardFrontUrl;
        @SerializedName("idCardBack")
        public String idCardBack;
        @SerializedName("idCardBackUrl")
        public String idCardBackUrl;
        @SerializedName("licence")
        public String licence;
        @SerializedName("licenceUrl")
        public String licenceUrl;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("detailAddress")
        public String detailAddress;
        @SerializedName("approvalOpinion")
        public String approvalOpinion;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("updateTime")
        public String updateTime;
        @SerializedName("industry")
        public String industry;
        @SerializedName("profile")
        public String profile;
        @SerializedName("vipLevel")
        public int vipLevel;
        @SerializedName("level")
        public int level;
        @SerializedName("status")
        public int status;
        @SerializedName("totalSendRed")
        public int totalSendRed;
        @SerializedName("checkStatus")
        public int checkStatus;
        @SerializedName("totalProfit")
        public double totalProfit;
        @SerializedName("fundAccount")
        public double fundAccount;
        @SerializedName("machineStatus")
        public int machineStatus;
        @SerializedName("agent")
        public AgentBean agent;
        @SerializedName("areaArr")
        public List<Integer> areaArr;

        public static class AgentBean {
            /**
             * name : Sdf
             * profile : Dfsdfsdf
             * level : 3
             * createTime : 2017-05-26 14:23:46
             * status : 1
             * remark :
             * shopName : 最牛逼的商铺没有之一
             * areaId : ["22657,魏都区","22671,许昌县"]
             * parentId : ["21387,河南省","22655,许昌市"]
             */

            @SerializedName("name")
            public String name;
            @SerializedName("profile")
            public String profile;
            @SerializedName("level")
            public int level;
            @SerializedName("createTime")
            public String createTime;
            @SerializedName("status")
            public int status;
            @SerializedName("remark")
            public String remark;
            @SerializedName("shopName")
            public String shopName;
            @SerializedName("areaId")
            public List<String> areaId;
            @SerializedName("parentId")
            public List<String> parentId;
        }
    }
}
