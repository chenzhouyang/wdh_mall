package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.AddGoodsKindsBean;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/18on 9:02.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsKindsAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AddGoodsKindsBean.DataBean> arrayList;
    private int pos;//判断点击位置

    public void setPos(int pos) {
        this.pos = pos;
    }
    public AddGoodsKindsAdapter(Context context, ArrayList<AddGoodsKindsBean.DataBean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (pos==-1){
            if (null == arrayList || 0 == arrayList.size())
                return 0;
            return arrayList.size();
        }else {
            if (null == arrayList.get(pos).children || 0 == arrayList.get(pos).children.size())
                return 0;
            return arrayList.get(pos).children.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (pos==-1){
            return arrayList.get(position);
        }else {
            return arrayList.get(pos).children.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_kinds, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (pos==-1){
            holder.tvName.setText(arrayList.get(position).name);
        }else {
            holder.tvName.setText(arrayList.get(pos).children.get(position).name);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        public ViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}

