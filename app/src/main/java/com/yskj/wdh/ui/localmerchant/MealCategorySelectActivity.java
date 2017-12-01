package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.CategoryListAdapter;
import com.yskj.wdh.adapter.CategoryListChildAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CategoryItemBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.util.JSONFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;


/**品类选择*/
public class MealCategorySelectActivity extends BaseActivity implements View.OnClickListener{

    private ListView category_list,category_list_child;
    private ImageView left_arrow;
    private TextView title_content;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private List<CategoryItemBean> parent_list = new ArrayList<>();
    private CategoryListAdapter adapter;
    private List<CategoryItemBean> child_list;
    private String goodstype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category_select);
        goodstype = getIntent().getStringExtra("goodstype");
        initView();
        initListener();
        initViewContent();
        initListViewContent();
    }


    /**初始化所有组件*/
    private void initView() {
        left_arrow = (ImageView)findViewById(R.id.left_arrow);
        title_content = (TextView)findViewById(R.id.title_content);
        category_list = (ListView) findViewById(R.id.category_list);
        category_list_child = (ListView) findViewById(R.id.category_list_child);
        adapter = new CategoryListAdapter(MealCategorySelectActivity.this,parent_list);
        category_list.setAdapter(adapter);
    }
    /**为需要监听的组件添加监听*/
    private void initListener() {
        left_arrow.setOnClickListener(this);
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CategoryItemBean bean = parent_list.get(position);
                child_list = bean.children;
                if(child_list != null && child_list.size() > 0){
                    category_list.setVisibility(View.GONE);
                    category_list_child.setVisibility(View.VISIBLE);
                    CategoryListChildAdapter childAdapter = new CategoryListChildAdapter(MealCategorySelectActivity.this, child_list);
                    category_list_child.setAdapter(childAdapter);
                    category_list_child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            setResultAndBack(child_list,position);
                        }
                    });
                }else {
                    setResultAndBack(parent_list,position);
                }

            }
        });

    }
    /**用于品类选择点击到最后一级时将选择的值带回上一activity*/
    private void setResultAndBack(List<CategoryItemBean> list, int position){
        Intent intent = new Intent();
        intent.putExtra(MealCategory.CATEGORY_NAME,list.get(position).name);
        intent.putExtra(MealCategory.CATEGORY_ID,list.get(position).cateId);
        setResult(RESULT_OK,intent);
        finish();
    }
    /**统一为组件设置显示内容*/
    private void initViewContent() {
        title_content.setText(R.string.pm_meal_category_select_title_content);
    }
    /**ListView显示内容*/
    private void initListViewContent() {
        getDataFromInternet();

    }

    private void getDataFromInternet() {
        //以下两个参数不传默认返回全部
//        params.put("cateId",5);     //商家注册商店时选择的商店性质，不传该值默认查询全部
//        params.put("isHot",true);
        OkHttpUtils.get().url(MealCategory.CATEGORY_QUERY_LIST)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("type",goodstype)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                try{
                    if(code == 0){
                        /**获取lifeCateList对象*/
                        JSONArray array = (JSONArray) map.get("data");
                        if(array.length() > 0){
                            parent_list.clear();
                            for (int j = 0; j < array.length(); j++) {
                                CategoryItemBean bean = JSONFormat.parseT(array.get(j).toString(),CategoryItemBean.class);
                                parent_list.add(bean);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    /**点击事件统一处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.left_arrow:
                finish();
                break;
            case R.id.select_snack:
                Intent intent1 = new Intent();
                intent1.putExtra(MealCategory.CATEGORY_NAME,MealCategory.SNACK);
                setResult(RESULT_OK,intent1);
                finish();
                break;
        }
    }
}
