package com.yskj.wdh.bean;

/**
 * 创建日期 2017/8/14on 17:44.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class DialogCheckBean {
    public boolean isChecked;
    public String areaId;
    public String areaName;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
