package com.yskj.wdh.ui.collectmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yskj.wdh.AppManager;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ProsceniumAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.EstablishDialog;
import com.yskj.wdh.entity.ProsceniumEntity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.SwipeListView;
import com.yskj.wdh.zxing.activity.CaptureCollectMoneyActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class ProsceniumActivity extends BaseActivity {
    @Bind(R.id.prosecnium_lv)
    SwipeListView prosecniumLv;
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.take_money)
    ImageView takeMoney;
    @Bind(R.id.paid_money)
    ImageView paidMoney;
    @Bind(R.id.scrollView_proseceni)
    PullToRefreshScrollView scrollViewProseceni;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private ProsceniumAdapter prosceniumAdapter;
    private ProsceniumEntity prosceniumEntity;
    private int cursor = 0;
    private List<ProsceniumEntity.DataBean.ListBean> prolist = new ArrayList<>();
    private List<ProsceniumEntity.DataBean.ListBean> prolistall = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_proscenium);
        ButterKnife.bind(this);
        txtTitle.setText("收银台");
        iniscrollview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cursor = 0;
        getdata();
    }

    @OnClick({R.id.take_money, R.id.paid_money
            , R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_money:
                EstablishDialog dialog = new EstablishDialog(context);
                dialog.show();
                break;
            case R.id.paid_money:
                startActivity(new Intent(context, AccountPaidActivity.class));
                break;
            case R.id.img_back:
                AppManager.getInstance().killActivity(ProsceniumActivity.class);
                AppManager.getInstance().killActivity(CaptureCollectMoneyActivity.class);
                break;
        }
    }

    private void iniscrollview() {
        prosceniumAdapter = new ProsceniumAdapter(context, prolistall,prosecniumLv.getRightViewWidth());
        prosecniumLv.setAdapter(prosceniumAdapter);
        prosceniumAdapter.setOnRightItemClickListener(new ProsceniumAdapter.onRightItemClickListener() {
            @Override
            public void onRightItemClick(View v, final int position) {
                ProsceniumEntity.DataBean.ListBean prosceniumEntity = prolistall.get(position);
                OkHttpUtils.get().url(Urls.DEL).addHeader("Authorization", caches.get("access_token"))
                        .addParams("orderId",prosceniumEntity.id+"").build().execute(new StringCallback() {
                    @Override
                    public void onBefore(Request request, int id) {
                        super.onBefore(request, id);
                        startMyDialog();
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        isLogin(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Map<String,Object> map = JSONFormat.jsonToMap(response);
                        int code = (int) map.get("code");
                        if(code == 0){
                            showToast("删除成功");
                            prosecniumLv.hiddenRight(prosecniumLv.getChildAt(position));
                            getdata();
                        }else {
                            showToast(Messge.geterr_code(code));
                        }

                    }
                });
            }
        });
        scrollViewProseceni.setMode(PullToRefreshBase.Mode.BOTH);
        scrollViewProseceni.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor = 0;
                getdata();
                scrollViewProseceni.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                cursor++;
                getdata();
                scrollViewProseceni.onRefreshComplete();
            }
        });
    }

    private void getdata() {
        OkHttpUtils.get().url(Urls.PROCSE).addHeader("Authorization", caches.get("access_token"))
                .addParams("status", "0")
                .addParams("count", "10")
                .addParams("cursor", cursor * 10 + "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                prosceniumEntity = new Gson().fromJson(response, new TypeToken<ProsceniumEntity>() {
                }.getType());
                if (prosceniumEntity.code == 0) {
                    prolist.clear();
                    prolist.addAll(prosceniumEntity.data.list);
                    if (prolist.size() != 0) {
                        if (cursor == 0) {
                            prolistall.clear();
                        }
                        prolistall.addAll(prolist);

                    } else {
                        prolistall.clear();
                        showToast("暂无记录");
                    }
                    prosceniumAdapter.notifyDataSetChanged();
                }

            }
        });
    }


}
