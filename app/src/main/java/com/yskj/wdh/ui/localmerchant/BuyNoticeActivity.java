package com.yskj.wdh.ui.localmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.PurchaseNoteBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.MealCategory;

import java.util.ArrayList;
import java.util.List;


public class BuyNoticeActivity extends BaseActivity {

    private View notice_set1, notice_set2, notice_set3, notice_set4, notice_set5, notice_set6, notice_set7, notice_set8;
    private TextView title_content, notice_category_name1, notice_category_name2, notice_category_name3, notice_category_name4, notice_category_name5, notice_category_name6, notice_category_name7, notice_category_name8;
    private ImageView left_arrow, notice_select1, notice_select2, notice_select3, notice_select4, notice_select5, notice_select6, notice_select7, notice_select8;
    private int isSelected1, isSelected2, isSelected3, isSelected4, isSelected5, isSelected6, isSelected7, isSelected8;
    private ArrayList<PurchaseNoteBean> list;
    private LoadingCaches caches = LoadingCaches.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_notice);
        Intent intent = getIntent();
        list = (ArrayList<PurchaseNoteBean>) intent.getSerializableExtra("list");
        initSet();
        initView();
        initListener();
        initViewContent();
    }


    private void initSet() {
        isSelected1 = getSelect("select0");
        isSelected2 = getSelect("select1");
        isSelected3 = getSelect("select2");
        isSelected4 = getSelect("select3");
        isSelected5 = getSelect("select4");
        isSelected6 = getSelect("select5");
        isSelected7 = getSelect("select6");
        isSelected8 = getSelect("select7");
    }

    private void initView() {
        left_arrow = (ImageView) findViewById(R.id.left_arrow);
        title_content = (TextView) findViewById(R.id.title_content);

        notice_set1 = findViewById(R.id.notice_set1);
        notice_category_name1 = (TextView) notice_set1.findViewById(R.id.notice_category_name);
        notice_select1 = (ImageView) notice_set1.findViewById(R.id.notice_select);
        setNotice(isSelected1, notice_select1);

        notice_set2 = findViewById(R.id.notice_set2);
        notice_category_name2 = (TextView) notice_set2.findViewById(R.id.notice_category_name);
        notice_select2 = (ImageView) notice_set2.findViewById(R.id.notice_select);
        setNotice(isSelected2, notice_select2);

        notice_set3 = findViewById(R.id.notice_set3);
        notice_category_name3 = (TextView) notice_set3.findViewById(R.id.notice_category_name);
        notice_select3 = (ImageView) notice_set3.findViewById(R.id.notice_select);
        setNotice(isSelected3, notice_select3);

        notice_set4 = findViewById(R.id.notice_set4);
        notice_category_name4 = (TextView) notice_set4.findViewById(R.id.notice_category_name);
        notice_select4 = (ImageView) notice_set4.findViewById(R.id.notice_select);
        setNotice(isSelected4, notice_select4);

        notice_set5 = findViewById(R.id.notice_set5);
        notice_category_name5 = (TextView) notice_set5.findViewById(R.id.notice_category_name);
        notice_select5 = (ImageView) notice_set5.findViewById(R.id.notice_select);
        setNotice(isSelected5, notice_select5);

        notice_set6 = findViewById(R.id.notice_set6);
        notice_category_name6 = (TextView) notice_set6.findViewById(R.id.notice_category_name);
        notice_select6 = (ImageView) notice_set6.findViewById(R.id.notice_select);
        setNotice(isSelected6, notice_select6);

        notice_set7 = findViewById(R.id.notice_set7);
        notice_category_name7 = (TextView) notice_set7.findViewById(R.id.notice_category_name);
        notice_select7 = (ImageView) notice_set7.findViewById(R.id.notice_select);
        setNotice(isSelected7, notice_select7);

        notice_set8 = findViewById(R.id.notice_set8);
        notice_category_name8 = (TextView) notice_set8.findViewById(R.id.notice_category_name);
        notice_select8 = (ImageView) notice_set8.findViewById(R.id.notice_select);
        setNotice(isSelected8, notice_select8);

    }

    /**
     * 设置每一个按钮的开关
     */
    private void setNotice(int selectState, ImageView view) {
        if (selectState == MealCategory.off) {
            view.setImageResource(R.mipmap.red_set_off);
        } else {
            view.setImageResource(R.mipmap.red_set_on);
        }
    }

    private void initListener() {
        left_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**返回时带回选择结果*/
                Intent intent = new Intent();
                int[] strings = {isSelected1, isSelected2, isSelected3, isSelected4, isSelected5, isSelected6, isSelected7, isSelected8};
                intent.putExtra("select", strings);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        notice_select1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected1) {
                    isSelected1 = MealCategory.on;
                    saveSelect("select0", isSelected1);
                    notice_select1.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected1 = MealCategory.off;
                    saveSelect("select0", isSelected1);
                    notice_select1.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected2) {
                    isSelected2 = MealCategory.on;
                    saveSelect("select1", isSelected2);
                    notice_select2.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected2 = MealCategory.off;
                    saveSelect("select1", isSelected2);
                    notice_select2.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected3) {
                    isSelected3 = MealCategory.on;
                    saveSelect("select2", isSelected3);
                    notice_select3.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected3 = MealCategory.off;
                    saveSelect("select2", isSelected3);
                    notice_select3.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected4) {
                    isSelected4 = MealCategory.on;
                    saveSelect("select3", isSelected4);
                    notice_select4.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected4 = MealCategory.off;
                    saveSelect("select3", isSelected4);
                    notice_select4.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected5) {
                    isSelected5 = MealCategory.on;
                    saveSelect("select4", isSelected5);
                    notice_select5.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected5 = MealCategory.off;
                    saveSelect("select4", isSelected5);
                    notice_select5.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected6) {
                    isSelected6 = MealCategory.on;
                    saveSelect("select5", isSelected6);
                    notice_select6.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected6 = MealCategory.off;
                    saveSelect("select5", isSelected6);
                    notice_select6.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected7) {
                    isSelected7 = MealCategory.on;
                    saveSelect("select6", isSelected7);
                    notice_select7.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected7 = MealCategory.off;
                    saveSelect("select6", isSelected7);
                    notice_select7.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
        notice_select8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MealCategory.off == isSelected8) {
                    isSelected8 = MealCategory.on;
                    saveSelect("select7", isSelected8);
                    notice_select8.setImageResource(R.mipmap.red_set_on);
                } else {
                    isSelected8 = MealCategory.off;
                    saveSelect("select7", isSelected8);
                    notice_select8.setImageResource(R.mipmap.red_set_off);
                }
            }
        });
    }

    private void initViewContent() {
        title_content.setText(R.string.pm_buy_notice_title_content);
        List<TextView> name = new ArrayList<>();
        name.add(notice_category_name1);
        name.add(notice_category_name2);
        name.add(notice_category_name3);
        name.add(notice_category_name4);
        name.add(notice_category_name5);
        name.add(notice_category_name6);
        name.add(notice_category_name7);
        name.add(notice_category_name8);
        String[] strings = {getName(R.string.pm_buy_notice_category_name1), getName(R.string.pm_buy_notice_category_name2),
                getName(R.string.pm_buy_notice_category_name3), getName(R.string.pm_buy_notice_category_name4),
                getName(R.string.pm_buy_notice_category_name5), getName(R.string.pm_buy_notice_category_name6),
                getName(R.string.pm_buy_notice_category_name7), getName(R.string.pm_buy_notice_category_name8),};
        for (int i = 0; i < name.size(); i++) {
            name.get(i).setText(strings[i]);
        }
    }

    private String getName(int res) {
        return getResources().getString(res);
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
        return getSharedPreferences(caches.get("mobile"), MODE_PRIVATE).getInt(name, 1);
    }
}
