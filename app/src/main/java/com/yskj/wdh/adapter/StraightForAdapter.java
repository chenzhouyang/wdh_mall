package com.yskj.wdh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.StraightForBean;
import com.yskj.wdh.ui.GoodsDetailActivity;
import com.yskj.wdh.util.NumberFormat;

import java.util.List;

/**
 * 创建日期 2017/5/26on 9:49.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class StraightForAdapter extends BaseAdapter {

    private List<StraightForBean.DataBean> straightForBeanList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private Context context;

    public StraightForAdapter(Context context) {
        this.context = context;
    }

    public void setStraightForBeanList(List<StraightForBean.DataBean> straightForBeanList) {
        this.straightForBeanList = straightForBeanList;
        notifyDataSetChanged();
    }

    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
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
        return straightForBeanList == null ? 0 : straightForBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return straightForBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_straight_for, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final StraightForBean.DataBean straightForBean = straightForBeanList.get(position);
        holder.tv_commodity_name.setText(straightForBean.name);
        holder.tv_pants.setText("商品来源："+straightForBean.shopName);
        holder.tv_price.setText("￥" + String.format("%.2f", NumberFormat.convertToDouble(straightForBean.price, 0d)));
        holder.ck_chose.setChecked(straightForBean.isChoosed());
        holder.et_show_num.setText(straightForBean.currentCount + "");
        holder.tv_num.setText("x" + straightForBean.currentCount);
        if (!TextUtils.isEmpty(straightForBeanList.get(position).cover)){
            Glide.with(context).load(straightForBeanList.get(position).cover).error(R.mipmap.img_error).into(holder.iv_show_pic);
        }
        holder.ck_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                straightForBean.setCheck(((CheckBox) v).isChecked());
                checkInterface.checkState(position, ((CheckBox) v).isChecked());//向外暴露接口
            }
        });

        holder.rl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,GoodsDetailActivity.class);
                intent.putExtra("lifeId",straightForBeanList.get(position).goodId);
                context.startActivity(intent);
            }
        });
        //单选框按钮
        holder.ck_chose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        straightForBean.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );

        //增加按钮
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, holder.et_show_num, holder.ck_chose.isChecked());//暴露增加接口
            }
        });

        //删减按钮
        holder.btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, holder.et_show_num, holder.ck_chose.isChecked());//暴露删减接口
            }
        });

        holder.et_show_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (holder.et_show_num.getText().toString().startsWith("0")){
                    Toast.makeText(context,"输入数量不合法",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(holder.et_show_num.getText().toString())){
//                    straightForBean.currentCount = 1;
                    modifyCountInterface.doEdit(position, holder.et_show_num, holder.ck_chose.isChecked(),1);//暴露编辑接口
                }else {
//                    straightForBean.currentCount = Integer.parseInt(holder.et_show_num.getText().toString());
                    modifyCountInterface.doEdit(position, holder.et_show_num, holder.ck_chose.isChecked(),Integer.parseInt(holder.et_show_num.getText().toString()));
                }
            }
        });
        if (straightForBean.isCheck) {
            holder.tv_commodity_name.setVisibility(View.GONE);
            holder.tv_pants.setVisibility(View.GONE);
            holder.ll_edit.setVisibility(View.VISIBLE);
            holder.ck_state.setText("完成");
        } else {
            holder.tv_commodity_name.setVisibility(View.VISIBLE);
            holder.tv_pants.setVisibility(View.VISIBLE);
            holder.ll_edit.setVisibility(View.GONE);
            holder.ck_state.setText("编辑");
        }
        return convertView;
    }


    //初始化控件
    class ViewHolder {
        ImageView iv_show_pic;
        Button btn_sub,btn_add;
        TextView tv_commodity_name, tv_pants, tv_price, tv_num;
        EditText et_show_num;
        CheckBox ck_chose, ck_state;
        RelativeLayout rl_title;
        LinearLayout ll_edit;
        @SuppressLint("WrongViewCast")
        public ViewHolder(View itemView) {
            ck_chose = (CheckBox) itemView.findViewById(R.id.ck_chose);
            iv_show_pic = (ImageView) itemView.findViewById(R.id.iv_show_pic);
            btn_add = (Button) itemView.findViewById(R.id.btn_add);
            btn_sub = (Button) itemView.findViewById(R.id.btn_sub);
            tv_commodity_name = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            ck_state = (CheckBox) itemView.findViewById(R.id.ck_state);
            tv_pants = (TextView) itemView.findViewById(R.id.tv_pants);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_num = (TextView) itemView.findViewById(R.id.tv_num);
            et_show_num = (EditText) itemView.findViewById(R.id.et_show_num);
            ll_edit = (LinearLayout) itemView.findViewById(R.id.ll_edit);
            rl_title = (RelativeLayout) itemView.findViewById(R.id.rl_title);
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

        void checkState(int position, boolean isChecked);
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
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        void doEdit(int position, View showCountView, boolean isChecked,int count);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}
