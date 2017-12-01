package com.yskj.wdh.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建日期 2017/5/26on 16:50.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class RegionAgentActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.tv_apply)
    TextView tvApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_agent);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        txtTitle.setText("区域代理申请");
        imgBack.setOnClickListener(this);
        tvApply.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_apply:
                startActivity(new Intent(context,RegionAgentDetailActivity.class));
                break;
        }
    }

}
