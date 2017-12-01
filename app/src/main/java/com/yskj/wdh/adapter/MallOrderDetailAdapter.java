package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.MallOrderDetailBean;
import com.yskj.wdh.util.NumberFormat;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 14:27.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MallOrderDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MallOrderDetailBean.DataBean.MillOrderGoodsVoListBean> arrayList;

    public MallOrderDetailAdapter(Context context, ArrayList<MallOrderDetailBean.DataBean.MillOrderGoodsVoListBean> arrayList) {
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mall_order_goods, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MallOrderDetailBean.DataBean.MillOrderGoodsVoListBean millOrderGoodsVoListBean = arrayList.get(position);
        Glide.with(context).load(millOrderGoodsVoListBean.cover).error(R.mipmap.img_error).into(holder.imageView);
        holder.tvName.setText(millOrderGoodsVoListBean.goodsName);
        holder.tvProperty.setText(millOrderGoodsVoListBean.parameterName);
        holder.tvPrice.setText("￥"+String.format("%.2f", NumberFormat.convertToFloat(millOrderGoodsVoListBean.goodsPrice,0.00f)));
        holder.tvCount.setText("x"+millOrderGoodsVoListBean.goodsCount);
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView tvDes, tvName, tvProperty,tvPrice,tvCount;

        public ViewHolder(View itemView) {
            imageView = (ImageView) itemView.findViewById(R.id.img);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvProperty = (TextView) itemView.findViewById(R.id.tv_property);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
        }
    }
}
