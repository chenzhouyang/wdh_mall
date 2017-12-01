package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.entity.ShoppingDetailEntity;
import com.yskj.wdh.util.StringUtils;

import java.util.List;

/**
 * Created by YSKJ-JH on 2017/1/14.
 */

public class CollectMoneyAdapter extends BaseAdapter {
    private ModifyCountInterface modifyCountInterface;
    private Context context;
    private List<ShoppingDetailEntity> shoppingDetailEntityList;

    public CollectMoneyAdapter(Context context) {
        this.context = context;
    }

    public void setShoppingCartBeanList(List<ShoppingDetailEntity> shoppingDetailEntityList) {
        this.shoppingDetailEntityList = shoppingDetailEntityList;
        notifyDataSetChanged();
    }


    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getCount() {
        return shoppingDetailEntityList == null ? 0 : shoppingDetailEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return 0;
        //return shoppingCartBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_collect_money, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       final ShoppingDetailEntity shoppingDetailEntity = shoppingDetailEntityList.get(position);
        holder.item_collect_name.setText(shoppingDetailEntity.getGoodname());
        holder.item_collect_price.setText(StringUtils.getStringtodouble(shoppingDetailEntity.getPrice()*shoppingDetailEntity.getCount()));
        holder.et_show_num.setText(shoppingDetailEntity.getCount()+"");
        //增加按钮
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.et_show_num,holder.item_collect_price);//暴露增加接口
            }
        });
        //删减按钮
        holder.btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.et_show_num,holder.item_collect_price);//暴露删减接口
            }
        });
        return convertView;
    }


    //初始化控件
    class ViewHolder {
        TextView item_collect_name,item_collect_price;
        ImageView btn_sub,btn_add;
        EditText et_show_num;
        public ViewHolder(View itemView) {
            item_collect_price = (TextView) itemView.findViewById(R.id.item_collect_price);
            item_collect_name = (TextView) itemView.findViewById(R.id.item_collect_name);
            btn_sub = (ImageView) itemView.findViewById(R.id.btn_sub);
            btn_add = (ImageView) itemView.findViewById(R.id.btn_add);
            et_show_num = (EditText) itemView.findViewById(R.id.et_show_num);
        }
    }
    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }
    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         */
        void doIncrease(int position, View showCountView,View showview);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         *
         */
        void doDecrease(int position, View showCountView,View showview);
        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);

    }
}
