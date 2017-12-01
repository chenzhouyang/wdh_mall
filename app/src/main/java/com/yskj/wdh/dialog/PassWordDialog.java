package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.verify.VerifyActivity;


/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class PassWordDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private TextView message;
    private String diamessage;
    private LoadingCaches caches = LoadingCaches.getInstance();

    public PassWordDialog(Context context, String diamessage) {
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
                caches.put("type","rolloff");
                    context.startActivity(new Intent(context, VerifyActivity.class).putExtra("type","6"));

                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
