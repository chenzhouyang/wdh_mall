package com.yskj.wdh.ui.localmerchant;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ProfitAdapter1;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.ProfitBean1;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * Created by wdx on 2016/11/7 0007.
 */
public class ProfitActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.sp_profit_date)
    Spinner sp_profit_date;
    @Bind(R.id.txt_profit_rolling_number)
    TextView txt_profit_rolling_number;
    @Bind(R.id.txt_profit_project)
    TextView txt_profit_project;
    @Bind(R.id.txt_profit_money)
    TextView txt_profit_money;
    @Bind(R.id.lv_history)
    PullToRefreshListView lv_history;
    private int mMaxCount = 10; //每次请求的最大条数
    private int mOffset = 0; //从第几条开始请求
    private int mCount;
    private ProfitAdapter1 mAdapter;
    private LoadingCaches aCache = new LoadingCaches();
    private String token;
    private Handler myhander;
    private String date = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profit_layout);
        ButterKnife.bind(this);
        txtTitle.setText("收益记录");
        initView();

    }

    private void initView() {
        myhander = new Handler();
        //数据
        ArrayList<String> data_list = new ArrayList<String>();
        data_list.add("今天");
        data_list.add("最近三天");
        data_list.add("最近七天");
        data_list.add("最近一月");
        //适配器
        final ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_profit_date.setAdapter(arr_adapter);

        lv_history = (PullToRefreshListView) findViewById(R.id.lv_history);
        lv_history.setMode(PullToRefreshBase.Mode.BOTH);
        lv_history.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mOffset = 0;
                MethodUtils.stopRefresh(lv_history);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                Toast.makeText(ProfitActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
                MethodUtils.stopRefresh(lv_history);

            }
        });
        myhander = new Handler();
        mAdapter = new ProfitAdapter1(this, R.layout.item_profit);
        lv_history.setAdapter(mAdapter);
        sp_profit_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.clear();
                switch ((int) arr_adapter.getItemId(position)) {
                    case 0:
                        date = "0";
                        break;
                    case 1:
                        date = "3";
                        break;
                    case 2:
                        date = "7";
                        break;
                    case 3:
                        date = "30";
                        break;
                }
                getrednews(date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getrednews(String data) {
        mOffset = 0;
        startMyDialog();
        OkHttpUtils.get().url(Urls.SHOUYIJILU)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("dayNum", data)
                .addParams("max", mMaxCount+"")
                .addParams("offset", mOffset + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                ProfitBean1 bean = new Gson().fromJson(response,new TypeToken<ProfitBean1>(){}.getType());
                txt_profit_rolling_number.setText(bean.getData().getStatistics().getCount()+ "");
                txt_profit_money.setText(String.format("%.2f", StringUtils.convertToDouble(bean.getData().getStatistics().getPayAmount(), 0d)));
                if (bean.getCode() == 0) {
                        List<ProfitBean1.DataBean.ListBean> mList = new ArrayList<ProfitBean1.DataBean.ListBean>();
                        mList.addAll(bean.getData().getList());
                        mAdapter.addItem(mList, mOffset == 0);
                }
            }
        });
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}
