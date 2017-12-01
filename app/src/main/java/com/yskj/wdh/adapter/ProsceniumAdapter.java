package com.yskj.wdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.wdh.R;
import com.yskj.wdh.entity.ProsceniumEntity;
import com.yskj.wdh.ui.collectmoney.OrderDetailsActivity;
import com.yskj.wdh.ui.collectmoney.PaymentCodeActivity;
import com.yskj.wdh.util.StringUtils;
import com.yskj.wdh.zxing.activity.CaptureCollectMoneyActivity;

import java.util.List;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class ProsceniumAdapter extends BaseAdapter {
    private List<ProsceniumEntity.DataBean.ListBean> prseclist;
    private Context context;
    private int mRightWidth = 0;
    public ProsceniumAdapter(Context context,List<ProsceniumEntity.DataBean.ListBean> prseclist,int rightWidth){
        this.context = context;
        this.prseclist = prseclist;
        mRightWidth = rightWidth;
    }
    @Override
    public int getCount() {
        if(prseclist!=null&&prseclist.size()!=0){
            return prseclist.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return prseclist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_proscenium, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ProsceniumEntity.DataBean.ListBean  prosceni = prseclist.get(position);
        holder.proscenium_name.setText(prosceni.orderName);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);
        if(prosceni.status == 0){
            holder.cashier_background.setBackgroundResource(R.mipmap.qrcode_background);
            holder.item_proscenium_stuse.setText("生成付款二维码");
            holder.item_proscenium_stuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(prseclist.get(position).totalAmount>0){
                        context.startActivity(new Intent(context, PaymentCodeActivity.class)
                                .putExtra("orderid", prseclist.get(position).id+"")
                                .putExtra("totalamount",prseclist.get(position).totalAmount+""));
                    }else {
                        Toast.makeText(context,"金额不能为零哦",Toast.LENGTH_SHORT).show();
                    }

                }
            });
            holder.scan_prosecenium.setImageResource(R.mipmap.qrcode_image);
            holder.scan_prosecenium.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, CaptureCollectMoneyActivity.class)
                            .putExtra("type","1")
                            .putExtra("orderid",prseclist.get(position).id+"")
                            .putExtra("ordername",prseclist.get(position).orderName+""));
                }
            });
        }else if(prosceni.status == 2){
            holder.scan_prosecenium.setImageResource(R.mipmap.qrcode_paid);
            holder.cashier_background.setBackgroundResource(R.mipmap.qr_code_background);
            holder.item_proscenium_stuse.setText("查看账单详情");
            holder.item_proscenium_stuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, OrderDetailsActivity.class).putExtra("orderid", prseclist.get(position).id+""));
                }
            });
        }

        holder.prosecnium_price.setText("合计："+StringUtils.getStringtodouble(prosceni.totalAmount));
        holder.item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        return convertView;
    }
    //初始化控件
    class ViewHolder {
        TextView proscenium_name,prosecnium_price,item_proscenium_stuse;
        ImageView scan_prosecenium;
        LinearLayout cashier_background;
        RelativeLayout item_right;
        public ViewHolder(View itemView) {
            prosecnium_price = (TextView) itemView.findViewById(R.id.prosecnium_price);
            proscenium_name = (TextView) itemView.findViewById(R.id.proscenium_name);
            item_proscenium_stuse = (TextView) itemView.findViewById(R.id.item_proscenium_stuse);
            scan_prosecenium = (ImageView) itemView.findViewById(R.id.scan_prosecenium);
            cashier_background = (LinearLayout) itemView.findViewById(R.id.cashier_background);
            item_right = (RelativeLayout) itemView.findViewById(R.id.item_right);
        }
    }
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public void setOnRightItemClickListener(onRightItemClickListener listener){
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }
}
