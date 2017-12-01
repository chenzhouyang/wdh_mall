package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuchaoya on 2016/11/12.
 * For yskj
 * Project Name : LSK
 */

public class CategoryItemBean {


    @SerializedName("cateId")
    public int cateId;
    @SerializedName("parentId")
    public Long parentId;
    @SerializedName("name")
    public String name;
    @SerializedName("profile")
    public String profile;
    @SerializedName("fee")
    public int fee;
    @SerializedName("sequence")
    public int sequence;
    @SerializedName("isHot")
    public boolean isHot;
    @SerializedName("children")
    public List<CategoryItemBean> children = new ArrayList<>();
}
