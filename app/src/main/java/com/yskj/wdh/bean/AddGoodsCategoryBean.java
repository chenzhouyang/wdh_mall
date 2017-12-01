package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/18on 9:09.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsCategoryBean {

    /**
     * code : 0
     * message : 成功
     * data : [{"id":5,"name":"唯多特惠"},{"id":7,"name":"个护美妆"},{"id":3,"name":"生活用品"},{"id":137,"name":"潮流女装"},{"id":53,"name":"日用家纺"},{"id":248,"name":"时尚女鞋"},{"id":1,"name":"数码家电"},{"id":593,"name":"品牌男装"},{"id":273,"name":"品质男鞋"},{"id":37,"name":"服装鞋包"},{"id":45,"name":"母婴用品"},{"id":312,"name":"母婴用品"},{"id":57,"name":"绿色保健"},{"id":400,"name":"家用电器"},{"id":76,"name":"生鲜食品"},{"id":109,"name":"卫生清洁"},{"id":519,"name":"箱包手袋"},{"id":434,"name":"智能数码"},{"id":178,"name":"家居家纺"},{"id":287,"name":"汽车用品"},{"id":210,"name":"厨房用品"},{"id":550,"name":"农资绿植"},{"id":301,"name":"运动户外"},{"id":324,"name":"内衣配饰"},{"id":587,"name":"计生情趣"},{"id":360,"name":"鲜花礼品"},{"id":375,"name":"宠物用品"},{"id":410,"name":"重复"},{"id":418,"name":"珠宝钟表"},{"id":449,"name":"家具家装"},{"id":635,"name":"酒水饮料"}]
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public ArrayList<DataBean> data;

    public static class DataBean {
        /**
         * id : 5
         * name : 唯多特惠
         */

        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
    }
}
