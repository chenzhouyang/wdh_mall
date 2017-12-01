package com.yskj.wdh.ui.providermanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.MillInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.NumberFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 陈宙洋
 * 2017/8/5.
 * 描述：我的收入activity
 */

public class IncomeActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.income_price)
    TextView incomePrice;
    @Bind(R.id.income_day)
    TextView incomeDay;
    @Bind(R.id.income_mouth)
    TextView incomeMouth;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        token = caches.get("access_token");
        txtTitle.setText("我的收入");
        OkHttpUtils.get().url(Urls.MILLINFO).addHeader(Config.HEADTOKEN,token).build().execute(new MillInfoCallBack());

        OkHttpUtils.get().url(Urls.GETPROFITAMOUNTBYUSER).addHeader(Config.HEADTOKEN,token).addParams("billTypes","1081").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                Double data = (Double) map.get("data");
                if (code==0){
                    incomeMouth.setText("月收入："+String.format("%.2f", NumberFormat.convertToDouble(data, 0d)));
                }
            }
        });
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        Logger.d(year+"-"+month+"-"+day);
        OkHttpUtils.get().url(Urls.GETPROFITAMOUNTBYUSER).addHeader(Config.HEADTOKEN,token).addParams("billTypes","1081").addParams("year",year+"")
                .addParams("month",month+"").addParams("day",day+"").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                Double data = (Double) map.get("data");
                if (code==0){
                    Logger.d(data+"");
                    incomeDay.setText("日收入："+String.format("%.2f", NumberFormat.convertToDouble(data, 0d)));
                }
            }
        });
    }

    private class MillInfoCallBack extends Callback<MillInfoBean>{

        @Override
        public MillInfoBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.json(s);
            MillInfoBean millInfoBean = new Gson().fromJson(s,new TypeToken<MillInfoBean>(){}.getType());
            return millInfoBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(MillInfoBean response, int id) {
            if (response.code==0){
                incomePrice.setText("总收入："+String.format("%.2f", NumberFormat.convertToDouble(response.data.totalProfit, 0d)));
            }
        }
    }

    @OnClick({R.id.img_back, R.id.income_day, R.id.income_mouth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.income_day:
                break;
            case R.id.income_mouth:
                break;
        }
    }
}
