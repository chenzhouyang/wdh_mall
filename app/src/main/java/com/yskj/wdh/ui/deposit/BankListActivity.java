package com.yskj.wdh.ui.deposit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.BankListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.BankListEntity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.DragDelListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/15.
 * 银行卡列表
 */

public class BankListActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.bank_list_pulllistview)
    DragDelListView bankListPulllistview;
    @Bind(R.id.add_bank)
    ImageView addBank;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banklist);
        ButterKnife.bind(this);
        txtTitle.setText("银行卡");
        //获取银行卡列表
        getbanklist();
    }

    private void getbanklist() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.BANKLIST).
                addHeader("Authorization",caches.get("access_token")).build().execute(new BankListCallBalk(context,bankListPulllistview));
        stopMyDialog();
    }

    @OnClick({R.id.img_back, R.id.add_bank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_bank:
                startActivity(new Intent(context,AddBankActivity.class).putExtra("type","0"));
                break;
        }
    }
    public class BankListCallBalk extends Callback<BankListEntity> {
        private Context context;
        private DragDelListView bankListPulllistview;
        private ArrayList<BankListEntity.DataBean> dataBeen;
        public BankListCallBalk(Context context,DragDelListView bankListPulllistview){
            super();
            this.context = context;
            this.bankListPulllistview = bankListPulllistview;
        }
        @Override
        public BankListEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            BankListEntity bankListEntity = new Gson().fromJson(s,new TypeToken<BankListEntity>(){}.getType());
            return bankListEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(BankListEntity response, int id) {
            if(response.code == 0){
                dataBeen = response.data;
                if (dataBeen.size()==0){
                    bankListPulllistview.setVisibility(View.GONE);
                }else {
                    bankListPulllistview.setVisibility(View.VISIBLE);
                    BankListAdapter adapter = new BankListAdapter(context,dataBeen);
                    bankListPulllistview.setAdapter(adapter);
                }

            }else {
                Toast.makeText(context, Messge.geterr_code(response.code),Toast.LENGTH_SHORT).show();
            }
        }
    }
}
