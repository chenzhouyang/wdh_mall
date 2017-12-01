package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/15on 16:58.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsInfoToJava {

    /**
     * goodId : 13
     * contentDetailVo : [{"id":"41","type":1,"content":"鹅鹅鹅414141，曲项向天歌，白毛浮绿水，红掌拨清波。","orders":1},{"id":"41","type":1,"content":"鹅鹅鹅414141，曲项向天歌，白毛浮绿水，红掌拨清波。","orders":1}]
     */
    @SerializedName("goodId")
    public String goodId;
    @SerializedName("contentDetailVo")
    public ArrayList<ContentDetailVoBean> contentDetailVo;
    public static class ContentDetailVoBean {
        /**
         * id : 41
         * type : 1
         * content : 鹅鹅鹅414141，曲项向天歌，白毛浮绿水，红掌拨清波。
         * orders : 1
         */
        @SerializedName("id")
        public String id;
        @SerializedName("type")
        public int type;
        @SerializedName("content")
        public String content;
        @SerializedName("orders")
        public int orders;
    }
}
