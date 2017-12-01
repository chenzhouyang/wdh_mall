package com.yskj.wdh.ui.revenue;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.RevenueAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.RevenueEntiity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by YSKJ-02 on 2017/1/19.
 */
//账单

public class RevenueActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.revenue_pull_lv)
    PullToRefreshListView revenuePullLv;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.shouyi_view)
    LinearLayout shouyiView;
    private int years, months,count = 10,cursor = 0;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ArrayList<RevenueEntiity.DataBean.ListBean> listBeenAll = new ArrayList<>();
    private ArrayList<RevenueEntiity.DataBean.ListBean> listBeenpage = new ArrayList<>();
    private RevenueAdapter adapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);
        ButterKnife.bind(this);
        txtTitle.setText("账单");
        //获取账单列表
        getreveulist();
        intiview();
        SharedPreferences share2    = getSharedPreferences("access_token", 0);
        token  = share2.getString("access_token", "null");
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        years = t.year;
        months = t.month + 1;
        adapter = new RevenueAdapter(context,listBeenAll);
        revenuePullLv.setAdapter(adapter);
    }

    //上拉下拉刷新
    private void intiview(){
        revenuePullLv.setMode(PullToRefreshBase.Mode.BOTH);
        revenuePullLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor = 0;
                if(time.getText().toString().equals("本月")){
                    getreveulist();
                }else {
                    getyearmonthlist();
                }
                MethodUtils.stopRefresh(revenuePullLv);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                cursor++;
                if(time.getText().toString().equals("本月")){
                    getreveulist();
                }else {
                    getyearmonthlist();
                }
                MethodUtils.stopRefresh(revenuePullLv);
            }
        });
    }
    //默认账单
    private void getreveulist() {
        OkHttpUtils.post().url(Urls.REVEBU).addHeader("Authorization",caches.get("access_token"))
                .addParams("accTypes","20")
                .addParams("count",count+"").addParams("cursor",cursor*10+"")
                .build().execute(new RevenuCallBack(context));
    }
    //根据年月查询账单
    private void getyearmonthlist(){
        OkHttpUtils.post().url(Urls.REVEBU).addHeader("Authorization",caches.get("access_token"))
                .addParams("accTypes","20")
                .addParams("year",years+"").addParams("month",months+"")
                .addParams("count",count+"").addParams("cursor",cursor*10+"")
                .build().execute(new RevenuCallBack(context));
    }
    @OnClick({R.id.img_back, R.id.shouyi_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.shouyi_view:
                showExitGameAlert();
                break;
        }
    }
    private void showExitGameAlert() {
        final AlertDialog dlg = new AlertDialog.Builder(this).create();
        dlg.show();
        Window window = dlg.getWindow();
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.datepicker_layout);
        // 为确认按钮添加事件,执行退出应用操作
        DatePicker dp = (DatePicker) window.findViewById(R.id.dpPicker);
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        // 隐藏日期View
        //((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        dp.init(years, months - 1, calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // 获取一个日历对象，并初始化为当前选中的时间
                        calendar.set(year, monthOfYear, dayOfMonth);
                        years = year;
                        months = monthOfYear + 1;
                    }
                });

        RelativeLayout ok = (RelativeLayout) window.findViewById(R.id.YES);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //todo 判断上次选择和本次选择的是否一样 如果1样就不需要清理掉数据
                time.setText(+ years +
                        "年" + months +
                        "月");
                listBeenAll.clear();
                adapter.notifyDataSetChanged();
                getyearmonthlist();
                dlg.cancel();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    public class RevenuCallBack extends Callback<RevenueEntiity> {
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

        private Context context;

        public RevenuCallBack(Context context) {
            super();
            this.context = context;

        }

        @Override
        public RevenueEntiity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            RevenueEntiity revenueEntiity = new Gson().fromJson(s, new TypeToken<RevenueEntiity>() {
            }.getType());
            return revenueEntiity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(RevenueEntiity response, int id) {
            if (response.code == 0) {
                listBeenpage.clear();
                listBeenpage=response.data.list;
                if(listBeenpage.size()==0){
                    Toast.makeText(context,"没有数据了哦",Toast.LENGTH_SHORT).show();
                }else {
                    if(cursor == 0){
                        listBeenAll.clear();
                    }
                    listBeenAll.addAll(listBeenpage);
                    adapter.notifyDataSetChanged();
                }


            }
        }
    }
}
