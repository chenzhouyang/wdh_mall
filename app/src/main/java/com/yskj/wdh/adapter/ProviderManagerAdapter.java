package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProviderGoodsEntity;
import com.yskj.wdh.ui.providermanager.ProviderManagerAdapterInterface;
import com.yskj.wdh.util.NumberFormat;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/11on 15:29.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class ProviderManagerAdapter  extends BaseAdapter {
    private Context context;
    private ArrayList<ProviderGoodsEntity.DataBean> arrayList;
    private ProviderManagerAdapterInterface providerManagerAdapterInterface;

    public void setProviderManagerAdapterInterface(ProviderManagerAdapterInterface providerManagerAdapterInterface) {
        this.providerManagerAdapterInterface = providerManagerAdapterInterface;
    }

    public ProviderManagerAdapter(Context context, ArrayList<ProviderGoodsEntity.DataBean> arrayList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_provider_manager, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProviderGoodsEntity.DataBean dataBean = arrayList.get(position);
        Glide.with(context).load(dataBean.thumbnail).error(R.mipmap.img_error).into(holder.imgGoods);
        holder.tvName.setText(dataBean.goodName);
        holder.tvPrice.setText("￥"+String.format("%.2f", NumberFormat.convertToDouble(dataBean.shopPrice, 0d)));
        holder.tvSoldCount.setText("销量"+dataBean.volume);
        holder.tvHasNum.setText("库存"+dataBean.stock);
        switch (arrayList.get(position).status) {
            case 0:
                holder.tvUpOrDown.setText("上架");
                holder.tvUpOrDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.upOrDown(position);
                    }
                });
                holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.edit(position);
                    }
                });
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.delete(position);
                    }
                });
                break;
            case 1:
                holder.tvUpOrDown.setText("上架审核中");
                break;
            case 2:
                holder.tvUpOrDown.setText("上架审核通过");
                holder.tvUpOrDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.upOrDown(position);
                    }
                });
                break;
            case 3:
                holder.tvUpOrDown.setText("上架审核失败");
                holder.tvUpOrDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.upOrDown(position);
                    }
                });
                holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.edit(position);
                    }
                });
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.delete(position);
                    }
                });
                break;
            case 4:
                holder.tvUpOrDown.setText("下架审核中");
                break;
            case 5:
                holder.tvUpOrDown.setText("下架审核通过");
                holder.tvUpOrDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.upOrDown(position);
                    }
                });
                holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.edit(position);
                    }
                });
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.delete(position);
                    }
                });
                break;
            case 6:
                holder.tvUpOrDown.setText("下架审核失败 ");
                holder.tvUpOrDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.upOrDown(position);
                    }
                });
                holder.tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.edit(position);
                    }
                });
                holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        providerManagerAdapterInterface.delete(position);
                    }
                });
                break;
//            case 7:
//                holder.tvUpOrDown.setText("删除");
//                break;
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView  tvEdit,tvUpOrDown,tvDelete;
        TextView tvName, tvPrice,tvSoldCount,tvHasNum,tvAddTime;
        public ViewHolder(View itemView) {
            imgGoods = (ImageView) itemView.findViewById(R.id.img_goods);
            tvEdit = (TextView) itemView.findViewById(R.id.tv_edit);
            tvUpOrDown = (TextView) itemView.findViewById(R.id.tv_up_or_down);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_delete);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvSoldCount = (TextView) itemView.findViewById(R.id.tv_sold_count);
            tvHasNum = (TextView) itemView.findViewById(R.id.tv_has_num);
            tvAddTime = (TextView) itemView.findViewById(R.id.tv_add_time);

        }
    }
}

