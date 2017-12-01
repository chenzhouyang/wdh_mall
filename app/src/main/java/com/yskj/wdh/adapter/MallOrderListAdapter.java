package com.yskj.wdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.MallOrderListBean;
import com.yskj.wdh.ui.providermanager.OrderDetailActivity;
import com.yskj.wdh.util.NumberFormat;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 9:21.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class MallOrderListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MallOrderListBean.DataBean.OrderListBean> arrayList;
    private String orderStatus;//订单状态： 0表示已付款待发货 1：表示已发货 2：表示确认收货

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public MallOrderListAdapter(Context context, ArrayList<MallOrderListBean.DataBean.OrderListBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mall_order_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MallOrderListBean.DataBean.OrderListBean orderListBean = arrayList.get(position);
        holder.tvOrderNum.setText("订单号：" + orderListBean.orderNo);
        holder.tvOrderNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderId",orderListBean.orderId);
                context.startActivity(intent);
            }
        });
        holder.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderId",orderListBean.orderId);
                context.startActivity(intent);
            }
        });
        double price = 0;
        for (MallOrderListBean.DataBean.OrderListBean.MillOrderGoodsVoListBean millOrderGoods : arrayList.get(position).millOrderGoodsVoList) {
            double mallGoodsPrice = millOrderGoods.goodsCount * millOrderGoods.goodsPrice;
            price+=mallGoodsPrice;
        }
        holder.tvCountAndPrice.setText("共" + orderListBean.millOrderGoodsVoList.size() + "件商品 合计：￥" +String.format("%.2f", NumberFormat.convertToFloat(price,0.00f)));
        MallOrderGoodsAdapter adapter = new MallOrderGoodsAdapter(context,orderListBean.millOrderGoodsVoList);
        holder.noScrollListView.setAdapter(adapter);

        switch (orderStatus) {
            case "0":
                holder.tvStatus.setText("待发货");
                break;
            case "1":
                holder.tvStatus.setText("已发货");
                break;
            case "2":
                holder.tvStatus.setText("已完成");
                break;
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvOrderNum, tvCountAndPrice, tvStatus;
        NoScrollListView noScrollListView;

        public ViewHolder(View itemView) {
            tvOrderNum = (TextView) itemView.findViewById(R.id.tv_order_num);
            tvCountAndPrice = (TextView) itemView.findViewById(R.id.tv_order_count_and_price);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            noScrollListView = (NoScrollListView) itemView.findViewById(R.id.listView);
        }
    }
}
