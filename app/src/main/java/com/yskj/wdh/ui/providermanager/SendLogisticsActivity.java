package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * 创建日期 2017/8/21on 10:30.
 * 描述：//发货方式选择，完善物流单号
 * 作者：姜贺YSKJ-JH
 */

public class SendLogisticsActivity extends BaseActivity implements View.OnClickListener{

    ImageView imgBack;
    TextView tvRight,tvName;
    RelativeLayout rlNeed;
    RelativeLayout rlNoNeed;
    EditText etExpressNum;
    ImageView imgScan;
    TextView tvSelectOne,tvSelectTwo;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private String orderId;
    private int type = 1; //0  快递  1  无需物流
    private String expressName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_logistics);
        initView();
    }

    private void initView() {
        token = caches.get("access_token");
        imgBack = (ImageView) findViewById(R.id.img_back);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlNeed = (RelativeLayout) findViewById(R.id.rl_need);
        rlNoNeed = (RelativeLayout) findViewById(R.id.rl_no_need);
        etExpressNum = (EditText) findViewById(R.id.et_express_num);
        imgScan = (ImageView) findViewById(R.id.img_scan);
        tvSelectOne = (TextView) findViewById(R.id.tv_select_one);
        tvSelectTwo = (TextView) findViewById(R.id.tv_select_two);
        tvName = (TextView) findViewById(R.id.tv_name);

        imgBack.setOnClickListener(this);
        rlNeed.setOnClickListener(this);
        rlNoNeed.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        imgScan.setOnClickListener(this);
        orderId = getIntent().getStringExtra("orderId");
//        expressName = getIntent().getStringExtra("expressName");
//        if (TextUtils.isEmpty(expressName)){
//            expressName = "顺丰";
//        }
//        tvName.setText(expressName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_need:
                //0  快递  1  无需物流
                type = 0;
                Intent intent1 = new Intent(context,LogisticsListActivity.class);
                startActivityForResult(intent1, Config.LOGISTICSLIST);
                break;
            case R.id.rl_no_need:
                type = 1;
                tvSelectTwo.setBackgroundResource(R.mipmap.fzg);
                tvSelectOne.setBackgroundResource(R.mipmap.fzf);
                break;
            case R.id.img_scan:
                //扫码验证
                Intent  intent = new Intent(SendLogisticsActivity.this, CaptureActivity.class);
                intent.putExtra("codebundle", "5");
                startActivityForResult(intent, Config.EXPRESSORDERID);
                break;
            case R.id.tv_right:
                sendDataToJava();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data==null){
            return;
        }
        String content = data.getStringExtra("data");
        // 根据上面发送过去的请求别
        switch (requestCode) {
            case Config.EXPRESSORDERID:
                etExpressNum.setText(content);
                break;
            case Config.LOGISTICSLIST:
                expressName = content;
                tvName.setText(expressName);
                tvSelectOne.setBackgroundResource(R.mipmap.fzg);
                tvSelectTwo.setBackgroundResource(R.mipmap.fzf);
                break;
        }
    }

    private void sendDataToJava() {
        //0  快递  1  无需物流
        switch (type){
            case 0:
                if (TextUtils.isEmpty(etExpressNum.getText().toString())){
                    showToast("请填写物流单号");
                    return;
                }
                OkHttpUtils.post().url(Urls.LOGISTICSSEND).addHeader(Config.HEADTOKEN,token).addParams("orderId",orderId)
                        .addParams("shippingName",expressName).addParams("shippingCode",etExpressNum.getText().toString())
                        .build().execute(myStringCallBack);
                break;
            case 1:
                OkHttpUtils.post().url(Urls.LOGISTICSSEND).addHeader(Config.HEADTOKEN,token).addParams("orderId",orderId)
                        .addParams("shippingName","wuxuwuliu")
                        .build().execute(myStringCallBack);
                break;
        }
    }


    private StringCallback myStringCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
            Logger.d(e.getMessage());
        }

        @Override
        public void onResponse(String response, int id) {
            Map<String, Object> map = JSONFormat.jsonToMap(response);
            int code = (int) map.get("code");
            Logger.d(response);
            switch (code){
                case 0:
                    showToast(Messge.geterr_code(code));
                    finish();
                    break;
                case 734:
                    showToast("商家不存在");
                    break;
                case 754:
                    showToast("订单不存在");
                    break;
                case 1022:
                    showToast("供应商不存在");
                    break;
            }
        }
    };
}
