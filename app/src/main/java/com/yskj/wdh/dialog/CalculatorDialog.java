package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.util.StringUtils;

import java.util.Arrays;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * 陈宙洋
 * 2017/6/12.
 */

public class CalculatorDialog extends Dialog implements View.OnClickListener{
    Button bt_0, bt_1, bt_2, bt_3, bt_4, bt_5, bt_6, bt_7, bt_8, bt_9;
    Button bt_add, bt_sub, bt_ride, bt_div, bt_dian, bt_equal, bt_clear,bt_ac,bt_confirm;
    TextView tv;
    String exp = "";
    boolean isClear = false;
    private CalcuatorInterface calcuatorInterface;
    private Context context;
    public CalculatorDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calculator);
        init();
        listen();

    }

    @Override
    protected void onStart() {
        super.onStart();
        exp = tv.getText().toString();
    }

    public void setCalcuatorInterface(CalcuatorInterface calcuatorInterface) {
        this.calcuatorInterface = calcuatorInterface;
    }
    public void init() {
        bt_0 = (Button) findViewById(R.id.bt_0);
        bt_1 = (Button) findViewById(R.id.bt_1);
        bt_2 = (Button) findViewById(R.id.bt_2);
        bt_3 = (Button) findViewById(R.id.bt_3);
        bt_4 = (Button) findViewById(R.id.bt_4);
        bt_5 = (Button) findViewById(R.id.bt_5);
        bt_6 = (Button) findViewById(R.id.bt_6);
        bt_7 = (Button) findViewById(R.id.bt_7);
        bt_8 = (Button) findViewById(R.id.bt_8);
        bt_9 = (Button) findViewById(R.id.bt_9);
        bt_ac= (Button) findViewById(R.id.bt_ac);
        bt_add = (Button) findViewById(R.id.bt_add);
        bt_sub = (Button) findViewById(R.id.bt_sub);
        bt_ride = (Button) findViewById(R.id.bt_ride);
        bt_div = (Button) findViewById(R.id.bt_div);
        bt_dian = (Button) findViewById(R.id.bt_dian);
        bt_equal = (Button) findViewById(R.id.bt_equal);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);
        tv = (TextView) findViewById(R.id.tv_show);
    }


    public void listen() {
        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_ac.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_sub.setOnClickListener(this);
        bt_ride.setOnClickListener(this);
        bt_div.setOnClickListener(this);
        bt_dian.setOnClickListener(this);
        bt_equal.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_dian:
                exp = exp + ".";
                tv.setText(exp);
                break;
            case R.id.bt_0:

                exp = exp + "0";
                tv.setText(exp);
                break;
            case R.id.bt_1:

                exp = exp + "1";
                tv.setText(exp);
                break;
            case R.id.bt_2:

                exp = exp + "2";
                tv.setText(exp);
                break;
            case R.id.bt_3:

                exp = exp + "3";
                tv.setText(exp);
                break;
            case R.id.bt_4:
                exp = exp + "4";
                tv.setText(exp);
                break;
            case R.id.bt_5:
                exp = exp + "5";
                tv.setText(exp);
                break;
            case R.id.bt_6:
                exp = exp + "6";
                tv.setText(exp);
                break;
            case R.id.bt_7:
                exp = exp + "7";
                tv.setText(exp);
                break;
            case R.id.bt_8:
                exp = exp + "8";
                tv.setText(exp);
                break;
            case R.id.bt_9:
                exp = exp + "9";
                tv.setText(exp);
                break;
            case R.id.bt_add:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = exp + "+";
                tv.setText(exp);
                break;
            case R.id.bt_sub:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = exp + "-";
                tv.setText(exp);
                break;
            case R.id.bt_ride:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = exp + "×";
                tv.setText(exp);
                break;
            case R.id.bt_div:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = exp + "÷";
                tv.setText(exp);
                break;
            case R.id.bt_equal:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = tv.getText().toString();
                exp = exp.replaceAll("×", "*");
                exp = exp.replaceAll("÷", "/");
                exp = getRs(exp);
                if(exp.endsWith(".0")){
                    exp = exp.substring(0, exp.indexOf(".0"));
                }

                tv.setText(StringUtils.getStringtodouble(Double.parseDouble(exp)));
                if(!exp.equals("算数公式错误")){
                    calcuatorInterface.caluator(exp);
                }
                isClear = false;
                exp = "";
                break;
            case R.id.bt_clear:
                tv.setText("");
                exp = "";
                break;
            case R.id.bt_ac:
                String str =tv.getText().toString();
                if(str.length() > 0)
                    tv.setText(str.substring(0, str.length() - 1));
                exp = tv.getText().toString();
                break;
            case R.id.bt_confirm:
                if(exp==null || exp.trim().length()==0)
                    return;
                exp = tv.getText().toString();
                exp = exp.replaceAll("×", "*");
                exp = exp.replaceAll("÷", "/");
                exp = getRs(exp);
                if(exp.endsWith(".0")){
                    exp = exp.substring(0, exp.indexOf(".0"));
                }

                tv.setText(StringUtils.getStringtodouble(Double.parseDouble(exp)));
                if(!exp.equals("算数公式错误")){
                    calcuatorInterface.caluator(exp);
                }
                isClear = false;
                exp = "";
                dismiss();
                break;
            default:
                break;
        }
    }
    /***
     * @param  exp 算数表达式
     * @return 根据表达式返回结果
     */
    private String getRs(String exp){
        Interpreter bsh = new Interpreter();
        Number result = null;
        try {
            exp = filterExp(exp);
            result = (Number)bsh.eval(exp);
        } catch (EvalError e) {
            e.printStackTrace();
            isClear = true;
            return "算数公式错误";
        }
        return result.doubleValue()+"";
    }


    /**
     * @param exp 算数表达式
     * @return 因为计算过程中,全程需要有小数参与.
     */
    private String filterExp(String exp) {
        String num[] = exp.split("");
        String temp = null;
        int begin=0,end=0;
        for (int i = 1; i < num.length; i++) {
            temp = num[i];
            if(temp.matches("[+-/()*]")){
                if(temp.equals(".")) continue;
                end = i - 1;
                temp = exp.substring(begin, end);
                if(temp.trim().length() > 0 && temp.indexOf(".")<0)
                    num[i-1] = num[i-1]+".0";
                begin = end + 1;
            }
        }
        return Arrays.toString(num).replaceAll("[\\[\\], ]", "");
    }

    public interface CalcuatorInterface{
        void caluator(String exp);
    }
}
