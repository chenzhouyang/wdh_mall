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
import com.yskj.wdh.bean.ShareForBean;
import com.yskj.wdh.util.NumberFormat;

import java.util.List;

/**
 * 创建日期 2017/5/26on 14:39.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ShareForAdapter  extends BaseAdapter {

    private List<ShareForBean.DataBean.ListBean> shareForBeanList;
    private Context context;

    public ShareForAdapter(Context context) {
        this.context = context;
    }

    public void setShareForBeanList(List<ShareForBean.DataBean.ListBean> shareForBeanList) {
        this.shareForBeanList = shareForBeanList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return shareForBeanList == null ? 0 : shareForBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return shareForBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_share_for, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ShareForBean.DataBean.ListBean listBean = shareForBeanList.get(position);
        holder.tv_commodity_name.setText(listBean.goodsName);
        holder.tv_pants.setText(listBean.address);
        holder.tv_price.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(listBean.price, 0d)));
        if (!TextUtils.isEmpty(shareForBeanList.get(position).cover)){
            Glide.with(context).load(shareForBeanList.get(position).cover).error(R.mipmap.img_error).into(holder.iv_show_pic);
        }
        return convertView;
    }

    //初始化控件
    class ViewHolder {
        ImageView iv_show_pic;
        TextView tv_commodity_name, tv_pants, tv_price;
        public ViewHolder(View itemView) {
            iv_show_pic = (ImageView) itemView.findViewById(R.id.iv_show_pic);
            tv_commodity_name = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            tv_pants = (TextView) itemView.findViewById(R.id.tv_pants);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
        }

    }
}
