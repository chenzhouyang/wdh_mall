package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/2/9.
 */

public class MealContentBean {
    /**
     * code : 0
     * message : 成功
     * data : {"id":20,"name":"102","cover":"http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886667ea","price":102,"profile":"","count":null}
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
         * id : 20
         * name : 102
         * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886667ea
         * price : 102
         * profile :
         * count : null
         */

        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("cover")
        private String cover;
        @SerializedName("price")
        private double price;
        @SerializedName("profile")
        private String profile;
        @SerializedName("count")
        private Object count;

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public void setCount(Object count) {
            this.count = count;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCover() {
            return cover;
        }

        public double getPrice() {
            return price;
        }

        public String getProfile() {
            return profile;
        }

        public Object getCount() {
            return count;
        }
    }
}
