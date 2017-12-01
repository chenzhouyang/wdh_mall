package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by liuchaoya on 2016/11/9.
 * For yskj
 * Project Name : LSK
 */

public class SetMealContentItemBean implements Serializable{

    /**
     * id : 11
     * name : 14984
     * cover : http://192.168.0.185:8080/api/v1/mgs/file/download?fid=57e650b7ec74a47d886667ea
     * price : 255
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


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int num = 1;
    /**
     * cover : http://localhost:8080/api/document/getFile?fileId=test01
     * id : 3
     * name : test02
     * price : 10.25
     * profile : test02
     */

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    /**选中状态*/
    public int state;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setPrice(double price) {
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
