package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProjectBean;
import com.yskj.wdh.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gtz on 2016/12/2 0002.
 */
public class MyProjectAdapter extends BaseAdapter {
    //项目数adapter

    private List<ProjectBean.DataBean.ListBean> projectDatas;
    private Context context;
    private ArrayList ID;
    private ArrayList COVER;
    private ArrayList Name;
    private ArrayList Price;

    public MyProjectAdapter(ArrayList Price, ArrayList Name, ArrayList COVER, ArrayList ID, Context context, List<ProjectBean.DataBean.ListBean> projectDatas) {
        this.projectDatas = projectDatas;
        this.context = context;
        this.COVER = COVER;
        this.ID = ID;
        this.Name = Name;
        this.Price = Price;

    }

    @Override
    public int getCount() {
        if (projectDatas == null || projectDatas.isEmpty()) {
            return 0;
        } else {
            return projectDatas.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyItemHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.xlv_item_project, null);
            holder = new MyItemHolder();
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_xlv_item);
            holder.tv_xlv_history_name = (TextView) convertView.findViewById(R.id.tv_xlv_history_Name);
            holder.tv_xlv_history_price = (TextView) convertView.findViewById(R.id.tv_xlv_history_price);
            holder.tv_xlv_checked_nub = (TextView) convertView.findViewById(R.id.tv_xlv_checked_nub);
            holder.tv_xlv_history_time = (TextView) convertView.findViewById(R.id.tv_xlv_history_time);
            holder.iv_xlv_item = (ImageView) convertView.findViewById(R.id.iv_xlv_item);

            convertView.setTag(holder);
        } else {
            holder = (MyItemHolder) convertView.getTag();
        }
        if (!(projectDatas == null || projectDatas.isEmpty())) {
            //填充数据
            holder.tv_xlv_history_name.setText(projectDatas.get(position).getName() + "");
            holder.tv_xlv_history_price.setText(StringUtils.getStringtodouble(projectDatas.get(position).getTeamBuyPrice()));
            holder.tv_xlv_history_time.setText(projectDatas.get(position).getOnlineTime() + "");
            holder.tv_xlv_checked_nub.setText(projectDatas.get(position).getCount() + "");
            ID.add(projectDatas.get(position).getLifeId());
            COVER.add(projectDatas.get(position).getCover());
            Name.add(projectDatas.get(position).getName());
            Price.add(StringUtils.getStringtodouble(projectDatas.get(position).getTeamBuyPrice()));
            //图片
            if (projectDatas.get(position).getCover().length() != 0) {
                Glide.with(context).load(projectDatas.get(position).getCover()).error(R.mipmap.img_error).into(holder.iv_xlv_item);
            } else {
                holder.iv_xlv_item.setImageResource(R.mipmap.img_error);
            }
        }

        return convertView;
    }


    class MyItemHolder {
        ImageView iv_img, iv_xlv_item;
        TextView tv_xlv_history_number, tv_xlv_history_count, tv_xlv_history_total, tv_xlv_history_name, tv_xlv_history_price, tv_xlv_history_data, tv_xlv_history_time, tv_xlv_checked_nub;
    }

}
