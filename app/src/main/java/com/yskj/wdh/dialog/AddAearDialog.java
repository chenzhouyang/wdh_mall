package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.AddAearAdapter;
import com.yskj.wdh.bean.CityTestEntity;
import com.yskj.wdh.bean.DialogCheckBean;
import com.yskj.wdh.ui.providermanager.DialogCheckInterface;
import com.yskj.wdh.ui.providermanager.GotAreaInterface;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 16:33.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddAearDialog extends Dialog implements View.OnClickListener, DialogCheckInterface {
    private Context context;
    private ArrayList<CityTestEntity> mArrProvinces;
    private ArrayList<DialogCheckBean> arraySelect = new ArrayList<>();//存是否点击
    private NoScrollListView listView;
    private TextView tvCancel, tvSure;

    private GotAreaInterface gotAreaInterface;

    public void setGotAreaInterface(GotAreaInterface gotAreaInterface) {
        this.gotAreaInterface = gotAreaInterface;
    }

    public AddAearDialog(Context context, ArrayList<CityTestEntity> mArrProvinces) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.mArrProvinces = mArrProvinces;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_aear);
        initView();
    }

    private void initView() {
        listView = (NoScrollListView) findViewById(R.id.listView);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        AddAearAdapter addAearAdapter = new AddAearAdapter(context, mArrProvinces);
        listView.setAdapter(addAearAdapter);
        addAearAdapter.setDialogCheckInterface(this);

        for (int i = 0; i < mArrProvinces.size(); i++) {
            DialogCheckBean dialogCheckBean = new DialogCheckBean();
            dialogCheckBean.setChecked(false);
            arraySelect.add(dialogCheckBean);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                for (int i = 0; i < arraySelect.size(); i++) {
                    DialogCheckBean dialogCheckBean = arraySelect.get(i);
                    if (dialogCheckBean.isChecked){
                        dialogCheckBean.setAreaId(mArrProvinces.get(i).id+"");
                        dialogCheckBean.setAreaName(mArrProvinces.get(i).name);
                    }
                }
                gotAreaInterface.gotAreaData(arraySelect);
                dismiss();
                break;
        }
    }

    @Override
    public void checkedOne(int position, boolean isCheck) {
        arraySelect.get(position).setChecked(isCheck);
    }
}
