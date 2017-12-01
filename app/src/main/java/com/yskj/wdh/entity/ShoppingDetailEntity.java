package com.yskj.wdh.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class ShoppingDetailEntity extends DataSupport {
    @Column(unique = true,nullable = false)
        private long id;
    @Column(nullable = false)
        private String goodname;
    @Column(nullable = false)
        private double price;
    @Column(nullable = false)
        private int goodsid;
    @Column(nullable = false)
    private String shapecode;
    @Column(nullable = true)
    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getShapecode() {
        return shapecode;
    }

    public void setShapecode(String shapecode) {
        this.shapecode = shapecode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int gooid) {
        this.goodsid = gooid;
    }
}
