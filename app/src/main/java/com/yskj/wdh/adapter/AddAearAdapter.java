package com.yskj.wdh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.bean.CityTestEntity;
import com.yskj.wdh.ui.providermanager.DialogCheckInterface;

import java.util.ArrayList;

/**
 * 创建日期 2017/8/14on 17:13.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class AddAearAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CityTestEntity> arrayList;
    private DialogCheckInterface dialogCheckInterface;
    public void setDialogCheckInterface(DialogCheckInterface dialogCheckInterface) {
        this.dialogCheckInterface = dialogCheckInterface;
    }
    public AddAearAdapter(Context context, ArrayList<CityTestEntity> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        if (null == arrayList || 0 == arrayList.size()) {
            return 0;
        }
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_add_aear, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(arrayList.get(position).name);
        holder.ckChoseOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dialogCheckInterface.checkedOne(position,isChecked);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        CheckBox ckChoseOne;
        public ViewHolder(View itemView) {
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            ckChoseOne = (CheckBox) itemView.findViewById(R.id.ck_chose_one);
        }
    }
}
