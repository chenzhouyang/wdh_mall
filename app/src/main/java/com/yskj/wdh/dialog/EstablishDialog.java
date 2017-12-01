package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.zxing.activity.CaptureCollectMoneyActivity;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class EstablishDialog extends Dialog {
    private Context context;
    private TextView dialog_payment;
    private EditText et_establish;
    public EstablishDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_establish);
        dialog_payment = (TextView) findViewById(R.id.dialog_payment);
        et_establish = (EditText) findViewById(R.id.et_establish);
        dialog_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_establish.length()!=0){
                    context.startActivity(new Intent(context, CaptureCollectMoneyActivity.class)
                            .putExtra("type","0")
                            .putExtra("ordername",et_establish.getText().toString()));
                }else {
                    context.startActivity(new Intent(context, CaptureCollectMoneyActivity.class)
                            .putExtra("type","0")
                            .putExtra("ordername","null"));
                }
            }
        });
    }
}
