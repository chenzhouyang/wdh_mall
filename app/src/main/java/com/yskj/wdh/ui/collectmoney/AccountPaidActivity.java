package com.yskj.wdh.ui.collectmoney;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ProsceniumAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.ProsceniumEntity;
import com.yskj.wdh.url.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class AccountPaidActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.accountpaid_lv)
    PullToRefreshListView accountpaidLv;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ProsceniumAdapter prosceniumAdapter;
    private ProsceniumEntity prosceniumEntity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountpaid);
        ButterKnife.bind(this);
        txtTitle.setText("已付款");
        getdata();
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
    private void getdata() {
        OkHttpUtils.get().url(Urls.PROCSE).addHeader("Authorization", caches.get("access_token"))
                .addParams("status", "2")
                .addParams("count", "10")
                .addParams("cursor", "0")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                prosceniumEntity = new Gson().fromJson(response, new TypeToken<ProsceniumEntity>() {
                }.getType());
                prosceniumAdapter = new ProsceniumAdapter(context, prosceniumEntity.data.list,0);
                accountpaidLv.setAdapter(prosceniumAdapter);
            }
        });
    }
}
