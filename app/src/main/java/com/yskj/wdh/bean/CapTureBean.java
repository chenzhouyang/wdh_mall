package com.yskj.wdh.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YSKJ-02 on 2017/2/8.
 */

public class CapTureBean {

    /**
     * code : 0
     * message : 成功
     * data :
     */

    @SerializedName("code")
    private int code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private String data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
