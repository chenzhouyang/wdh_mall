package com.yskj.wdh.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProviderGoodsInfoBean;
import com.yskj.wdh.ui.providermanager.AddGoodsInfoAdapterInterface;
import com.yskj.wdh.ui.providermanager.EditExpressCompleteInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/3on 14:56.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ProviderGoodsInfoBean> arrayList;
    private AddGoodsInfoAdapterInterface addGoodsInfoAdapterInterface;
    private EditExpressCompleteInterface editExpressCompleteInterface;
    public void setEditExpressCompleteInterface(EditExpressCompleteInterface editExpressCompleteInterface) {
        this.editExpressCompleteInterface = editExpressCompleteInterface;
    }

    public AddGoodsInfoAdapter(Context context, ArrayList<ProviderGoodsInfoBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public void setAddGoodsInfoAdapterInterface(AddGoodsInfoAdapterInterface addGoodsInfoAdapterInterface) {
        this.addGoodsInfoAdapterInterface = addGoodsInfoAdapterInterface;
    }

    @Override
    public int getCount() {
        if (null == arrayList || 0 == arrayList.size()) {
            return 0;
        }
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
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        convertView = LayoutInflater.from(context).inflate(R.layout.item_add_goods_info, parent, false);
        holder = new ViewHolder(convertView);
        convertView.setTag(holder);
        final ViewHolder finalHolder = holder;
        final int finalPosition = position;
        holder.etName.setText(arrayList.get(position).getContent());
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
        holder.image.setImageBitmap(arrayList.get(position).getBitmap());
        //删除
        holder.tvType1Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoodsInfoAdapterInterface.deleteType(finalPosition);
            }
        });
        //删除
        holder.tvType2Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoodsInfoAdapterInterface.deleteType(finalPosition);
            }
        });
        //增加
        holder.tvType2Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoodsInfoAdapterInterface.addImg(finalPosition, finalHolder.image);
            }
        });
        //上移
        int type = arrayList.get(position).getType();
        switch (type) {
            case 1:
                holder.llTypeOne.setVisibility(View.VISIBLE);
                holder.llTypeTwo.setVisibility(View.GONE);
                holder.tvType1Up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addGoodsInfoAdapterInterface.upType(finalPosition);

                    }
                });
                break;
            case 2:
                holder.llTypeTwo.setVisibility(View.VISIBLE);
                holder.llTypeOne.setVisibility(View.GONE);
                holder.tvType2Up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addGoodsInfoAdapterInterface.upType(finalPosition);
                    }
                });
                break;
        }
        return convertView;
    }
    private class ViewHolder {
        LinearLayout llTypeOne, llTypeTwo;
        TextView tvType1Up, tvType2Up, tvType2Insert, tvType1Del, tvType2Del;
        ImageView image;
        EditText etName;

        public ViewHolder(View itemView) {
            llTypeOne = (LinearLayout) itemView.findViewById(R.id.ll_type_one);
            llTypeTwo = (LinearLayout) itemView.findViewById(R.id.ll_type_two);
            tvType2Up = (TextView) itemView.findViewById(R.id.tv_type2_up);
            tvType1Up = (TextView) itemView.findViewById(R.id.tv_type1_up);
            tvType1Del = (TextView) itemView.findViewById(R.id.tv_type1_del);
            tvType2Del = (TextView) itemView.findViewById(R.id.tv_type2_del);
            tvType2Insert = (TextView) itemView.findViewById(R.id.tv_type2_insert);
            image = (ImageView) itemView.findViewById(R.id.img);
            etName = (EditText) itemView.findViewById(R.id.et_name);
        }
    }
}

