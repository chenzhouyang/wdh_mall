package com.yskj.wdh.ui.localmerchant;
/**
 * Created by gtz on 2016/11/7 0007.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.yskj.wdh.AppManager;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.bean.ConsumeVerifyBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.QuitDialog;
import com.yskj.wdh.dialog.VersionDialog;
import com.yskj.wdh.entity.DectionEntity;
import com.yskj.wdh.entity.UserInfoEntity;
import com.yskj.wdh.ui.GoodsBuyActivity;
import com.yskj.wdh.ui.RegionAgentActivity;
import com.yskj.wdh.ui.RegionAgentInfoActivity;
import com.yskj.wdh.ui.collectmoney.ProsceniumActivity;
import com.yskj.wdh.ui.providermanager.ProviderManagerActivity;
import com.yskj.wdh.ui.providermanager.SuppliersActivity;
import com.yskj.wdh.ui.revenue.RevenueActivity;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.ExampleUtil;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.NumberFormat;
import com.yskj.wdh.util.StringUtils;
import com.yskj.wdh.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;


public class LocalityShopHome extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_shop_name)
    TextView tv_shop_name;
    @Bind(R.id.et_shop_check_code)
    EditText et_shop_check_code;
    @Bind(R.id.bt_checked)
    Button btChecked;
    @Bind(R.id.ll_shop_scancheck)
    LinearLayout llShopScancheck;
    @Bind(R.id.ll_shop_checkedhistory)
    LinearLayout llShopCheckedhistory;
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.logout)
    TextView logout;
    @Bind(R.id.tv_count)
    TextView tvCount;
    private Intent intent;
    Gson gson = new Gson();
    private BusinessInfoBean bean;
    private int REQUSET = 1;
    private String json;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private UserInfoEntity userInfoEntity;
    public static boolean isForeground = false;

    private RefreshDataReceiver refreshDataReceiver;
    private IntentFilter intentFilter;
    private DectionEntity dectionEntity;
    public MyLocationListenner myListener = new MyLocationListenner();
    public LocationClient mLocationClient = null;
    private boolean isFirstLoc = true;
    private BusinessInfoBean.AgentBean regionAgent;
    private int regionAgentStatus;
    private UserInfoEntity.DataBean.RealnameVoBean realnameVoBean;
    private BusinessInfoBean.Mill mill;
    private int machineStatus,millStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);
        ButterKnife.bind(this);
        isFirstLoc = true; // 每次进来定位一次
        initLocation();
        registerMessageReceiver();
        PgyUpdateManager.register(LocalityShopHome.this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {

                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        VersionDialog.Builder builder = new VersionDialog.Builder(LocalityShopHome.this);
                        builder.setTitle("版本更新");
                        builder.setDetailMessage(appBean.getReleaseNote());
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startDownloadTask(LocalityShopHome.this, appBean.getDownloadURL());
                            }
                        });
                        builder.setCancelable(false);
                        builder.create().show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
        refreshDataReceiver = new RefreshDataReceiver();
        intentFilter = new IntentFilter("com.yskj.android.USER_ACTION");
    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //经纬度
            if (isFirstLoc) {
                isFirstLoc = false;
                String lati = location.getLatitude() + "";
                String longa = location.getLongitude() + "";
                if(lati.equals("4.9E-324")){
                    sp.edit().putString(Config.SPKEY_LATITUDE, "0").commit();
                    sp.edit().putString(Config.SPKEY_LONGITUDE, "0").commit();
                }else {
                    sp.edit().putString(Config.SPKEY_LATITUDE, lati).commit();
                    sp.edit().putString(Config.SPKEY_LONGITUDE, longa).commit();
                }
                sp.edit().putString(Config.SPKEY_CITYNAME, location.getCity()).commit();
                String str = (location.getCity());
//                showdialog("定位为：" + str+"经度："+lati+"维度："+longa);
            }
        }
    }

    private void initLocation() {
        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        //就是这个方法设置为true，才能获取当前的位置信息
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        //int span = 1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
                userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
                }.getType());
                realnameVoBean = userInfoEntity.data.realnameVo;
                String alias = userInfoEntity.data.userVo.id + "";

                if (TextUtils.isEmpty(alias)) {
                    return;
                }
                if (!ExampleUtil.isValidTagAndAlias(alias)) {
                    return;
                }
                // 调用 Handler 来异步设置别名
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
                caches.put("spreadCode", userInfoEntity.data.userVo.spreadCode + "");
            }
        });
    }

    private void initGridView() {
        //准备要添加的数据条目
        List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
        Map<String, Object> item = new HashMap<String, Object>();
        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.manage_buses);//业务管理
        items.add(item);

        /*item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.money_record);//收益记录
        items.add(item);*/

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.manage_shop);//门店管理
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.buess_zhuanru);//转出
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.buess_tixian);//提现
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.goods_buy_main);//商品采购
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.code_pay_password);//收款码
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.more_goods);//账单
        items.add(item);

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.check_stand);//收银台
        items.add(item);
        if (machineStatus == 1) {
            item = new HashMap<String, Object>();
            item.put("textItem", R.mipmap.dection_gathering);//检测收费
            items.add(item);
        } else {
            item = new HashMap<String, Object>();
            item.put("textItem", R.mipmap.dection_for);
            items.add(item);
        }

        item = new HashMap<String, Object>();
        item.put("textItem", R.mipmap.region_agent);//区域代理
        items.add(item);

        item = new HashMap<String, Object>();
        if (millStatus == 1) {
            item.put("textItem", R.mipmap.provider_after);//供应商
        } else {
            item.put("textItem", R.mipmap.provider_before);
        }
        items.add(item);

        //实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(this,
                items,
                R.layout.item_grid_view,
                new String[]{"textItem"},
                new int[]{R.id.gridview_image});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        //业务管理
                        intent = new Intent(LocalityShopHome.this, BusinessProjectManagerActivity.class);
                        intent.putExtra("bean", bean);
                        startActivity(intent);
                        break;
                    case 1:
                        //门店也无
                        intent = new Intent(LocalityShopHome.this, StoreManagementActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("bean", bean);
                        startActivity(intent);
                        break;
                    case 2:
                        //转出
                        startActivity(new Intent(context, RolloffActivity.class).putExtra("into", "0"));
                        break;
                    case 3:
                        //体现
                        startActivity(new Intent(context, DepositActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        break;

                    case 4:
                        //商品采购
                        startActivity(new Intent(context, GoodsBuyActivity.class));
                        break;
                    case 5:
                        //收款码
                        intent = new Intent(LocalityShopHome.this, ParaCodeActivity.class);
                        intent.putExtra("type", "1");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                        break;
                    case 6:
                        //账单
                        startActivity(new Intent(context, RevenueActivity.class));
                        break;
                    case 7:
                        //收银台
                        startActivity(new Intent(context, ProsceniumActivity.class));
                        break;
                    case 8:
                        //检查收费
                        if (machineStatus == -1) {
                            //未提交过
                            DialogDetection dialogDetection = new DialogDetection(context);
                            dialogDetection.show();
                        } else if (machineStatus == 0) {
                            //已提交
                            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                    .setMessage("申请已提交，24小时内我们会电话回访，请保持电话畅通。")//设置显示的内容
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                        } else if (machineStatus == 1) {
                            //审批通过
                            intent = new Intent(LocalityShopHome.this, ParaCodeActivity.class);
                            intent.putExtra("type", "3");
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else if (machineStatus == 2) {
                            //驳回
                            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                    .setMessage("您本次申请已被驳回，是否再次申请")//设置显示的内容
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            DialogDetection dialogDetection = new DialogDetection(context);
                                            dialogDetection.show();
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                        }
                        break;
                    case 9:
                        //区域代理
                        switch (regionAgentStatus){
                            case -1:
                                startActivity(new Intent(context, RegionAgentActivity.class));
                                break;
                            case 0:
                                startActivity(new Intent(context, RegionAgentInfoActivity.class));
                                showToast("待审批");
                                break;
                            case 1:
                                startActivity(new Intent(context, RegionAgentInfoActivity.class));
                                showToast("审批通过");
                                break;
                            case 2:
                                startActivity(new Intent(context, RegionAgentActivity.class));
                                showToast("审批不通过");
                                break;
                            case 3:
                                showToast("禁用");
                                break;
                            case 4:
                                startActivity(new Intent(context, RegionAgentActivity.class));
                                break;
                        }
                        break;
                    case 10:
                        if (millStatus == -1) {
                            //未提交过
                            startActivity(new Intent(context, SuppliersActivity.class));
                        } else if (millStatus == 0) {
                            //已提交
                            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                    .setMessage("申请已提交，24小时内我们会电话回访，请保持电话畅通。")//设置显示的内容
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                        } else if (millStatus == 1) {
                            //审批通过
                            intent = new Intent(LocalityShopHome.this, ProviderManagerActivity.class);
                            intent.putExtra("millId", mill.id);
                            startActivity(intent);
                        } else if (millStatus == 2) {
                            //驳回
                            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                    .setMessage("您本次申请已被驳回，是否再次申请")//设置显示的内容
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                            startActivity(new Intent(context, SuppliersActivity.class));
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                        }else if (millStatus == 3) {
                            new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                    .setMessage("该供应商已冻结")//设置显示的内容
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        }
                                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                }
                            }).show();//在按键响应事件中显示此对话框
                        }
                        break;
                }
            }
        });
    }

    /**
     * 请求申请弹出框
     */
    public class DialogDetection extends Dialog implements View.OnClickListener {
        private Context context;
        private TextView close_detection, apply_for_detection, dection_title;
        private WebView web_details;
        private LoadingCaches caches = LoadingCaches.getInstance();

        public DialogDetection(Context context) {
            super(context, R.style.ShareDialog);
            this.context = context;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_detection);
            iniview();
        }

        private void iniview() {
            close_detection = (TextView) findViewById(R.id.close_detection);
            apply_for_detection = (TextView) findViewById(R.id.apply_for_detection);
            web_details = (WebView) findViewById(R.id.web_details);
            dection_title = (TextView) findViewById(R.id.dection_title);
            web_details.getSettings().setDomStorageEnabled(true);
            web_details.setVerticalScrollBarEnabled(false); //垂直不显示
            close_detection.setOnClickListener(this);
            apply_for_detection.setOnClickListener(this);
            OkHttpUtils.get().url(Urls.DETIONDAT).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    dectionEntity = new Gson().fromJson(response, new TypeToken<DectionEntity>() {
                    }.getType());
                    if (dectionEntity.errorCode.equals("000")) {
                        if (dectionEntity.adList.size() != 0) {
                            web_details.loadUrl(dectionEntity.adList.get(0).url);
                            dection_title.setText(dectionEntity.adList.get(0).title);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.close_detection:
                    dismiss();
                    break;
                case R.id.apply_for_detection:
                    dection();
                    dismiss();
                    break;
            }
        }

        //申请请求
        private void dection() {
            OkHttpUtils.post().url(Urls.DECTION).addHeader("Authorization", caches.get("access_token"))
                    .build().execute(new StringCallback() {

                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        machineStatus = 0;
                        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                                .setMessage("申请已提交，24小时内我们会电话回访，请保持电话畅通。")//设置显示的内容
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                        dismiss();
                                    }
                                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                            public void onClick(DialogInterface dialog, int which) {//响应事件
                                dismiss();
                            }
                        }).show();//在按键响应事件中显示此对话框
                    } else {
                        showToast(Messge.geterr_code(code));
                    }

                }
            });
        }
    }

    ConsumeVerifyBean verifyBean;
    TextView tv_name, tv_price, tv_ordernumber, tv_phonenumber, tv_datatime;

    @OnClick({R.id.bt_checked, R.id.ll_shop_scancheck, R.id.ll_shop_checkedhistory
            , R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_checked:
                //验证按钮
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                yanzheng();
                break;
            case R.id.ll_shop_scancheck:
                //扫码验证
                intent = new Intent(LocalityShopHome.this, CaptureActivity.class);
                intent.putExtra("codebundle", "4");
                startActivityForResult(intent, REQUSET);
                break;
            case R.id.ll_shop_checkedhistory:
                //验证记录
                intent = new Intent(LocalityShopHome.this, CheckedHistory.class);
                startActivity(intent);
                break;
            case R.id.logout:
                QuitDialog dialog = new QuitDialog(context, "是否切换账号？");
                dialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                if (data != null) {
                    json = data.getStringExtra("json");
                    if (json == null || json.length() <= 0) {
                        Toast.makeText(LocalityShopHome.this, "验证码有误,请重新扫码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if ("nodata".equals(json)) {
                        showToast("请再次扫码");
                        return;
                    } else {
                        yanZhengParse(json);

                    }
                }


                break;

        }
    }

    //消费卷验证
    private void yanzheng() {
        String code = et_shop_check_code.getText().toString().trim();
        if (code == null || code.length() <= 0) {
            showToast("请输入消费验证码");
        } else if (code.length() < 16) {
            showToast("请输入正确的消费验证码");
        } else {
            OkHttpUtils.post().url(Urls.consumptionInvokeUrl).addHeader("Authorization", caches.get("access_token"))
                    .addParams("consumePwd", code)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    yanZhengParse(response);
                }
            });
        }


    }

    //验证解析
    private void yanZhengParse(String json) {

        Map<String, Object> map = JSONFormat.jsonToMap(json);
        int code = (int) map.get("code");
        if (code == 0) {
            verifyBean = gson.fromJson(json, ConsumeVerifyBean.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(LocalityShopHome.this);
            final View view = View.inflate(LocalityShopHome.this, R.layout.consume_verify_dialog, null);
            final AlertDialog dialog = builder.create();
            dialog.setView(view, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(false);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_ordernumber = (TextView) view.findViewById(R.id.tv_ordernumber);
            tv_phonenumber = (TextView) view.findViewById(R.id.tv_phonenumber);
            tv_datatime = (TextView) view.findViewById(R.id.tv_datatime);
            TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
            TextView tv_total = (TextView) view.findViewById(R.id.tv_total);
            tv_total.setText("合计 :" + StringUtils.getStringtodouble(verifyBean.data.fundAmount) + "-积分" + StringUtils.getStringtodouble(verifyBean.data.accPointsAmount));
            tv_name.setText("用户昵称: " + verifyBean.data.nickName);
            tv_ordernumber.setText("订单号: " + verifyBean.data.orderNo);
            tv_datatime.setText("验证时间: " + verifyBean.data.validityTime);

            Button btn_confirm = (Button) view.findViewById(R.id.bt_verify_positivebutton);
            //Button btn_cancle = (Button) view.findViewById(R.id.bt_verify_canclebutton);

            btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_shop_check_code.setText("");
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LocalityShopHome.this);
            builder.setTitle("通知");
            builder.setMessage(Messge.geterr_code(code));
            //设置确定按钮的点击事件
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_shop_check_code.setText("");
                    dialog.dismiss();
                }
            });
            //设置取消按钮的点击事件
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    et_shop_check_code.setText("");
                    dialog.dismiss();
                }
            });
            //将对话框显示出来
            builder.create().show();

        }
    }

    private static Boolean isExit = false;

    public boolean dispatchKeyEvent(KeyEvent event) {
        //用户按下返回键
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exitBy2Click();
            }
            return false;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次返回,退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            AppManager.getInstance().AppExit(this);
        }
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };

    //自定义推送消息展示
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg) {
       /* if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }*/
    }

    @Override
    protected void onResume() {
        isForeground = true;
        dismissJP();
        super.onResume();
        registerReceiver(refreshDataReceiver, intentFilter);
        getBusinessInfo();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
        unregisterReceiver(refreshDataReceiver);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        mLocationClient.stop();
        super.onDestroy();
    }

    private void getBusinessInfo() {
        OkHttpUtils.get().url(Urls.BUSINESS)
                .addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    JSONObject ob = (JSONObject) map.get("data");
                    caches.put("business", ob.toString());
                    bean = JSONFormat.parseT(ob.toString(), BusinessInfoBean.class);
                    machineStatus = bean.getMachineStatus();//-1表示未提交过申请；0表示已提交待审批；1表示审批通过；2表示被驳回
                    regionAgent = bean.agent;
                    if (!ob.toString().contains("agent")){
                        regionAgentStatus=-1;//说明没有申请过代理
                    }else {
                        regionAgentStatus = regionAgent.status;
                    }
                    caches.put("regionAgent",regionAgentStatus+"");
                    if (!ob.toString().contains("mill")){
                        millStatus = -1;
                    }else {
                        mill = bean.mill;
                        millStatus = mill.status;//-1表示未提交过申请；0表示已提交待审批；1表示审批通过；2表示被驳回
                    }
                    tv_shop_name.setText(bean.getShopName());
                    tvCount.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(bean.getFundAccount(), 0d)));
                    initGridView();
                    getuserinfo();
                } else {
                    showToast(Messge.geterr_code(code));
                }

            }
        });
    }

    public class RefreshDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            OkHttpUtils.get().url(Urls.BUSINESS)
                    .addHeader("Authorization", caches.get("access_token"))
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        JSONObject ob = (JSONObject) map.get("data");
                        bean = JSONFormat.parseT(ob.toString(), BusinessInfoBean.class);
                        caches.put("business", ob.toString());
                        tvCount.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(bean.getFundAccount(), 0d)));
                    } else {
                        showToast(Messge.geterr_code(code));
                    }

                }
            });
        }
    }

}

