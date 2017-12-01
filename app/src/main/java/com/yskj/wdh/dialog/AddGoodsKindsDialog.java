package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.AddGoodsKindsAdapter;
import com.yskj.wdh.bean.AddGoodsKindsBean;
import com.yskj.wdh.bean.DialogCheckBean;
import com.yskj.wdh.ui.providermanager.AddKindsInterface;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/18on 8:58.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsKindsDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private ArrayList<AddGoodsKindsBean.DataBean> arrayList;
    private ArrayList<DialogCheckBean> arraySelect = new ArrayList<>();//存是否点击
    private NoScrollListView listView;
    private TextView tvCancel;
    private AddKindsInterface addKindsInterface;
    private int type = 0;//判断是第几级列表
    private int pos = -1;//判断点击位置
    private AddGoodsKindsAdapter addGoodsKindsAdapter;

    public void setAddKindsInterface(AddKindsInterface addKindsInterface) {
        this.addKindsInterface = addKindsInterface;
    }

    public AddGoodsKindsDialog(Context context,ArrayList<AddGoodsKindsBean.DataBean> arrayList) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_goods_kinds);
        initView();
    }


    private void initView() {
        listView = (NoScrollListView) findViewById(R.id.listView);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        addGoodsKindsAdapter = new AddGoodsKindsAdapter(context, arrayList);
        listView.setAdapter(addGoodsKindsAdapter);
        addGoodsKindsAdapter.setPos(-1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type++;
                if (type==1){
                    tvCancel.setText("返回上一级");
                    pos = position;
                    addGoodsKindsAdapter.setPos(position);
                    addGoodsKindsAdapter.notifyDataSetChanged();
                }
                if (type==2){
                    addKindsInterface.addKinds(pos,position);
                    dismiss();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                type--;
                pos = -1;
                if (type==0){
                    tvCancel.setText("取消");
                }
                if (type>=0){
                    addGoodsKindsAdapter.setPos(-1);
                    addGoodsKindsAdapter.notifyDataSetChanged();
                }else {
                    dismiss();
                }
                break;
        }
    }
//    @Override
//    public void checkedOne(int position, boolean isCheck) {
//        arraySelect.get(position).setChecked(isCheck);
//    }
}