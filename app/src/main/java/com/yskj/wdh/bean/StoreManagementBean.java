package com.yskj.wdh.bean;

/**
 * Created by wdx on 2016/11/11 0011.
 */
public class StoreManagementBean {

    /**
     * error_code : 0
     * error_msg : SUCCESS
     * ret_data : {"approvalOpinion":null,"areaId":1,"areaString":"河南省许昌市尚集镇跨境电商基地2号楼11层","categoryId":1,"cityId":null,"cover":"http://localhost:8080/api/document/getFile?fileId=url地址","createTime":"2016-11-09 11:28:41","detailAddress":"跨境电商基地2号楼11层","idCardBack":"http://localhost:8080/api/document/getFile?fileId=url地址","idCardFront":"http://localhost:8080/api/document/getFile?fileId=url地址","industry":null,"latitude":null,"level":1,"licence":"http://localhost:8080/api/document/getFile?fileId=url地址","longitude":null,"mobile":"18201992953","name":"云客92953","profile":"老中医","provinceId":null,"shopName":"小帅店铺","status":0,"totalSendRed":null,"updateTime":"2016-11-09 11:28:41","vipLevel":0}
     */

    private int error_code;
    private String error_msg;
    /**
     * approvalOpinion : null
     * areaId : 1
     * areaString : 河南省许昌市尚集镇跨境电商基地2号楼11层
     * categoryId : 1
     * cityId : null
     * cover : http://localhost:8080/api/document/getFile?fileId=url地址
     * createTime : 2016-11-09 11:28:41
     * detailAddress : 跨境电商基地2号楼11层
     * idCardBack : http://localhost:8080/api/document/getFile?fileId=url地址
     * idCardFront : http://localhost:8080/api/document/getFile?fileId=url地址
     * industry : null
     * latitude : null
     * level : 1
     * licence : http://localhost:8080/api/document/getFile?fileId=url地址
     * longitude : null
     * mobile : 18201992953
     * name : 云客92953
     * profile : 老中医
     * provinceId : null
     * shopName : 小帅店铺
     * status : 0
     * totalSendRed : null
     * updateTime : 2016-11-09 11:28:41
     * vipLevel : 0
     */

    private RetDataBean ret_data;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public RetDataBean getRet_data() {
        return ret_data;
    }

    public void setRet_data(RetDataBean ret_data) {
        this.ret_data = ret_data;
    }

    public static class RetDataBean {
        private Object approvalOpinion;
        private int areaId;
        private String areaString;
        private int categoryId;
        private Object cityId;
        private String cover;
        private String createTime;
        private String detailAddress;
        private String idCardBack;
        private String idCardFront;
        private Object industry;
        private Object latitude;
        private int level;
        private String licence;
        private Object longitude;
        private String mobile;
        private String name;
        private String profile;
        private Object provinceId;
        private String shopName;
        private int status;
        private Object totalSendRed;
        private String updateTime;
        private int vipLevel;

        public Object getApprovalOpinion(Object approvalOpinion) {
            return this.approvalOpinion;
        }

        public void setApprovalOpinion(Object approvalOpinion) {
            this.approvalOpinion = approvalOpinion;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getAreaString() {
            return areaString;
        }

        public void setAreaString(String areaString) {
            this.areaString = areaString;
        }

        public int getCategoryId(int categoryId) {
            return this.categoryId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public Object getCityId() {
            return cityId;
        }

        public void setCityId(Object cityId) {
            this.cityId = cityId;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDetailAddress(String detailAddress) {
            return this.detailAddress;
        }

        public void setDetailAddress(String detailAddress) {
            this.detailAddress = detailAddress;
        }

        public String getIdCardBack() {
            return idCardBack;
        }

        public void setIdCardBack(String idCardBack) {
            this.idCardBack = idCardBack;
        }

        public String getIdCardFront() {
            return idCardFront;
        }

        public void setIdCardFront(String idCardFront) {
            this.idCardFront = idCardFront;
        }

        public Object getIndustry() {
            return industry;
        }

        public void setIndustry(Object industry) {
            this.industry = industry;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLicence() {
            return licence;
        }

        public void setLicence(String licence) {
            this.licence = licence;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public String getMobile(String mobile) {
            return this.mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public Object getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Object provinceId) {
            this.provinceId = provinceId;
        }

        public String getShopName(String shopName) {
            return this.shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getTotalSendRed() {
            return totalSendRed;
        }

        public void setTotalSendRed(Object totalSendRed) {
            this.totalSendRed = totalSendRed;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getVipLevel() {
            return vipLevel;
        }

        public void setVipLevel(int vipLevel) {
            this.vipLevel = vipLevel;
        }
    }
}
