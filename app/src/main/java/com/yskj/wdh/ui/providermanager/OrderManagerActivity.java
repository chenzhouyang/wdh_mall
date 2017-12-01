package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.MallOrderListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.MallOrderListBean;
import com.yskj.wdh.bean.MallOrderListCountBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 创建日期 2017/8/12on 16:55.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class OrderManagerActivity extends BaseActivity implements View.OnClickListener{
    ImageView imgBack;
    TextView txtTitle;
    TextView tvRight;
    TextView tvOrderWait;
    TextView tvOrderSend;
    TextView tvOrderOk;
    PullToRefreshListView lvOrder;
    private int cursor = 0;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private String orderStatus ="0";//订单状态： 0表示已付款待发货 1：表示已发货 2：表示确认收货
    private String count="10";//每页最大记录数
    private ArrayList<MallOrderListBean.DataBean.OrderListBean> goodsOrderListPage = new ArrayList<>();//每页数据
    private ArrayList<MallOrderListBean.DataBean.OrderListBean> goodsOrderListALL= new ArrayList<>();//总数据
    private MallOrderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initOrderCount();
        getData();
    }

    private void initOrderCount() {
        OkHttpUtils.get().url(Urls.MALLORDERLISTCOUNT).addHeader(Config.HEADTOKEN,token).build().execute(new MallOrderListCountCallBack());
    }

    private class MallOrderListCountCallBack extends Callback<MallOrderListCountBean>{

        @Override
        public MallOrderListCountBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            MallOrderListCountBean mallOrderListCountBean = new Gson().fromJson(s,new TypeToken<MallOrderListCountBean>(){}.getType());
            return mallOrderListCountBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(MallOrderListCountBean response, int id) {
            if (response.code==0){
                tvOrderWait.setText(response.data.toSendCount+"\n待发货");
                tvOrderSend.setText(response.data.shippedCount+"\n已发货");
                tvOrderOk.setText(response.data.confirmCount+"\n已完成");
            }
        }
    }


    private void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvOrderWait = (TextView) findViewById(R.id.tv_order_wait);
        tvOrderSend = (TextView) findViewById(R.id.tv_order_send);
        tvOrderOk = (TextView) findViewById(R.id.tv_order_ok);
        lvOrder = (PullToRefreshListView) findViewById(R.id.lv_order);
        token = caches.get("access_token");

        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvOrderWait.setOnClickListener(this);
        tvOrderSend.setOnClickListener(this);
        tvOrderOk.setOnClickListener(this);

        adapter = new MallOrderListAdapter(context,goodsOrderListALL);
        lvOrder.setAdapter(adapter);
        lvOrder.setMode(PullToRefreshBase.Mode.BOTH);
        lvOrder.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor=0;
                getData();
                MethodUtils.stopRefresh(lvOrder);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                getData();
                MethodUtils.stopRefresh(lvOrder);
            }
        });
    }

    private void getData() {
        OkHttpUtils.get().url(Urls.MALLORDERLIST).addHeader(Config.HEADTOKEN,token).addParams("orderStatus",orderStatus).addParams("count",count).addParams("cursor",cursor*10+"").build().execute(new MallOrderListCallBack());
    }

    private class MallOrderListCallBack extends Callback<MallOrderListBean>{

        @Override
        public MallOrderListBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            MallOrderListBean mallOrderListBean = new Gson().fromJson(s,new TypeToken<MallOrderListBean>(){}.getType());
            return mallOrderListBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(MallOrderListBean response, int id) {
            if (cursor == 0) {
                goodsOrderListALL.clear();
            }
            //每次清空上页数据
            goodsOrderListPage.clear();
            switch (response.code) {
                case 0:
                    goodsOrderListPage = response.data.orderList;
                    goodsOrderListALL.addAll(goodsOrderListPage);
                    adapter.setOrderStatus(orderStatus);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_right:
                startActivity(new Intent(context,IncomeActivity.class));
                break;
            case R.id.tv_order_wait:
                orderStatus="0";
                cursor = 0;
                getData();
                initOrderCount();
                break;
            case R.id.tv_order_send:
                orderStatus="1";
                cursor = 0;
                getData();
                initOrderCount();
                break;
            case R.id.tv_order_ok:
                orderStatus="2";
                cursor = 0;
                getData();
                initOrderCount();
                break;
        }
    }
}
