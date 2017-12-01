package com.yskj.wdh.ui.localmerchant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.PassWordDialog;
import com.yskj.wdh.entity.UserInfoEntity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.GridPasswordView;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by YSKJ-02 on 2017/1/3.
 */

public class RolloffActivity extends BaseActivity {
    @Bind(R.id.folloff_amount)
    EditText folloffamount;
    @Bind(R.id.rolloff_true)
    LinearLayout rollofftrue;
    @Bind(R.id.img_back)
    ImageView backtoptitlell;
    @Bind(R.id.txt_title)
    TextView redlisttitle;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private double available;
    private UserInfoEntity information;
    private SurePwdDialog surePwdDialog;
    private String token, into, url, msg, message, messages;
    private Intent intent;
    private boolean paychara = false;
    private BusinessInfoBean bean;
    private boolean ispassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rolloff);
        ButterKnife.bind(this);
        caches.put("type", "null");
        folloffamount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        folloffamount.setText(s);
                        folloffamount.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    folloffamount.setText(s);
                    folloffamount.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        folloffamount.setText(s.subSequence(0, 1));
                        folloffamount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        intent = getIntent();
        information = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        bean = new Gson().fromJson(caches.get("business"), new TypeToken<BusinessInfoBean>() {
        }.getType());
        ispassword = information.data.accountPasswordExist;
        if (!information.data.accountPasswordExist) {
            PassWordDialog dialog = new PassWordDialog(context, "您还没有设置支付密码,是否前往设置");
            dialog.show();
        }
        into = intent.getStringExtra("into");
        caches.put("into", into);
        if (into.equals("0")) {
            redlisttitle.setText("转出到可用通贝");
            url = Urls.TRANSFERTOUSER;
            msg = "转出成功";
            message = "请输入要转出的金额";
            folloffamount.setHint("本次最多可转出" + StringUtils.getStringtodouble(bean.getFundAccount()) + "通贝");
        } else {
            // rolloffHint.setVisibility(View.GONE);
            url = Urls.TRANSFERTOLOCALSHOP;
            redlisttitle.setText("通贝转入到商家");
            msg = "转入成功";
            message = "请输入要转入的金额";
            if (information.data.userVo.fundAccount <= 0) {
                folloffamount.setHint("本次最多可转入0.00通贝");
            } else {
                folloffamount.setHint("本次最多可转入" + StringUtils.getStringtodouble(information.data.userVo.fundAccount));
            }

        }
        rollofftrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollofftrue();
            }
        });
        backtoptitlell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void rollofftrue() {


        if (TextUtils.isEmpty(folloffamount.getText().toString())) {
            showToast(message);
        } else if (folloffamount.getText().toString().startsWith(".")) {
            showToast("请输入正确的金额");
        } else {
            if (into.equals("0")) {
                if (bean.getFundAccount() >= Double.parseDouble(folloffamount.getText().toString())
                        && Double.parseDouble(folloffamount.getText().toString()) > 0) {
                    paychara = true;
                } else {
                    paychara = false;
                    messages = "资金不足";
                }
            } else {
                if ((information.data.userVo.fundAccount) >= Double.parseDouble(folloffamount.getText().toString())
                        && Double.parseDouble(folloffamount.getText().toString()) > 0) {
                    paychara = true;
                } else {
                    paychara = false;
                    messages = "可用通贝不足";
                }
            }
            if (paychara) {
                zhifumima();
            } else {
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage(messages)//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        }
    }


    private void zhifumima() {
        if (ispassword) {
            surePwdDialog = new SurePwdDialog(context);
            surePwdDialog.show();
        } else {
        }
    }

    public class SurePwdDialog extends Dialog {

        private ImageView img_dismiss;
        private TextView tv_forget_pwd;
        private GridPasswordView pwdView;

        public SurePwdDialog(Context context) {
            super(context, R.style.GiftDialog);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_suregift_pwd);
            initView();
        }


        private void initView() {
            img_dismiss = (ImageView) findViewById(R.id.img_dismiss);
            tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);
            pwdView = (GridPasswordView) findViewById(R.id.pwd);


            //dialog弹出时弹出软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

            pwdView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onChanged(String psw) {

                }

                @Override
                public void onMaxLength(String psw) {
                    dismissJP();
                    dismiss();
                    startMyDialog();
                    OkHttpUtils.post().url(url)
                            .addHeader("Authorization", caches.get("access_token"))
                            .addParams("amount", folloffamount.getText().toString())
                            .addParams("payPassword", pwdView.getPassWord())
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
                            int code = (int) map.get("code");
                            if (code == 0) {
                                showToast(msg);
                                startActivity(new Intent(context, LocalityShopHome.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            } else {
                                showToast(Messge.geterr_code(code));
                            }
                        }
                    });
                }
            });

            img_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_forget_pwd.setText(folloffamount.getText().toString());
            Window dialogWindow = getWindow();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
    }

    //软键盘消失
    public void dismissJP() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) RolloffActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
