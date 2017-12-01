package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.MallOrderDetailAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.MallOrderDetailBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.NumberFormat;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

import static com.yskj.wdh.R.id.tv_right;

/**
 * 创建日期 2017/8/14on 11:22.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener{
    private ImageView imgBack;
    private TextView txtTitle;
    private TextView tvRight;
    private TextView tvOrderNum;
    private TextView tvOrderTime;
    private TextView tvOrderPayTime;
    private NoScrollListView listView;
    private TextView tvOrderCountAndPrice;
    private TextView tvBuyerName;
    private TextView tvBuyerPhone;
    private TextView tvBuyerAddress;
    private TextView tvExpress;
    private TextView tvStatus;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token, orderId;
    private String expressName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        initData();
    }

    private void initData() {
        OkHttpUtils.get().url(Urls.MALLORDERDETAIL).addHeader(Config.HEADTOKEN, token).addParams("orderId", orderId).build().execute(new MallOrderDetailCallBack());
    }

    private void initView() {
        token = caches.get("access_token");
        orderId = getIntent().getStringExtra("orderId");
        tvStatus = (TextView) findViewById(R.id.tv_status);
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvRight = (TextView) findViewById(tv_right);
        tvOrderNum = (TextView) findViewById(R.id.tv_order_num);
        tvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        tvOrderPayTime = (TextView) findViewById(R.id.tv_order_pay_time);
        listView = (NoScrollListView) findViewById(R.id.listView);
        tvOrderCountAndPrice = (TextView) findViewById(R.id.tv_order_count_and_price);
        tvBuyerName = (TextView) findViewById(R.id.tv_buyer_name);
        tvBuyerPhone = (TextView) findViewById(R.id.tv_buyer_phone);
        tvBuyerAddress = (TextView) findViewById(R.id.tv_buyer_address);
        tvExpress = (TextView) findViewById(R.id.tv_express);

        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(context,SendLogisticsActivity.class);
                intent.putExtra("orderId",orderId);
//                intent.putExtra("expressName",expressName);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_express:

                break;
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) {
//            return;
//        }
//        String content = data.getStringExtra("data");
//        // 根据上面发送过去的请求别
//        switch (requestCode) {
//            case Config.LOGISTICSLIST:
//                expressName = content;
//                tvExpress.setText(expressName);
//                break;
//        }
//    }

    private class MallOrderDetailCallBack extends Callback<MallOrderDetailBean> {

        @Override
        public MallOrderDetailBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            MallOrderDetailBean mallOrderDetailBean = new Gson().fromJson(s, new TypeToken<MallOrderDetailBean>() {
            }.getType());
            return mallOrderDetailBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(MallOrderDetailBean response, int id) {
            if (response.code == 0) {
                MallOrderDetailBean.DataBean data = response.data;
                switch (data.shippingStatus) {
                    case 0:
                        tvStatus.setText("待发货");
                        tvRight.setVisibility(View.VISIBLE);
//                        tvExpress.setOnClickListener(OrderDetailActivity.this);
                        break;
                    case 1:
                        tvStatus.setText("已发货");
                        tvRight.setVisibility(View.GONE);
                        break;
                    case 2:
                        tvStatus.setText("已完成");
                        tvRight.setVisibility(View.GONE);
                        break;
                }
                tvOrderNum.setText(data.orderNo);
                tvOrderTime.setText(data.createTime);
                tvOrderPayTime.setText(data.payTime);
                expressName = data.shippingName;
                tvExpress.setText(expressName);
                MallOrderDetailAdapter adapter = new MallOrderDetailAdapter(context,data.millOrderGoodsVoList);
                listView.setAdapter(adapter);
                double price = 0;
                for (MallOrderDetailBean.DataBean.MillOrderGoodsVoListBean millOrderGoods : data.millOrderGoodsVoList) {
                    double mallGoodsPrice = millOrderGoods.goodsCount * millOrderGoods.goodsPrice;
                    price+=mallGoodsPrice;
                }
                tvOrderCountAndPrice.setText("共" + data.millOrderGoodsVoList.size() + "件商品 合计：￥" +String.format("%.2f", NumberFormat.convertToFloat(price,0.00f)));
                tvBuyerName.setText(data.consignee);
                tvBuyerPhone.setText(data.mobile);
                tvBuyerAddress.setText(data.address);
            }
        }
    }
}
