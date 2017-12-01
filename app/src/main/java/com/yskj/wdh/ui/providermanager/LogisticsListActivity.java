package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.LogisticsListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.LogisticsListBean;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/26on 14:30.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class LogisticsListActivity extends BaseActivity implements View.OnClickListener, CheckOneInterface {

    ImageView imgBack;
    TextView txtTitle;
    TextView tvRight;
    NoScrollListView listView;
    ArrayList<LogisticsListBean> arrayList = new ArrayList<>();
    String[] nameList = new String[]{"邮政快递包裹", "圆通速递", "申通快递", "中通快递", "顺丰", "天天快递", "韵达快运", "宅急送", "德邦物流", "汇通快运", "百世汇通", "天地华宇", "优速物流", "快捷速递", "佳怡物流","京东"};

    private LogisticsListAdapter adapter;
    private String dataName;
    private boolean isCheckedOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_list);
        initView();
        initData();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new LogisticsListAdapter(context, arrayList);
        listView.setAdapter(adapter);
        adapter.setCheckOneInterface(this);
    }

    private void initData() {
        for (int i = 0; i < nameList.length; i++) {
            LogisticsListBean bean = new LogisticsListBean();
            bean.setName(nameList[i]);
            bean.setChecked(false);
            arrayList.add(bean);
        }
    }

    private void initView() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        listView = (NoScrollListView) findViewById(R.id.listView);

        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("data", dataName);
        switch (v.getId()) {
            case R.id.img_back:
                if (isCheckedOne){
                    setResult(Config.LOGISTICSLIST, intent);
                    finish();
                }else {
                  showToast("至少选择一个呗");
                }
                break;
            case R.id.tv_right:
                if (isCheckedOne){
                    setResult(Config.LOGISTICSLIST, intent);
                    finish();
                }else {
                    showToast("至少选择一个呗");
                }
                break;
        }
    }

    @Override
    public void checkOne(int position) {
        arrayList.clear();
        initData();
        arrayList.get(position).setChecked(true);
        dataName = arrayList.get(position).getName();
        isCheckedOne = true;
        adapter.notifyDataSetChanged();

    }

    @Override
    public void checkEdit(int position) {

    }
}
