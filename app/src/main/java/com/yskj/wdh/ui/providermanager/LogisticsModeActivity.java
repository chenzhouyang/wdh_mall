package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.LogisticsModeAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.LogisticsModeBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 创建日期 2017/8/2on 15:12.
 * 描述：运费模板
 * 作者：姜贺YSKJ-JH
 */

public class LogisticsModeActivity extends BaseActivity implements View.OnClickListener, CheckOneInterface {
    NoScrollListView listView;
    ImageView imgBack;
    TextView txtTitle;
    TextView tvRight;
    CheckBox ckChoseOne;
    CheckBox ckChoseTwo;
    private ArrayList<LogisticsModeBean.DataBean> arrayListAll = new ArrayList<>();
    private ArrayList<LogisticsModeBean.DataBean> arrayListPage = new ArrayList<>();
    private LogisticsModeAdapter adapter;
    private LinearLayout llAdd;
    private ImageView imgSelect;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private String millId;
    private String modeId="";
    private String modeName;
    private PullToRefreshScrollView scrollView;

    private int cursor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_mode);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gotData();
    }

    private void gotData() {
        String json = "{" +"\"millId\": "+millId +",\"count\": 10"+",\"cursor\": "+cursor + "}";
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.FINDEXPRESSMODE).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new FindExpressModeCallback());
    }

    private class FindExpressModeCallback extends Callback<LogisticsModeBean>{

        @Override
        public LogisticsModeBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.d(s);
            LogisticsModeBean logisticsModeBean = new Gson().fromJson(s,new TypeToken<LogisticsModeBean>(){}.getType());
            return logisticsModeBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(LogisticsModeBean response, int id) {
            if (cursor == 0) {
                arrayListAll.clear();
            }
            //每次清空上页数据
            arrayListPage.clear();
            switch (response.code) {
                case 0:
                    arrayListPage = response.data;
                    arrayListAll.addAll(arrayListPage);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void initData() {
        token = caches.get("access_token");
        millId = getIntent().getStringExtra("millId");
    }
    private void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvRight = (TextView) findViewById(R.id.tv_right);
        llAdd = (LinearLayout) findViewById(R.id.ll_add);
        imgSelect = (ImageView) findViewById(R.id.img_select);
        listView = (NoScrollListView) findViewById(R.id.listView);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.scroll_view);
        txtTitle.setText("运费模板");
        ckChoseOne = (CheckBox) findViewById(R.id.ck_chose_one);
        ckChoseTwo = (CheckBox) findViewById(R.id.ck_chose_two);

        tvRight.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        imgSelect.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        adapter = new LogisticsModeAdapter(context, arrayListAll);
        listView.setAdapter(adapter);
        adapter.setCheckOneInterface(LogisticsModeActivity.this);

        scrollView.setMode(PullToRefreshBase.Mode.BOTH);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor =0;
                gotData();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor+=10;
                gotData();
                scrollView.onRefreshComplete();
            }
        });
        ckChoseOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckChoseTwo.setChecked(false);
                    checkNone();
                    modeId = "0";
                    modeName = "包邮";
                }
            }
        });
        ckChoseTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ckChoseOne.setChecked(false);
                    checkNone();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent();
                intent.putExtra("data",modeId+"-"+modeName);
                setResult(Config.ADDGOODSINFORESULTCODE,intent);
                finish();
                break;
            case R.id.tv_right:
                Intent intent1 = new Intent();
                intent1.putExtra("data",modeId+"-"+modeName);
                setResult(Config.ADDGOODSINFORESULTCODE,intent1);
                finish();
                break;
            //添加新模板
            case R.id.ll_add:
                Intent intent2 = new Intent(context,EditLogisticsActivityActivity.class);
                intent2.putExtra("id","");
                intent2.putExtra("type","3");
                intent2.putExtra("millId",millId);
                intent2.putExtra("operaType","1");//1新增 ， 2更新编辑已有模板
                startActivity(intent2);
                break;
        }
    }

    /**
     * 单选
     *
     * @param position
     */
    @Override
    public void checkOne(int position) {
        for (LogisticsModeBean.DataBean bean : arrayListAll) {
            bean.setSclected(false);
        }
        arrayListAll.get(position).setSclected(true);
        modeId = arrayListAll.get(position).id;
        modeName = arrayListAll.get(position).name;
        ckChoseOne.setChecked(false);
        ckChoseTwo.setChecked(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void checkEdit(int position) {
        Intent intent = new Intent(context,EditLogisticsActivityActivity.class);
        intent.putExtra("id",arrayListAll.get(position).id);
        intent.putExtra("type",arrayListAll.get(position).type);
        intent.putExtra("operaType","2");//1新增 ， 2更新编辑已有模板
        startActivity(intent);
    }

    /**
     * 全不选
     */
    public void checkNone() {
        for (LogisticsModeBean.DataBean bean : arrayListAll) {
            bean.setSclected(false);
        }
        adapter.notifyDataSetChanged();
    }
}
