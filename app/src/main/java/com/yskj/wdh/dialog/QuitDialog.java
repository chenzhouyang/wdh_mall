package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.ShoppingDetailEntity;
import com.yskj.wdh.login.LoginActivity;

import org.litepal.crud.DataSupport;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class QuitDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;
    private String diamessage;
    private LoadingCaches caches = LoadingCaches.getInstance();

    public QuitDialog(Context context, String diamessage) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.diamessage = diamessage;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_give_up);

        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);
        message = (TextView) findViewById(R.id.message);
        message.setText(diamessage);
        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                dismiss();
                SharedPreferences share2 = context.getSharedPreferences("isFirstIn", 0);
                SharedPreferences.Editor editor2 = share2.edit();
                editor2.putString("isFirstIn", "1");
                editor2.commit();
                DataSupport.deleteAll(ShoppingDetailEntity.class, "id > ?" , "0");
                caches.put("access_token","null");
                mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, ""));
                context.startActivity(new Intent(context, LoginActivity.class));
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
            }
//            ExampleUtil.showToast(logs, getApplicationContext());
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(context,
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
            }
        }
    };
}
