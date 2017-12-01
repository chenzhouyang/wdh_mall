package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;

/**
 * 陈宙洋
 * 2017/8/5.
 * 描述：供应商申请activity
 */

public class SuppliersActivity extends BaseActivity implements View.OnClickListener{
    ImageView imgBack;
    TextView txtTitle;
    Button suppliersNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
        initView();
    }
    private void initView() {
        imgBack= (ImageView) findViewById(R.id.img_back);
        txtTitle= (TextView) findViewById(R.id.txt_title);
        suppliersNext = (Button)findViewById(R.id.suppliers_next);
        txtTitle.setText("供应商申请");
        imgBack.setOnClickListener(this);
        suppliersNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.suppliers_next:
                startActivity(new Intent(context,SupplierWriteActivity.class));
                break;
        }
    }
}
