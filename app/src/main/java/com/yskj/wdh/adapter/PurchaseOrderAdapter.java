package com.yskj.wdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.PurchaseOrderBean;
import com.yskj.wdh.ui.BuyerInfoActivity;
import com.yskj.wdh.util.NumberFormat;
import com.yskj.wdh.widget.NoScrollListView;

import java.util.ArrayList;

/**
 * 创建日期 2017/5/26on 9:20.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class PurchaseOrderAdapter extends BaseAdapter {
    private Context context;
    private String type;//1=申请者；2=审批者
    private ArrayList<PurchaseOrderBean.DataBean> datas;

    private OrderCancelInterface orderCancelInterface;

    public PurchaseOrderAdapter(Context context, String type, ArrayList<PurchaseOrderBean.DataBean> datas) {
        this.context = context;
        this.type = type;
        this.datas = datas;
    }

    /**
     * 取消订单接口
     *
     * @param orderCancelInterface
     */
    public void setOrderCancelInterface(OrderCancelInterface orderCancelInterface) {
        this.orderCancelInterface = orderCancelInterface;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_purchase_order, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PurchaseOrderBean.DataBean dataBean = datas.get(position);
        holder.tvCount.setText("共计" + dataBean.lpgList.size() + "件商品");
        holder.tvMoney.setText("合计：" + String.format("%.2f", NumberFormat.convertToDouble(dataBean.totalAmount, 0d)));
        ItemPurchaseOrderAdapter adapter = new ItemPurchaseOrderAdapter(context, String.valueOf(dataBean.status), dataBean.lpgList);
        holder.listView.setAdapter(adapter);
        switch (type) {
            //申请者
            case "1":
                //0：草稿；1：待审批；2：审批通过；3：审批不通过；4：取消订单；5：废弃
                holder.tvBusName.setText(dataBean.sellerName);
                holder.tvStatus.setVisibility(View.VISIBLE);
                holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int count = 0;
                        for (int i = 0; i < dataBean.lpgList.size(); i++) {
                            count +=dataBean.lpgList.get(i).count;
                        }
                        Intent intent = new Intent(context, BuyerInfoActivity.class);
                        intent.putExtra("type","seeSeller");
                        intent.putExtra("orderId", dataBean.orderId);
                        intent.putExtra("count", count+"");
                        context.startActivity(intent);
                    }
                });
                switch (dataBean.status) {
                    case 1:
                        holder.tvStatus.setText("正在审批");
                        holder.llBottom.setVisibility(View.VISIBLE);
                        holder.tvCancelOrder.setText("取消订单");
                        holder.tvCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderCancelInterface.orderCancel(dataBean.orderId);
                            }
                        });
                        break;
                    case 2:
                        holder.tvStatus.setText("审批通过");
                        holder.llBottom.setVisibility(View.GONE);
                        break;
                    case 3:
                        holder.tvStatus.setText("审批不通过");
                        holder.llBottom.setVisibility(View.GONE);
                        holder.tvCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderCancelInterface.orderCancel(dataBean.orderId);
                            }
                        });
                        break;
                }
                holder.tvApply.setVisibility(View.GONE);
                break;
            //审批者
            case "2":
                holder.tvBusName.setText(dataBean.buyerName);
                holder.tvStatus.setVisibility(View.GONE);
                holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int count = 0;
                        for (int i = 0; i < dataBean.lpgList.size(); i++) {
                            count +=dataBean.lpgList.get(i).count;
                        }
                        Intent intent = new Intent(context, BuyerInfoActivity.class);
                        intent.putExtra("type","seeBuyer");
                        intent.putExtra("orderId", dataBean.orderId);
                        intent.putExtra("count", count+"");
                        context.startActivity(intent);
                    }
                });
                switch (dataBean.status) {
                    case 1://待我审批
                        holder.llBottom.setVisibility(View.VISIBLE);
                        holder.tvApply.setVisibility(View.VISIBLE);
                        holder.tvCancelOrder.setText("不通过审批");
                        holder.tvCancelOrder.setTextColor(Color.parseColor("#424242"));
                        holder.tvCancelOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 审批结果 0：不通过；1：通过
                                orderCancelInterface.applyOrder(dataBean.orderId,"0");
                            }
                        });
                        holder.tvApply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                orderCancelInterface.applyOrder(dataBean.orderId,"1");
                            }
                        });
                        break;
                    case 2://我已审批通过
                        holder.llBottom.setVisibility(View.GONE);
                        break;
                }
                break;
        }
        return convertView;
    }

    class ViewHolder {
        TextView tvStatus, tvCount, tvMoney, tvCancelOrder, tvApply,tvBusName;
        NoScrollListView listView;
        LinearLayout llBottom;

        public ViewHolder(View itemView) {
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvCount = (TextView) itemView.findViewById(R.id.tv_count);
            tvMoney = (TextView) itemView.findViewById(R.id.tv_money);
            tvCancelOrder = (TextView) itemView.findViewById(R.id.tv_cancel_order);
            listView = (NoScrollListView) itemView.findViewById(R.id.list_view);
            tvApply = (TextView) itemView.findViewById(R.id.tv_apply);
            tvBusName = (TextView) itemView.findViewById(R.id.tv_bus_name);
            llBottom = (LinearLayout) itemView.findViewById(R.id.ll_bottom);
        }
    }

    public interface OrderCancelInterface {
        void orderCancel(String orderId);

        void applyOrder(String orderId, String result);
    }
}
