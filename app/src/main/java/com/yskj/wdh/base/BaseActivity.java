package com.yskj.wdh.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.AppManager;
import com.yskj.wdh.R;
import com.yskj.wdh.ThemeColor;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.MyLoading;
import com.yskj.wdh.entity.LoginEntity;
import com.yskj.wdh.login.LoginActivity;
import com.yskj.wdh.ui.localmerchant.LocalityShopHome;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.SimplexToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public abstract class BaseActivity extends Activity {
    public SharedPreferences sp;
    private MyLoading myloading;
    public Context context;
    private LoadingCaches aCache = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCustomTheme();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止所有activity横屏
        sp = getSharedPreferences("UserInfor", 0);
        context = this;


    }
    /**重写此方法，达到某些界面不要沉浸式状态栏，直接从写一个空方法即可*/
    protected void setCustomTheme() {
        ThemeColor.setTranslucentStatus(this, theme());
    }
    /**重写此方法，达到子类界面自定义颜色*/
    protected int theme() {
        return R.color.layout_title_color;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String getToken() {
        Long token = Long.valueOf(sp.getString("token", "0")) + Long.valueOf(sp.getString("server_salt", "0"));
        return String.valueOf(token);
    }
public  void showdialog(String mess){
    new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
            .setMessage(mess)//设置显示的内容
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                    startActivity(new Intent(context,LoginActivity.class));
                }
            }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
        public void onClick(DialogInterface dialog, int which) {//响应事件
            startActivity(new Intent(context,LoginActivity.class));
        }
    }).show();//在按键响应事件中显示此对话框
}
    public  void showdialogs(String mess){
        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                .setMessage(mess)//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                    }
                }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
            public void onClick(DialogInterface dialog, int which) {//响应事件
            }
        }).show();//在按键响应事件中显示此对话框
    }
    /**
     * 判断是否登录
     */
    public void isLogin(Exception e) {
        if (e.getMessage()==null){
            return;
        }
        if (e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("401")){
            goHome();
        }else if(e!=null&&e.getMessage()!=null&&e.getMessage().toString().contains("500")){
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
        }else if(e.getMessage().contains("502")){
            showToast("服务器维护中");
        }else {
            showToast("网络链接错误 ");
        }
    }

    public void onBack(View v) {

        dismissInputMethod();
        finish();
    }

    public void rightTextview(View v) {

    }

    /**
     * dialog 启动
     */
    public void startMyDialog() {
        if (myloading == null) {
            myloading = MyLoading.createLoadingDialog(context);
        }
        if (!isFinishing()) {
            myloading.show();
        }
    }

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null) {
            if (!isFinishing()){
                myloading.dismiss();
            }
            myloading = null;
        }
    }

    public void showToast(String msg) {
        SimplexToast.show(context,msg);
    }

    public void showToast(int strId) {
        SimplexToast.show(context,strId);
    }



    protected void dismissInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseActivity.this.getCurrentFocus() != null) {
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                        0);
            }
        }

    }

    protected void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (BaseActivity.this.getCurrentFocus() != null) {
            imm.toggleSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     * 字体大小不随系统改变而变
     * @return
     */
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        OkHttpUtils.getInstance().cancelTag(this);
    }

    private void goHome() {
            SharedPreferences share2    = getSharedPreferences("mobile", 0);
            String mobile  = share2.getString("mobile", "null");
            SharedPreferences share    = getSharedPreferences("password", 0);
            String password  = share.getString("password", "null");
            startMyDialog();
            OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
                    .addHeader("Authorization", Ips.AUTHORIZATION)
                    .addParams("username", mobile)
                    .addParams("password", password)
                    .addParams("grant_type", "password").build()
                    .execute(new LoginCallBack());
            stopMyDialog();
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
            if(e.getMessage().contains("401")||e.getMessage().contains("403")||e.getMessage().contains("400")){
                showToast("帐号或密码错误");
                startActivity(new Intent(context, LoginActivity.class));
            }else {
                showToast("网络链接错误");
            }
        }

        @Override
        public void onResponse(LoginEntity response, int id) {
            aCache.put("access_token", Config.TOKENHEADER+response.accessToken);
            aCache.put("token_type",response.tokenType);
            aCache.put("expires_in",response.expiresIn+"");
            aCache.put("scope",response.scope);
            startActivity(new Intent(context, LocalityShopHome.class));
        }
    }
}
