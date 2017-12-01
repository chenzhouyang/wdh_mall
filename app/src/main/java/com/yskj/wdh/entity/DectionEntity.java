package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/5/17.
 */

public class DectionEntity {
    /**
     * error_code : 000
     * error_msg : SUCCESS
     * ad_list : [{"article_id":"24","article_type":"2","title":"检测点说明","publish_time":"2017-04-11 00:00:00","thumb":"http://192.168.0.161/Public/upload/article/2017/04-10/58eafd62118e3.png","url":"http://192.168.0.161/App/Article/articleInfo/aid/24"}]
     * cat_info : {"cat_id":"53","cat_name":"商家版 仪器检测点说明","thumb":""}
     */

    @SerializedName("error_code")
    public String errorCode;
    @SerializedName("error_msg")
    public String errorMsg;
    @SerializedName("cat_info")
    public CatInfoBean catInfo;
    @SerializedName("ad_list")
    public List<AdListBean> adList;

    public static class CatInfoBean {
        /**
         * cat_id : 53
         * cat_name : 商家版 仪器检测点说明
         * thumb :
         */

        @SerializedName("cat_id")
        public String catId;
        @SerializedName("cat_name")
        public String catName;
        @SerializedName("thumb")
        public String thumb;
    }

    public static class AdListBean {
        /**
         * article_id : 24
         * article_type : 2
         * title : 检测点说明
         * publish_time : 2017-04-11 00:00:00
         * thumb : http://192.168.0.161/Public/upload/article/2017/04-10/58eafd62118e3.png
         * url : http://192.168.0.161/App/Article/articleInfo/aid/24
         */

        @SerializedName("article_id")
        public String articleId;
        @SerializedName("article_type")
        public String articleType;
        @SerializedName("title")
        public String title;
        @SerializedName("publish_time")
        public String publishTime;
        @SerializedName("thumb")
        public String thumb;
        @SerializedName("url")
        public String url;
    }
}
