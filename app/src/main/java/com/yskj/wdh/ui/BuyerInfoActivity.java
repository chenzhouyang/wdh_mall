package com.yskj.wdh.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BuyerInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/5/26on 16:17.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class BuyerInfoActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_buy_count)
    TextView tvBuyCount;
    @Bind(R.id.tv_number)
    TextView tvNumber;
    private String orderId;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String count;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_info);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.BUYERINFO).addHeader(Config.HEADTOKEN,caches.get("access_token")).addParams("orderId",orderId).build().execute(new buyerInfoCallBack());
    }

    private class buyerInfoCallBack extends Callback<BuyerInfoBean>{
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            super.onAfter(id);
        }

        @Override
        public BuyerInfoBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            BuyerInfoBean buyerInfoBean = new Gson().fromJson(s, new TypeToken<BuyerInfoBean>() {}.getType());
            return buyerInfoBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(final BuyerInfoBean response, int id) {
            switch (response.code){
                case 0:
                    switch (type){
                        case "seeBuyer":
                            tvName.setText(response.data.buyerName);
                            tvAddress.setText(response.data.buyerAddress);
                            tvNumber.setText(response.data.buyerMobile);
                            tvNumber.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (response.data.buyerMobile == null || "".equals(response.data.buyerMobile.trim())) {
                                        Toast.makeText(getApplicationContext(), "暂时没用电话号码",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent0 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + response.data.buyerMobile));
                                        startActivity(intent0);
                                    }
                                }
                            });
                            break;
                        case "seeSeller":
                            tvName.setText(response.data.sellerName);
                            tvAddress.setText(response.data.sellerAddress);
                            tvNumber.setText(response.data.sellerMobile);
                            tvNumber.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (response.data.buyerMobile == null || "".equals(response.data.buyerMobile.trim())) {
                                        Toast.makeText(getApplicationContext(), "暂时没用电话号码",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent0 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + response.data.sellerMobile));
                                        startActivity(intent0);
                                    }
                                }
                            });
                            break;
                    }
                    tvBuyCount.setText("x"+count);
                    break;
                case 734:
                    showToast("采购申请人未注册商家或者状态异常");
                    break;
            }
        }
    }

    private void initView() {
        orderId = getIntent().getStringExtra("orderId");
        count = getIntent().getStringExtra("count");
        type = getIntent().getStringExtra("type");
        switch (type){
            case "seeBuyer":
                txtTitle.setText("采购人信息");
                break;
            case "seeSeller":
                txtTitle.setText("审批人信息");
                break;
        }
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
