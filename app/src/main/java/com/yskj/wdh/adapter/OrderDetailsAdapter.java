package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.entity.OrderDetailsEntity;
import com.yskj.wdh.util.StringUtils;

import java.util.List;

import static com.yskj.wdh.R.id.odrder_name;
import static com.yskj.wdh.R.id.order_count;
import static com.yskj.wdh.R.id.order_price;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class OrderDetailsAdapter extends BaseAdapter {
    private List<OrderDetailsEntity.DataBean.RelationsBean> orderlist;
    private Context context;
    public OrderDetailsAdapter(Context context,List<OrderDetailsEntity.DataBean.RelationsBean> orderlist){
        this.context = context;
        this.orderlist = orderlist;
    }
    @Override
    public int getCount() {
        if (orderlist!=null&&orderlist.size()!=0){
            return orderlist.size();
        }
        return 0;
    }
    @Override
    public Object getItem(int position) {
        return orderlist.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_orderdetail,null);
            holder.order_price = (TextView) convertView.findViewById(order_price);
            holder.order_count = (TextView) convertView.findViewById(order_count);
            holder.odrder_name = (TextView) convertView.findViewById(odrder_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.odrder_name.setText(orderlist.get(position).goodName+"");
        holder.order_count.setText(orderlist.get(position).count+"");
        holder.order_price.setText(StringUtils.getStringtodouble(orderlist.get(position).price*orderlist.get(position).count));
        return convertView;
    }
    private class ViewHolder{
        TextView odrder_name,order_count,order_price;
    }
}
