package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.CheckedNumberBean;
import com.yskj.wdh.util.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/2 0002.
 */
public class MyCheckedNumberAdapter extends BaseAdapter {


    private Context context;
    private List<CheckedNumberBean.DataBean.ListBean> DatasNumberList;

    public MyCheckedNumberAdapter(Context context, List<CheckedNumberBean.DataBean.ListBean> datasList) {
        this.DatasNumberList = datasList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (DatasNumberList == null || DatasNumberList.isEmpty()) {
            return 0;
        } else {
            return DatasNumberList.size();
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
            convertView = View.inflate(context, R.layout.xlv_item_numbert, null);
            holder = new MyItemHolder();
            holder.tv_xlv_history_number = (TextView) convertView.findViewById(R.id.tv_xlv_history_number);
            holder.tv_xlv_history_name = (TextView) convertView.findViewById(R.id.tv_xlv_history_name);
            holder.tv_xlv_history_price = (TextView) convertView.findViewById(R.id.tv_xlv_history_price);
            holder.tv_xlv_history_data = (TextView) convertView.findViewById(R.id.tv_xlv_history_data);
            holder.tv_xlv_history_count = (TextView) convertView.findViewById(R.id.tv_xlv_history_count);
            holder.tv_xlv_history_total = (TextView) convertView.findViewById(R.id.tv_xlv_history_total);
            holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);

            convertView.setTag(holder);
        } else {
            holder = (MyItemHolder) convertView.getTag();
        }
        if (!(DatasNumberList == null || DatasNumberList.isEmpty())) {
            //填充数据
            holder.tv_xlv_history_count.setText(DatasNumberList.get(position).getCount() + "");
            if(DatasNumberList.get(position).getAccPointsAmount()<=0){
                holder.tv_xlv_history_total.setText("合计: " + StringUtils.getStringtodouble(DatasNumberList.get(position).getFundAmount())+"-积分:"+StringUtils.getStringtodouble(DatasNumberList.get(position).getAccPointsAmount()));
            }else {
                holder.tv_xlv_history_total.setText("合计: " + StringUtils.getStringtodouble(DatasNumberList.get(position).getFundAmount()));
            }

            holder.tv_xlv_history_number.setText("订单号: " + DatasNumberList.get(position).getOrderNo());
            holder.tv_xlv_history_name.setText("套餐名: " + DatasNumberList.get(position).getLifeItemName());
            holder.tv_xlv_history_price.setText(StringUtils.getStringtodouble(DatasNumberList.get(position).getPrice()));
            holder.tv_xlv_history_data.setText("日期: " + DatasNumberList.get(position).getValidityDate());

            //图片
            if(DatasNumberList.get(position).getCover().length()!=0){
                Glide.with(context).load(DatasNumberList.get(position).getCover()).error(R.mipmap.img_error).into(holder.iv_img);
            }else{
                holder.iv_img.setImageResource(R.mipmap.img_error);
            }
        }

        return convertView;
    }
    class MyItemHolder {
        ImageView iv_img, iv_xlv_item;
        TextView tv_xlv_history_number, tv_xlv_history_count, tv_xlv_history_total, tv_xlv_history_name, tv_xlv_history_price, tv_xlv_history_data, tv_xlv_history_time, tv_xlv_checked_nub;
    }
}
