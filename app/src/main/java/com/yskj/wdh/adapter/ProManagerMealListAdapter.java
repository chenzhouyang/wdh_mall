package com.yskj.wdh.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yskj.wdh.R;
import com.yskj.wdh.bean.ProManagerSetMealBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.ui.localmerchant.NewAddContentActivity;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.SimplexToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by liuchaoya on 2016/11/9.
 * For yskj
 * Project Name : LSK
 */

/**
 * 项目管理-套餐列表显示适配器
 */
public class ProManagerMealListAdapter extends BaseAdapter {
    private Context context;
    private List<ProManagerSetMealBean.DataBean.ListBean> mealList;
    /**商品上下架状态*/
    private int state;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                SimplexToast.show(context, platform + " 收藏成功啦");
            } else {
                SimplexToast.show(context, platform + " 分享成功啦");
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SimplexToast.show(context, platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SimplexToast.show(context, platform + " 分享取消了");
        }
    };
    private int isshared;

    public ProManagerMealListAdapter(Context context,List<ProManagerSetMealBean.DataBean.ListBean> list) {
        this.context = context;
        mealList = list;
    }

    @Override
    public int getCount() {
        if (mealList==null||mealList.size()==0)
            return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_meal_list_view, null);
            holder.operate_btn_ll = (LinearLayout) convertView.findViewById(R.id.operate_btn_ll);
            holder.meal_name = (TextView) convertView.findViewById(R.id.meal_name);
            holder.meal_on_line = (TextView) convertView.findViewById(R.id.meal_on_line);
            holder.meal_updata_time = (TextView) convertView.findViewById(R.id.meal_updata_time);
            holder.meal_delete = (TextView) convertView.findViewById(R.id.meal_delete);
            holder.meal_edit = (TextView) convertView.findViewById(R.id.meal_edit);
            holder.meal_show_image = (ImageView) convertView.findViewById(R.id.meal_show_image);
            holder.meal_delete_shap = (TextView) convertView.findViewById(R.id.meal_delete_shap);
            holder.share_straight = (TextView) convertView.findViewById(R.id.share_straight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ProManagerSetMealBean.DataBean.ListBean bean = mealList.get(position);
        holder.meal_name.setText(bean.name);
        state = bean.status;
        if (mealList.get(position).status == 2) {
            holder.meal_on_line.setText(MealCategory.SHOW_DOWN);
            holder.meal_delete_shap.setVisibility(View.VISIBLE);
            if(mealList.get(position).isOwner == 0){
                //不是商品所属者
                holder.share_straight.setVisibility(View.VISIBLE);//共享此商品
            }else {
                holder.share_straight.setVisibility(View.GONE);//共享此商品
            }

            holder.meal_edit.setVisibility(View.GONE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
        } else if(mealList.get(position).status == 4){
            holder.meal_on_line.setText(MealCategory.SHOW_UP);
            holder.share_straight.setVisibility(View.GONE);//共享此商品
            holder.meal_edit.setVisibility(View.VISIBLE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
        }else if(mealList.get(position).status == 1){
            holder.meal_on_line.setText("上线审核中");
            holder.meal_edit.setVisibility(View.GONE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
            holder.share_straight.setVisibility(View.GONE);//共享此商品
        }else if(mealList.get(position).status == 12){
            holder.meal_on_line.setText("上线审批不通过");
            holder.meal_edit.setVisibility(View.VISIBLE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
            holder.share_straight.setVisibility(View.GONE);//共享此商品
        }else if(mealList.get(position).status == 3){
            holder.meal_on_line.setText("下线申请中");
            holder.meal_edit.setVisibility(View.GONE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
            holder.share_straight.setVisibility(View.GONE);//共享此商品
        }else  if(mealList.get(position).status == 14){
            holder.meal_on_line.setText("下线审批不通过");
            holder.meal_edit.setVisibility(View.GONE); //编辑
            holder.meal_delete.setVisibility(View.GONE);//删除
            holder.share_straight.setVisibility(View.GONE);//共享此商品
        }else if(mealList.get(position).status == 0){
            holder.meal_delete.setVisibility(View.VISIBLE);
            holder.meal_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OkHttpUtils.get().url(MealCategory.PM_NO_ON_LINE_DELETE)
                            .addHeader("Authorization", aCache.get("access_token"))
                            .addParams("lifeId", bean.id+"")
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Map<String, Object> map = JSONFormat.jsonToMap(response);
                            int code = (int) map.get("code");
                            if (code == 0) {
                                mealList.remove(bean);
                                notifyDataSetChanged();
                                MethodUtils.showToast(context, context.getString(R.string.pm_business_project_manager_delete_success));
                            }
                        }
                    });
                }
            });
        }else {
            holder.meal_on_line.setText("禁用");
        }
        holder.meal_updata_time.setText(bean.updateTime);
        isshared = mealList.get(position).isShared;
        if(mealList.get(position).isShared == 0){
            holder.share_straight.setText("分享直供");
        }else {
            holder.share_straight.setText("取消直供");
        }
        if(mealList.get(position).goodsType == 2){
            final ViewHolder finalHolder1 = holder;
            holder.share_straight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isshared == 0){
                    OkHttpUtils.post().url(Urls.SHARLIFE).addHeader("Authorization", aCache.get("access_token"))
                            .addParams("goodsId",mealList.get(position).id+"")
                            .addParams("isShared","1")
                            .build().execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Map<String,Object> map = JSONFormat.jsonToMap(response);
                            int code = (int) map.get("code");
                            if(code == 0){
                                Toast.makeText(context,"共享成功，快去分享商品中查看吧",Toast.LENGTH_SHORT).show();
                                finalHolder1.share_straight.setText("取消直供");
                               isshared = 1;
                            }
                        }
                    });
                }else {
                    OkHttpUtils.post().url(Urls.SHARLIFE).addHeader("Authorization", aCache.get("access_token"))
                            .addParams("goodsId",mealList.get(position).id+"")
                            .addParams("isShared","0")
                            .build().execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Map<String,Object> map = JSONFormat.jsonToMap(response);
                            int code = (int) map.get("code");
                            if(code == 0){
                                Toast.makeText(context,"取消成功",Toast.LENGTH_SHORT).show();
                                finalHolder1.share_straight.setText("分享直供");
                                isshared = 0;
                            }
                        }
                    });
                }

                }

        });
        }else if(bean.goodsType == 1){
            holder.share_straight.setVisibility(View.GONE);
        }
        holder.meal_delete_shap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .withTitle("唯多惠最牛店主")
                            .withText(mealList.get(position).name)
                            .withMedia(new UMImage(context,mealList.get(position).coverAndUrl))
                            .withTargetUrl(Ips.SHAREURL)
                            .setCallback(umShareListener)
                            .open();

            }
        });
        if(bean.coverAndUrl.length()!=0){
            Glide.with(context).load(bean.coverAndUrl).error(R.mipmap.default_image).into(holder.meal_show_image);
        }else{
            holder.meal_show_image.setImageResource(R.mipmap.default_image);
        }
        /**编辑按钮点击响应*/
        holder.meal_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.abandoned == bean.status){
                    MethodUtils.showToast(context,context.getResources().getString(R.string.pm_business_project_manager_remind_abandoned));
                    return;
                }
                Intent intent = new Intent(context, NewAddContentActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //TODO 传值
                intent.putExtra(MealCategory.WHERE_FROM, MealCategory.EDIT);
                intent.putExtra("life_id", bean.id);
                intent.putExtra("shoptype",mealList.get(position).goodsType+"");
                context.startActivity(intent);
            }
        });

        /**上下线操作按钮点击响应*/
        final ViewHolder finalHolder = holder;
        holder.meal_on_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.uped == bean.status) {
                    /**上架过的，当前显示为下架，点击后变为上架*/
                    if(mealList.get(position).goodsType == 2||mealList.get(position).goodsType == 12){
                        //如果是直供商品显示下架申请
                        bean.setStatus(MealCategory.downing);
                        finalHolder.meal_on_line.setText("审核中");
                        finalHolder.meal_delete.setVisibility(View.GONE);
                        finalHolder.meal_delete_shap.setVisibility(View.VISIBLE);
                        finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                        if(mealList.get(position).isOwner==0){
                            //不是所属着
                            finalHolder.share_straight.setVisibility(View.GONE);
                        }else {
                            //是所属着
                            finalHolder.share_straight.setVisibility(View.VISIBLE);
                        }
                        upOrDownLine(bean.id, 2,finalHolder);
                    }else {
                        //如果不是直供商品下架
                        bean.setStatus(MealCategory.downed);
                        finalHolder.meal_on_line.setText(MealCategory.SHOW_UP);
                        finalHolder.meal_delete.setVisibility(View.GONE);
                        finalHolder.meal_delete_shap.setVisibility(View.VISIBLE);
                        finalHolder.share_straight.setVisibility(View.VISIBLE);
                        if(mealList.get(position).isOwner==0){
                            finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                            finalHolder.share_straight.setVisibility(View.GONE);
                        }else {
                            finalHolder.meal_edit.setVisibility(View.VISIBLE);
                            finalHolder.share_straight.setVisibility(View.VISIBLE);
                        }
                        upOrDownLine(bean.id, 0,finalHolder);
                    }

                } else if (MealCategory.downed == bean.getStatus()||MealCategory.text == bean.getStatus()||bean.getStatus()==14) {
                    /**下架过的，当前显示为上架，点击后变为下架*/
                    if(state == MealCategory.text){
                        finalHolder.meal_delete.setVisibility(View.GONE);
                    }
                    if(mealList.get(position).goodsType == 2){
                        //如果是直供商品显示下架申请
                        bean.setStatus(MealCategory.uping);
                        finalHolder.meal_on_line.setText("审核中");
                        finalHolder.share_straight.setVisibility(View.GONE);
                        finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                        finalHolder.meal_delete.setVisibility(View.GONE);
                        finalHolder.meal_delete_shap.setVisibility(View.GONE);
                        finalHolder.share_straight.setVisibility(View.GONE);
                        upOrDownLine(bean.id, 3,finalHolder);
                    }else {
                        //如果不是直供商品下架
                        bean.setStatus(MealCategory.uped);
                        finalHolder.meal_on_line.setText(MealCategory.SHOW_DOWN);
                        finalHolder.meal_delete.setVisibility(View.GONE);
                        finalHolder.meal_delete_shap.setVisibility(View.GONE);
                        finalHolder.share_straight.setVisibility(View.GONE);
                        if(mealList.get(position).isOwner==0){
                            finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                            finalHolder.share_straight.setVisibility(View.GONE);
                        }else {
                            finalHolder.meal_edit.setVisibility(View.VISIBLE);
                            finalHolder.share_straight.setVisibility(View.VISIBLE);
                        }
                        upOrDownLine(bean.id, 1,finalHolder);
                    }

                }else if (MealCategory.abandoned == bean.getStatus()){
                    MethodUtils.showToast(context, context.getString(R.string.pm_business_project_manager_remind_abandoned));
                }
            }
        });

        return convertView;
    }

    /**上下架联网操作*/
    private void upOrDownLine(int id, final int expectState, final ViewHolder finalHolder) {
        int oldint=0;
        if(expectState == 3||expectState == 1){
            oldint = 1;
        }else {
            oldint = 0;
        }
        OkHttpUtils.get().url(MealCategory.PM_ON_OR_OUT_LINE)
                .addHeader("Authorization", aCache.get("access_token"))
                .addParams("lifeId", id+"")
                .addParams("status", oldint+"")
                .build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                if (code == 0 && expectState == 1) {
                    MethodUtils.showToast(context, context.getString(R.string.pm_business_project_manager_up_success));
                    finalHolder.meal_delete.setVisibility(View.GONE);
                    finalHolder.meal_delete_shap.setVisibility(View.VISIBLE);
                    finalHolder.share_straight.setVisibility(View.VISIBLE);
                    finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                } else if (code == 0 && expectState == 0) {
                    finalHolder.meal_delete_shap.setVisibility(View.GONE);
                    finalHolder.share_straight.setVisibility(View.GONE);
                    finalHolder.meal_edit.setVisibility(View.VISIBLE);
                    MethodUtils.showToast(context, context.getString(R.string.pm_business_project_manager_down_success));
                }else if(code == 0 && expectState == 2){
                    finalHolder.meal_delete_shap.setVisibility(View.GONE);
                    finalHolder.share_straight.setVisibility(View.GONE);
                    finalHolder.meal_edit.setVisibility(View.VISIBLE);
                    MethodUtils.showToast(context, "下架申请已提交");
                }else if(code == 0 && expectState == 3){
                    finalHolder.meal_delete.setVisibility(View.GONE);
                    finalHolder.meal_delete_shap.setVisibility(View.VISIBLE);
                    finalHolder.share_straight.setVisibility(View.VISIBLE);
                    finalHolder.meal_edit.setVisibility(View.INVISIBLE);
                    MethodUtils.showToast(context, "上架申请已提交");
                }
                notifyDataSetChanged();
            }
        });
    }

    class ViewHolder {
        LinearLayout operate_btn_ll;
        TextView meal_name, meal_on_line, meal_updata_time, meal_edit, meal_delete,meal_delete_shap,share_straight;
        ImageView meal_show_image;
    }
}
