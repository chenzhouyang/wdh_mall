package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.ProjectDetailsBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by gtz on 2016/11/8 0008.
 */
public class CheckedHistoryDetails extends BaseActivity implements View.OnClickListener{
    private PullToRefreshListView xlv_details;
    private ImageView iv_history_back,iv_details;
    int ID;
    String cover,Name,Price;
    private String token;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    Gson gson = new Gson();
    private ProjectDetailsBean detailsBean;
    private TextView history_name,xlv_item_pric;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Intent intent = getIntent();
        String tmp = intent.getStringExtra("extra");
        cover = intent.getStringExtra("cover");
        Name = intent.getStringExtra("name");
        Price = intent.getStringExtra("price");
        ID = Integer.valueOf(tmp);
        time = intent.getStringExtra("time");
        initView();
        startNet();
    }

    private void startNet() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.projectNumberUrl)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("lifeId", ID+"")
                .addParams("type","1")
                .addParams("beginTime", time)
                .addParams("endTime", time)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String,Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if(code == 0){
                    detailsBean = gson.fromJson(response, ProjectDetailsBean.class);
                    ArrayList<ProjectDetailsBean.DataBean.ListBean> detailsDatas = detailsBean.getData().getList();
                    MyProjectAdapter myProjectAdapter = new MyProjectAdapter(detailsDatas);
                    xlv_details.setAdapter(myProjectAdapter);

                }else {
                    showToast(Messge.geterr_code(code));
                }
            }
        });

    }


    //项目详情
    class MyProjectAdapter extends BaseAdapter {
        ArrayList<ProjectDetailsBean.DataBean.ListBean> detailsDatas;

        public MyProjectAdapter(ArrayList<ProjectDetailsBean.DataBean.ListBean> detailsDatas) {
            this.detailsDatas = detailsDatas;
        }

        @Override
        public int getCount() {
            if (detailsDatas == null || detailsDatas.isEmpty()) {
                return 0;
            } else {
                return detailsDatas.size();
            }

        }


        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyItemHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(CheckedHistoryDetails.this, R.layout.xlv_item_details, null);
                holder = new MyItemHolder();
                holder.iv_details = (ImageView) convertView.findViewById(R.id.iv_details);
                holder.tv_xlv_history_price = (TextView) convertView.findViewById(R.id.tv_xlv_history_price);
                holder.tv_xlv_checked_time = (TextView) convertView.findViewById(R.id.tv_xlv_checked_time);
                convertView.setTag(holder);
            } else {
                holder = (MyItemHolder) convertView.getTag();
            }
            if (!detailsDatas.isEmpty()) {
                //填充数据
                holder.tv_xlv_checked_time.setText(detailsDatas.get(position).getCount() + "");
                holder.tv_xlv_history_price.setText(detailsDatas.get(position).getValidityDate() + "");

            }

            return convertView;
        }

    }

    private void initView() {
        xlv_details = (PullToRefreshListView) findViewById(R.id.xlv_details);
        xlv_details.setMode(PullToRefreshBase.Mode.BOTH);
        xlv_details.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                startNet();
                MethodUtils.stopRefresh(xlv_details);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                MethodUtils.stopRefresh(xlv_details);

            }
        });
        iv_history_back = (ImageView) findViewById(R.id.iv_history_back);
        iv_history_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_details = (ImageView) findViewById(R.id.iv_details);
        history_name = (TextView) findViewById(R.id.history_name);
        history_name.setText(Name);
        xlv_item_pric = (TextView) findViewById(R.id.xlv_item_pric);
        xlv_item_pric.setText(Price);
        if(cover.length()!=0){
            Glide.with(CheckedHistoryDetails.this).load(cover).error(R.mipmap.img_error).into(iv_details);
        }else{
            iv_details.setImageResource(R.mipmap.img_error);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_history_back:
                CheckedHistoryDetails.this.finish();
                break;
        }
    }

    class MyItemHolder {
        ImageView iv_details;
        TextView tv_xlv_history_price, tv_xlv_checked_time;
    }
}
