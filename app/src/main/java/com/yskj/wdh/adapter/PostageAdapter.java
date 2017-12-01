package com.yskj.wdh.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.FindLogisticsDetailBean;
import com.yskj.wdh.ui.providermanager.CheckOneInterface;
import com.yskj.wdh.ui.providermanager.EditExpressCompleteInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/2on 17:43.
 * 描述：编辑运费中运费设定
 * 作者：姜贺YSKJ-JH
 */

public class PostageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FindLogisticsDetailBean.DataBean.CustomTransportListBean> arrayList;
    private CheckOneInterface checkOneInterface;
    private EditExpressCompleteInterface editExpressCompleteInterface;

    public void setEditExpressCompleteInterface(EditExpressCompleteInterface editExpressCompleteInterface) {
        this.editExpressCompleteInterface = editExpressCompleteInterface;
    }

    public void setCheckOneInterface(CheckOneInterface checkOneInterface) {
        this.checkOneInterface = checkOneInterface;
    }

    public PostageAdapter(Context context, ArrayList<FindLogisticsDetailBean.DataBean.CustomTransportListBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (null == arrayList || 0 == arrayList.size())
            return 0;
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_postage, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
        FindLogisticsDetailBean.DataBean.CustomTransportListBean postageEntity = arrayList.get(position);
        StringBuilder areaName = new StringBuilder();
        if (postageEntity.customTransportAreaList != null) {
            for (FindLogisticsDetailBean.DataBean.CustomTransportListBean.CustomTransportAreaListBean bean : postageEntity.customTransportAreaList) {
                areaName.append(bean.areaName + " ");
            }
        }
        if (TextUtils.isEmpty(areaName.toString())) {
            holder.tvOtherNoSendArea.setText("选择地区");
        } else {
            holder.tvOtherNoSendArea.setText(areaName.toString());
        }

        holder.etCount.setText(postageEntity.number);
        holder.etPrice.setText(postageEntity.price);
        holder.etAddCount.setText(postageEntity.addNumber);
        holder.etAddPrice.setText(postageEntity.addPrice);
        holder.tvOtherNoSendArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOneInterface.checkOne(position);
            }
        });
        holder.etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("Count", position, holder.etCount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("Price", position, holder.etPrice.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.etAddCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("AddCount", position, holder.etAddCount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.etAddPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("AddPrice", position, holder.etAddPrice.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvOtherNoSendArea;
        EditText etCount, etPrice, etAddCount, etAddPrice;

        public ViewHolder(View itemView) {
            tvOtherNoSendArea = (TextView) itemView.findViewById(R.id.tv_other_no_send_area);
            etCount = (EditText) itemView.findViewById(R.id.et_count);
            etPrice = (EditText) itemView.findViewById(R.id.et_price);
            etAddCount = (EditText) itemView.findViewById(R.id.et_add_count);
            etAddPrice = (EditText) itemView.findViewById(R.id.et_add_price);
        }
    }
}