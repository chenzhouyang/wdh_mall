package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yskj.wdh.R;
import com.yskj.wdh.util.SimplexToast;
import com.yskj.wdh.zxing.activity.CaptureActivity;

/**
 * Created by YSKJ-02 on 2017/1/17.
 */

public class GatheringDialog extends Dialog implements View.OnClickListener {
    private Button btnCancle;
    private Button btnSure;
    private Context context;
    private EditText message;

    public GatheringDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gathering);

        initView();
    }

    private void initView() {
        btnCancle = (Button) findViewById(R.id.btn_cancle);
        btnSure = (Button) findViewById(R.id.btn_sure);
        message = (EditText) findViewById(R.id.message);
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        message.setText(s);
                        message.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    message.setText(s);
                    message.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        message.setText(s.subSequence(0, 1));
                        message.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sure:
                if(message.length() == 0){
                    SimplexToast.show(context,"请填写收款金额");
                }else {
                    Intent openCameraIntent = new Intent(context, CaptureActivity.class);
                    openCameraIntent.putExtra("codebundle", "1");
                    openCameraIntent.putExtra("anmount", message.getText().toString());
                    context.startActivity(openCameraIntent);
                }
                dismiss();
                break;
            case R.id.btn_cancle:
                dismiss();
                break;
        }
    }
}
