package com.yskj.wdh.ui.localmerchant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CapTureBean;
import com.yskj.wdh.bean.PurchaseNoteBean;
import com.yskj.wdh.bean.PurchaseNoteVo;
import com.yskj.wdh.bean.SetMealContentItemBean;
import com.yskj.wdh.bean.SetMealItemDetailBean;
import com.yskj.wdh.bean.ShopImageBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.UserInfoEntity;
import com.yskj.wdh.ui.CropImageActivity;
import com.yskj.wdh.ui.DialogImageActivity;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.StringUtil;
import com.yskj.wdh.util.StringUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 新增-内容  商品上架
 */
public class NewAddContentActivity extends BaseActivity implements View.OnClickListener {

    private Activity activity = NewAddContentActivity.this;
    private ImageView left_arrow, meal_show_image;
    private TextView title_content, title_menu, meal_shop_name, category_show, meal_content_show, validity_time, upload_image_remind;
    private LinearLayout upload_meal_show_image,percentage,describe,shape_code_ll,m_worth_ll;
    private LinearLayout meal_category, meal_content, validity, notice,package_name,repertory_ll;
    private EditText meal_name, meal_team_buy_price,meal_describe,shape_code,repertory,m_worth,meal_team_buy_tag;
    private View notice_one, notice_two, notice_three, notice_four, notice_five, notice_six, notice_seven, notice_eight;
    private TextView item_notice0, item_notice1, item_notice2, item_notice3, item_notice4, item_notice5, item_notice6, item_notice7, item_notice8;
    private File uploadFile;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private Intent intent;
    private int category_id = -1;
    private int category_default = 1;
    private int where;
    private String TIP;
    /**
     * 上传图片返回的图片标示
     */
    private String license,purchaseNote;
    private ArrayList<SetMealContentItemBean> list;
    private SetMealItemDetailBean bean;
    private ArrayList<PurchaseNoteBean> list1 = new ArrayList<>();
    private String noticeJson;
    private StringBuffer meal_ids = new StringBuffer() ;
    private ProgressDialog dialogUp;
    private Bitmap bitMap;
    private Gson gson = new Gson();
    private PopupWindow popupWindow;
    private String ImageName;
    private Spinner sp_profit_date;
    private String date = "0";
    private UserInfoEntity userInfoEntity;
    private int goodsType = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_meal);
        if (!caches.get("userinfo").equals("null")) {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
        }
        initView();
        initListener();
        initViewContent();
        intent = getIntent();
        goodsType = Integer.parseInt(intent.getStringExtra("shoptype"));

        if (!caches.get("userinfo").equals("null")) {
            userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
            }.getType());
        }
        initView();
        if(goodsType == 2){
            notice.setVisibility(View.GONE);
            validity.setVisibility(View.GONE);
            percentage.setVisibility(View.GONE);
            meal_content.setVisibility(View.GONE);
            shape_code_ll.setVisibility(View.VISIBLE);
            repertory_ll.setVisibility(View.VISIBLE);
            m_worth_ll.setVisibility(View.VISIBLE);
        }else {
            notice.setVisibility(View.VISIBLE);
            validity.setVisibility(View.VISIBLE);
            percentage.setVisibility(View.VISIBLE);
            meal_content.setVisibility(View.VISIBLE);
            shape_code_ll.setVisibility(View.GONE);
            repertory_ll.setVisibility(View.GONE);
            m_worth_ll.setVisibility(View.GONE);
        }
        if (intent.getIntExtra(MealCategory.WHERE_FROM, MealCategory.DEFAULT) == MealCategory.EDIT) {
            getSetMealInfo(intent.getIntExtra("life_id", MealCategory.DEFAULT));
            where = MealCategory.EDIT;
        } else {
            where = MealCategory.NEW_ADD;
            meal_shop_name.setText(intent.getStringExtra("shopName"));
            /**初始设置*/
            getInitialSet();
        }
        indate();

    }
    private void indate(){
        //数据
        ArrayList<String> data_list = new ArrayList<String>();
        data_list.add("0");
        data_list.add("10%");
        data_list.add("20%");
        data_list.add("30%");
        data_list.add("40%");
        data_list.add("50%");
        //适配器
        final ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getView(position, convertView, parent));
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return setCentered(super.getDropDownView(position, convertView, parent));
            }

            private View setCentered(View view) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(14);
                textView.setTextColor(getResources().getColor(R.color.black));
                return view;
            }
        };
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_profit_date.setAdapter(arr_adapter);
        sp_profit_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) arr_adapter.getItemId(position)) {
                    case 0:
                        date = "0";
                        break;
                    case 1:
                        date = "10";
                        break;
                    case 2:
                        date = "20";
                        break;
                    case 3:
                        date = "30";
                        break;
                    case 4:
                        date = "40";
                        break;
                    case 5:
                        date = "50";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    /**
     * 给一个默认的所有都打开的设置，防止用户不设置导致提交失败
     */
    private void getInitialSet() {
        List<PurchaseNoteVo> plist = new ArrayList<>();
        PurchaseNoteVo v1 = new PurchaseNoteVo(1000, 1, "", "是否支持极速退款", "是");
        PurchaseNoteVo v2 = new PurchaseNoteVo(1001, 1, "", "是否支持wifi", "是");
        PurchaseNoteVo v3 = new PurchaseNoteVo(1002, 1, "", "是否展示发票", "是");
        PurchaseNoteVo v4 = new PurchaseNoteVo(1003, 1, "", "同时享有优惠", "可以同时享有");
        PurchaseNoteVo v5 = new PurchaseNoteVo(1004, 1, "", "是否预约", "是");
        PurchaseNoteVo v6 = new PurchaseNoteVo(1005, 0, "", "是否限制最高销量（接待量）", "否");
        PurchaseNoteVo v7 = new PurchaseNoteVo(1006, 0, "", "是否限购团购券", "否");
        PurchaseNoteVo v8 = new PurchaseNoteVo(1007, 0, "", "是否限用团购券", "否");
        plist.add(v1);
        plist.add(v2);
        plist.add(v3);
        plist.add(v4);
        plist.add(v5);
        plist.add(v6);
        plist.add(v7);
        plist.add(v8);
        Gson gson = new Gson();
        noticeJson = gson.toJson(plist);
    }

    /**
     * 拉取已有信息并填充
     */
    private void getSetMealInfo(int id) {
        OkHttpUtils.get().url(MealCategory.PM_LIST_ITEM_EDIT)
        .addHeader("Authorization", caches.get("access_token"))
                .addParams("lifeId", id+"")
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
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                    if (code == 0) {
                        bean = new Gson().fromJson(response,new TypeToken<SetMealItemDetailBean>(){}.getType());
                        goodsType = bean.getData().getGoodsType();
                        if(bean.getData().getGoodsType() == 2){
                            notice.setVisibility(View.GONE);
                            validity.setVisibility(View.GONE);
                            percentage.setVisibility(View.GONE);
                            meal_content.setVisibility(View.GONE);
                            shape_code_ll.setVisibility(View.VISIBLE);
                            repertory_ll.setVisibility(View.VISIBLE);
                            m_worth_ll.setVisibility(View.VISIBLE);
                        }else {
                            notice.setVisibility(View.VISIBLE);
                            validity.setVisibility(View.VISIBLE);
                            percentage.setVisibility(View.VISIBLE);
                            meal_content.setVisibility(View.VISIBLE);
                            shape_code_ll.setVisibility(View.GONE);
                            repertory_ll.setVisibility(View.GONE);
                            m_worth_ll.setVisibility(View.GONE);
                        }
                        shape_code.setText(bean.getData().getBarCode()+"");
                        repertory.setText(bean.getData().getGoodsInTrade()+"");
                        m_worth.setText(StringUtils.getStringtodouble(bean.getData().getMAccount()));
                        if(bean.getData().getCoverAndUrl().length()!=0){
                            Glide.with(getBaseContext()).load(bean.getData().getCoverAndUrl()).error(R.mipmap.img_error).into(meal_show_image);
                        }else{
                            meal_show_image.setImageResource(R.mipmap.img_error);
                        }
                        upload_image_remind.setVisibility(View.GONE);
                        license = bean.getData().getCover();
                        meal_show_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        category_show.setText(bean.getData().getLocalLifeCategory().getName());
                        meal_shop_name.setText(bean.getData().getLocalShop().getShopName());
                        meal_name.setText(bean.getData().getName());
                        meal_describe.setText(bean.getData().getProfile());
                        meal_team_buy_price.setText(bean.getData().getTeamBuyPrice() + "");
                        validity_time.setText(bean.getData().getValidityPeriod() + "个月");
                        category_id = bean.getData().getLocalLifeCategory().getId();
                        purchaseNote = bean.getData().getPurchaseNote();
                        switch (bean.getData().getCloudIntPercent()){
                            case 0:
                                sp_profit_date.setSelection(0);
                                break;
                            case 10:
                                sp_profit_date.setSelection(1);
                                break;
                            case 20:
                                sp_profit_date.setSelection(2);
                                break;
                            case 30:
                                sp_profit_date.setSelection(3);
                                break;
                            case 40:
                                sp_profit_date.setSelection(4);
                                break;
                            case 50:
                                sp_profit_date.setSelection(5);
                                break;
                        }
                        noticeJson = purchaseNote;
                        /**套餐内容拉取填充*/
                        List<SetMealItemDetailBean.DataBean.SetMealListBean> setMealList = bean.getData().getSetMealList();
                        StringBuffer buffer = new StringBuffer("");
                        meal_ids = new StringBuffer("");
                        for (int j = 0; j < setMealList.size(); j++) {
                            if (j == setMealList.size()) {
                                meal_ids.append(setMealList.get(j).getId() + "-" + setMealList.get(j).getCount());
                                buffer.append(setMealList.get(j).getName() + "(" + setMealList.get(j).getCount() + ")");
                            } else {
                                meal_ids.append(setMealList.get(j).getId() + "-" + setMealList.get(j).getCount() + ",");
                                buffer.append(setMealList.get(j).getName() + "(" + setMealList.get(j).getCount() + ")" + "  ");
                            }
                        }
                        if (buffer.toString().length() > 30) {
                            meal_content_show.setText(buffer.toString().substring(0, 29) + "...");
                        } else {
                            meal_content_show.setText(buffer.toString());
                        }
                        list1 = new Gson().fromJson(purchaseNote, new TypeToken<ArrayList<PurchaseNoteBean>>() {
                        }.getType());
                        if(list1!=null){
                            for (int j = 0; j < list1.size(); j++) {
                               /* *网络拉取设置存本地*/
                                saveSelect("select" + j, list1.get(j).getType());
                            }
                        }

                    }
            }
        });
    }

    /**
     * 选择状态设置存本地
     */
    private void saveSelect(String name, int state) {
        getSharedPreferences(caches.get("mobile"), MODE_PRIVATE).edit().putInt(name, state).commit();
    }

    /**
     * 从本地读取设置
     */
    private int getSelect(String name) {
        return getSharedPreferences(caches.get("mobile"), MODE_PRIVATE).getInt(name, MealCategory.DEFAULT);
    }


    /**
     * 初始化所有组件
     */
    private void initView() {
        meal_ids.append("");
        /**标题*/
        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        title_content = (TextView) findViewById(R.id.title_content);
        title_menu = (TextView) findViewById(R.id.title_menu);
        package_name = (LinearLayout) findViewById(R.id.package_name);
        describe = (LinearLayout) findViewById(R.id.describe);
        shape_code_ll = (LinearLayout) findViewById(R.id.shape_code_ll);
        repertory_ll = (LinearLayout) findViewById(R.id.repertory_ll);
        m_worth_ll = (LinearLayout) findViewById(R.id.m_worth_ll);
        shape_code = (EditText) findViewById(R.id.shape_code);
        repertory = (EditText) findViewById(R.id.repertory);
        m_worth = (EditText) findViewById(R.id.m_worth);
        meal_team_buy_tag = (EditText) findViewById(R.id.meal_team_buy_tag);
        /**食品详情设置*/
        upload_meal_show_image = (LinearLayout) findViewById(R.id.upload_meal_show_image);
        meal_show_image = (ImageView) findViewById(R.id.meal_show_image);
        upload_image_remind = (TextView) findViewById(R.id.upload_image_remind);
        meal_category = (LinearLayout) findViewById(R.id.meal_category);
        sp_profit_date = (Spinner) findViewById(R.id.sp_profit_date);
        category_show = (TextView) findViewById(R.id.category_show);
        meal_shop_name = (TextView) findViewById(R.id.meal_shop_name);
        meal_name = (EditText) findViewById(R.id.meal_name);
        meal_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = meal_name.getSelectionStart() - 1;
                if (index > 0) {
                    if (StringUtil.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = meal_name.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
        meal_content = (LinearLayout) findViewById(R.id.meal_content);
        meal_content_show = (TextView) findViewById(R.id.meal_content_show);
        meal_team_buy_price = (EditText) findViewById(R.id.meal_team_buy_price);
        meal_team_buy_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = meal_team_buy_price.getSelectionStart() - 1;
                if (index > 0) {
                    if (StringUtil.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = meal_team_buy_price.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
        meal_describe = (EditText) findViewById(R.id.meal_describe);
        meal_describe.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = meal_describe.getSelectionStart() - 1;
                if (index > 0) {
                    if (StringUtil.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = meal_describe.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
        percentage = (LinearLayout) findViewById(R.id.percentage);
        validity = (LinearLayout) findViewById(R.id.validity);
        validity_time = (TextView) findViewById(R.id.validity_time);
        notice = (LinearLayout) findViewById(R.id.notice);
        /**NOTICE-TEXT*/
        notice_one = findViewById(R.id.notice_one);
        item_notice0 = (TextView) notice_one.findViewById(R.id.item_notice);
        notice_two = findViewById(R.id.notice_two);
        item_notice1 = (TextView) notice_two.findViewById(R.id.item_notice);
        notice_three = findViewById(R.id.notice_three);
        item_notice2 = (TextView) notice_three.findViewById(R.id.item_notice);
        notice_four = findViewById(R.id.notice_four);
        item_notice3 = (TextView) notice_four.findViewById(R.id.item_notice);
        notice_five = findViewById(R.id.notice_five);
        item_notice4 = (TextView) notice_five.findViewById(R.id.item_notice);
        notice_six = findViewById(R.id.notice_six);
        item_notice5 = (TextView) notice_six.findViewById(R.id.item_notice);
        notice_seven = findViewById(R.id.notice_seven);
        item_notice6 = (TextView) notice_seven.findViewById(R.id.item_notice);
        notice_eight = findViewById(R.id.notice_eight);
        item_notice7 = (TextView) notice_eight.findViewById(R.id.item_notice);
    }

    /**
     * 为需要监听的组件添加监听
     */
    private void initListener() {
        left_arrow.setOnClickListener(this);
        title_menu.setOnClickListener(this);
        upload_meal_show_image.setOnClickListener(this);
        meal_category.setOnClickListener(this);
        meal_content.setOnClickListener(this);
        validity.setOnClickListener(this);
        notice.setOnClickListener(this);
        /**团购价输入数字限制*/
        meal_team_buy_price.addTextChangedListener(new TextWatcher() {
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
                    MethodUtils.showToast(NewAddContentActivity.this, "第一位不能为小数点");
                    meal_team_buy_price.setText("");
                }
            }
        });
    }

    /**
     * 统一为组件设置显示内容
     */
    private void initViewContent() {
        title_content.setText(R.string.pm_new_add_content_title_content);
        title_menu.setText(R.string.pm_new_add_content_title_menu);
        item_notice0.setText(R.string.pm_buy_notice_category_text0);
        item_notice1.setText(R.string.pm_buy_notice_category_text1);
        item_notice2.setText(R.string.pm_buy_notice_category_text2);
        item_notice3.setText(R.string.pm_buy_notice_category_text3);
        item_notice4.setText(R.string.pm_buy_notice_category_text4);
        item_notice5.setText(R.string.pm_buy_notice_category_text5);
        item_notice6.setText(R.string.pm_buy_notice_category_text6);
        item_notice7.setText(R.string.pm_buy_notice_category_text7);
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
                if (judge()) {
                    if(meal_team_buy_tag.getText().toString().length()>0){
                        saveSetMealTag();
                    }else {
                        saveSetMeal();
                    }

                } else {
                    showToast(TIP);
                }
                break;
            case R.id.upload_meal_show_image:
                dialogShow(v);
                break;
            case R.id.meal_category:
                Intent intent1 = new Intent(NewAddContentActivity.this, MealCategorySelectActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("goodstype",goodsType+"");
                startActivityForResult(intent1, MealCategory.TO_CATEGORY_SELECT);
                break;
            case R.id.meal_content:
                Intent intent2 = new Intent(NewAddContentActivity.this, NewAddSetMealContentActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra(MealCategory.WHERE_FROM, where);
                startActivityForResult(intent2, MealCategory.TO_SET_MEAL_SELECT);
                break;
            case R.id.validity:
                Dialog alertDialog2 = new AlertDialog.Builder(NewAddContentActivity.this, R.style.add_dialog).
                        setTitle(R.string.pm_new_add_content_item7)
                        .setItems(MealCategory.arrayvalidity, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                validity_time.setText(MealCategory.arrayvalidity[which]+"个月");
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                alertDialog2.show();
                break;
            case R.id.notice:
                Intent intent3 = new Intent(NewAddContentActivity.this, BuyNoticeActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent3, MealCategory.TO_NOTICE_SELECT);
                break;
        }
    }

    private boolean judge() {
        if(goodsType == 1){
            if (license == null) {
                TIP = MealCategory.tip1;
                return false;
            } else if (category_id < 0) {
                TIP = MealCategory.tip2;
                return false;
            } else if (meal_name.getText().toString().trim().length() < 1) {
                TIP = MealCategory.tip3;
                return false;
            } else if (meal_team_buy_price.getText().toString().trim().length() < 1) {
                TIP = MealCategory.tip5;
                return false;
            } else  if (meal_name.getText().toString().trim().length() < 1) {
                TIP = MealCategory.tip8;
                return false;
            }else{
                return true;
            }
        }else {
            if(license==null||license.length()==0){
                showToast("请上传图片");
                return false;
            }else if(category_id<0){
                showToast("请选择品类");
                return false;
            }else if(meal_team_buy_price.length() == 0){
                showToast("请添加直供商品价格");
                return false;
            }else if(meal_describe.length() == 0){
                showToast("请添加直供商品描述");
                return false;
            }else if(meal_name.length() == 0){
                showToast("请添加直供商品套餐名");
                return false;
            }else if(shape_code.length() == 0){
                showToast("请添加直供商品条形码");
                return false;
            }else if(repertory.length() == 0){
                showToast("请添加直供商品库存");
                return false;
            }else if(m_worth.length() == 0){
                showToast("请添加直供产品分润");
                return false;
            }else if((Double.parseDouble(meal_team_buy_price.getText().toString())/2)>Double.parseDouble(m_worth.getText().toString())){
                showToast("产品分润不能低于价格的50%");
                return false;
            }else {
                return true;
            }
        }

    }

    /**
     * 上传没有标签的数据
     */
    private void saveSetMeal() {
        //TODO 保存项目
       // RequestParams params = new RequestParams();
        if (where == MealCategory.EDIT) {
           // params.put("life_id", bean.getId());//life_id: ID
            if(goodsType == 1){
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        .addHeader("Authorization", caches.get("access_token"))
                        .addParams("lifeId", bean.getData().getId()+"")
                        .addParams("goodsType",goodsType+"")
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("cloudIntPercent", date)
                        .addParams("mealIds", meal_ids+"")
                        .addParams("validityPeriod", Integer.parseInt(validity_time.getText().toString().trim().replace("个月",""))+"")
                        .addParams("consumeStartTime", "0")
                        .addParams("consumeEndTime", "23")
                        .addParams("purchaseNote", noticeJson)
                        .addParams("saveAndOnline", "0")
                        .build().execute(new SaveCallBack());
            }else {
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("lifeId", bean.getData().getId()+"")
                        .addParams("goodsType",goodsType+"")
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("saveAndOnline", "0")
                        .addParams("barCode",shape_code.getText().toString())
                        .addParams("goodsInTrade",repertory.getText().toString())
                        .addParams("mAccount",m_worth.getText().toString())
                        .build().execute(new SaveCallBack());
            }

        }else {
            if(goodsType == 1){
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("goodsType",goodsType+"")
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("cloudIntPercent", date)
                        .addParams("mealIds", meal_ids+"")
                        .addParams("validityPeriod", Integer.parseInt(validity_time.getText().toString().trim().replace("个月",""))+"")
                        .addParams("consumeStartTime", "0")
                        .addParams("consumeEndTime", "23")
                        .addParams("purchaseNote", noticeJson)
                        .addParams("saveAndOnline", "0")
                        .build().execute(new SaveCallBack());
            }else {
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("goodsType",goodsType+"")
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("saveAndOnline", "0")
                        .addParams("barCode",shape_code.getText().toString())
                        .addParams("goodsInTrade",repertory.getText().toString())
                        .addParams("mAccount",m_worth.getText().toString())
                        .build().execute(new SaveCallBack());
            }

        }
        startMyDialog();

    }

    /**
     * 上传含有标签的数据
     */
    private void saveSetMealTag() {
        //TODO 保存项目
        // RequestParams params = new RequestParams();
        if (where == MealCategory.EDIT) {
            // params.put("life_id", bean.getId());//life_id: ID
            if(goodsType == 1){
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        .addHeader("Authorization", caches.get("access_token"))
                        .addParams("lifeId", bean.getData().getId()+"")
                        .addParams("goodsType",goodsType+"")
                        .addParams("tag",meal_team_buy_tag.getText().toString())
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("cloudIntPercent", date)
                        .addParams("mealIds", meal_ids+"")
                        .addParams("validityPeriod", Integer.parseInt(validity_time.getText().toString().trim().replace("个月",""))+"")
                        .addParams("consumeStartTime", "0")
                        .addParams("consumeEndTime", "23")
                        .addParams("purchaseNote", noticeJson)
                        .addParams("saveAndOnline", "0")
                        .build().execute(new SaveCallBack());
            }else {
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("lifeId", bean.getData().getId()+"")
                        .addParams("goodsType",goodsType+"")
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("tag",meal_team_buy_tag.getText().toString())
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("saveAndOnline", "0")
                        .addParams("barCode",shape_code.getText().toString())
                        .addParams("goodsInTrade",repertory.getText().toString())
                        .addParams("mAccount",m_worth.getText().toString())
                        .build().execute(new SaveCallBack());
            }

        }else {
            if(goodsType == 1){
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("goodsType",goodsType+"")
                        .addParams("tag",meal_team_buy_tag.getText().toString())
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("cloudIntPercent", date)
                        .addParams("mealIds", meal_ids+"")
                        .addParams("validityPeriod", Integer.parseInt(validity_time.getText().toString().trim().replace("个月",""))+"")
                        .addParams("consumeStartTime", "0")
                        .addParams("consumeEndTime", "23")
                        .addParams("purchaseNote", noticeJson)
                        .addParams("saveAndOnline", "0")
                        .build().execute(new SaveCallBack());
            }else {
                OkHttpUtils.post().url(MealCategory.PM_NEW_ADD_ITEM)
                        . addHeader("Authorization", caches.get("access_token"))
                        .addParams("name", meal_name.getText().toString().trim())
                        .addParams("cover", license)
                        .addParams("tag",meal_team_buy_tag.getText().toString())
                        .addParams("goodsType",goodsType+"")
                        .addParams("profile", meal_describe.getText().toString().trim())
                        .addParams("categoryId", category_id+"")
                        .addParams("teamBuyPrice", Double.parseDouble(meal_team_buy_price.getText().toString().trim())+"")
                        .addParams("saveAndOnline", "0")
                        .addParams("barCode",shape_code.getText().toString())
                        .addParams("goodsInTrade",repertory.getText().toString())
                        .addParams("mAccount",m_worth.getText().toString())
                        .build().execute(new SaveCallBack());
            }

        }
        startMyDialog();

    }
    public class SaveCallBack extends Callback<CapTureBean>{
    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        stopMyDialog();
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        stopMyDialog();
    }

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
        if(response.getCode() == 0){
            MethodUtils.showToast(getBaseContext(), getResources().getString(R.string.pm_new_add_content_save_remind));
            Intent intent = new Intent(NewAddContentActivity.this, BusinessProjectManagerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }else {
            showToast(Messge.geterr_code(response.getCode()));
        }
    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!=RESULT_OK){
            return;
        }
        switch (requestCode) {
            /**选择品类  结果*/
            case MealCategory.TO_CATEGORY_SELECT:
                category_id = data.getIntExtra(MealCategory.CATEGORY_ID, category_default);
                category_show.setText(data.getStringExtra(MealCategory.CATEGORY_NAME));
                if(goodsType ==2){
                    notice.setVisibility(View.GONE);
                    validity.setVisibility(View.GONE);
                    percentage.setVisibility(View.GONE);
                    meal_content.setVisibility(View.GONE);
                    shape_code_ll.setVisibility(View.VISIBLE);
                    repertory_ll.setVisibility(View.VISIBLE);
                    m_worth_ll.setVisibility(View.VISIBLE);
                }else {
                    notice.setVisibility(View.VISIBLE);
                    validity.setVisibility(View.VISIBLE);
                    percentage.setVisibility(View.VISIBLE);
                    meal_content.setVisibility(View.VISIBLE);
                    shape_code_ll.setVisibility(View.GONE);
                    repertory_ll.setVisibility(View.GONE);
                    m_worth_ll.setVisibility(View.GONE);
                }

                break;
            /**选择套餐组合  结果*/
            case MealCategory.TO_SET_MEAL_SELECT:
                list = (ArrayList<SetMealContentItemBean>) data.getSerializableExtra(MealCategory.Selecteds);
                StringBuffer buffer = new StringBuffer("");
                meal_ids = new StringBuffer("");
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size()) {
                        meal_ids.append(list.get(i).getId() + "-" + list.get(i).num);
                        buffer.append(list.get(i).getName() + "(" + list.get(i).num + ")");
                    } else {
                        meal_ids.append(list.get(i).getId() + "-" + list.get(i).num + ",");
                        buffer.append(list.get(i).getName() + "(" + list.get(i).num + ")" + "  ");
                    }
                }
                if (buffer.toString().length() > 30) {
                    meal_content_show.setText(buffer.toString().substring(0, 29) + "...");
                } else {
                    meal_content_show.setText(buffer.toString());
                }
                break;
            /**选择购买须知  结果*/
            case MealCategory.TO_NOTICE_SELECT:
                List<PurchaseNoteVo> plist = new ArrayList<>();
                PurchaseNoteVo v1 = new PurchaseNoteVo(1000, (getSelect("select0")), "", "是否支持极速退款", ((getSelect("select0") == 0 ? "否" : "是")));
                PurchaseNoteVo v2 = new PurchaseNoteVo(1001, (getSelect("select1")), "", "是否支持wifi", ((getSelect("select0") == 0 ? "否" : "是")));
                PurchaseNoteVo v3 = new PurchaseNoteVo(1002, (getSelect("select2")), "", "是否展示发票", ((getSelect("select0") == 0 ? "否" : "是")));
                PurchaseNoteVo v4 = new PurchaseNoteVo(1003, (getSelect("select3")), "", "同时享有优惠", ((getSelect("select0") == 0 ? "否" : "可以同时享有")));
                PurchaseNoteVo v5 = new PurchaseNoteVo(1004, (getSelect("select4")), "", "是否预约", ((getSelect("select0") == 0 ? "否" : "是")));
                PurchaseNoteVo v6 = new PurchaseNoteVo(1005, 0, "", "是否限制最高销量（接待量）", (0 == 0 ? "否" : "是"));
                PurchaseNoteVo v7 = new PurchaseNoteVo(1006, 0, "", "是否限购团购券", (0 == 0 ? "否" : "是，每人限购#{val}张"));
                PurchaseNoteVo v8 = new PurchaseNoteVo(1007, 0, "", "是否限用团购券", (0 == 0 ? "否" : "是，每人限用#{val}张"));
                plist.add(v1);
                plist.add(v2);
                plist.add(v3);
                plist.add(v4);
                plist.add(v5);
                plist.add(v6);
                plist.add(v7);
                plist.add(v8);
                Gson gson = new Gson();
                noticeJson = gson.toJson(plist);

                /**创建一个要上传的json字符串用于上传*/
                break;
            case MealCategory.TO_PHOTO:
                if (data == null) {
                    uploadFile = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                    Uri uri = Uri.fromFile(uploadFile);
                    startImageZoom(uri);
                    //startCropImage(uri.toString());
                    return;
                } else {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        Bitmap bm = extras.getParcelable("data");
                        Uri uri = saveBitmap(bm);

                    }
                }
                uploadImage(uploadFile);
                break;
            case 5:
                if (data == null) {
                    return;
                }
                startCropImage(data.getStringExtra("path"));
                break;
            case MealCategory.TO_TAILOE:
                if (data == null) {
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                }
                Bitmap bm = extras.getParcelable("data");
                meal_show_image.setImageBitmap(bm);
                saveBitmap(bm);
                File f = new File(Environment.getExternalStorageDirectory() + "", "share.png");
                if (f.exists()) {
                    f.delete();
                }
                if (bm == null) {
                    return;
                }
                try {
                    FileOutputStream out = new FileOutputStream(f);
                    bm.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadImage(f);
                break;
        }
    }
    /**选择图片来源*/
    private void dialogShow(View view) {
        getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }
    /***
     * 获取图片PopupWindow实例
     */
    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 图片PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        // 点击其他地方消失
    }
    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory()
                , ImageName)));
        startActivityForResult(intent, MealCategory.TO_PHOTO);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }
    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        Intent intent = new Intent(this, DialogImageActivity.class);
        intent.setType("image/*");
        startActivityForResult(intent, 5);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }
    /**
     * 相册裁剪
     * @param photoPath
     */
    public void startCropImage(String photoPath) {
        Intent intent = new Intent(context, CropImageActivity.class);
        intent.putExtra(CropImageActivity.IMAGE_PATH, photoPath);
        intent.putExtra(CropImageActivity.SCALE, true);
        intent.putExtra(CropImageActivity.ASPECT_X, 5);
        intent.putExtra(CropImageActivity.ASPECT_Y, 2);
        startActivityForResult(intent, MealCategory.TO_TAILOE);
    }

    /**
     * 拍照裁剪
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 210);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, MealCategory.TO_TAILOE);
    }

        /**
         * 压缩图片（质量压缩）
         * @param bitmap
         */
        public static Uri saveBitmap(Bitmap bitmap) {
            File tmpDir = new File(Environment.getExternalStorageDirectory() + "/lsk/");
            if (!tmpDir.exists()) {
                tmpDir.mkdirs();
            }
            File img = new File(tmpDir.getAbsolutePath() + "share.png");
            try {
                FileOutputStream fos = new FileOutputStream(img);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
                return Uri.fromFile(img);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri geturi(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = this.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
    /**
     * 上传图片
     */
    private void uploadImage(File updateimage) {
        dialogUp = new ProgressDialog(NewAddContentActivity.this);
        dialogUp.setCanceledOnTouchOutside(false);
        dialogUp.setTitle("上传");
        dialogUp.setMessage("玩命上传中，请稍等...");
        dialogUp.show();
        OkHttpUtils.post().url(MealCategory.UPLOAD_IMAGE)
         .addParams("userId", userInfoEntity.data.userVo.id + "")
        .addFile("files",System.currentTimeMillis() + ".jpg",updateimage)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialogUp.dismiss();
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                dialogUp.dismiss();
                ShopImageBean bean = gson.fromJson(response, ShopImageBean.class);
                int error_code = bean.getCode();
                if (error_code == 0) {
                    license = bean.getData().get(0);
                    String url =  Ips.API_URL + "/api/v1/mgs/file/download?fid=" + license;
                    Glide.with(getBaseContext()).load(url).error(R.mipmap.img_error).into(meal_show_image);
                    MethodUtils.showToast(NewAddContentActivity.this, getResources().getString(R.string.pm_new_add_content_upload_image_success));
                    dialogUp.dismiss();
                }
            }
        });
    }
}
