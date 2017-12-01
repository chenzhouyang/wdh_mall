package com.yskj.wdh.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.ShareGoodsInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.NumberFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/5/26on 16:25.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.commodity_goods_name)
    TextView commodityGoodsName;
    @Bind(R.id.commodity_describe)
    TextView commodityDescribe;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.image_button_tel)
    ImageButton imageButtonTel;
    private String phoneNum;
    private String lifeId;
    private String token;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initView() {
        txtTitle.setText("商品详情");
        imgBack.setOnClickListener(this);
        imageButtonTel.setOnClickListener(this);
        lifeId = getIntent().getStringExtra("lifeId");
        token = caches.get("access_token");
        OkHttpUtils.get().url(Urls.SHAREGOODSINFO).addHeader(Config.HEADTOKEN,token).addParams("lifeId",lifeId).build().execute(new getShareInfoCallBack());
    }

    private class getShareInfoCallBack extends Callback<ShareGoodsInfoBean>{
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
        public ShareGoodsInfoBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ShareGoodsInfoBean shareGoodsInfoBean = new Gson().fromJson(s, new TypeToken<ShareGoodsInfoBean>() {}.getType());
            return shareGoodsInfoBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(ShareGoodsInfoBean response, int id) {
            switch (response.code){
                case 0:
                    ShareGoodsInfoBean.DataBean data = response.data;
                    if (!TextUtils.isEmpty(data.cover)){
                        Glide.with(context).load(data.cover).error(R.mipmap.img_error).into(img);
                    }
                    phoneNum = data.mobile;
                    commodityGoodsName.setText(data.goodName);
                    commodityDescribe.setText(data.profile);
                    tvPrice.setText("￥"+String.format("%.2f", NumberFormat.convertToDouble(data.price, 0d)));
                    tvName.setText(data.shopName);
                    tvAddress.setText(data.address);
                    break;
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_button_tel:
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(getApplicationContext(), "暂时没用电话号码",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent0 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
                    startActivity(intent0);
                }
                break;
        }
    }
}
