package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.yskj.wdh.adapter.ProviderManagerAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.PostGoodsListEntity;
import com.yskj.wdh.bean.ProviderGoodsEntity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 创建日期 2017/8/1on 9:29.
 * 描述：供应商中商品管理页面
 * 作者：姜贺YSKJ-JH
 */

public class ProviderManagerActivity extends BaseActivity implements View.OnClickListener ,ProviderManagerAdapterInterface{
    TextView tvAddGoods;
    TextView tvOrderOrGot;
    ImageView imgBack;
    TextView txtTitle;
    ImageView imageRight;
    TextView tvSold;
    TextView tvSoldOut;
    TextView tvAddTime;
    TextView tvSoldCount;
    TextView tvHasCount;
    PullToRefreshListView lvGoods;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private PostGoodsListEntity goodsListEntity;//提交json数据实体类
    private ArrayList<ProviderGoodsEntity.DataBean> goodsListALL = new ArrayList<>();//全部数据
    private ArrayList<ProviderGoodsEntity.DataBean> goodsListPage = new ArrayList<>();//请求的每一页的数据
    private ProviderManagerAdapter adapter;

    private int upOrDownStatus;//1. 上架审核中, 2.上架审核通过, 3. 上架审核失败4. 下架审核中 5.下架审核通过 6. 下架审核失败 7. 删除
    private String millId;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_manager);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        switch (goodsListEntity.status) {
            case 1:
                tvSold.setBackgroundResource(R.color.provider_sold_red);
                tvSold.setTextColor(getResources().getColor(R.color.white));
                tvSoldOut.setBackgroundResource(R.color.provider_sold_gray);
                tvSoldOut.setTextColor(getResources().getColor(R.color.provider_text_color_gray));
                break;
            case 2:
                tvSoldOut.setBackgroundResource(R.color.provider_sold_red);
                tvSoldOut.setTextColor(getResources().getColor(R.color.white));
                tvSold.setBackgroundResource(R.color.provider_sold_gray);
                tvSold.setTextColor(getResources().getColor(R.color.provider_text_color_gray));
                break;
        }
    }

    private void initData() {
        goodsListEntity = new PostGoodsListEntity();
        goodsListEntity.millId=millId;
        goodsListEntity.status=1;//“1”:查询除了删除和已下架的所有状态的商品 “2”:查询已下架的商品
        goodsListEntity.orderParam="create_time";
        goodsListEntity.orderType = 0;//0递减 1递增
        page = 0;
        goodsListEntity.index = page;
        goodsListEntity.max = 10;
        adapter = new ProviderManagerAdapter(context,goodsListALL);
        lvGoods.setAdapter(adapter);
        adapter.setProviderManagerAdapterInterface(this);
        getData();
    }

    /**
     * 请求数据
     * @param
     */
    private void getData() {
        String json = new Gson().toJson(goodsListEntity).toString();
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.PROVIDERGOODSLIST).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new ProviderGoodsInfoCallBack());
    }

    //点击编辑
    @Override
    public void edit(int position) {
        String goodsId = goodsListALL.get(position).goodId;
        String millId = goodsListALL.get(position).millId;
        goToAddGoods(goodsId,millId,2);
    }

    //点击上下架
    @Override
    public void upOrDown(int position) {
        //1是商品上架状态  2是下架状态
        switch (goodsListALL.get(position).status) {
            case 0:
                upOrDownStatus = 1 ;
                break;
            case 1:
//                upOrDownStatus = 4 ;
                break;
            case 2:
                upOrDownStatus = 4 ;
                break;
            case 3:
                upOrDownStatus = 4 ;
                break;
            case 4:
//                upOrDownStatus = 4 ;
                break;
            case 5:
                upOrDownStatus = 1 ;
                break;
            case 6:
                upOrDownStatus = 1 ;
//                upOrDownStatus = 4 ;
                break;
            case 7:
//                upOrDownStatus = 4 ;
                break;
        }
        upDataAdapter(position);
    }

    //状态变更后请求数据
    private void upDataAdapter(int position) {
        String goodsId = goodsListALL.get(position).goodId;
        Logger.d(upOrDownStatus+"upOrDownStatus"+goodsListALL.get(position).status);
        OkHttpUtils.get().url(Urls.GOODSSTATUSCHANGE+"/"+upOrDownStatus+"/"+goodsId).addHeader(Config.HEADTOKEN,token).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                Logger.d(response);
                int code = (int) map.get("code");
                showToast(Messge.geterr_code(code));
                if (code==0){
                    goodsListEntity.index = 0;
                    getData();
                }else {
                    showToast(Messge.geterr_code(code));
                }
            }
        });
    }

    //点击删除
    @Override
    public void delete(int position) {
        upOrDownStatus = 7;
        upDataAdapter(position);
    }

    private class ProviderGoodsInfoCallBack extends Callback<ProviderGoodsEntity>{

        @Override
        public ProviderGoodsEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            ProviderGoodsEntity recordEntiity = new Gson().fromJson(s,new TypeToken<ProviderGoodsEntity>(){}.getType());
            return recordEntiity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(ProviderGoodsEntity response, int id) {
            if (goodsListEntity.index == 0) {
                goodsListALL.clear();
            }
            //每次清空上页数据
            goodsListPage.clear();
            switch (response.code) {
                case 0:
                    goodsListPage = response.data;
                    goodsListALL.addAll(goodsListPage);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void initView() {
        token = caches.get("access_token");
        millId = getIntent().getStringExtra("millId");//暂时为1
        tvAddGoods = (TextView) findViewById(R.id.tv_add_goods);
        tvOrderOrGot = (TextView) findViewById(R.id.tv_order_or_got);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvSold = (TextView) findViewById(R.id.tv_sold);
        tvSoldOut = (TextView) findViewById(R.id.tv_sold_out);
        tvAddTime = (TextView) findViewById(R.id.tv_add_time);
        tvSoldCount = (TextView) findViewById(R.id.tv_sold_count);
        tvHasCount = (TextView) findViewById(R.id.tv_has_count);
        lvGoods = (PullToRefreshListView) findViewById(R.id.lv_goods);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imageRight = (ImageView) findViewById(R.id.image_right);

        txtTitle.setText("商品管理");
        imgBack.setOnClickListener(this);

        tvSold.setOnClickListener(this);
        tvSoldOut.setOnClickListener(this);
        tvAddTime.setOnClickListener(this);
        tvSoldCount.setOnClickListener(this);
        tvHasCount.setOnClickListener(this);

        tvAddGoods.setOnClickListener(this);
        tvOrderOrGot.setOnClickListener(this);


        lvGoods.setMode(PullToRefreshBase.Mode.BOTH);
        lvGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page=0;
                goodsListEntity.index=page;
                getData();
                MethodUtils.stopRefresh(lvGoods);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                goodsListEntity.index=page*10;
                getData();
                MethodUtils.stopRefresh(lvGoods);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_add_goods:
                goToAddGoods("",millId,1);
                break;
            case R.id.tv_order_or_got:
                startActivity(new Intent(context, OrderManagerActivity.class));
                break;
            case R.id.tv_sold:
                page = 0;
                goodsListEntity.index = page;
                goodsListEntity.status = 1;
                goodsListALL.clear();
                getData();
                tvSold.setBackgroundResource(R.color.provider_sold_red);
                tvSold.setTextColor(getResources().getColor(R.color.white));
                tvSoldOut.setBackgroundResource(R.color.provider_sold_gray);
                tvSoldOut.setTextColor(getResources().getColor(R.color.provider_text_color_gray));
                break;
            case R.id.tv_sold_out:
                page = 0;
                goodsListEntity.index = page;
                goodsListEntity.status = 2;
                goodsListALL.clear();
                getData();
                tvSoldOut.setBackgroundResource(R.color.provider_sold_red);
                tvSoldOut.setTextColor(getResources().getColor(R.color.white));
                tvSold.setBackgroundResource(R.color.provider_sold_gray);
                tvSold.setTextColor(getResources().getColor(R.color.provider_text_color_gray));
                break;
            case R.id.tv_add_time:
                setSortData("create_time",tvAddTime,tvSoldCount,tvHasCount);
                break;
            case R.id.tv_sold_count:
                setSortData("volume",tvSoldCount,tvHasCount,tvAddTime);
                break;
            case R.id.tv_has_count:
                setSortData("stock",tvHasCount,tvSoldCount,tvAddTime);
                break;
        }
    }

    /**
     * 排序显示按钮颜色及图片并刷新数据
     * @param orderParam 用于修改状态比较
     * @param checkView 点击的view
     * @param tvSort1  另外的view
     * @param tvSort2   另外的view
     */
    private void setSortData(String orderParam, TextView checkView, TextView tvSort1, TextView tvSort2) {
        Drawable drawable = null;
        if (goodsListEntity.orderParam.equals(orderParam)){
            if (goodsListEntity.orderType==0){
                goodsListEntity.orderType = 1;
                drawable= getResources().getDrawable(R.mipmap.img_status_up);
            }else {
                goodsListEntity.orderType = 0;
                drawable= getResources().getDrawable(R.mipmap.img_status_down);
            }
        }else {
            goodsListEntity.orderType = 0;
            drawable= getResources().getDrawable(R.mipmap.img_status_down);
        }
        goodsListEntity.orderParam = orderParam;
        goodsListALL.clear();
        page = 0;
        goodsListEntity.index = page;
        getData();
        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
        checkView.setCompoundDrawables(null , null,drawable , null  );
        checkView.setTextColor(getResources().getColor(R.color.provider_add_time_red));
        drawable= getResources().getDrawable(R.mipmap.img_status_gray);
        drawable.setBounds( 0 ,  0 , drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvSort1.setTextColor(getResources().getColor(R.color.provider_add_time_black));
        tvSort2.setTextColor(getResources().getColor(R.color.provider_add_time_black));
        tvSort2.setCompoundDrawables(null , null,drawable , null );
        tvSort1.setCompoundDrawables(null , null,drawable , null );
    }

    private void goToAddGoods(String goodsId,String millId,int operaType) {
        PublicWay.goodsContentList.clear();
        Intent intent  = new Intent(context,AddGoodsActivity.class);
        intent.putExtra("goodsId",goodsId);
        intent.putExtra("millId",millId);
        intent.putExtra("operaType",operaType);
        startActivity(intent);
    }
}
