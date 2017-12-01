package com.yskj.wdh.ui.deposit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.ProveEntitiy;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MobileEncryption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.ParseException;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 实名认证
 */

public class ApproveActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.informatation_number)
    EditText informatationNumber;
    @Bind(R.id.again_number)
    EditText againNumber;
    @Bind(R.id.deposittobank_true)
    TextView deposittobankTrue;
    @Bind(R.id.approve_true)
    LinearLayout approveTrue;
    @Bind(R.id.approve_tishi)
    TextView approveTishi;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        //查询认证信息
        getrealname();
    }

    private void getrealname() {
        startMyDialog();
        OkHttpUtils.get().url(Urls.REALNAME).addHeader("Authorization", caches.get("access_token")).build()
                .execute(new ProveCallBalk());
        stopMyDialog();
    }

    @OnClick({R.id.img_back, R.id.deposittobank_true})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (type.equals("0")) {
                    finish();
                } else {
                    startActivity(new Intent(context, BankListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                break;
            case R.id.deposittobank_true:
                try {
                    if (name.length() == 0) {
                        showToast("请填写您的真实名字");
                    } else if (informatationNumber.length() == 0) {
                        showToast("请填写您的身份证号");
                    } else if (againNumber.length() == 0) {
                        showToast("请确认您的身份证号");
                    } else if (!MobileEncryption.IDCardValidate(informatationNumber.getText().toString()).equals("合法的身份证号")) {
                        showToast("请正确填写您的身份证号");
                    } else if (!informatationNumber.getText().toString().equals(againNumber.getText().toString())) {
                        showToast("两次填写不一致");
                    } else {
                        getcardNo();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getcardNo() {
        OkHttpUtils.post().url(Urls.IDENRITY).addHeader("Authorization", caches.get("access_token"))
                .addParams("name", name.getText().toString()).addParams("identityCardNo", againNumber.getText().toString()).build()
                .execute(new StringCallback() {
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
                        Map<String, Object> map = JSONFormat.jsonToMap(response);
                        int code = (int) map.get("code");
                        if (code == 0) {
                            showToast("认证成功");
                            getuserinfo();
                        } else {
                            showToast(Messge.geterr_code(code));
                        }
                    }
                });

    }

    public class ProveCallBalk extends Callback<ProveEntitiy> {
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
        public ProveEntitiy parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ProveEntitiy proveEntitiy = new Gson().fromJson(s, new TypeToken<ProveEntitiy>() {
            }.getType());
            return proveEntitiy;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().equals("500")){
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("服务器忙着哩，请耐心等待!")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }else if(e.getMessage().equals("502")){
                showToast("服务器维护中");
            }

        }

        @Override
        public void onResponse(ProveEntitiy response, int id) {
            if (response.code == 0) {
                deposittobankTrue.setVisibility(View.GONE);
                name.setFocusable(false);
                name.setText(response.data.name);
                informatationNumber.setFocusable(false);
                informatationNumber.setText(response.data.identityCardNo);
                againNumber.setVisibility(View.GONE);
                approveTrue.setVisibility(View.GONE);
                approveTishi.setVisibility(View.GONE);
            } else if(response.code == 101){
            }else {
                showToast(Messge.geterr_code(response.code));
            }
        }
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
                caches.put("approve", "approve");
                caches.put("userinfo", response);
                startActivity(new Intent(context, AddBankActivity.class).putExtra("type", "0").setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
    }
}
