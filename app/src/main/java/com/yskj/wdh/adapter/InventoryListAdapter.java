package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.entity.ProvisionalOrderEntivity;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class InventoryListAdapter extends BaseAdapter {
    private Context context;
    private List<ProvisionalOrderEntivity.DataBean.ListBean> list;
    public InventoryListAdapter(Context context, List<ProvisionalOrderEntivity.DataBean.ListBean> list){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if(list!= null&&list.size()!=0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewBankHolder holder = null;
        if(convertView == null){
            holder = new ViewBankHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_inventory,null);
            holder.inventory_tv = (TextView) convertView.findViewById(R.id.inventory_tv);
            convertView.setTag(holder);
        }else {
            holder = (ViewBankHolder) convertView.getTag();
        }
        ProvisionalOrderEntivity.DataBean.ListBean listBean = list.get(position);
        holder.inventory_tv.setText(listBean.goodName);
        return convertView;
    }
     class ViewBankHolder{
        TextView inventory_tv;

    }
}
