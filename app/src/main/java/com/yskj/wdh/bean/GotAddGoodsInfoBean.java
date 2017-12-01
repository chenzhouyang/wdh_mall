package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 创建日期 2017/8/22on 17:32.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GotAddGoodsInfoBean {

    /**
     * code : 0
     * message : 成功
     * data : {"count":10,"cursor":0,"store":null,"storeIp":null,"millId":15,"orderParam":null,"status":1,"strStatus":null,"statusArr":null,"index":null,"max":null,"goodId":62,"thumbnail":"http://192.168.0.197/images/20170822152029054-359.jpg","goodName":"2222","shopPrice":111,"volume":0,"stock":111,"createTime":"2017-08-22 15:21:12","approvalOpinion":null,"barCode":"0","costPrice":0,"keyWord":"我得name,家纺装饰,2222,2222","mAccount":111,"marketPrice":0,"updateTime":"2017-08-22 15:45:31","goodsCategoryId":56,"sequence":null,"profile":null,"transportId":49,"goodImageList":[{"id":241,"goodsId":62,"source":null,"thumbnail":"http://192.168.0.197/images/20170822152029054-359.jpg","title":null,"orders":1,"type":1,"store":null,"storeIp":null}],"goodContentList":[{"count":10,"cursor":0,"store":null,"storeIp":null,"id":162,"goodsId":62,"type":1,"content":"111","file":null,"orders":0,"operaType":null,"index":null,"max":null}],"parameterList":[{"id":31,"goods_id":null,"name":"111","price":111,"stock":111,"mAccount":111,"orders":1}],"transportList":[{"id":23,"mill_id":null,"type":2,"name":"包邮（除偏远地区）","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":44,"mill_id":null,"type":3,"name":"编辑运费模板名称","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":45,"mill_id":null,"type":3,"name":"编辑运费模板名称1","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":46,"mill_id":null,"type":3,"name":"编辑运费模板名称2","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":47,"mill_id":null,"type":3,"name":"编辑运费模板名称3","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":48,"mill_id":null,"type":3,"name":"编辑运费模板名称4","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":49,"mill_id":null,"type":3,"name":"编辑运费模板名称","areaUntransportList":null,"customTransportList":null,"operaType":null}],"goodCategroyList":[]}
     */

    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public DataBean data;

    public static class DataBean {
        /**
         * count : 10
         * cursor : 0
         * store : null
         * storeIp : null
         * millId : 15
         * orderParam : null
         * status : 1
         * strStatus : null
         * statusArr : null
         * index : null
         * max : null
         * goodId : 62
         * thumbnail : http://192.168.0.197/images/20170822152029054-359.jpg
         * goodName : 2222
         * shopPrice : 111.0
         * volume : 0
         * stock : 111
         * createTime : 2017-08-22 15:21:12
         * approvalOpinion : null
         * barCode : 0
         * costPrice : 0.0
         * keyWord : 我得name,家纺装饰,2222,2222
         * mAccount : 111.0
         * marketPrice : 0.0
         * updateTime : 2017-08-22 15:45:31
         * goodsCategoryId : 56
         * sequence : null
         * profile : null
         * transportId : 49
         * goodImageList : [{"id":241,"goodsId":62,"source":null,"thumbnail":"http://192.168.0.197/images/20170822152029054-359.jpg","title":null,"orders":1,"type":1,"store":null,"storeIp":null}]
         * goodContentList : [{"count":10,"cursor":0,"store":null,"storeIp":null,"id":162,"goodsId":62,"type":1,"content":"111","file":null,"orders":0,"operaType":null,"index":null,"max":null}]
         * parameterList : [{"id":31,"goods_id":null,"name":"111","price":111,"stock":111,"mAccount":111,"orders":1}]
         * transportList : [{"id":23,"mill_id":null,"type":2,"name":"包邮（除偏远地区）","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":44,"mill_id":null,"type":3,"name":"编辑运费模板名称","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":45,"mill_id":null,"type":3,"name":"编辑运费模板名称1","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":46,"mill_id":null,"type":3,"name":"编辑运费模板名称2","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":47,"mill_id":null,"type":3,"name":"编辑运费模板名称3","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":48,"mill_id":null,"type":3,"name":"编辑运费模板名称4","areaUntransportList":null,"customTransportList":null,"operaType":null},{"id":49,"mill_id":null,"type":3,"name":"编辑运费模板名称","areaUntransportList":null,"customTransportList":null,"operaType":null}]
         * goodCategroyList : []
         */

        @SerializedName("count")
        public int count;
        @SerializedName("cursor")
        public int cursor;
        @SerializedName("millId")
        public int millId;
        @SerializedName("orderParam")
        public String orderParam;
        @SerializedName("status")
        public int status;
        @SerializedName("strStatus")
        public String strStatus;
        @SerializedName("statusArr")
        public String statusArr;
        @SerializedName("goodId")
        public int goodId;
        @SerializedName("thumbnail")
        public String thumbnail;
        @SerializedName("goodName")
        public String goodName;
        @SerializedName("volume")
        public int volume;
        @SerializedName("stock")
        public int stock;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("mAccount")
        public double mAccount;
        @SerializedName("marketPrice")
        public double marketPrice;
        @SerializedName("goodsCategoryId")
        public String goodsCategoryId;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("sequence")
        public String sequence;
        @SerializedName("profile")
        public String profile;
        @SerializedName("keyWord")
        public String keyWord;
        @SerializedName("transportId")
        public String transportId;
        @SerializedName("transportName")
        public String transportName;
        @SerializedName("goodImageList")
        public List<GoodImageListBean> goodImageList;
        @SerializedName("goodContentList")
        public List<GoodContentListBean> goodContentList;
        @SerializedName("parameterList")
        public List<ParameterListBean> parameterList;
        @SerializedName("transportList")
        public List<TransportListBean> transportList;

        public static class GoodImageListBean {
            /**
             * id : 241
             * goodsId : 62
             * source : null
             * thumbnail : http://192.168.0.197/images/20170822152029054-359.jpg
             * title : null
             * orders : 1
             * type : 1
             * store : null
             * storeIp : null
             */

            @SerializedName("id")
            public int id;
            @SerializedName("goodsId")
            public int goodsId;
            @SerializedName("source")
            public Object source;
            @SerializedName("thumbnail")
            public String thumbnail;
            @SerializedName("title")
            public Object title;
            @SerializedName("orders")
            public int orders;
            @SerializedName("type")
            public int type;
            @SerializedName("store")
            public Object store;
            @SerializedName("storeIp")
            public Object storeIp;
        }

        public static class GoodContentListBean {
            /**
             * count : 10
             * cursor : 0
             * store : null
             * storeIp : null
             * id : 162
             * goodsId : 62
             * type : 1
             * content : 111
             * file : null
             * orders : 0
             * operaType : null
             * index : null
             * max : null
             */

            @SerializedName("count")
            public int count;
            @SerializedName("cursor")
            public int cursor;
            @SerializedName("store")
            public Object store;
            @SerializedName("storeIp")
            public Object storeIp;
            @SerializedName("id")
            public int id;
            @SerializedName("goodsId")
            public int goodsId;
            @SerializedName("type")
            public int type;
            @SerializedName("content")
            public String content;
            @SerializedName("file")
            public Object file;
            @SerializedName("orders")
            public int orders;
            @SerializedName("operaType")
            public Object operaType;
            @SerializedName("index")
            public Object index;
            @SerializedName("max")
            public Object max;
        }

        public static class ParameterListBean {
            /**
             * id : 31
             * goods_id : null
             * name : 111
             * price : 111.0
             * stock : 111
             * mAccount : 111.0
             * orders : 1
             */

            @SerializedName("id")
            public int id;
            @SerializedName("goods_id")
            public Object goodsId;
            @SerializedName("name")
            public String name;
            @SerializedName("price")
            public double price;
            @SerializedName("stock")
            public int stock;
            @SerializedName("mAccount")
            public double mAccount;
            @SerializedName("orders")
            public int orders;
        }

        public static class TransportListBean {
            /**
             * id : 23
             * mill_id : null
             * type : 2
             * name : 包邮（除偏远地区）
             * areaUntransportList : null
             * customTransportList : null
             * operaType : null
             */

            @SerializedName("id")
            public int id;
            @SerializedName("mill_id")
            public Object millId;
            @SerializedName("type")
            public int type;
            @SerializedName("name")
            public String name;
            @SerializedName("areaUntransportList")
            public Object areaUntransportList;
            @SerializedName("customTransportList")
            public Object customTransportList;
            @SerializedName("operaType")
            public Object operaType;
        }
    }
}
