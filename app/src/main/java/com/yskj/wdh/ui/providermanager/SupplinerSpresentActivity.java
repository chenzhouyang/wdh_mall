package com.yskj.wdh.ui.providermanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 陈宙洋
 * 2017/8/5.
 * 描述：供应商资料提交成功activity
 */

public class SupplinerSpresentActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.suppliers_next)
    Button suppliersNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spresent);
        ButterKnife.bind(this);
        txtTitle.setText("申请成功");
    }

    @OnClick({R.id.img_back, R.id.suppliers_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.suppliers_next:
                finish();
                break;
        }
    }
}
