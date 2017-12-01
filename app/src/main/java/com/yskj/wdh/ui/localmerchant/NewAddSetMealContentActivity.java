package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.adapter.SetMealContentListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.SetMealContentItemBean;
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

/**
 * 新增-套餐内容  套餐条目
 */
public class NewAddSetMealContentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView left_arrow, select_state;
    private TextView title_content, title_menu;
    private ListView set_meal_list_view;
    private List<SetMealContentItemBean> setMeaContentList = new ArrayList<>();
    private SetMealContentListAdapter adapter;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_meal_content);
        initView();
        initListener();
        initViewContent();
        initListViewContent();
    }


    private void initView() {
        /**标题*/
        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        title_content = (TextView) findViewById(R.id.title_content);
        title_menu = (TextView) findViewById(R.id.title_menu);
        /**套餐内容列表*/
        set_meal_list_view = (ListView) findViewById(R.id.set_meal_list_view);
    }

    private void initListener() {
        left_arrow.setOnClickListener(this);
        title_menu.setOnClickListener(this);
        set_meal_list_view.setOnItemClickListener(this);
        set_meal_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                /**条目长按监听*/
//                Dialog alertDialog = new AlertDialog.Builder(getBaseContext(), R.style.add_dialog)
//                        .setTitle("")
//                        .setItems(MealCategory.arraysetmealitem, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                if (which == 0) {
//                                    Intent intent = new Intent(NewAddSetMealContentActivity.this, NewAddSetMealAddActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    intent.putExtra(MealCategory.WHERE_FROM, MealCategory.EDIT);
//                                    intent.putExtra(MealCategory.NEW_ADD_MEAL, setMeaContentList.get(position));
//                                    intent.putExtra("categoryId", categoryId);
//                                    startActivity(intent);
//                                }
//                            }
//                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                            }
//                        }).create();
//                alertDialog.show();
                return false;
            }
        });
    }


    private void initViewContent() {
        title_content.setText(R.string.pm_new_add_set_meal_content_title_content);
        title_menu.setText(R.string.pm_business_project_manager_menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_arrow:
                //TODO 暂时将保存放在这里
                saveSelectedSetMealItem();
                //传值
                finish();
                break;
            case R.id.title_menu:
                Intent intent = new Intent(NewAddSetMealContentActivity.this, NewAddSetMealAddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MealCategory.WHERE_FROM, MealCategory.NEW_ADD);
                startActivity(intent);
                break;
        }
    }

    /**
     * 保存
     */
    private void saveSelectedSetMealItem() {
        /**用于存放要传回的被选择的对象*/
        ArrayList<SetMealContentItemBean> list = new ArrayList<>();
        Intent intent = new Intent(NewAddSetMealContentActivity.this, NewAddContentActivity.class);
        for (SetMealContentItemBean bean : setMeaContentList) {
            if (bean.state == MealCategory.Selected) {
                list.add(bean);
            }
        }
        intent.putExtra(MealCategory.Selecteds, list);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getBaseContext(), NewAddSetMealAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MealCategory.WHERE_FROM, MealCategory.EDIT);
        intent.putExtra("bean", setMeaContentList.get(position));//
        startActivity(intent);
    }

    private void initListViewContent() {
        getListDataFromInternet();
        adapter = new SetMealContentListAdapter(getBaseContext(), setMeaContentList);
        set_meal_list_view.setAdapter(adapter);
    }

    /**
     * 联网获取套餐列表
     */
    private void getListDataFromInternet() {
        startMyDialog();
        OkHttpUtils.get().url(MealCategory.SET_MEAL_QUERY_LIST)
                .addHeader("Authorization", caches.get("access_token"))
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
                try {
                    if (code == 0) {
                        JSONArray array = (JSONArray) map.get("data");
                        if (array.length() > 0) {
                            SetMealContentItemBean bean;
                            setMeaContentList.clear();
                            for (int j = 0; j < array.length(); j++) {
                                bean = JSONFormat.parseT(array.get(j).toString(), SetMealContentItemBean.class);
                                bean.state = MealCategory.noSelect;
                                setMeaContentList.add(bean);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                saveSelectedSetMealItem();
                break;
        }
        return false;
    }
}
