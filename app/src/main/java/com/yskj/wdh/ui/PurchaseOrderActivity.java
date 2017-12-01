package com.yskj.wdh.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.PurchaseOrderAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.JavaBaseBean;
import com.yskj.wdh.bean.PurchaseOrderBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 创建日期 2017/5/26on 9:03.
 * 描述：采购单通过与未通过列表
 * 作者：姜贺YSKJ-JH
 */

public class PurchaseOrderActivity extends BaseActivity implements View.OnClickListener, PurchaseOrderAdapter.OrderCancelInterface {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.rb_one)
    RadioButton rbOne;
    @Bind(R.id.rb_two)
    RadioButton rbTwo;
    @Bind(R.id.rg_purchase)
    RadioGroup rgPurchase;
    @Bind(R.id.list_view)
    PullToRefreshListView listView;
    @Bind(R.id.rb_three)
    RadioButton rbThree;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.img_examine)
    ImageView imgExamine;
    @Bind(R.id.tv_examine)
    TextView tvExamine;
    @Bind(R.id.ll_examine)
    LinearLayout llExamine;
    @Bind(R.id.img_pending)
    ImageView imgPending;
    @Bind(R.id.tv_pending)
    TextView tvPending;
    @Bind(R.id.rb_wait_me_one)
    RadioButton rbWaitMeOne;
    @Bind(R.id.rb_wait_me_two)
    RadioButton rbWaitMeTwo;
    @Bind(R.id.rg_wait_me)
    RadioGroup rgWaitMe;
    @Bind(R.id.ll_wait_me)
    LinearLayout llWaitMe;
    @Bind(R.id.img_no_data)
    ImageView imgNoData;
    @Bind(R.id.tv_no_data)
    TextView tvNoData;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;

    private Drawable drawableBottom;

    private PurchaseOrderAdapter adapterOne, adapterTwo, adapterThree, adapterWaitMeOne, adapterWaitMeTwo;
    private ArrayList<PurchaseOrderBean.DataBean> listOne = new ArrayList<>();//审批通过
    private ArrayList<PurchaseOrderBean.DataBean> listTwo = new ArrayList<>();//正在审批
    private ArrayList<PurchaseOrderBean.DataBean> listThree = new ArrayList<>();//审批未通过

    private ArrayList<PurchaseOrderBean.DataBean> listWaitMeOne = new ArrayList<>();//待审批
    private ArrayList<PurchaseOrderBean.DataBean> listWaitMeTwo = new ArrayList<>();//已审批

    private ArrayList<PurchaseOrderBean.DataBean> listPage = new ArrayList<>();//审批未通过

    private String roleType = "1";//当前会员角色类型：1=申请者；2=审批者

    private String status = "2";//订单进行状态， 0：草稿；1：待审批；2：审批通过；3：审批不通过；4：取消订单；5：废弃

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private int offset = 0;
    private int count = 10;

    private String resultType;//审批结果 0：不通过；1：通过

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_order);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        txtTitle.setText("采购审批单");

        imgBack.setOnClickListener(this);
        llExamine.setOnClickListener(this);
        llWaitMe.setOnClickListener(this);
        token = caches.get("access_token");
        drawableBottom = getResources().getDrawable(R.drawable.line_ee1230_2dp);
        drawableBottom.setBounds(0, 0, drawableBottom.getMinimumWidth(), drawableBottom.getMinimumHeight());

        adapterOne = new PurchaseOrderAdapter(context, "1", listOne); //申请者
        adapterTwo = new PurchaseOrderAdapter(context, "1", listTwo);
        adapterThree = new PurchaseOrderAdapter(context, "1", listThree);

        adapterWaitMeOne = new PurchaseOrderAdapter(context, "2", listWaitMeOne); //审批者
        adapterWaitMeTwo = new PurchaseOrderAdapter(context, "2", listWaitMeTwo);

        adapterOne.setOrderCancelInterface(this);
        adapterTwo.setOrderCancelInterface(this);
        adapterThree.setOrderCancelInterface(this);

        adapterWaitMeOne.setOrderCancelInterface(this);
        adapterWaitMeTwo.setOrderCancelInterface(this);

        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                offset = 0;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                offset++;
                getData();
            }
        });

        rgPurchase.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_one:
                        status = "2"; //审批通过
                        offset = 0;
                        rbOne.setCompoundDrawables(null, null, null, drawableBottom);
                        rbTwo.setCompoundDrawables(null, null, null, null);
                        rbThree.setCompoundDrawables(null, null, null, null);
                        getData();
                        break;
                    case R.id.rb_two:
                        status = "1"; //待审批
                        offset = 0;
                        rbOne.setCompoundDrawables(null, null, null, null);
                        rbTwo.setCompoundDrawables(null, null, null, drawableBottom);
                        rbThree.setCompoundDrawables(null, null, null, null);
                        getData();
                        break;
                    case R.id.rb_three:
                        status = "3"; //审批不通过
                        offset = 0;
                        rbOne.setCompoundDrawables(null, null, null, null);
                        rbTwo.setCompoundDrawables(null, null, null, null);
                        rbThree.setCompoundDrawables(null, null, null, drawableBottom);
                        getData();
                        break;
                }
            }
        });

        rgWaitMe.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_wait_me_one:
                        status = "1"; //待我审批
                        offset = 0;
                        rbWaitMeOne.setCompoundDrawables(null, null, null, drawableBottom);
                        rbWaitMeTwo.setCompoundDrawables(null, null, null, null);
                        getData();
                        break;
                    case R.id.rb_wait_me_two:
                        status = "2"; //我已审批通过
                        offset = 0;
                        rbWaitMeOne.setCompoundDrawables(null, null, null, null);
                        rbWaitMeTwo.setCompoundDrawables(null, null, null, drawableBottom);
                        getData();
                        break;
                }
            }
        });
    }

    /**
     * 获取首页数据
     */
    private void getData() {
        OkHttpUtils.get().url(Urls.PURCHASEORDER).addHeader(Config.HEADTOKEN, token).addParams("roleType", roleType)
                .addParams("type", "1").addParams("status", status).addParams("offset", offset * count + "").addParams("count", count + "").build().execute(new purchaseOrderCallBack());
    }

    /**
     * 获取首页数据
     */
    private class purchaseOrderCallBack extends Callback<PurchaseOrderBean> {
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
        public PurchaseOrderBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            PurchaseOrderBean purchaseOrderBean = new Gson().fromJson(s, new TypeToken<PurchaseOrderBean>() {
            }.getType());
            return purchaseOrderBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            listView.onRefreshComplete();
            isLogin(e);
        }

        @Override
        public void onResponse(PurchaseOrderBean response, int id) {
            switch (response.code) {
                case 0:
                    listPage.clear();
                    listPage.addAll(response.data);
                    setData();
                    break;
                case 1003:
                    showToast("您暂时不是区域代理，请先申请成为区域代理吧.");
                    rlNoData.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    break;
                case 734:
                    showToast("采购申请人未注册商家或者状态异常");
                    rlNoData.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * 成功请求后设置数据
     */
    private void setData() {
        switch (roleType) {
            //申请者
            case "1":
                //1：待审批；2：审批通过；3：审批不通过
                switch (status) {
                    case "1":
                        if (offset == 0) {
                            listOne.clear();
                        }
                        listOne.addAll(listPage);
                        if (listOne.size() == 0) {
                            rlNoData.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            rlNoData.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapterOne);
                            adapterOne.notifyDataSetChanged();
                        }
                        break;
                    case "2":
                        if (offset == 0) {
                            listTwo.clear();
                        }
                        listTwo.addAll(listPage);
                        if (listTwo.size() == 0) {
                            rlNoData.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            rlNoData.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapterTwo);
                            adapterTwo.notifyDataSetChanged();
                        }
                        break;
                    case "3":
                        if (offset == 0) {
                            listThree.clear();
                        }
                        listThree.addAll(listPage);
                        if (listThree.size() == 0) {
                            rlNoData.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            rlNoData.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapterThree);
                            adapterThree.notifyDataSetChanged();
                        }
                        break;
                }
                break;
            //审批者
            case "2":
                //1：待审批；2：审批通过；
                switch (status) {
                    case "1":
                        if (offset == 0) {
                            listWaitMeOne.clear();
                        }
                        listWaitMeOne.addAll(listPage);
                        if (listWaitMeOne.size() == 0) {
                            rlNoData.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            rlNoData.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapterWaitMeOne);
                            adapterWaitMeOne.notifyDataSetChanged();
                        }
                        break;
                    case "2":
                        if (offset == 0) {
                            listWaitMeTwo.clear();
                        }
                        listWaitMeTwo.addAll(listPage);
                        if (listWaitMeTwo.size() == 0) {
                            rlNoData.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            rlNoData.setVisibility(View.GONE);
                            listView.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapterWaitMeTwo);
                            adapterWaitMeTwo.notifyDataSetChanged();
                        }
                        break;
                }
                break;
        }
    }


    /**
     * adapter中接口回掉，用于删除选中订单并刷新数据
     *
     * @param orderId
     */
    @Override
    public void orderCancel(String orderId) {
        OkHttpUtils.post().url(Urls.PURCHASECANCELORDER).addHeader(Config.HEADTOKEN, token).addParams("orderId", orderId).addParams("type", "1").build().execute(new purchaseOrderCancelCallBack());
    }

    /**
     * adapter中接口回掉，用于审批订单并刷新数据
     *
     * @param orderId
     * @param result
     */
    @Override
    public void applyOrder(String orderId, String result) {
        resultType = result;
        String remark;
        if (resultType.equals("0")) {
            remark = "拒绝";
        } else {
            remark = "通过";
        }
        OkHttpUtils.post().url(Urls.APPLYORDER).addHeader(Config.HEADTOKEN, token)
                .addParams("orderId", orderId).addParams("type", "3").addParams("remark", remark).addParams("result", resultType)
                .build().execute(new orderApplyCallBack());
    }

    private class orderApplyCallBack extends Callback<JavaBaseBean> {
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
            JavaBaseBean javaBaseBean = new Gson().fromJson(s, new TypeToken<JavaBaseBean>() {
            }.getType());
            return javaBaseBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(JavaBaseBean response, int id) {
            switch (response.code) {
                case 0:
                    if (resultType.equals("1")) {
                        showToast("审批通过");
                    } else if (resultType.equals("0")) {
                        showToast("拒绝审批通过");
                    }
                    getData();
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
                case 1010:
                    showToast("订单状态错误");
                    break;
                case 1008:
                    showToast("本商品不是直供商品");
                    break;
                case 300301:
                    showToast("内部错误");
                    break;
            }
        }
    }

    /**
     * 采购订单取消
     */
    private class purchaseOrderCancelCallBack extends Callback<JavaBaseBean> {
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
            JavaBaseBean javaBaseBean = new Gson().fromJson(s, new TypeToken<JavaBaseBean>() {
            }.getType());
            return javaBaseBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(JavaBaseBean response, int id) {
            switch (response.code) {
                case 0:
                    showToast("取消订单成功");
                    getData();
                    break;
                case 734:
                    showToast("采购申请人未注册商家或者状态异常");
                    break;
                case 1010:
                    showToast("订单状态错误");
                    break;
                case 1008:
                    showToast("本商品不是直供商品");
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_examine:
                tvExamine.setTextColor(getResources().getColor(R.color.straight));
                tvPending.setTextColor(getResources().getColor(R.color.gray));
                rgWaitMe.setVisibility(View.GONE);
                rgPurchase.setVisibility(View.VISIBLE);
                rgPurchase.check(R.id.rb_one);
                roleType = "1"; //申请者
                status = "2"; //申请通过
                offset = 0;
                getData();
                break;
            case R.id.ll_wait_me:
                tvPending.setTextColor(getResources().getColor(R.color.straight));
                tvExamine.setTextColor(getResources().getColor(R.color.gray));
                rgPurchase.setVisibility(View.GONE);
                rgWaitMe.setVisibility(View.VISIBLE);
                rgWaitMe.check(R.id.rb_wait_me_one);
                roleType = "2"; //审批者
                status = "1";//待审批
                offset = 0;
                getData();
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }
}
