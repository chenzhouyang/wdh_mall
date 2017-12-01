package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuchaoya on 2016/11/9.
 * For yskj
 * Project Name : LSK
 */
public class ProManagerSetMealBean implements Serializable{

    /**
     * code : 0
     * message : 成功
     * data : {"cursor":0,"count":100,"list":[{"id":76,"name":"炫迈口香糖","cover":"59310a411416a50001d256e5","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=59310a411416a50001d256e5","status":2,"updateTime":"2017-06-02 15:27:52","goodsType":2,"goodsInTrade":5,"isShared":1,"frozenCount":0,"isOwner":0},{"id":77,"name":"直供商品001","cover":"5927d1726515fe00011157dc","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927d1726515fe00011157dc","status":2,"updateTime":"2017-06-02 15:14:35","goodsType":2,"goodsInTrade":2,"isShared":0,"frozenCount":0,"isOwner":0},{"id":78,"name":"直供商品003","cover":"5927d1566515fe00011157d8","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927d1566515fe00011157d8","status":2,"updateTime":"2017-06-02 15:14:35","goodsType":2,"goodsInTrade":1,"isShared":0,"frozenCount":0,"isOwner":0}]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * cursor : 0
         * count : 100
         * list : [{"id":76,"name":"炫迈口香糖","cover":"59310a411416a50001d256e5","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=59310a411416a50001d256e5","status":2,"updateTime":"2017-06-02 15:27:52","goodsType":2,"goodsInTrade":5,"isShared":1,"frozenCount":0,"isOwner":0},{"id":77,"name":"直供商品001","cover":"5927d1726515fe00011157dc","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927d1726515fe00011157dc","status":2,"updateTime":"2017-06-02 15:14:35","goodsType":2,"goodsInTrade":2,"isShared":0,"frozenCount":0,"isOwner":0},{"id":78,"name":"直供商品003","cover":"5927d1566515fe00011157d8","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5927d1566515fe00011157d8","status":2,"updateTime":"2017-06-02 15:14:35","goodsType":2,"goodsInTrade":1,"isShared":0,"frozenCount":0,"isOwner":0}]
         */

        @SerializedName("cursor")
        public int cursor;
        @SerializedName("count")
        public int count;
        @SerializedName("list")
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 76
             * name : 炫迈口香糖
             * cover : 59310a411416a50001d256e5
             * coverAndUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=59310a411416a50001d256e5
             * status : 2
             * updateTime : 2017-06-02 15:27:52
             * goodsType : 2
             * goodsInTrade : 5
             * isShared : 1
             * frozenCount : 0
             * isOwner : 0
             */

            @SerializedName("id")
            public int id;
            @SerializedName("name")
            public String name;
            @SerializedName("cover")
            public String cover;
            @SerializedName("coverAndUrl")
            public String coverAndUrl;
            @SerializedName("status")
            public int status;
            @SerializedName("updateTime")
            public String updateTime;
            @SerializedName("goodsType")
            public int goodsType;
            @SerializedName("goodsInTrade")
            public int goodsInTrade;
            @SerializedName("isShared")
            public int isShared;
            @SerializedName("frozenCount")
            public int frozenCount;
            @SerializedName("isOwner")
            public int isOwner;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
