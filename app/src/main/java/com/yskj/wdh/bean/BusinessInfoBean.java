package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/16 0016.
 */
public class BusinessInfoBean implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("shopName")
    private String shopName;
    @SerializedName("areaCode")
    private String areaCode;
    @SerializedName("cityCode")
    private Object cityCode;
    @SerializedName("provinceCode")
    private Object provinceCode;
    @SerializedName("areaName")
    private String areaName;
    @SerializedName("categoryId")
    private int categoryId;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("areaString")
    private String areaString;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("cover")
    private String cover;
    @SerializedName("coverUrl")
    private String coverUrl;
    @SerializedName("idCardFront")
    private String idCardFront;
    @SerializedName("idCardFrontUrl")
    private String idCardFrontUrl;
    @SerializedName("idCardBack")
    private String idCardBack;
    @SerializedName("idCardBackUrl")
    private String idCardBackUrl;
    @SerializedName("licence")
    private String licence;
    @SerializedName("licenceUrl")
    private String licenceUrl;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("detailAddress")
    private String detailAddress;
    @SerializedName("approvalOpinion")
    private String approvalOpinion;
    @SerializedName("createTime")
    private String createTime;
    @SerializedName("updateTime")
    private String updateTime;
    @SerializedName("industry")
    private String industry;
    @SerializedName("profile")
    private String profile;
    @SerializedName("vipLevel")
    private int vipLevel;
    @SerializedName("level")
    private int level;
    @SerializedName("status")
    private int status;
    @SerializedName("totalSendRed")
    private double totalSendRed;
    @SerializedName("checkStatus")
    private int checkStatus;
    @SerializedName("totalProfit")
    private double totalProfit;
    @SerializedName("fundAccount")
    private double fundAccount;
    @SerializedName("machineStatus")
    private int machineStatus;
    public int getMachineStatus() {
        return machineStatus;
    }

    /**
     * agent : {"name":"哈哈","profile":"hahaha","level":1,"createTime":"2017-05-24 14:33:43","status":0,"remark":"","shopName":"电商","areaId":["14,西城区"],"parentId":["1,市辖区","2,西城区"]}
     */

    @SerializedName("agent")
    public AgentBean agent;

    @SerializedName("mill")
    public Mill mill;

    public static class Mill implements Serializable{
        @SerializedName("id")
        public String id;
        @SerializedName("localShopId")
        public String localShopId;
        @SerializedName("name")
        public String name;
        @SerializedName("content")
        public String content;
        @SerializedName("url")
        public String url;
        @SerializedName("status")
        public int status;
        @SerializedName("approvalOpinion")
        public String approvalOpinion;
        @SerializedName("largeOrderLimit")
        public String largeOrderLimit;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("updatedTime")
        public String updatedTime;
        @SerializedName("totalProfit")
        public String totalProfit;
        @SerializedName("enable")
        public boolean enable;
    }

    public static class AgentBean implements Serializable{
        /**
         * name : 哈哈
         * profile : hahaha
         * level : 1
         * createTime : 2017-05-24 14:33:43
         * status : 0
         * remark :
         * shopName : 电商
         * areaId : ["14,西城区"]
         * parentId : ["1,市辖区","2,西城区"]
         */

        @SerializedName("name")
        public String name;
        @SerializedName("profile")
        public String profile;
        @SerializedName("level")
        public int level;
        @SerializedName("createTime")
        public String createTime;
        @SerializedName("status")
        public int status;
        @SerializedName("remark")
        public String remark;
        @SerializedName("shopName")
        public String shopName;
        @SerializedName("areaId")
        public List<String> areaId;
        @SerializedName("parentId")
        public List<String> parentId;
    }

    public void setMachineStatus(int machineStatus) {
        this.machineStatus = machineStatus;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public void setCityCode(Object cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceCode(Object provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAreaString(String areaString) {
        this.areaString = areaString;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
    }

    public void setIdCardFrontUrl(String idCardFrontUrl) {
        this.idCardFrontUrl = idCardFrontUrl;
    }

    public void setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
    }

    public void setIdCardBackUrl(String idCardBackUrl) {
        this.idCardBackUrl = idCardBackUrl;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public void setLicenceUrl(String licenceUrl) {
        this.licenceUrl = licenceUrl;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public void setApprovalOpinion(String approvalOpinion) {
        this.approvalOpinion = approvalOpinion;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTotalSendRed(double totalSendRed) {
        this.totalSendRed = totalSendRed;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public void setFundAccount(int fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getName() {
        return name;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public Object getCityCode() {
        return cityCode;
    }

    public Object getProvinceCode() {
        return provinceCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getAreaString() {
        return areaString;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCover() {
        return cover;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getIdCardFront() {
        return idCardFront;
    }

    public String getIdCardFrontUrl() {
        return idCardFrontUrl;
    }

    public String getIdCardBack() {
        return idCardBack;
    }

    public String getIdCardBackUrl() {
        return idCardBackUrl;
    }

    public String getLicence() {
        return licence;
    }

    public String getLicenceUrl() {
        return licenceUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public String getApprovalOpinion() {
        return approvalOpinion;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getIndustry() {
        return industry;
    }

    public String getProfile() {
        return profile;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getStatus() {
        return status;
    }

    public double getTotalSendRed() {
        return totalSendRed;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public double getFundAccount() {
        return fundAccount;
    }
}
