package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.yskj.wdh.R;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class HintDialog extends Dialog {
    private Context context;
    private TextView dialog_payment;
    private TimeCount time;
    private String message;

    public HintDialog(Context context,String message) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.message  = message;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint_establish);
        dialog_payment = (TextView) findViewById(R.id.dialog_payment);
        dialog_payment.setText(message);
        time = new TimeCount(3000, 1000);//构造CountDownTimer对象
        time.start();
    }

    /* 定义一个倒计时的内部类 */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
           dismiss();
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
        }
    }
}
