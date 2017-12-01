package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.bean.CapTureBean;
import com.yskj.wdh.bean.MealContentBean;
import com.yskj.wdh.bean.SetMealContentItemBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 新增-套餐内容添加
 */
public class NewAddSetMealAddActivity extends BaseActivity implements View.OnClickListener {

    private ImageView left_arrow;
    private TextView title_content, new_add_meal_save, attribute_meal_of_name, attribute_meal_of_price;
    private View new_add_meal_name, new_add_meal_price;
    private EditText attribute_meal_content_of_name, attribute_meal_content_of_price;
    private SetMealContentItemBean bean;
    private int categoryId = MealCategory.DEFAULTERROR;
    private int where;
    private  BusinessInfoBean Businessbean;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int shopid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add);
        Intent intent = getIntent();
        initView();
        initListener();
        initViewContent();
        if (intent.getIntExtra(MealCategory.WHERE_FROM, MealCategory.DEFAULT) == MealCategory.EDIT) {
            bean = (SetMealContentItemBean) intent.getSerializableExtra("bean");
            /**查询填充*/
            //getDataById(bean.getId());
            attribute_meal_content_of_name.setText(bean.getName());
            attribute_meal_content_of_price.setText(bean.getPrice() + "");
        }
        where = intent.getIntExtra(MealCategory.WHERE_FROM, MealCategory.DEFAULT);
    }



    private void initView() {
        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        title_content = (TextView) findViewById(R.id.title_content);
        new_add_meal_save = (TextView) findViewById(R.id.new_add_meal_save);
        /**名称属性相关组件*/
        new_add_meal_name = findViewById(R.id.new_add_meal_name);
        attribute_meal_of_name = (TextView) new_add_meal_name.findViewById(R.id.attribute_meal);
        attribute_meal_content_of_name = (EditText) new_add_meal_name.findViewById(R.id.attribute_meal_content);
        /**价格属性相关组件*/
        new_add_meal_price = findViewById(R.id.new_add_meal_price);
        attribute_meal_of_price = (TextView) new_add_meal_price.findViewById(R.id.attribute_meal);
        attribute_meal_content_of_price = (EditText) new_add_meal_price.findViewById(R.id.attribute_meal_content);
        Businessbean = JSONFormat.parseT(caches.get("business"), BusinessInfoBean.class);
        shopid = Businessbean.getCategoryId();
    }

    private void initListener() {
        left_arrow.setOnClickListener(this);
        new_add_meal_save.setOnClickListener(this);
        attribute_meal_content_of_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d("textchange1", s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("textchange2", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("textchange3", s.toString());

                if (".".equals(s.toString())) {
                    MethodUtils.showToast(NewAddSetMealAddActivity.this, "第一位不能为小数点");
                    attribute_meal_content_of_price.setText("");
                }
            }
        });
    }

    private void initViewContent() {
        title_content.setText(R.string.pm_business_project_manager_menu);
        attribute_meal_of_name.setText(R.string.pm_new_add_set_meal_add_attribute2);
        attribute_meal_of_price.setText(R.string.pm_new_add_set_meal_add_attribute3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_arrow:
                finish();
                break;
            case R.id.new_add_meal_save:
                SetMealContentItemBean bean;
                if (attribute_meal_content_of_name.getText() != null &&
                        attribute_meal_content_of_price.getText() != null &&
                        attribute_meal_content_of_name.getText().toString().trim().length() > 0 &&
                        attribute_meal_content_of_price.getText().toString().trim().length() > 0) {
                    bean = new SetMealContentItemBean();
                    bean.setName(attribute_meal_content_of_name.getText().toString());
                    bean.setPrice(Double.parseDouble(attribute_meal_content_of_price.getText().toString().trim()));
                    bean.state = MealCategory.noSelect;
                    /**保存套餐条目*/
                    saveSetMeal();
                } else {
                    Toast.makeText(NewAddSetMealAddActivity.this, "请输入正确的信息", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    String url;

    private void saveSetMeal() {
        if (where == MealCategory.EDIT) {
            url = MealCategory.SET_MEAL_UPDATA;
//            params.put("categoryId", categoryId);
            OkHttpUtils.post().url(url).addHeader("Authorization", caches.get("access_token"))
                    .addParams("id", bean.getId()+"")
                    .addParams("price", attribute_meal_content_of_price.getText().toString().trim())
                    .addParams("name", attribute_meal_content_of_name.getText().toString().trim())
                    .addParams("shopId",shopid+"")
                    .build().execute(new SaveCallBall());
        } else if (where == MealCategory.NEW_ADD) {
            url = MealCategory.SET_MEAL_NEW_ADD;
            OkHttpUtils.post().url(url).addHeader("Authorization", caches.get("access_token"))
                    .addParams("price", attribute_meal_content_of_price.getText().toString().trim())
                    .addParams("name", attribute_meal_content_of_name.getText().toString().trim())
                    .addParams("shopId",shopid+"")
                    .build().execute(new SaveCallBall());
        }

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        setResult(RESULT_OK, intent);
        finish();
    }
public class SaveCallBall extends Callback<CapTureBean>{

    @Override
    public CapTureBean parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        CapTureBean capTureBean = new Gson().fromJson(s,new TypeToken<CapTureBean>(){}.getType());
        return capTureBean;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        isLogin(e);
    }

    @Override
    public void onResponse(CapTureBean response, int id) {
    if (response.getCode() == 0){
        Intent intent = new Intent(NewAddSetMealAddActivity.this, NewAddSetMealContentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        MethodUtils.showToast(getBaseContext(), getResources().getString(R.string.pm_business_project_manager_remind_tip4));
    }else {
        showToast(Messge.geterr_code(response.getCode()));
    }
    }
}
    private void getDataById(int id) {
        OkHttpUtils.get().url(MealCategory.SET_MEAL_QUERY_BY_ID)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("Id", id+"")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                MealContentBean bean = new Gson().fromJson(response,new TypeToken<MealContentBean>(){}.getType());
                if (bean.getCode() == 0) {
                    //填充
                    attribute_meal_content_of_name.setText(bean.getData().getName());
                    attribute_meal_content_of_price.setText(StringUtils.getStringtodouble(bean.getData().getPrice()));
                } else  {
                    showToast(Messge.geterr_code(bean.getCode()));
                }
            }
        });
    }
}

