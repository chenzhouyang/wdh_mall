package com.yskj.wdh.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.CategoryItemBean;

import java.util.List;

/**
 * Created by liuchaoya on 2016/11/18.
 * For yskj
 * Project Name : LSK
 */

public class CategoryListChildAdapter extends BaseAdapter {
    private Activity context;
    private List<CategoryItemBean> list;

    public CategoryListChildAdapter(Activity context, List<CategoryItemBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_category_list,null);
            holder.select_snack = (LinearLayout) convertView.findViewById(R.id.select_snack);
            holder.category_name = (TextView) convertView.findViewById(R.id.category_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CategoryItemBean bean = list.get(position);
        holder.category_name.setText(bean.name);
        return convertView;
    }

    private class ViewHolder{
        LinearLayout select_snack;
        TextView category_name;
    }
}
