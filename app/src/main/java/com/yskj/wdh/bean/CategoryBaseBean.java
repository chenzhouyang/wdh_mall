package com.yskj.wdh.bean;

/**
 * Created by liuchaoya on 2016/11/23.
 * For yskj
 * Project Name : LSK
 */
/**品类选择基类，需要显示多少属性，就让此类拥有多少方法，好处是可以用GsonFormat去快速创建其子类，目的是不同子类可用相同方法去传值*/
public class CategoryBaseBean {
    protected String name;
    protected int cateId;
    public String getName() {
        return name;
    }
    public int getCateId() {
        return cateId;
    }
}
