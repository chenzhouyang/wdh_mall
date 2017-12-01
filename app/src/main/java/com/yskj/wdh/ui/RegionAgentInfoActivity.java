package com.yskj.wdh.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ItemRegionAgentAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建日期 2017/6/1on 20:26.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class RegionAgentInfoActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_des)
    TextView tvDes;
    @Bind(R.id.tv_province)
    TextView tvProvince;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.no_scroll_view)
    NoScrollListView noScrollView;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.ll_add)
    LinearLayout llAdd;

    private LoadingCaches caches = LoadingCaches.getInstance();
    private BusinessInfoBean bean;
    private List<String> parentId;
    private List<String> areaId;
    private String province, city;
    private ArrayList<String> areaList = new ArrayList<>();
    private ItemRegionAgentAdapter agentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_agent_info);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        txtTitle.setText("审核结果");
        bean = JSONFormat.parseT(caches.get("business"), BusinessInfoBean.class);
        tvName.setText(bean.agent.name);
        tvDes.setText(bean.agent.profile);
        parentId = bean.agent.parentId;
        areaId = bean.agent.areaId;
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < parentId.size(); i++) {
            province = parentId.get(0).split(",")[1];
            city = parentId.get(1).split(",")[1];
        }
        tvProvince.setText(province);
        tvCity.setText(city);

        for (int i = 0; i < areaId.size(); i++) {
            areaList.add(areaId.get(i).split(",")[1]);
        }
        agentAdapter = new ItemRegionAgentAdapter(context, areaList);
        noScrollView.setAdapter(agentAdapter);
        switch (bean.agent.status) {
            case 0:
                tvStatus.setText("待审批");
                break;
            case 1:
                tvStatus.setText("审批通过");
                break;
            case 2:
                tvStatus.setText("审批不通过");
                break;
            case 3:
                tvStatus.setText("禁用");
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
