package com.yskj.wdh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ShareForAdapter;
import com.yskj.wdh.adapter.StraightForAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.JavaBaseBean;
import com.yskj.wdh.bean.ShareForBean;
import com.yskj.wdh.bean.StraightForBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/5/26on 9:39.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class StraightForActivity extends BaseActivity implements View.OnClickListener, StraightForAdapter.CheckInterface, StraightForAdapter.ModifyCountInterface {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.ck_all)
    CheckBox ckAll;
    @Bind(R.id.tv_settlement)
    TextView tvSettlement;
    @Bind(R.id.tv_show_price)
    TextView tvShowPrice;
    @Bind(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.img_straight)
    ImageView imgStraight;
    @Bind(R.id.ll_straight)
    LinearLayout llStraight;
    @Bind(R.id.img_share)
    ImageView imgShare;
    @Bind(R.id.ll_share)
    LinearLayout llShare;
    @Bind(R.id.tv_straight)
    TextView tvStraight;
    @Bind(R.id.tv_share)
    TextView tvShare;

    private StraightForAdapter straightForAdapter;
    private List<StraightForBean.DataBean> straightForBeanList = new ArrayList<>();
    private List<StraightForBean.DataBean> straightForBeanPage = new ArrayList<>();

    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private ShareForAdapter shareAdapter;
    private List<ShareForBean.DataBean.ListBean> shareForList = new ArrayList<>();
    private List<ShareForBean.DataBean.ListBean> shareForPage = new ArrayList<>();

    private String type;

    private int cursor = 0;
    private int count = 10;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_straight_for);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        switch (type){
            //直供商品适配器
            case "straight":
                cursor = 0;
                straightForBeanList.clear();
                rlBottom.setVisibility(View.VISIBLE);
                tvStraight.setTextColor(getResources().getColor(R.color.straight));
                tvShare.setTextColor(getResources().getColor(R.color.gray));
                listView.setAdapter(straightForAdapter);
                straightForAdapter.setStraightForBeanList(straightForBeanList);
                OkHttpUtils.get().url(Urls.STRAIGHTFOR).addHeader(Config.HEADTOKEN,token)
                        .addParams("count",String.valueOf(count)).addParams("cursor",String.valueOf(cursor*count))
                        .build().execute(new straightForCallBack());
                break;
            //分享商品适配器
            case "share":
                cursor = 0;
                shareForList.clear();
                rlBottom.setVisibility(View.GONE);
                tvStraight.setTextColor(getResources().getColor(R.color.gray));
                tvShare.setTextColor(getResources().getColor(R.color.straight));
                listView.setAdapter(shareAdapter);
                shareAdapter.setShareForBeanList(shareForList);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(context,GoodsDetailActivity.class);
                        intent.putExtra("lifeId",shareForList.get(position-1).goodsId);
                        startActivity(intent);
                    }
                });
                OkHttpUtils.get().url(Urls.SHAREFOR).addHeader(Config.HEADTOKEN,token)
                        .addParams("count",String.valueOf(count)).addParams("cursor",String.valueOf(cursor*count)).build().execute(new shareForCallBack());
                break;
        }
    }
    //分享商品适配器
    private class shareForCallBack extends Callback<ShareForBean>{

        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            listView.onRefreshComplete();
            super.onAfter(id);
        }

        @Override
        public ShareForBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ShareForBean straightForBean = new Gson().fromJson(s, new TypeToken<ShareForBean>() {}.getType());
            return straightForBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            listView.onRefreshComplete();
            isLogin(e);
        }

        @Override
        public void onResponse(ShareForBean response, int id) {
            if (response.code==0){
                shareForPage.clear();
                shareForPage.addAll(response.data.list);
                shareForList.addAll(shareForPage);
                shareAdapter.notifyDataSetChanged();
            }
        }
    }

    //直供商品适配器
    private class straightForCallBack extends Callback<StraightForBean>{

        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            listView.onRefreshComplete();
            super.onAfter(id);
        }

        @Override
        public StraightForBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            StraightForBean straightForBean = new Gson().fromJson(s, new TypeToken<StraightForBean>() {}.getType());
            return straightForBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            listView.onRefreshComplete();
            isLogin(e);
        }

        @Override
        public void onResponse(StraightForBean response, int id) {
            if (response.code==0){
                straightForBeanPage.clear();
                if(response.data.size()!=0){
                    straightForBeanPage.addAll(response.data);
                    if(cursor == 0){
                        straightForBeanList.clear();
                    }
                    straightForBeanList.addAll(straightForBeanPage);
                }else {
                    showToast("暂无数据");
                }
                straightForAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initView() {
        txtTitle.setText("直供商品");
        ckAll.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        llStraight.setOnClickListener(this);
        llShare.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvSettlement.setOnClickListener(this);
        token =caches.get("access_token");
        type = getIntent().getStringExtra("type");

        //直供商品适配器
        straightForAdapter = new StraightForAdapter(this);
        straightForAdapter.setCheckInterface(this);
        straightForAdapter.setModifyCountInterface(this);
        //分享商品适配器
        shareAdapter = new ShareForAdapter(this);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor=0;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                switch (type){
                    //直供商品适配器
                    case "straight":
                        OkHttpUtils.get().url(Urls.STRAIGHTFOR).addHeader(Config.HEADTOKEN,token)
                                .addParams("count",String.valueOf(count)).addParams("cursor",String.valueOf(cursor*count)).build().execute(new straightForCallBack());
                        break;
                    //分享商品适配器
                    case "share":
                        OkHttpUtils.get().url(Urls.SHAREFOR).addHeader(Config.HEADTOKEN,token)
                                .addParams("count",String.valueOf(count)).addParams("cursor",String.valueOf(cursor*count)).build().execute(new shareForCallBack());
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //全选按钮
            case R.id.ck_all:
                if (straightForBeanList.size() != 0) {
                    if (ckAll.isChecked()) {
                        for (int i = 0; i < straightForBeanList.size(); i++) {
                            straightForBeanList.get(i).setChoosed(true);
                        }
                        straightForAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < straightForBeanList.size(); i++) {
                            straightForBeanList.get(i).setChoosed(false);
                        }
                        straightForAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.ll_straight:
                type = "straight";
                initData();
                break;
            case R.id.ll_share:
                type = "share";
                initData();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_settlement:
                straightForApply();
                break;
        }
    }

    private void straightForApply() {
        StringBuilder goodsBuilder =new StringBuilder();
        boolean hasGoods = false;
        for (StraightForBean.DataBean group : straightForBeanList) {
            if (group.isChoosed()){
                goodsBuilder.append(group.goodId+"-"+group.getCurrentCount()+",");
                hasGoods = true;
            }
        }
        if (!hasGoods){
            showToast("请选择商品");
            return;
        }
        StringBuilder goods = null;
        if (null!=goodsBuilder.toString()&&goodsBuilder.toString().contains("-")){
            goods = new StringBuilder(goodsBuilder.toString().substring(0,goodsBuilder.toString().length()));
        }
        OkHttpUtils.post().url(Urls.STRAIGHTFORAPPLY).addHeader(Config.HEADTOKEN,token).addParams("type","1").addParams("goods",goods.toString()).build().execute(new straightForApplyCallBack());
    }

    private class straightForApplyCallBack extends Callback<JavaBaseBean>{
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
        public JavaBaseBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            JavaBaseBean javaBaseBean = new Gson().fromJson(s, new TypeToken<JavaBaseBean>() {}.getType());
            return javaBaseBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(JavaBaseBean response, int id) {
            switch (response.code){
                case 0:
                    showToast("采购成功");
                    break;
                case 732:
                    showToast("该采购商有采购订单正在审批中");
                    break;
                case 734:
                    showToast("采购申请人未注册商家或者状态异常");
                    break;
                case 735:
                    showToast("直供商品不存在");
                    break;
                case 758:
                    showToast("状态异常");
                    break;
                case 1003:
                    showToast("区域代理信息不存在");
                    break;
                case 1004:
                    showToast("区域代理状态不正常");
                    break;
                case 1007:
                    showToast("所选商品必须属于同一商家");
                    break;
                case 1008:
                    showToast("本商品不是直供商品");
                    break;
                case 1009:
                    showToast("超出每次采购限额");
                    break;
                case 1010:
                    showToast("订单状态错误");
                    break;
                case 1015:
                    showToast("直供商品库存不足");
                    break;
                case 300301:
                    showToast("商家禁止从商家本店采购商品");
                    break;
            }
        }
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        straightForBeanList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);

        straightForAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void checkState(int position, boolean isChecked) {
        straightForBeanList.get(position).setCheck(isChecked);
//        straightForBeanList.get(position).setChoosed(isChecked);

        if (isAllCheck())
            ckAll.setChecked(true);
        else
            ckAll.setChecked(false);

        straightForAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {

        for (StraightForBean.DataBean group : straightForBeanList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 增加
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        StraightForBean.DataBean straightForBean = straightForBeanList.get(position);
        int currentCount = straightForBean.currentCount;
        currentCount++;
        straightForBean.currentCount =currentCount;
        ((EditText) showCountView).setText(currentCount + "");
        straightForAdapter.notifyDataSetChanged();
        statistics();
    }

    /**
     * 删减
     *
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        StraightForBean.DataBean straightForBean = straightForBeanList.get(position);
        int currentCount = straightForBean.currentCount;
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        straightForBean.currentCount=currentCount;
        ((EditText) showCountView).setText(currentCount + "");
        straightForAdapter.notifyDataSetChanged();
        statistics();

    }

    @Override
    public void doEdit(int position, View showCountView, boolean isChecked,int count) {
//        straightForAdapter.notifyDataSetChanged();
        Logger.d(straightForBeanList.size()+"");
        StraightForBean.DataBean straightForBean = straightForBeanList.get(position);
        straightForBean.currentCount = count;
        statistics();
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void childDelete(int position) {
        straightForBeanList.remove(position);
        straightForAdapter.notifyDataSetChanged();
        statistics();

    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < straightForBeanList.size(); i++) {
            StraightForBean.DataBean shoppingCartBean = straightForBeanList.get(i);
            if (shoppingCartBean.isChoosed()) {
                totalCount++;
                totalPrice += shoppingCartBean.price * shoppingCartBean.currentCount;
            }
        }
        tvShowPrice.setText("¥"+StringUtils.getStringtodouble(totalPrice));
    }
}
