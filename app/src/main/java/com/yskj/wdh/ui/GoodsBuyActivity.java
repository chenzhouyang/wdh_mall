package com.yskj.wdh.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.GoodsBuyAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.GoodsBuyBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建日期 2017/5/26on 15:38.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GoodsBuyActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.list_view)
    ListView listView;

    private GoodsBuyAdapter adapter;

    private ArrayList<GoodsBuyBean> arrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_buy);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private void initView() {

        txtTitle.setText("商品采购");
        imgBack.setOnClickListener(this);

        GoodsBuyBean goodsBuyBean = new GoodsBuyBean();
        goodsBuyBean.setImg(R.mipmap.goods_buy);
        goodsBuyBean.setName("商品采购");
        goodsBuyBean.setDescribe("直供商品采购");
        goodsBuyBean.setButton("立即采购");

        GoodsBuyBean goodsBuyBean1 = new GoodsBuyBean();
        goodsBuyBean1.setImg(R.mipmap.straight_for);
        goodsBuyBean1.setName("采购单");
        goodsBuyBean1.setDescribe("商家采购信息及审批信息");
        goodsBuyBean1.setButton("立即查看");

        arrayList.add(goodsBuyBean);
        arrayList.add(goodsBuyBean1);

        adapter = new GoodsBuyAdapter(context,arrayList);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
    }
}
