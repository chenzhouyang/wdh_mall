package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.AddGoodsInfoDialogAdapter;
import com.yskj.wdh.bean.AddGoodsInfoDialogBean;
import com.yskj.wdh.ui.providermanager.AddGoodsInfoDialogInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/3on 17:05.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsInfoDialog extends Dialog implements View.OnClickListener {
    private Context context;
    GridView gridView;
    private TextView tvDel;
    private AddGoodsInfoDialogInterface addGoodsInfoDialogInterface;
    public AddGoodsInfoDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }

    public void setAddGoodsInfoDialogInterface(AddGoodsInfoDialogInterface addGoodsInfoDialogInterface) {
        this.addGoodsInfoDialogInterface = addGoodsInfoDialogInterface;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_goods_info);
        initView();
    }

    private void initView() {
        gridView = (GridView) findViewById(R.id.gridView);
        tvDel = (TextView) findViewById(R.id.tv_del);
        tvDel.setOnClickListener(this);
        ArrayList<AddGoodsInfoDialogBean> arrayList = new ArrayList<>();
        AddGoodsInfoDialogBean addGoodsInfoDialogBean = new AddGoodsInfoDialogBean();
        addGoodsInfoDialogBean.setName("文字");
        addGoodsInfoDialogBean.setIconId(R.mipmap.add_goods_info_text);
        arrayList.add(addGoodsInfoDialogBean);
        AddGoodsInfoDialogBean addGoodsInfoDialogBean1 = new AddGoodsInfoDialogBean();
        addGoodsInfoDialogBean1.setName("图片");
        addGoodsInfoDialogBean1.setIconId(R.mipmap.add_goods_info_img);
        arrayList.add(addGoodsInfoDialogBean1);
        AddGoodsInfoDialogAdapter allFragmentHeadGridViewAdapter = new AddGoodsInfoDialogAdapter(context, arrayList);
        gridView.setAdapter(allFragmentHeadGridViewAdapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        addGoodsInfoDialogInterface.addTypeOne();
                        break;
                    case 1:
                        addGoodsInfoDialogInterface.addTypeTwo();
                        break;
                }
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_del:
                dismiss();
                break;

        }
    }

}
