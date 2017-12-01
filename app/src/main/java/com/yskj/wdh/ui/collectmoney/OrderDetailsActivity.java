package com.yskj.wdh.ui.collectmoney;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.OrderDetailsAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.OrderDetailsEntity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class OrderDetailsActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.orderdetails_name)
    TextView orderdetailsName;
    @Bind(R.id.orderdetails_price)
    TextView orderdetailsPrice;
    @Bind(R.id.orderdetails_lv)
    PullToRefreshListView orderdetailsLv;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String orderid;
    private OrderDetailsEntity orderDetailsEntity;
    private OrderDetailsAdapter detailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        ButterKnife.bind(this);
        orderid = getIntent().getStringExtra("orderid");
        txtTitle.setText("订单详情");
        getdata();
    }

    private void getdata() {
        OkHttpUtils.get().url(Urls.DETAIL).addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId",orderid)
                .build().execute(new StringCallback() {
            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                stopMyDialog();
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
                startMyDialog();
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                orderDetailsEntity = new Gson().fromJson(response,new TypeToken<OrderDetailsEntity>(){}.getType());
                orderdetailsName.setText(orderDetailsEntity.data.orderName);
                orderdetailsPrice.setText("合计："+StringUtils.getStringtodouble(orderDetailsEntity.data.totalAmount));
                detailsAdapter = new OrderDetailsAdapter(context,orderDetailsEntity.data.relations);
                orderdetailsLv.setAdapter(detailsAdapter);
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

}
