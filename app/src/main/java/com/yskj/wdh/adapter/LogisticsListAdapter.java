package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.LogisticsListBean;
import com.yskj.wdh.ui.providermanager.CheckOneInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/26on 14:40.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class LogisticsListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LogisticsListBean> arrayList;
    private CheckOneInterface checkOneInterface;

    public void setCheckOneInterface(CheckOneInterface checkOneInterface) {
        this.checkOneInterface = checkOneInterface;
    }

    public LogisticsListAdapter(Context context, ArrayList<LogisticsListBean> arrayList) {
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
        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_logistics_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(arrayList.get(position).getName());
        holder.rlNeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOneInterface.checkOne(position);
            }
        });
        if (arrayList.get(position).isChecked()){
            holder.tvSelect.setVisibility(View.VISIBLE);
        }else {
            holder.tvSelect.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class ViewHolder {
        RelativeLayout rlNeed;
        TextView tvSelect, tvName;
        public ViewHolder(View itemView) {
            rlNeed = (RelativeLayout) itemView.findViewById(R.id.rl_need);
            tvSelect = (TextView) itemView.findViewById(R.id.tv_select);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}


