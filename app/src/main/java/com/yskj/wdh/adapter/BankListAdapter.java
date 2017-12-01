package com.yskj.wdh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.wdh.R;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.BankListEntity;
import com.yskj.wdh.ui.deposit.AddBankActivity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.DragDelItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class BankListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<BankListEntity.DataBean> list;
private  BankListEntity.DataBean  databean;
    private LoadingCaches caches = LoadingCaches.getInstance();
    public BankListAdapter(Context context, ArrayList<BankListEntity.DataBean> list){
        super();
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        if(list!= null&&list.size()!=0){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewBankHolder holder = null;
        View menuView=null;
        if(convertView == null){
            menuView =  LayoutInflater.from(context).inflate(R.layout.swipemenu,null);
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bank,null);
            convertView = new DragDelItem(convertView,menuView);
            holder = new ViewBankHolder(convertView);
        }else {
            holder = (ViewBankHolder) convertView.getTag();
        }
        databean = list.get(position);
        holder.item_bank_name.setText(databean.bank);
        holder.item_bank_number.setText(databean.cardNo);
        switch (databean.bank){
            case "建设银行":
                holder.item_bank_image.setImageResource(R.mipmap.jian_pay_ico);
                break;
            case "中国银行":
                holder.item_bank_image.setImageResource(R.mipmap.china_pay_ico);
                break;
            case "农业银行":
                holder.item_bank_image.setImageResource(R.mipmap.long_pay_ico);
                break;
            case "工商银行":
                holder.item_bank_image.setImageResource(R.mipmap.buess_pay_ico);
                break;
            case "招商银行":
                holder.item_bank_image.setImageResource(R.mipmap.zhao_pay_ico);
                break;
            case "交通银行":
                holder.item_bank_image.setImageResource(R.mipmap.jiao_pay_ico);
                break;
            case "支付宝":
                holder.item_bank_image.setImageResource(R.mipmap.zhifu_pay_ico);
                break;
            case "微信":
                holder.item_bank_image.setImageResource(R.mipmap.wei_pay_ico);
                break;
        }
        holder.tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OkHttpUtils.post().url(Urls.DELBANK)
                        .addParams("id",databean.id+"")
                        .addHeader("Authorization",caches.get("access_token"))
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Map<String,Object> map = JSONFormat.jsonToMap(response);
                        int code = (int) map.get("code");
                        if(code == 0){
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            notifyDataSetChanged();
                        }else {
                            String mesage = Messge.geterr_code(code);
                            Toast.makeText(context, mesage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        holder.ll_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddBankActivity.class);
                intent.putExtra("bank",databean.bank);
                intent.putExtra("name",databean.name);
                intent.putExtra("cardNo",databean.cardNo);
                intent.putExtra("cardType",databean.cardType+"");
                intent.putExtra("cardid",databean.id+"");
                intent.putExtra("type","1");
                context.startActivity(intent);
            }
        });
        return convertView;
    }
     class ViewBankHolder{
        ImageView item_bank_image;
        TextView item_bank_name,item_bank_number,tv_del;
        LinearLayout ll_layout;

         public ViewBankHolder(View view) {
             item_bank_image = (ImageView)view.findViewById(R.id.item_bank_image);
             item_bank_name = (TextView)view.findViewById(R.id.item_bank_name);
             item_bank_number = (TextView)view.findViewById(R.id.item_bank_number);
             tv_del=(TextView)view.findViewById(R.id.tv_del);
             ll_layout = (LinearLayout) view.findViewById(R.id.ll_layout);
             WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
             int width = wm.getDefaultDisplay().getWidth();
             ll_layout.setMinimumWidth(width-10);
             view.setTag(this);
         }
    }
}
