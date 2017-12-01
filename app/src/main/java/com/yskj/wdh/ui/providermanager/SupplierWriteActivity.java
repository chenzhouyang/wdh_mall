package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.yskj.wdh.R.id.suppliers_write_abstract;

/**
 * 陈宙洋
 * 2017/8/5.
 * 描述：申请资料填写activity
 */

public class SupplierWriteActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(suppliers_write_abstract)
    EditText suppliersWriteAbstract;
    @Bind(R.id.suppliers_write_url)
    EditText suppliersWriteUrl;
    @Bind(R.id.suppliers_write_restrict)
    EditText suppliersWriteRestrict;
    @Bind(R.id.suppliers_next)
    Button suppliersNext;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private BusinessInfoBean bean;
    private int millStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_write);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        txtTitle.setText("资料填写");

        bean = JSONFormat.parseT(caches.get("business"), BusinessInfoBean.class);
        if (caches.get("business").contains("mill")){
            millStatus = bean.mill.status;
        }else {
            millStatus = -1;
        }
        //-1表示未提交过申请；0表示已提交待审批；1表示审批通过；2表示被驳回
        switch (millStatus) {
            case 2:
                suppliersWriteAbstract.setText(bean.mill.content);
                suppliersWriteUrl.setText(bean.mill.url);
                suppliersWriteRestrict.setText(bean.mill.largeOrderLimit);
                break;
        }
    }

    @OnClick({R.id.img_back, R.id.suppliers_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.suppliers_next:
                applyProviderManager();
                break;
        }
    }

    private void applyProviderManager() {
        final String content = suppliersWriteAbstract.getText().toString();
        if (TextUtils.isEmpty(content)){
            showToast("请填写简介");
            return;
        }
        String url = suppliersWriteUrl.getText().toString();
        if (TextUtils.isEmpty(url)){
            showToast("请填写视频链接");
            return;
        }
        String largeOrderLimit = suppliersWriteRestrict.getText().toString();
        if (TextUtils.isEmpty(largeOrderLimit)){
            showToast("请填写最低限额");
            return;
        }
        token =caches.get("access_token");
        Logger.d(token);
        OkHttpUtils.post().url(Urls.PROVIDERMANAGER).addHeader(Config.HEADTOKEN,token).addParams("content",content).addParams("url",url).addParams("largeOrderLimit",largeOrderLimit).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                switch (code){
                    case 0:
                        showToast("申请成功");
                        startActivity(new Intent(SupplierWriteActivity.this,SupplinerSpresentActivity.class));
                        break;
                    case 732:
                        showToast("审批中");
                        break;
                    case 734:
                        showToast("该用户未注册商店");
                        break;
                    case 1023:
                        showToast("供应商已存在");
                        break;
                    case 1027:
                        showToast("低于供应商最低限额");
                        break;
                }
            }
        });
    }
}
