package com.yskj.wdh.callback;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yskj.wdh.adapter.RecordAdapter;
import com.yskj.wdh.entity.RecordEntiity;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by YSKJ-02 on 2017/1/20.
 */

public class RecordCallBack extends Callback<RecordEntiity> {
    private Context context;
    private NoScrollListView depositLv;
    private ArrayList<RecordEntiity.DataBean.ListBean> listBeen;

    public RecordCallBack(Context context, NoScrollListView depositLv){
        super();
        this.context = context;
        this.depositLv = depositLv;
    }
    @Override
    public RecordEntiity parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        RecordEntiity recordEntiity = new Gson().fromJson(s,new TypeToken<RecordEntiity>(){}.getType());

        return recordEntiity;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(RecordEntiity response, int id) {
        if(response.code == 0){
            listBeen = response.data.list;
            RecordAdapter recordAdapter = new RecordAdapter(context,listBeen);
            depositLv.setAdapter(recordAdapter);
        }else {
            String message = Messge.geterr_code(response.code);
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }

    }
}
