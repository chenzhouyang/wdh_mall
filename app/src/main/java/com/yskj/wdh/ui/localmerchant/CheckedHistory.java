package com.yskj.wdh.ui.localmerchant;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.MyCheckedNumberAdapter;
import com.yskj.wdh.adapter.MyProjectAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CheckedNumberBean;
import com.yskj.wdh.bean.ProjectBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by gtz on 2016/11/7 0007.
 */
public class CheckedHistory extends BaseActivity implements View.OnClickListener {
    private LinearLayout ll_history_checkedamount;
    private LinearLayout ll_history_projectnumber, ll_history_data;
    private Button btn_history_today;
    private Button btn_history_otherday;
    private ImageView iv_history_back;
    private Handler mHandler;
    private MyCheckedNumberAdapter checkedNumberAdapter;
    private View history_view;
    private boolean isProjectnumber = false;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private Gson gson = new Gson();
    private CheckedNumberBean numberBean, otherTimeBean;
    private ProjectBean projectBean;
    private TextView tv_history_checkednumber, tv_history_projectnub, tv_history_number;
    private TextView tv_history_project,txt_title;
    private PullToRefreshListView xlv_checked_history;
    private int years, months, days;
    private View view_history_checked;
    private ArrayList ID = new ArrayList();
    private ArrayList COVER = new ArrayList();
    private ArrayList Name = new ArrayList();
    private ArrayList Price = new ArrayList();
    private boolean isOtherTime = false;
    private int cursorOther = 0;
    private int cursor = 0;
    private List<CheckedNumberBean.DataBean.ListBean> CashCouponsTemporaryDatas;
    private List<CheckedNumberBean.DataBean.ListBean> otherTimeTemporaryDatas;
    private List<CheckedNumberBean.DataBean.ListBean> CashCouponsDatas;
    private boolean noPickerTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkedhistory);
        initView();
        startCheckedNumberNet();

    }


    private void initView() {
        Calendar c = Calendar.getInstance();
        years = c.get(Calendar.YEAR);
        months = c.get(Calendar.MONTH)+1;
        days = c.get(Calendar.DAY_OF_MONTH);
        xlv_checked_history = (PullToRefreshListView) findViewById(R.id.xlv_checked_history);
        ll_history_checkedamount = (LinearLayout) findViewById(R.id.ll_history_checkedamount);
        ll_history_projectnumber = (LinearLayout) findViewById(R.id.ll_history_projectnumber);
        btn_history_today = (Button) findViewById(R.id.btn_history_today);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("验证记录");
        btn_history_otherday = (Button) findViewById(R.id.btn_history_otherday);
        tv_history_projectnub = (TextView) findViewById(R.id.tv_history_projectnub);
        tv_history_number = (TextView) findViewById(R.id.tv_history_number);
        view_history_checked = findViewById(R.id.view_history_checked);
        ll_history_data = (LinearLayout) findViewById(R.id.ll_history_data);
        history_view = findViewById(R.id.history_view);
        xlv_checked_history.setMode(PullToRefreshBase.Mode.BOTH);
        xlv_checked_history.setAdapter(checkedNumberAdapter);//指定adapter
        xlv_checked_history.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isProjectnumber) {
                    startProjectNet();
                } else {
                    if (isOtherTime) {
                        //验证量(张)其他日期
                        startNumberOtherTime();
                    } else {
                        //今天验证
                        startCheckedNumberNet();
                    }
                }
                MethodUtils.stopRefresh(xlv_checked_history);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isProjectnumber) {
                    showToast("没有更多数据了");
                } else {
                    if (isOtherTime) {
                        cursorOther = 10 + cursorOther;
                        //验证量(张)
                        OkHttpUtils.get().url(Urls.checkedNumberUrl)
                                .addHeader("Authorization", aCache.get("access_token"))
                                .addParams("cursor", cursorOther+"")
                                .addParams("count", "10")
                                .addParams("type","1")
                                .addParams("beginTime",StringUtils.formatDate(years,months,days))
                                .addParams("endTime", StringUtils.formatDate(years,months,days))
                                .build().execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                                stopMyDialog();
                                isLogin(e);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                stopMyDialog();
                                otherTimeBean = gson.fromJson(response, CheckedNumberBean.class);
                                int code = otherTimeBean.getCode();
                                if (code == 0) {
                                    otherTimeTemporaryDatas = otherTimeBean.getData().getList();
                                    if (otherTimeTemporaryDatas.size() != 10) {
                                        showToast("没有更多数据了");
                                    }
                                    if (otherTimeTemporaryDatas != null) {
                                        CashCouponsDatas.addAll(otherTimeTemporaryDatas);
                                        otherTimeTemporaryDatas.clear();
                                    }

                                    if (checkedNumberAdapter == null) {
                                        checkedNumberAdapter = new MyCheckedNumberAdapter(getApplicationContext(), CashCouponsDatas);
                                        xlv_checked_history.setAdapter(checkedNumberAdapter);
                                    } else {

                                        checkedNumberAdapter.notifyDataSetChanged();//adapter刷新,是在数据源发生变化之后
                                    }


                                } else {
                                    MethodUtils.showToast(getApplicationContext(), "请检查网络或稍后再试");
                                }
                            }
                        });
                    } else {
                        todayLoadMore();
                    }
                }
                MethodUtils.stopRefresh(xlv_checked_history);

            }
        });
        mHandler = new Handler();
        iv_history_back = (ImageView) findViewById(R.id.img_back);
        btn_history_today.setOnClickListener(this);
        btn_history_otherday.setOnClickListener(this);
        iv_history_back.setOnClickListener(this);
        ll_history_checkedamount.setOnClickListener(this);
        ll_history_projectnumber.setOnClickListener(this);
        tv_history_checkednumber = (TextView) findViewById(R.id.tv_history_checkednumber);
        tv_history_project = (TextView) findViewById(R.id.tv_history_project);
    }


    //项目数(个)联网
    private void startProjectNet() {
        OkHttpUtils.get().url(Urls.projectNumberUrl)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("type","2")
                .addParams("beginTime", StringUtils.formatDate(years,months,days))
                .addParams("endTime", StringUtils.formatDate(years,months,days))
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
                projectParseJson(response);
            }
        });
    }
    //验证量(张)联网
    private void startCheckedNumberNet() {
        OkHttpUtils.get().url(Urls.checkedNumberUrl)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("count", "10")
                .addParams("type","1")
                .addParams("beginTime", StringUtils.formatDate(years,months,days))
                .addParams("endTime", StringUtils.formatDate(years,months,days))
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
                checkedNumberParseJson(response);
            }

        });
    }

    MyProjectAdapter myProjectAdapter;
    
    List<ProjectBean.DataBean.ListBean> projectDatas;

    //项目解析
    private void projectParseJson(String s) {
        projectBean = gson.fromJson(s, new TypeToken<ProjectBean>() {
        }.getType());
        int code = projectBean.getCode();
        if (code == 0) {
            tv_history_project.setText(projectBean.getData().getStatistics().getLifeItemCount() + "");
            projectDatas = projectBean.getData().getList();
            myProjectAdapter = new MyProjectAdapter(Price,Name,COVER, ID, getApplicationContext(), projectDatas);
            xlv_checked_history.setAdapter(myProjectAdapter);
        } else {
            MethodUtils.showToast(getApplicationContext(), "请检查网络或稍后再试");

        }
    }


    //验证量解析
    private void checkedNumberParseJson(String s) {
        numberBean = gson.fromJson(s, new TypeToken<CheckedNumberBean>() {
        }.getType());
        int code = numberBean.getCode();
        if (code == 0) {
            tv_history_project.setText(numberBean.getData().getStatistics().getLifeItemCount() + "");
            tv_history_checkednumber.setText(numberBean.getData().getStatistics().getCouponCount() + "");
            //for (int i = 0; i < numberBean.getRet_data().getCashCoupons().size(); i++) {
            CashCouponsDatas = numberBean.getData().getList();
            MyCheckedNumberAdapter checkedNumberAdapter = new MyCheckedNumberAdapter
                    (getApplicationContext(), CashCouponsDatas);
            xlv_checked_history.setAdapter(checkedNumberAdapter);
        } else {
            MethodUtils.showToast(getApplicationContext(), "请检查网络或稍后再试");

        }

    }


    //点击处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_history_today:
                //日期筛选今天
                btn_history_otherday.setText("其他日期");
                startCheckedNumberNet();
                break;
            case R.id.btn_history_otherday:
                //日期筛选其他
                showDataDialog();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_history_projectnumber:
                //项目数
                if (CashCouponsDatas != null && CashCouponsDatas.size() > 0) {
                    CashCouponsDatas.clear();
                }
                cursor = 0;
                cursorOther = 0;
                ll_history_data.setVisibility(View.GONE);
                view_history_checked.setVisibility(View.GONE);
                tv_history_project.setTextColor(getResources().getColor(R.color.checked_history_lightgreen));
                tv_history_projectnub.setTextColor(getResources().getColor(R.color.checked_history_lightgreen));
                tv_history_checkednumber.setTextColor(getResources().getColor(R.color.checked_history_black));
                tv_history_number.setTextColor(getResources().getColor(R.color.checked_history_black));
                startProjectNet();
                isProjectnumber = true;
                break;
            case R.id.ll_history_checkedamount:
                //验证量
                btn_history_otherday.setText(StringUtils.formatDate(null,null,null));
                view_history_checked.setVisibility(View.VISIBLE);
                ll_history_data.setVisibility(View.VISIBLE);
                tv_history_project.setTextColor(getResources().getColor(R.color.checked_history_black));
                tv_history_projectnub.setTextColor(getResources().getColor(R.color.checked_history_black));
                tv_history_checkednumber.setTextColor(getResources().getColor(R.color.checked_history_lightgreen));
                tv_history_number.setTextColor(getResources().getColor(R.color.checked_history_lightgreen));
                startCheckedNumberNet();
                isProjectnumber = false;
                break;

        }

        xlv_checked_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isProjectnumber) {
                    Intent intent = new Intent(CheckedHistory.this, CheckedHistoryDetails.class);
                    intent.putExtra("extra", (String.valueOf(ID.get(position - 1))));
                    intent.putExtra("cover", (String.valueOf(COVER.get(position - 1))));
                    intent.putExtra("name",(String.valueOf(Name.get(position - 1))));
                    intent.putExtra("price",(String.valueOf(Price.get(position - 1))));
                    intent.putExtra("time",StringUtils.formatDate(years,months,days));
                    startActivity(intent);
                }
            }
        });

    }


    //日期选择
    private void showDataDialog() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_chekedhistory_datepicker);
        DatePicker dpPicker = (DatePicker) window.findViewById(R.id.dpPicker);
        if (noPickerTime) {
            //获取系统当前时间
            years = dpPicker.getYear();
            months = dpPicker.getMonth() + 1;
            days = dpPicker.getDayOfMonth();
        } else {

        }

        //初始化日历控件年月日和选择监听
        dpPicker.init(years, months - 1, days, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                years = year;
                months = monthOfYear + 1;
                days = dayOfMonth;
                btn_history_otherday.setText(StringUtils.formatDate(years,months,days));

            }
        });

        Date date = new Date(years,months,days);

        RelativeLayout ok = (RelativeLayout) window.findViewById(R.id.YES);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
                if (CashCouponsDatas != null) {
                    CashCouponsDatas.clear();
                }
                noPickerTime = false;
                isOtherTime = true;
                //// TODO: 2016/11/8 0008
                //按年月日筛选
                startNumberOtherTime();

            }
        });


        // 关闭alert对话框架
        RelativeLayout cancel = (RelativeLayout) window.findViewById(R.id.NO);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dlg.cancel();
            }
        });
    }

    //其他时间联网
    private void startNumberOtherTime() {
        //验证量(张)
        cursorOther = 0;
        OkHttpUtils.get().url(Urls.checkedNumberUrl)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("cursor", cursorOther+"")
                .addParams("count", "10")
                .addParams("type","1")
                .addParams("beginTime", StringUtils.formatDate(years,months,days))
                .addParams("endTime", StringUtils.formatDate(years,months,days))
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
                checkedNumberParseJson(response);
            }
        });
    }

    private void todayLoadMore() {
        cursor = 10 + cursor;
        OkHttpUtils.get().url(Urls.checkedNumberUrl)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("cursor", cursor+"")
                .addParams("count", "10")
                .addParams("type","1")
                .addParams("beginTime", StringUtils.formatDate(years,months,days))
                .addParams("endTime", StringUtils.formatDate(years,months,days))
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
                numberBean = gson.fromJson(response, new TypeToken<CheckedNumberBean>() {
                }.getType());
                int code = numberBean.getCode();
                if (code == 0) {
                    CashCouponsTemporaryDatas = numberBean.getData().getList();
                    if (CashCouponsTemporaryDatas.size() != 10) {
                        showToast("没有更多数据了");
                    }
                    if (CashCouponsTemporaryDatas != null) {
                        CashCouponsDatas.addAll(CashCouponsTemporaryDatas);
                        CashCouponsTemporaryDatas.clear();
                    }

                    if (checkedNumberAdapter == null) {
                        checkedNumberAdapter = new MyCheckedNumberAdapter(getApplicationContext(), CashCouponsDatas);
                        xlv_checked_history.setAdapter(checkedNumberAdapter);
                    } else {
                        checkedNumberAdapter.notifyDataSetChanged();//adapter刷新,是在数据源发生变化之后
                    }


                }
            }
        });
    }

}


