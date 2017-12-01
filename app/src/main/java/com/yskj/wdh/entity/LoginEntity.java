package com.yskj.wdh.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
public class LoginEntity {

    /**
     * access_token : 2cefb4b5-0e3d-486a-a122-243f5d59eec5
     * token_type : bearer
     * expires_in : 41691
     * scope : api
     */

    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("token_type")
    public String tokenType;
    @SerializedName("expires_in")
    public int expiresIn;
    @SerializedName("scope")
    public String scope;
}
