package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yskj.wdh.R;

import java.util.ArrayList;

/**
 * 创建日期 2017/6/1on 16:57.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ItemRegionAgentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> listBeen;

    public ItemRegionAgentAdapter(Context context, ArrayList<String> listBeen) {
        this.context = context;
        this.listBeen = listBeen;
    }

    @Override
    public int getCount() {
        if(listBeen!=null&&listBeen.size()!=0){
            return listBeen.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return listBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_region_agent_detail,null);
            holder.itemCounty = (TextView) convertView.findViewById(R.id.et_county);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemCounty.setText(listBeen.get(position));
        return convertView;
    }
    public class ViewHolder{
        TextView itemCounty;
    }
}
