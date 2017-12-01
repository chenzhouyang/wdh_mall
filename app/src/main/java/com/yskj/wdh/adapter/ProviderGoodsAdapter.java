package com.yskj.wdh.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProviderGoodsPropertyBean;
import com.yskj.wdh.ui.providermanager.EditExpressCompleteInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/1on 10:59.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ProviderGoodsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProviderGoodsPropertyBean> arrayList;
    private EditExpressCompleteInterface editExpressCompleteInterface;

    public void setEditExpressCompleteInterface(EditExpressCompleteInterface editExpressCompleteInterface) {
        this.editExpressCompleteInterface = editExpressCompleteInterface;
    }

    public ProviderGoodsAdapter(Context context, ArrayList<ProviderGoodsPropertyBean> arrayList) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_provider_goods_property, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
        final ViewHolder finalHolder = holder;
        final int finalPosition = position;
        holder.etName.setText(arrayList.get(position).getSpecification());
        holder.etPrice.setText(arrayList.get(position).getPrice());
        holder.etNum.setText(arrayList.get(position).getNumber());
        holder.etMValue.setText(arrayList.get(position).getmValue());
        holder.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("etName", finalPosition, finalHolder.etName.getText().toString());
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
                editExpressCompleteInterface.editComplete("etPrice", finalPosition, finalHolder.etPrice.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("etNum", finalPosition, finalHolder.etNum.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        holder.etMValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editExpressCompleteInterface.editComplete("etMValue", finalPosition, finalHolder.etMValue.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return convertView;
    }

    private class ViewHolder {
        EditText etName, etPrice, etNum, etMValue;

        public ViewHolder(View itemView) {
            etName = (EditText) itemView.findViewById(R.id.et_name);
            etPrice = (EditText) itemView.findViewById(R.id.et_price);
            etNum = (EditText) itemView.findViewById(R.id.et_num);
            etMValue = (EditText) itemView.findViewById(R.id.et_mValue);
        }
    }
}
