package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ProManagerMealListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.bean.ProManagerSetMealBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.NewlyIncreasedDialog;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.MethodUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


/**
 * Created by liuchaoya on 2016/11/5.
 * For yskj
 * Project Name : LSK
 */

/**
 * 项目管理
 */
public class BusinessProjectManagerActivity extends BaseActivity implements View.OnClickListener {
    private ImageView left_arrow;
    private TextView title_content, title_menu, all_meal, on_lined, out_lined, abandoned;
    private ListView meal_list_view;
    private ProManagerMealListAdapter adapter;
    /**
     * 筛选过的数据，全部也可视为筛选
     */
    private List<ProManagerSetMealBean.DataBean.ListBean> filtSetMealList = new ArrayList<>();
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_project_manager);
        initView();
        initListener();
        initViewContent();
        initListViewContent();
        getShopName();
    }

    private void getShopName() {
        Intent intent = getIntent();
        BusinessInfoBean bean = (BusinessInfoBean) intent.getSerializableExtra("bean");
        if (bean != null) {
            shopName = bean.getShopName();
            caches.put("shopName", shopName);
        }
    }


    /**
     * 初始化所有组件
     */
    private void initView() {
        /**标题*/
        left_arrow = (ImageView) findViewById(R.id.left_arrow);              //界面返回键
        title_content = (TextView) findViewById(R.id.title_content);         //界面大标题
        title_menu = (TextView) findViewById(R.id.title_menu);               //界面菜单
        /**操作按钮*/
        all_meal = (TextView) findViewById(R.id.all_meal);                   //筛选全部食品
        on_lined = (TextView) findViewById(R.id.on_lined);                   //筛选已上线食品
        out_lined = (TextView) findViewById(R.id.out_lined);                 //筛选已下线食品
        abandoned = (TextView) findViewById(R.id.abandoned);                 //筛选已废弃食品
        /**食品列表*/
        meal_list_view = (ListView) findViewById(R.id.meal_list_view);      //食品列表
    }

    /**
     * 为需要监听的组件添加监听
     */
    private void initListener() {
        left_arrow.setOnClickListener(this);    //界面返回键监听
        title_menu.setOnClickListener(this);    //界面菜单监听
        all_meal.setOnClickListener(this);      //筛选全部食品监听
        on_lined.setOnClickListener(this);      //筛选已上线食品监听
        out_lined.setOnClickListener(this);     //筛选已下线食品监听
        abandoned.setOnClickListener(this);     //筛选已下线食品监听
    }

    /**
     * 统一为组件设置显示内容
     */
    private void initViewContent() {
        title_content.setText(R.string.pm_business_project_manager_title_content);
        title_menu.setText(R.string.pm_business_project_manager_menu);
    }

    /**
     * 点击事件统一处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.title_menu:
                //TODO 菜单键-新增
                NewlyIncreasedDialog dialog = new NewlyIncreasedDialog(context);
                dialog.show();
                break;
            case R.id.all_meal:
                //TODO 筛选全部
                filtSetMealList.clear();
                getListDataFromInternet(MealCategory.ALL);
                adapter.notifyDataSetChanged();
                break;
            case R.id.on_lined:
                //TODO 筛选已上线
                filtSetMealList.clear();
                getListDataFromInternet(MealCategory.ON_LINED);
                adapter.notifyDataSetChanged();
                break;
            case R.id.out_lined:
                //TODO 筛选已下线
                filtSetMealList.clear();
                getListDataFromInternet(MealCategory.OUT_LINED);
                adapter.notifyDataSetChanged();
                break;
            case R.id.abandoned:
                //TODO 筛选已下线
                filtSetMealList.clear();
                getListDataFromInternet(MealCategory.ABANDONED);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 加载listview列表显示数据
     */
    private void initListViewContent() {
        getListDataFromInternet(MealCategory.ALL);
    }
    /**
     * 联网获取数据
     */
    private void getListDataFromInternet(int state) {
        if (state != MealCategory.ALL) {
            startMyDialog();
            OkHttpUtils.get().url(MealCategory.PM_LIST_QUERY).
                    addHeader("Authorization", caches.get("access_token"))
                    .addParams("status", state+"")
                    .addParams("count","100")
                    .addParams("cursor","0")
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    stopMyDialog();
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    stopMyDialog();
                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        ProManagerSetMealBean mealBean = new Gson().fromJson(response,new TypeToken<ProManagerSetMealBean>(){}.getType());
                        filtSetMealList=mealBean.data.list;
                        adapter = new ProManagerMealListAdapter(BusinessProjectManagerActivity.this,filtSetMealList);
                        meal_list_view.setAdapter(adapter);
                    } else if (code == 734) {
                        MethodUtils.showToast(BusinessProjectManagerActivity.this, getResources().getString(R.string.pm_business_project_manager_remind_tip2));
                    }
                }
            });
        }else {
            OkHttpUtils.get().url(MealCategory.PM_LIST_QUERY).
                    addHeader("Authorization", caches.get("access_token"))
                    .addParams("count","100")
                    .addParams("cursor","0")
                    .build().execute(new StringCallback() {
                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    stopMyDialog();
                }

                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                    startMyDialog();
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    isLogin(e);
                }

                @Override
                public void onResponse(String response, int id) {
                    stopMyDialog();

                    Map<String, Object> map = JSONFormat.jsonToMap(response);
                    int code = (int) map.get("code");
                    if (code == 0) {
                        ProManagerSetMealBean mealBean = new Gson().fromJson(response,new TypeToken<ProManagerSetMealBean>(){}.getType());
                        filtSetMealList = mealBean.data.list;
                        adapter = new ProManagerMealListAdapter(BusinessProjectManagerActivity.this,filtSetMealList);
                        meal_list_view.setAdapter(adapter);
                    } else if (code == 734) {
                        MethodUtils.showToast(BusinessProjectManagerActivity.this, getResources().getString(R.string.pm_business_project_manager_remind_tip2));
                    }
                }
            });
        }
    }

}
