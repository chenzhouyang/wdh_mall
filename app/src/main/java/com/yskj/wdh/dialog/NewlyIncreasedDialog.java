package com.yskj.wdh.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yskj.wdh.R;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.ui.MealCategory;
import com.yskj.wdh.ui.localmerchant.NewAddContentActivity;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class NewlyIncreasedDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private TextView direct_shopping,common_shopping,abolish_dialog;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private Intent intent;


    public NewlyIncreasedDialog(Context context) {
        super(context, R.style.ShareDialog);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_newlyincerased);
        abolish_dialog = (TextView) findViewById(R.id.abolish_dialog);
        common_shopping = (TextView) findViewById(R.id.common_shopping);
        direct_shopping = (TextView) findViewById(R.id.direct_shopping);
        if(!caches.get("regionAgent").equals("null")&&caches.get("regionAgent").equals("1")){
            direct_shopping.setVisibility(View.VISIBLE);
        }else {
            direct_shopping.setVisibility(View.GONE);
        }
        abolish_dialog.setOnClickListener(this);
        common_shopping.setOnClickListener(this);
        direct_shopping.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.abolish_dialog:
                dismiss();
                break;
            case R.id.common_shopping:
                //普通商品
                 intent = new Intent(context, NewAddContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MealCategory.WHERE_FROM, MealCategory.NEW_ADD);
                intent.putExtra("shopName", caches.get("shopName"));
                intent.putExtra("shoptype","1");
                context.startActivity(intent);
                break;
            case R.id.direct_shopping:
                //直供商品
                 intent = new Intent(context, NewAddContentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MealCategory.WHERE_FROM, MealCategory.NEW_ADD);
                intent.putExtra("shopName", caches.get("shopName"));
                intent.putExtra("shoptype","2");
                context.startActivity(intent);
                break;
        }
    }
}
