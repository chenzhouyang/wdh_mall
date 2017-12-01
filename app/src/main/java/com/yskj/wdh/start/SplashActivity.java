package com.yskj.wdh.start;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.LoginEntity;
import com.yskj.wdh.login.LoginActivity;
import com.yskj.wdh.ui.localmerchant.LocalityShopHome;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 *
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 *
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 *
 *
 */
public class SplashActivity extends BaseActivity {
	private LoadingCaches aCache = LoadingCaches.getInstance();
	public SharedPreferences preferences;
	private static final int GO_START = 1000;
	private static final int GO_GUIDE = 1001;
	private static final int GO_HOME = 1002;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private String   isFirstIn = null;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GO_START:
					goHome();
					break;
				case GO_GUIDE:
					startActivity(new Intent(context,LoginActivity.class));
					break;
			}
			super.handleMessage(msg);
		}
	};
	private BusinessInfoBean bean;
	private String password,mobile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		aCache.put("access_token","null");
		Intent intent = getIntent();
		mobile = intent.getStringExtra("mobile");
		password = intent.getStringExtra("password");
		sign();
	}
	private void sign(){
		OkHttpUtils.get().url(Urls.QNYJSON).build().execute(new StringCallback() {
			@Override
			public void onError(Call call, Exception e, int id) {
				SharedPreferences share4    = getSharedPreferences("isFirstIn", 0);
				isFirstIn  = share4.getString("isFirstIn", "");
				if(isFirstIn.equals("0")){
					mHandler.sendEmptyMessage(GO_START);
				}else if (isFirstIn.equals("1")){
					if (mobile == null) {
						mHandler.sendEmptyMessage(GO_GUIDE);
					}else {
						mHandler.sendEmptyMessage(GO_START);
					}
				}else {
					if (mobile == null) {
						mHandler.sendEmptyMessage(GO_GUIDE);
					}else {
						mHandler.sendEmptyMessage(GO_START);
					}

				}
			}

			@Override
			public void onResponse(String response, int id) {
				Map<String,Object> map = JSONFormat.jsonToMap(response);
				String sign = (String) map.get("sign");
				if(sign.equals("1")){
					SharedPreferences share4    = getSharedPreferences("isFirstIn", 0);
					isFirstIn  = share4.getString("isFirstIn", "");
					if(isFirstIn.equals("0")){
						mHandler.sendEmptyMessage(GO_START);
					}else if (isFirstIn.equals("1")){
						if (mobile == null) {
							mHandler.sendEmptyMessage(GO_GUIDE);
						}else {
							mHandler.sendEmptyMessage(GO_START);
						}
					}else {
						if (mobile == null) {
							mHandler.sendEmptyMessage(GO_GUIDE);
						}else {
							mHandler.sendEmptyMessage(GO_START);
						}
					}
				}else {
					new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
							.setMessage("唯多慧正在更新中，请稍后再试")//设置显示的内容
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
								public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
								}
							}).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
						public void onClick(DialogInterface dialog, int which) {//响应事件
							dialog.dismiss();
						}
					}).show();//在按键响应事件中显示此对话框
				}
			}
		});
	}


	private void goHome() {
		if (mobile == null ) {
			SharedPreferences share2    = getSharedPreferences("mobile", 0);
			mobile  = share2.getString("mobile", "null");
			SharedPreferences share    = getSharedPreferences("password", 0);
			password  = share.getString("password", "null");
		}
			OkHttpUtils.post().url(Urls.LOGIN).addHeader("Content-Type", Ips.CONTENT_TYPE)
					.addHeader("Authorization", Ips.AUTHORIZATION)
					.addParams("username", mobile)
					.addParams("password", password)
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
			isLogin(e);
			startActivity(new Intent(context, LocalityShopHome.class));
		}

		@Override
		public void onResponse(LoginEntity response, int id) {
			aCache.put("access_token", Config.TOKENHEADER+response.accessToken);
			aCache.put("token_type",response.tokenType);
			aCache.put("expires_in",response.expiresIn+"");
			aCache.put("scope",response.scope);
			getBusinessInfo(Config.TOKENHEADER+response.accessToken);
		}
	}
	private void getBusinessInfo(String token) {
		OkHttpUtils.get().url(Urls.BUSINESS).addHeader("Authorization", token)
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
					aCache.put("business",ob.toString());
					if(bean.getStatus() == 1){
						SharedPreferences share2 = getSharedPreferences("isFirstIn", 0);
						SharedPreferences.Editor editor2 = share2.edit();
						editor2.putString("isFirstIn", "0");
						editor2.commit();
						startActivity(new Intent(context, LocalityShopHome.class));
					}else if(bean.getStatus() == 0){
						showdialog("您的资料正在审核中，请耐心等待");
					}else if(bean.getStatus() == 3){
						showdialog("抱歉！您的商户已冻结，请联系客服");
					}else if(bean.getStatus() == 0){
						showdialog("您的资料正在审核中，请耐心等待");
					}else if(bean.getStatus() == 2){
						showdialog("您提交的资料未通过审核，请重新提交");
					}

				} else if(code == 300302){
					showdialog("您还不是商家，请申请后登录");
				}else {
					showToast(Messge.geterr_code(code));
				}

			}
		});
	}

}
