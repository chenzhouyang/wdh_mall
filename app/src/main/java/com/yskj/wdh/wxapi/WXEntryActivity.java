package com.yskj.wdh.wxapi;


import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    private IWXAPI api;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int code = baseResp.errCode;
        switch (code){
            case 0://支付成功后的界面
                finish();
                break;
            case -1:
                finish();
                break;
            case -2://用户取消支付后的界面
                finish();
                break;
        }
    }
}
