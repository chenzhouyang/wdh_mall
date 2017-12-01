package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.bean.StoreManagementBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by wdx on 2016/11/8 0008.
 */
public class StoreManagementActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.img_mark)
    ImageView img_mark;
    @Bind(R.id.image_right)
    TextView image_right;
    @Bind(R.id.txt_the_name)
    TextView txt_the_name;
    @Bind(R.id.txt_type)
    TextView txt_type;
    @Bind(R.id.txt_message)
    TextView txt_message;
    @Bind(R.id.txt_address)
    TextView txt_address;
    @Bind(R.id.txt_the_map)
    TextView txt_the_map;
    @Bind(R.id.txt_account)
    TextView txt_account;
    private String a;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private BusinessInfoBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_management_layout);
        ButterKnife.bind(this);
        txtTitle.setText("门店信息");
        StoreManagementBean.RetDataBean mBean = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBusinessInfo();
    }

    private String txt(String a) {
        if (!TextUtils.isEmpty(a) && a.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < a.length(); i++) {
                char c = a.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            txt_account.setText(sb.toString());

        }

        return a;
    }

        private void getBusinessInfo() {
            OkHttpUtils.get().url(Urls.BUSINESS).addHeader("Authorization", aCache.get("access_token"))
                    .build().execute(new StringCallback() {
                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    stopMyDialog();
                }

                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    stopMyDialog();
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    stopMyDialog();
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {

                        JSONObject ob = (JSONObject) map.get("data");
                        bean = JSONFormat.parseT(ob.toString(), BusinessInfoBean.class);
                        aCache.put("business", ob.toString());
                        txt_account.setText(bean.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2") + "");//电话号
                        txt_the_name.setText(bean.getShopName());//商家名称
                        txt_the_map.setText(bean.getAreaString());//详细地址
                        if (bean.getCoverUrl().length() != 0) {
                            Glide.with(context).load(bean.getCoverUrl()).error(R.mipmap.default_image).into(img_mark);
                        } else {
                            img_mark.setImageResource(R.mipmap.default_image);
                        }
                        txt_message.setText(bean.getProfile());//商家简介
                        txt_address.setText(bean.getDetailAddress());//地址
                        txt_type.setText(bean.getCategoryName());//类型

                    }
                }
            });
        }

    @OnClick({R.id.img_back, R.id.image_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.image_right:
                startActivity(new Intent(context,MerChantActivity.class).putExtra("where",2));
                break;
        }
    }
}
