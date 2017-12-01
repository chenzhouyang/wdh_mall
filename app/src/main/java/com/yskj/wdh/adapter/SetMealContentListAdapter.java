package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.SetMealContentItemBean;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.util.NumberFormat;

import java.util.List;

/**
 * Created by liuchaoya on 2016/11/9.
 * For yskj
 * Project Name : LSK
 */

public class SetMealContentListAdapter extends BaseAdapter {

    private Context context;
    private List<SetMealContentItemBean> mealList;
    public SetMealContentListAdapter(Context c, List<SetMealContentItemBean> list) {
        context = c;
        mealList = list;
    }

    @Override
    public int getCount() {
        return mealList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealList.get(position);
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
            convertView = View.inflate(context, R.layout.item_set_meal_list_view,null);
            holder.item_set_meal_content = (LinearLayout)convertView.findViewById(R.id.item_set_meal_content);
            holder.set_meal_name = (TextView) convertView.findViewById(R.id.set_meal_name);
            holder.set_meal_sub = (ImageView) convertView.findViewById(R.id.set_meal_sub);
            holder.set_meal_num = (TextView) convertView.findViewById(R.id.set_meal_num);
            holder.set_meal_add = (ImageView) convertView.findViewById(R.id.set_meal_add);
            holder.set_meal_price = (TextView) convertView.findViewById(R.id.set_meal_price);
            holder.select_state = (ImageView) convertView.findViewById(R.id.select_state);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        final SetMealContentItemBean bean = mealList.get(position);
        holder.set_meal_name.setText(bean.getName());
        holder.set_meal_num.setText(1+"");
        holder.set_meal_price.setText("¥"+String.format("%.2f", NumberFormat.convertToDouble(bean.getPrice(), 0d)));
        /**编辑监听，点击天目入*/
//        holder.item_set_meal_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, NewAddSetMealAddActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.putExtra(MealCategory.WHERE_FROM,MealCategory.EDIT);
//                intent.putExtra("bean",bean);//
//                intent.putExtra("categoryId",categoryId);//
//                context.startActivity(intent);
//            }
//        });
        final ViewHolder finalHolder = holder;
        final SetMealContentItemBean finalBean = bean;
        /**减号点击*/
        holder.set_meal_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 两个textview的数字都要变化
                int currentNum = Integer.parseInt(finalHolder.set_meal_num.getText().toString().trim());
                if(currentNum > 1){
                    finalHolder.set_meal_num.setText((currentNum - 1)+"");
//                    finalHolder.set_meal_price.setText("¥"+String.format("%.2f", NumberFormat.convertToDouble(finalBean.getPrice(), 0d)));
                    finalBean.num = currentNum - 1;
                }
            }
        });
        /**加号点击*/
        holder.set_meal_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 两个textview的数字都要变化
                int currentNum = Integer.parseInt(finalHolder.set_meal_num.getText().toString().trim());
                if(currentNum >= 1){
                    finalHolder.set_meal_num.setText((currentNum + 1)+"");
//                    finalHolder.set_meal_price.setText("¥"+String.format("%.2f", NumberFormat.convertToDouble(finalBean.getPrice(), 0d)));
                    finalBean.num = currentNum + 1;
                }
            }
        });
        /**选择标示按钮点击监听*/
        holder.select_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MealCategory.noSelect == finalBean.state){
                    finalHolder.select_state.setImageResource(R.mipmap.dian_on);
                    finalBean.state = MealCategory.Selected;
                }else {
                    finalHolder.select_state.setImageResource(R.mipmap.dian_w);
                    finalBean.state = MealCategory.noSelect;
                }
            }
        });

        return convertView;
    }

    private class ViewHolder{
        LinearLayout item_set_meal_content;
        TextView set_meal_name,set_meal_num,set_meal_price;
        ImageView select_state,set_meal_sub,set_meal_add;
    }
}
