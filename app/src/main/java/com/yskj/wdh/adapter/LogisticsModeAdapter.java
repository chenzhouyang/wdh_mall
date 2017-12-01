package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.LogisticsModeBean;
import com.yskj.wdh.ui.providermanager.CheckOneInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/2on 16:41.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class LogisticsModeAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<LogisticsModeBean.DataBean> arrayList;
    private CheckOneInterface checkOneInterface;

    public void setCheckOneInterface(CheckOneInterface checkOneInterface) {
        this.checkOneInterface = checkOneInterface;
    }

    public LogisticsModeAdapter(Context context, ArrayList<LogisticsModeBean.DataBean> arrayList) {
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_logistics_mode, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final LogisticsModeBean.DataBean logisticsModeEntity = arrayList.get(position);
        holder.tvName.setText(logisticsModeEntity.name);
        holder.tvDes.setText(logisticsModeEntity.name);
        if (logisticsModeEntity.isSclected()){
            holder.tvBox.setBackgroundResource(R.mipmap.fzg);
        }else {
            holder.tvBox.setBackgroundResource(R.mipmap.fzf);
        }
        holder.tvBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!logisticsModeEntity.isSclected()){
                    checkOneInterface.checkOne(position);
                }
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOneInterface.checkEdit(position);
            }
        });
        return convertView;
    }

    private class ViewHolder{
        TextView tvBox,tvName,tvDes;
        ImageView imageView;
        public ViewHolder(View itemView) {
            tvBox = (TextView) itemView.findViewById(R.id.ck_chose_select);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            imageView = (ImageView) itemView.findViewById(R.id.img_select);
        }
    }
}
