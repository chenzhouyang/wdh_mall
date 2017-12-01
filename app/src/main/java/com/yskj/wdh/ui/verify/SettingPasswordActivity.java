package com.yskj.wdh.ui.verify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.LoginPassWordEntity;
import com.yskj.wdh.entity.UserInfoEntity;
import com.yskj.wdh.login.LoginActivity;
import com.yskj.wdh.ui.localmerchant.DepositToBankActivity;
import com.yskj.wdh.ui.localmerchant.RolloffActivity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.Messge;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 * 设置密码界面
 */

public class SettingPasswordActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.login_phone)
    EditText loginPhone;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.save_password)
    TextView savePassword;
    @Bind(R.id.setpassword_tishi)
    TextView setpasswordTishi;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token, type, mobile, code;
    private UserInfoEntity infoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpasseword);
        ButterKnife.bind(this);
        token = caches.get("access_token");
        type = getIntent().getStringExtra("type");
        if(!token.equals("null")){
            infoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());

            if (type.equals("2") && token.equals("null")) {
                txtTitle.setText("修改登录密码");
            } else if (type.equals("2") && !token.equals("null")) {
                txtTitle.setText("修改登录密码");
            } else {
                if (infoEntity.data.accountPasswordExist) {
                    txtTitle.setText("修改支付密码");
                } else {
                    txtTitle.setText("设置支付密码");
                }

            }
        }

        if (type.equals("2")) {
            mobile = getIntent().getStringExtra("mobile");
        } else {
            setpasswordTishi.setVisibility(View.VISIBLE);
            SharedPreferences share2 = getSharedPreferences("mobile", 0);
            mobile = share2.getString("mobile", "null");
            loginPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            loginPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            loginPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            loginPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        code = getIntent().getStringExtra("code");


    }


    //更改未登陆的登陆密码
    private void setloginpasswore() {
        OkHttpUtils.post().url(Urls.NOTLOGINPW)
                .addParams("mobile", mobile)
                .addParams("password", loginPassword.getText().toString()).build().execute(new PassWordCall());
    }


    //更改支付密码
    private void setupdateAccount() {
        OkHttpUtils.post().url(Urls.UPDATEACCOUNT)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("password", loginPassword.getText().toString())
                .build().execute(new PassWordCall());
    }

    //设置支付密码
    private void addupdateAccount() {
        OkHttpUtils.post().url(Urls.UPDATEACCOUNT)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("password", loginPassword.getText().toString())
                .build().execute(new PassWordCall());
    }

    @OnClick({R.id.img_back, R.id.save_password})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.save_password:
                if (loginPhone.length() == 0) {
                    showToast("输入新密码");
                } else if (loginPassword.length() == 0) {
                    showToast("输入确认新密码");
                } else if(!loginPhone.getText().toString().equals(loginPassword.getText().toString())){
                    showToast("两次输入的密码不一致，请确认");
                }else {
                    if (type.equals("2")) {
                        setloginpasswore();
                    } else {
                        if (infoEntity.data.accountPasswordExist) {
                            setupdateAccount();
                        } else {
                            addupdateAccount();
                        }

                    }
                }

                break;
        }
    }

    public class PassWordCall extends Callback<LoginPassWordEntity> {
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
        public LoginPassWordEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginPassWordEntity passwordentity = new Gson().fromJson(s, new TypeToken<LoginPassWordEntity>() {
            }.getType());
            return passwordentity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
isLogin(e);
        }

        @Override
        public void onResponse(LoginPassWordEntity response, int id) {
            if (response.code == 0) {
                showToast("密码修改成功");
                if(type.equals("2")){
                    SharedPreferences share = getSharedPreferences("password", 0);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("password", loginPassword.getText().toString());
                    editor.commit();
                    SharedPreferences share2    = getSharedPreferences("mobile", 0);
                    String mobile  = share2.getString("mobile", "null");
                    if(type.equals("2")){
                        startActivity(new Intent(context, LoginActivity.class));

                    }else {
                        startActivity(new Intent(context, DepositToBankActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }

                }else {
                    if (code.equals("0")) {
                        getuserinfo();
                    } else {
                        if(type.equals("2")){
                            startActivity(new Intent(context, LoginActivity.class));
                        }else if(type.equals("6")){
                            getuserinfo();
                        }
                    }
                }

            }else {
                showToast(Messge.geterr_code(response.code));
            }
        }
    }

    //更新个人信息
    private void getuserinfo() {
        OkHttpUtils.get().url(Urls.USERINFO).addHeader("Authorization", caches.get("access_token"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                caches.put("userinfo", response);
                if(caches.get("type").equals("rolloff")){
                    startActivity(new Intent(context, RolloffActivity.class).putExtra("into",caches.get("into")));
                }
                if(type.equals("2")){
                    startActivity(new Intent(context, LoginActivity.class));
                }else {
                    startActivity(new Intent(context, DepositToBankActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

            }
        });
    }
}
