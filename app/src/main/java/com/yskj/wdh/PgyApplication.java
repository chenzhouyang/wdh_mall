package com.yskj.wdh;

import android.content.Intent;
import android.content.SharedPreferences;

import com.pgyersdk.crash.PgyCrashManager;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.zhy.http.okhttp.OkHttpUtils;

import org.litepal.LitePalApplication;

import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/10/9 0009.
 */
public class PgyApplication extends LitePalApplication {
    public static PgyApplication instance = null;
    public static SharedPreferences sp;
    private Intent intent;
    public static PgyApplication getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //注册蒲公英崩溃日志
        PgyCrashManager.register(getApplicationContext());
        instance = this;
        // 初始化 JPush
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //分享
        UMShareAPI.get(this);
        sp = getSharedPreferences("UserInfor", 0);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //TODO		打包的时候 LogLevel.NONE
    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //微信 wx12342956d1cab4f9,a5ae111de7d9ea13788a5e02c07c94d
        PlatformConfig.setWeixin("wxe9a85bd474382b77", "f5e72c2091572e783f1df56886b566f1");
        //新浪微博
        //PlatformConfig.setSinaWeibo(com.yskj.nlsk.config.Config.SSINA_APP_ID, com.yskj.nlsk.config.Config.SSINA_APP_SECRET);
        //最新的版本需要加上这个回调地址，可以在微博开放平台申请的应用获取，必须要有
        Config.REDIRECT_URL="http://sns.whalecloud.com/sina2/callback";
        //QQ
        PlatformConfig.setQQZone("1105999374", "ojD7eSzpDgDK85Zc");

    }
}
