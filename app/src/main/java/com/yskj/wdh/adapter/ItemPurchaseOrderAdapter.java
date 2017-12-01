package com.yskj.wdh.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.PurchaseOrderBean;
import com.yskj.wdh.util.NumberFormat;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/27on 9:59.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ItemPurchaseOrderAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<PurchaseOrderBean.DataBean.LpgListBean> datas;
    private String type;

    public ItemPurchaseOrderAdapter(Context context,String type, ArrayList<PurchaseOrderBean.DataBean.LpgListBean> datas) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_purchase_goods_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PurchaseOrderBean.DataBean.LpgListBean lpgListBean = datas.get(position);
        holder.tvName.setText(lpgListBean.goodName);
        holder.tvCount.setText("x"+lpgListBean.count);
        holder.tvPrice.setText("￥"+String.format("%.2f", NumberFormat.convertToDouble(lpgListBean.price, 0d)));
        if (type.equals("3")){
            holder.imageStatus.setVisibility(View.VISIBLE);
        }else {
            holder.imageStatus.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(lpgListBean.cover)){
            Glide.with(context).load(lpgListBean.cover).error(R.mipmap.img_error).into(holder.imageView);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvName,tvCount,tvPrice;
        ImageView imageView,imageStatus;
        public ViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            imageStatus = (ImageView) itemView.findViewById(R.id.image_status);
        }
    }

}
