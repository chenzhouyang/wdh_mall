package com.yskj.wdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.GoodsBuyBean;
import com.yskj.wdh.ui.PurchaseOrderActivity;
import com.yskj.wdh.ui.StraightForActivity;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/26on 15:45.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class GoodsBuyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GoodsBuyBean> arrayList;

    public GoodsBuyAdapter(Context context, ArrayList<GoodsBuyBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_goods_buy_adapter, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final GoodsBuyBean goodsBuyBean = arrayList.get(position);
        holder.img.setImageResource(goodsBuyBean.getImg());
        holder.tvName.setText(goodsBuyBean.getName());
        holder.tvDes.setText(goodsBuyBean.getDescribe());
        holder.btn.setText(goodsBuyBean.getButton());
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){
                    Intent intent =new Intent(context, StraightForActivity.class);
                    intent.putExtra("type","straight");
                    context.startActivity(intent);

                }else {
                    context.startActivity(new Intent(context, PurchaseOrderActivity.class));
                }

            }
        });
        return convertView;
    }

    class ViewHolder{
        ImageView img;
        TextView tvName,tvDes;
        Button btn;

        public ViewHolder(View itemView) {
            img = (ImageView) itemView.findViewById(R.id.img);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            btn = (Button) itemView.findViewById(R.id.btn);
        }
    }
}
