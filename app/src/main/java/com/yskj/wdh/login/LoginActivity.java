package com.yskj.wdh.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.LoginEntity;
import com.yskj.wdh.ui.localmerchant.LocalityShopHome;
import com.yskj.wdh.ui.verify.VerifyActivity;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MobileEncryption;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/13.
 */

public class LoginActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.login_phone)
    EditText loginPhone;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.login_forget_password)
    TextView loginForgetPassword;
    @Bind(R.id.login_btn)
    Button loginBtn;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private BusinessInfoBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        txtTitle.setText("登录");
        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(loginPassword.length()!=0){
                    loginBtn.setBackgroundResource(R.drawable.login_btn_true);
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.login_forget_password, R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                //startActivity(new Intent(context,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.login_forget_password:
                startActivity(new Intent(context, VerifyActivity.class).putExtra("type","2").putExtra("code","0"));
                break;
            case R.id.login_btn:

                boolean isPhoneNum = MobileEncryption.isMobileNO(loginPhone.getText().toString());
                if (!isPhoneNum){
                    showToast("请输入正确的手机号");
                }else if(loginPassword.length()==0){
                    showToast("请输入密码");
                }else {
                    SharedPreferences share2 = getSharedPreferences("mobile", 0);
                    SharedPreferences.Editor editor2 = share2.edit();
                    editor2.putString("mobile", loginPhone.getText().toString());
                    editor2.commit();
                    SharedPreferences share = getSharedPreferences("password", 0);
                    SharedPreferences.Editor editor = share.edit();
                    editor.putString("password", loginPassword.getText().toString());
                    editor.commit();
                    login();
                }
                break;
        }
    }

    private void login() {
        OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                .addHeader("Authorization", Ips.AUTHORIZATION)
                .addParams("username", loginPhone.getText().toString())
                .addParams("password", loginPassword.getText().toString())
                .addParams("grant_type", "password").build()
                .execute(new LoginCallBack());
    }

    private class LoginCallBack extends Callback<LoginEntity> {
        @Override
        public void onBefore(Request request, int id) {
            super.onBefore(request, id);
            stopMyDialog();
        }
        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            stopMyDialog();
        }
        @Override
        public LoginEntity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            LoginEntity loginEntity = new Gson().fromJson(s, new TypeToken<LoginEntity>() {
            }.getType());
            return loginEntity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            if (e.getMessage()==null&&e==null){
                return;
            }
            if (e.getMessage().toString().contains("401")||e.getMessage().toString().contains("403")||e.getMessage().toString().contains("400")){
                showToast("账号或密码错误");
            }else if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("服务器忙着哩，请耐心等待")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        dialog.dismiss();
                    }
                }).show();//在按键响应事件中显示此对话框
            }else if(e.getMessage().contains("502")){
                showToast("服务器维护中");
            }else {
                showToast("网络链接错误 ");
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            caches.put("access_token", Config.TOKENHEADER+response.accessToken);
            caches.put("token_type",response.tokenType);
            caches.put("expires_in",response.expiresIn+"");
            caches.put("scope",response.scope);
            SharedPreferences share2 = getSharedPreferences("mobile", 0);
            SharedPreferences.Editor editor2 = share2.edit();
            editor2.putString("access_token", Config.TOKENHEADER+response.accessToken);
            editor2.commit();
            getBusinessInfo(Config.TOKENHEADER+response.accessToken);
        }
    }
    private void getBusinessInfo(String token) {
        startMyDialog();
        OkHttpUtils.get().url(Urls.BUSINESS)
                .addHeader("Authorization", token)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopMyDialog();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                stopMyDialog();
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                Logger.d(response);
                int code = (int) map.get("code");
                if (code == 0) {
                    JSONObject ob = (JSONObject) map.get("data");
                    bean = JSONFormat.parseT(ob.toString(), BusinessInfoBean.class);
                    caches.put("business",ob.toString());
                    Logger.json(ob.toString());
                    if(bean.getStatus() == 1){
                        SharedPreferences share2 = getSharedPreferences("isFirstIn", 0);
                        SharedPreferences.Editor editor2 = share2.edit();
                        editor2.putString("isFirstIn", "0");
                        editor2.commit();
                        startActivity(new Intent(context, LocalityShopHome.class));
                    }else if(bean.getStatus() == 0){
                        showdialogs("您的资料正在审核中，请耐心等待");
                    }else if(bean.getStatus() == 3){
                        showdialogs("抱歉！您的商户已冻结，请联系客服");
                    }else if(bean.getStatus() == 0){
                        showdialogs("您的资料正在审核中，请耐心等待");
                    }else if(bean.getStatus() == 2){
                        showdialogs("您提交的资料未通过审核，请重新提交");
                    }

                } else if(code == 300302){
                    showdialogs("您还不是商家，请申请后登录");
                }else {
                    showToast(Messge.geterr_code(code));
                }

            }
        });
    }
    private static Boolean isExit = false;
}
