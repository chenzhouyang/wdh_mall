package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuchaoya on 2016/11/10.
 * For yskj
 * Project Name : LSK
 */

public class SetMealItemDetailBean{

    /**
     * code : 0
     * message : 成功
     * data : {"id":33,"localShop":{"name":"唯多00234","shopName":"隔壁老李","mobile":"15225700235","profile":"阿萨德"},"name":"现在","cover":"5913b95e1a23a70001608de3","coverAndUrl":"http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5913b95e1a23a70001608de3","profile":"好吃","localLifeCategory":{"id":13,"parentId":1,"name":"自助餐","profile":"自助餐","fee":3,"sequence":4,"hot":true},"type":1,"teamBuyPrice":11,"cloudIntPercent":30,"validityPeriod":11,"consumeStartTime":0,"consumeEndTime":23,"purchaseNote":"[\n  {\n    \"content\" : \"是\",\n    \"key\" : \"1000\",\n    \"val\" : \"\",\n    \"title\" : \"是否支持极速退款\",\n    \"type\" : \"1\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1001\",\n    \"val\" : \"\",\n    \"title\" : \"是否支持wifi\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"是\",\n    \"key\" : \"1002\",\n    \"val\" : \"\",\n    \"title\" : \"是否展示发票\",\n    \"type\" : \"1\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1003\",\n    \"val\" : \"\",\n    \"title\" : \"是否享有优惠\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1004\",\n    \"val\" : \"\",\n    \"title\" : \"是否预约\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1005\",\n    \"val\" : \"\",\n    \"title\" : \"是否限制最高销量(接待量)\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1006\",\n    \"val\" : \"\",\n    \"title\" : \"是否限购团购券\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1007\",\n    \"val\" : \"\",\n    \"title\" : \"是否限用团购券\",\n    \"type\" : \"0\"\n  },\n  {\n    \"content\" : \"否\",\n    \"key\" : \"1008\",\n    \"val\" : \"\",\n    \"title\" : \"是否限制团购券使用人数\",\n    \"type\" : \"0\"\n  }\n]","status":4,"label":null,"onlineTime":"2017-05-31 16:42:06","offlineTime":"2017-06-06 18:40:36","createTime":"2017-05-11 09:09:08","updateTime":"2017-05-11 09:09:08","sequence":null,"setMealList":[],"goodsType":1,"barCode":"","goodsInTrade":-1,"mAccount":0,"hot":false}
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 33
         * localShop : {"name":"唯多00234","shopName":"隔壁老李","mobile":"15225700235","profile":"阿萨德"}
         * name : 现在
         * cover : 5913b95e1a23a70001608de3
         * coverAndUrl : http://192.168.0.196:8080/api/v1/mgs/file/download?fid=5913b95e1a23a70001608de3
         * profile : 好吃
         * localLifeCategory : {"id":13,"parentId":1,"name":"自助餐","profile":"自助餐","fee":3,"sequence":4,"hot":true}
         * type : 1
         * teamBuyPrice : 11
         * cloudIntPercent : 30
         * validityPeriod : 11
         * consumeStartTime : 0
         * consumeEndTime : 23
         * purchaseNote : [
         {
         "content" : "是",
         "key" : "1000",
         "val" : "",
         "title" : "是否支持极速退款",
         "type" : "1"
         },
         {
         "content" : "否",
         "key" : "1001",
         "val" : "",
         "title" : "是否支持wifi",
         "type" : "0"
         },
         {
         "content" : "是",
         "key" : "1002",
         "val" : "",
         "title" : "是否展示发票",
         "type" : "1"
         },
         {
         "content" : "否",
         "key" : "1003",
         "val" : "",
         "title" : "是否享有优惠",
         "type" : "0"
         },
         {
         "content" : "否",
         "key" : "1004",
         "val" : "",
         "title" : "是否预约",
         "type" : "0"
         },
         {
         "content" : "否",
         "key" : "1005",
         "val" : "",
         "title" : "是否限制最高销量(接待量)",
         "type" : "0"
         },
         {
         "content" : "否",
         "key" : "1006",
         "val" : "",
         "title" : "是否限购团购券",
         "type" : "0"
         },
         {
         "content" : "否",
         "key" : "1007",
         "val" : "",
         "title" : "是否限用团购券",
         "type" : "0"
         },
         {
         "content" : "否",
         "key" : "1008",
         "val" : "",
         "title" : "是否限制团购券使用人数",
         "type" : "0"
         }
         ]
         * status : 4
         * label : null
         * onlineTime : 2017-05-31 16:42:06
         * offlineTime : 2017-06-06 18:40:36
         * createTime : 2017-05-11 09:09:08
         * updateTime : 2017-05-11 09:09:08
         * sequence : null
         * setMealList : []
         * goodsType : 1
         * barCode :
         * goodsInTrade : -1
         * mAccount : 0
         * hot : false
         */

        @SerializedName("id")
        private int id;
        @SerializedName("localShop")
        private LocalShopBean localShop;
        @SerializedName("name")
        private String name;
        @SerializedName("cover")
        private String cover;
        @SerializedName("coverAndUrl")
        private String coverAndUrl;
        @SerializedName("profile")
        private String profile;
        @SerializedName("localLifeCategory")
        private LocalLifeCategoryBean localLifeCategory;
        @SerializedName("type")
        private int type;
        @SerializedName("teamBuyPrice")
        private double teamBuyPrice;
        @SerializedName("cloudIntPercent")
        private int cloudIntPercent;
        @SerializedName("validityPeriod")
        private int validityPeriod;
        @SerializedName("consumeStartTime")
        private int consumeStartTime;
        @SerializedName("consumeEndTime")
        private int consumeEndTime;
        @SerializedName("purchaseNote")
        private String purchaseNote;
        @SerializedName("status")
        private int status;
        @SerializedName("label")
        private Object label;
        @SerializedName("onlineTime")
        private String onlineTime;
        @SerializedName("offlineTime")
        private String offlineTime;
        @SerializedName("createTime")
        private String createTime;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("sequence")
        private Object sequence;
        @SerializedName("goodsType")
        private int goodsType;
        @SerializedName("barCode")
        private String barCode;
        @SerializedName("goodsInTrade")
        private int goodsInTrade;
        @SerializedName("mAccount")
        private double mAccount;
        @SerializedName("hot")
        private boolean hot;
        @SerializedName("setMealList")
        private List<SetMealListBean> setMealList;
        public static class SetMealListBean {
            @SerializedName("id")
            private int id;
            @SerializedName("name")
            private String name;
            @SerializedName("cover")
            private String cover;
            @SerializedName("price")
            private double price;
            @SerializedName("profile")
            private Object profile;
            @SerializedName("count")
            private int count;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public Object getProfile() {
                return profile;
            }

            public void setProfile(Object profile) {
                this.profile = profile;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LocalShopBean getLocalShop() {
            return localShop;
        }

        public void setLocalShop(LocalShopBean localShop) {
            this.localShop = localShop;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCoverAndUrl() {
            return coverAndUrl;
        }

        public void setCoverAndUrl(String coverAndUrl) {
            this.coverAndUrl = coverAndUrl;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public LocalLifeCategoryBean getLocalLifeCategory() {
            return localLifeCategory;
        }

        public void setLocalLifeCategory(LocalLifeCategoryBean localLifeCategory) {
            this.localLifeCategory = localLifeCategory;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getTeamBuyPrice() {
            return teamBuyPrice;
        }

        public void setTeamBuyPrice(double teamBuyPrice) {
            this.teamBuyPrice = teamBuyPrice;
        }

        public int getCloudIntPercent() {
            return cloudIntPercent;
        }

        public void setCloudIntPercent(int cloudIntPercent) {
            this.cloudIntPercent = cloudIntPercent;
        }

        public int getValidityPeriod() {
            return validityPeriod;
        }

        public void setValidityPeriod(int validityPeriod) {
            this.validityPeriod = validityPeriod;
        }

        public int getConsumeStartTime() {
            return consumeStartTime;
        }

        public void setConsumeStartTime(int consumeStartTime) {
            this.consumeStartTime = consumeStartTime;
        }

        public int getConsumeEndTime() {
            return consumeEndTime;
        }

        public void setConsumeEndTime(int consumeEndTime) {
            this.consumeEndTime = consumeEndTime;
        }

        public String getPurchaseNote() {
            return purchaseNote;
        }

        public void setPurchaseNote(String purchaseNote) {
            this.purchaseNote = purchaseNote;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }

        public String getOnlineTime() {
            return onlineTime;
        }

        public void setOnlineTime(String onlineTime) {
            this.onlineTime = onlineTime;
        }

        public String getOfflineTime() {
            return offlineTime;
        }

        public void setOfflineTime(String offlineTime) {
            this.offlineTime = offlineTime;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getSequence() {
            return sequence;
        }

        public void setSequence(Object sequence) {
            this.sequence = sequence;
        }

        public int getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(int goodsType) {
            this.goodsType = goodsType;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public int getGoodsInTrade() {
            return goodsInTrade;
        }

        public void setGoodsInTrade(int goodsInTrade) {
            this.goodsInTrade = goodsInTrade;
        }

        public double getMAccount() {
            return mAccount;
        }

        public void setMAccount(double mAccount) {
            this.mAccount = mAccount;
        }

        public boolean isHot() {
            return hot;
        }

        public void setHot(boolean hot) {
            this.hot = hot;
        }

        public List<SetMealListBean > getSetMealList() {
            return setMealList;
        }

        public void setSetMealList(List<SetMealListBean > setMealList) {
            this.setMealList = setMealList;
        }

        public static class LocalShopBean {
            /**
             * name : 唯多00234
             * shopName : 隔壁老李
             * mobile : 15225700235
             * profile : 阿萨德
             */

            @SerializedName("name")
            private String name;
            @SerializedName("shopName")
            private String shopName;
            @SerializedName("mobile")
            private String mobile;
            @SerializedName("profile")
            private String profile;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }
        }

        public static class LocalLifeCategoryBean {
            /**
             * id : 13
             * parentId : 1
             * name : 自助餐
             * profile : 自助餐
             * fee : 3
             * sequence : 4
             * hot : true
             */

            @SerializedName("id")
            private int id;
            @SerializedName("parentId")
            private int parentId;
            @SerializedName("name")
            private String name;
            @SerializedName("profile")
            private String profile;
            @SerializedName("fee")
            private int fee;
            @SerializedName("sequence")
            private int sequence;
            @SerializedName("hot")
            private boolean hot;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfile() {
                return profile;
            }

            public void setProfile(String profile) {
                this.profile = profile;
            }

            public int getFee() {
                return fee;
            }

            public void setFee(int fee) {
                this.fee = fee;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }

            public boolean isHot() {
                return hot;
            }

            public void setHot(boolean hot) {
                this.hot = hot;
            }
        }
    }


}
