package com.yskj.wdh.ui.providermanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.PostageAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CityTestEntity;
import com.yskj.wdh.bean.DialogCheckBean;
import com.yskj.wdh.bean.EditCityBean;
import com.yskj.wdh.bean.EditExpressSendToJavaJavaBean;
import com.yskj.wdh.bean.FindLogisticsDetailBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.AddAearDialog;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.AppJsonFileReader;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 创建日期 2017/8/2on 17:17.
 * 描述：编辑运费
 * 作者：姜贺YSKJ-JH
 */

public class EditLogisticsActivityActivity extends BaseActivity implements View.OnClickListener, GotAreaInterface, CheckOneInterface, EditExpressCompleteInterface {
    NoScrollListView listView;

    private ArrayList<FindLogisticsDetailBean.DataBean.CustomTransportListBean> arrayList = new ArrayList<>();//运费模板集合
    private ArrayList<EditExpressSendToJavaJavaBean> arrayListToJson = new ArrayList<>();//要发送后台的集合
    private PostageAdapter adapter;
    private ImageView imgBack;
    private TextView txtTitle, tvRight;
    private TextView tvAdd;
    private final static String fileName = "city_full.json";
    private LinearLayout llNoSendArea;
    private TextView tvNoSendArea;
    private ArrayList<CityTestEntity> mArrProvinces = new ArrayList<>();//省份列表
    private Map<Integer, ArrayList<EditCityBean>> mapList = new HashMap<>();//存取选过的城市id

    private EditText etName;

    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            AddAearDialog dialog = new AddAearDialog(context, mArrProvinces);
            dialog.show();
            dialog.setGotAreaInterface(EditLogisticsActivityActivity.this);
        }
    };

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private int clickPosition = -1;//记录选择地区时位置
    private String modeId, type, millId;//type 2 除偏远地区  3 自定义
    private String operaType;//1新增 ， 2更新编辑已有模板

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_logistics);
        initView();
        initData();
    }

    private void initData() {
        adapter = new PostageAdapter(context, arrayList);
        listView.setAdapter(adapter);
        adapter.setCheckOneInterface(this);
        adapter.setEditExpressCompleteInterface(this);
        //1新增 ， 2更新编辑已有模板
        if (operaType.equals("1")) {
            //初始化上传时的数据
            initPostJsonData();
            FindLogisticsDetailBean.DataBean.CustomTransportListBean bean = new FindLogisticsDetailBean.DataBean.CustomTransportListBean();
            bean.customTransportAreaList = new ArrayList<>();
            arrayList.add(bean);
            adapter.notifyDataSetChanged();
        }
        if (operaType.equals("2")) {
            //获取网络数据初始化传值实体
            gotData();
        }
    }

    private void gotData() {
        String json = "{" + "\"id\": " + modeId + ",\"type\": " + type + "}";
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.FINDEXPRESSMODEDETAIL).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new FindExpressModeDetailCallback());
    }

    private class FindExpressModeDetailCallback extends Callback<FindLogisticsDetailBean> {

        @Override
        public FindLogisticsDetailBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.d(s);
            FindLogisticsDetailBean findLogisticsDetailBean = new Gson().fromJson(s, new TypeToken<FindLogisticsDetailBean>() {
            }.getType());
            return findLogisticsDetailBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(FindLogisticsDetailBean response, int id) {
            if (response.code == 0) {
                //存取选过的城市
                ArrayList<EditCityBean> editCityBeanArrayList = new ArrayList<>();
                EditExpressSendToJavaJavaBean editExpressSendToJavaJavaBean = new EditExpressSendToJavaJavaBean();
                //添加不发货地区模板
                editExpressSendToJavaJavaBean.id = response.data.id;
                editExpressSendToJavaJavaBean.millId = response.data.millId;
                editExpressSendToJavaJavaBean.name = response.data.name;
                etName.setText(response.data.name);
                editExpressSendToJavaJavaBean.type = type;
                editExpressSendToJavaJavaBean.operaType = operaType;
                if (response.data.areaUntransportList != null) {
                    /**
                     * 上传数据用的实体封装
                     */
                    editExpressSendToJavaJavaBean.areaUntransportList = new ArrayList<>();
                    StringBuilder areaName = new StringBuilder();
                    for (int i = 0; i < response.data.areaUntransportList.size(); i++) {
                        EditExpressSendToJavaJavaBean.AreaUntransportListBean areaUntransportListBeanItem = new EditExpressSendToJavaJavaBean.AreaUntransportListBean();
                        areaUntransportListBeanItem.areaId = response.data.areaUntransportList.get(i).areaId;
                        areaUntransportListBeanItem.areaName = response.data.areaUntransportList.get(i).areaName;
                        editExpressSendToJavaJavaBean.areaUntransportList.add(areaUntransportListBeanItem);
                        EditCityBean editCityBean = new EditCityBean();
                        editCityBean.areaId = response.data.areaUntransportList.get(i).areaId;
                        editCityBean.areaName = response.data.areaUntransportList.get(i).areaName;
                        areaName.append(response.data.areaUntransportList.get(i).areaName + " ");
                        editCityBeanArrayList.add(editCityBean);
                    }
                    if (TextUtils.isEmpty(areaName.toString())) {
                        tvNoSendArea.setText("选择地区");
                    } else {
                        tvNoSendArea.setText(areaName.toString());
                    }
                    mapList.put(-1, editCityBeanArrayList);
                }
                if (response.data.customTransportList != null) {
                    arrayList.addAll(response.data.customTransportList);
                    adapter.notifyDataSetChanged();
                    /**
                     * 上传数据用的实体封装
                     */
                    editExpressSendToJavaJavaBean.customTransportList = new ArrayList<>();
                    for (int i = 0; i < arrayList.size(); i++) {
                        FindLogisticsDetailBean.DataBean.CustomTransportListBean bean = arrayList.get(i);
                        EditExpressSendToJavaJavaBean.CustomTransportListBean customListBeanItem = new EditExpressSendToJavaJavaBean.CustomTransportListBean();
                        customListBeanItem.addNumber = bean.addNumber;
                        customListBeanItem.addPrice = bean.addPrice;
                        customListBeanItem.price = bean.price;
                        customListBeanItem.number = bean.number;
                        customListBeanItem.id = bean.id;
                        customListBeanItem.customTransportAreaList = new ArrayList<>();
                        for (int n = 0; n < bean.customTransportAreaList.size(); n++) {
                            EditExpressSendToJavaJavaBean.CustomTransportListBean.CustomTransportAreaListBean areaListBean = new EditExpressSendToJavaJavaBean.CustomTransportListBean.CustomTransportAreaListBean();
                            areaListBean.areaId = bean.customTransportAreaList.get(n).areaId;
                            areaListBean.areaName = bean.customTransportAreaList.get(n).areaName;
                            customListBeanItem.customTransportAreaList.add(areaListBean);

                            EditCityBean editCityBean = new EditCityBean();
                            editCityBean.areaId = response.data.customTransportList.get(i).customTransportAreaList.get(n).areaId;
                            editCityBean.areaName = response.data.customTransportList.get(i).customTransportAreaList.get(n).areaName;
                            editCityBeanArrayList.add(editCityBean);
                        }
                        mapList.put(i, editCityBeanArrayList);
                        editExpressSendToJavaJavaBean.customTransportList.add(customListBeanItem);
                    }
                }
                arrayListToJson.add(editExpressSendToJavaJavaBean);
                adapter.notifyDataSetChanged();
            } else {
                showToast(Messge.geterr_code(response.code));
            }
        }
    }

    /**
     * 添加输入框
     */
    private void addOne() {
        //添加一个运费模板
        FindLogisticsDetailBean.DataBean.CustomTransportListBean bean = new FindLogisticsDetailBean.DataBean.CustomTransportListBean();
        bean.customTransportAreaList = new ArrayList<>();
        arrayList.add(bean);
        adapter.notifyDataSetChanged();
        //添加一个要发送到后台的模板
        EditExpressSendToJavaJavaBean.CustomTransportListBean data = new EditExpressSendToJavaJavaBean.CustomTransportListBean();
        data.customTransportAreaList = new ArrayList<>();
        if (arrayListToJson.get(0).customTransportList == null) {
            arrayListToJson.get(0).customTransportList = new ArrayList<>();
        }
        arrayListToJson.get(0).customTransportList.add(data);
    }

    private void initView() {
        token = caches.get("access_token");
        modeId = getIntent().getStringExtra("id");
        millId = getIntent().getStringExtra("millId");
        type = getIntent().getStringExtra("type");
        operaType = getIntent().getStringExtra("operaType");
        etName = (EditText) findViewById(R.id.et_name);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        listView = (NoScrollListView) findViewById(R.id.listView);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvNoSendArea = (TextView) findViewById(R.id.tv_no_send_area);
        llNoSendArea = (LinearLayout) findViewById(R.id.ll_no_send_area);
        llNoSendArea.setOnClickListener(this);
        tvAdd.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
//        type 2 除偏远地区  3 自定义
        if (type.equals("3")) {
            etName.setVisibility(View.VISIBLE);
        } else {
            etName.setVisibility(View.GONE);
        }
    }

    private void initPostJsonData() {
        EditExpressSendToJavaJavaBean customTransportListBean = new EditExpressSendToJavaJavaBean();
        customTransportListBean.id = "";
        customTransportListBean.millId = millId;
        customTransportListBean.name = "自定义地区模板";
        customTransportListBean.type = "3";
        customTransportListBean.operaType = operaType;
        customTransportListBean.customTransportList = new ArrayList<>();
        customTransportListBean.areaUntransportList = new ArrayList<>();
        EditExpressSendToJavaJavaBean.CustomTransportListBean customListBeanItem = new EditExpressSendToJavaJavaBean.CustomTransportListBean();
        customListBeanItem.addNumber = "";
        customListBeanItem.addPrice = "";
        customListBeanItem.price = "";
        customListBeanItem.number = "";
        customListBeanItem.customTransportAreaList = new ArrayList<>();
        customTransportListBean.customTransportList.add(customListBeanItem);
        arrayListToJson.add(customTransportListBean);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent();
                intent.putExtra("data", "");
                setResult(Config.LOGISTICSMODERESULTCODE, intent);
                finish();
                break;
            case R.id.tv_add:
                switch (type) {
                    case "2":
                        showToast("此模板暂不支持哦");
                        break;
                    case "3":
                        addOne();
                        adapter.notifyDataSetChanged();
                        break;
                }
                break;
            case R.id.ll_no_send_area:
                clickPosition = -1;
                initCityData(clickPosition);
                break;
            case R.id.tv_right:
                sendToJavaJson();
                break;
        }
    }

    private void sendToJavaJson() {
        Logger.d(type + "type");
        if (type.equals("3") && !TextUtils.isEmpty(etName.getText().toString())) {
            arrayListToJson.get(0).name = etName.getText().toString();
//            for (EditExpressSendToJavaJavaBean bean : arrayListToJson) {
//                bean.name = etName.getText().toString();
//            }
        }
        Gson gson = new Gson();
        String json = gson.toJson(arrayListToJson);
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.EDITEXPRESS).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                int code = (int) map.get("code");
                String data = (String) map.get("data");
                switch (code) {
                    case 0:
                        finish();
                        break;
                }
                showToast(Messge.geterr_code(code));
            }
        });
    }

    private void initCityData(final int clickPosition) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonStr = AppJsonFileReader.getJson(context, fileName);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<CityTestEntity>>() {
                }.getType();
                mArrProvinces = gson.fromJson(jsonStr, type);
                for (Map.Entry<Integer, ArrayList<EditCityBean>> entity : mapList.entrySet()) {
                    if (entity.getKey() != clickPosition) {
                        for (int i = 0; i < entity.getValue().size(); i++) {
                            for (int j = 0; j < mArrProvinces.size(); j++) {
                                if (entity.getValue().get(i).areaId.equals(mArrProvinces.get(j).id + "")) {
                                    mArrProvinces.remove(mArrProvinces.get(j));
                                }
                            }
                        }
                    }
                }
                dataHandler.sendMessage(dataHandler.obtainMessage());
            }
        }).start();
    }

    @Override
    public void gotAreaData(ArrayList<DialogCheckBean> areaList) {
        //存放选择过的城市id
        ArrayList<EditCityBean> mapListItem = new ArrayList<>();
        //-1为不发货地区
        if (clickPosition == -1) {
            StringBuilder areaName = new StringBuilder();
            arrayListToJson.get(0).areaUntransportList.clear();
            for (DialogCheckBean bean : areaList) {
                if (bean.isChecked) {
                    areaName.append(bean.areaName + " ");
                    EditExpressSendToJavaJavaBean.AreaUntransportListBean areaUntransportListBean = new EditExpressSendToJavaJavaBean.AreaUntransportListBean();
                    areaUntransportListBean.areaId = bean.areaId;
                    areaUntransportListBean.areaName = bean.areaName;
                    arrayListToJson.get(0).areaUntransportList.add(areaUntransportListBean);
                    EditCityBean editCityBean = new EditCityBean();
                    editCityBean.areaId = bean.areaId;
                    editCityBean.areaName = bean.areaName;
                    mapListItem.add(editCityBean);
                }
            }
            mapList.put(clickPosition, mapListItem);
            if (TextUtils.isEmpty(areaName.toString())) {
                tvNoSendArea.setText("选择地区");
            } else {
                tvNoSendArea.setText(areaName.toString());
            }
        } else {
            arrayList.get(clickPosition).customTransportAreaList.clear();
            arrayListToJson.get(0).customTransportList.get(clickPosition).customTransportAreaList.clear();
            for (int i = 0; i < areaList.size(); i++) {
                DialogCheckBean bean = areaList.get(i);
                if (bean.isChecked) {
                    EditExpressSendToJavaJavaBean.CustomTransportListBean.CustomTransportAreaListBean customTransportListBean = new EditExpressSendToJavaJavaBean.CustomTransportListBean.CustomTransportAreaListBean();
                    customTransportListBean.areaId = bean.areaId;
                    customTransportListBean.areaName = bean.areaName;
                    arrayListToJson.get(0).customTransportList.get(clickPosition).customTransportAreaList.add(customTransportListBean);
                    EditCityBean editCityBean = new EditCityBean();
                    editCityBean.areaId = bean.areaId;
                    editCityBean.areaName = bean.areaName;
                    mapListItem.add(editCityBean);

                    FindLogisticsDetailBean.DataBean.CustomTransportListBean.CustomTransportAreaListBean customTransportAreaListBean = new FindLogisticsDetailBean.DataBean.CustomTransportListBean.CustomTransportAreaListBean();
                    customTransportAreaListBean.areaId = bean.areaId;
                    customTransportAreaListBean.areaName = bean.areaName;
                    arrayList.get(clickPosition).customTransportAreaList.add(customTransportAreaListBean);
                }
            }
        }
        mapList.put(clickPosition, mapListItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void checkOne(int position) {
        clickPosition = position;
        initCityData(clickPosition);
    }

    @Override
    public void checkEdit(int position) {

    }

    /**
     * 编辑结束后存值
     *
     * @param tag //AddCount AddPrice  Count  Price
     */
    @Override
    public void editComplete(String tag, int position, String content) {
        EditExpressSendToJavaJavaBean.CustomTransportListBean customTransportListBean;
        //operaType1新增 ， 2更新编辑已有模板
        //type 2 除偏远地区  3 自定义
        customTransportListBean = arrayListToJson.get(0).customTransportList.get(position);
        switch (tag) {
            case "Count":
                arrayList.get(position).number = content;
                customTransportListBean.number = content;
                break;
            case "Price":
                arrayList.get(position).price = content;
                customTransportListBean.price = content;
                break;
            case "AddCount":
                arrayList.get(position).addNumber = content;
                customTransportListBean.addNumber = content;
                break;
            case "AddPrice":
                arrayList.get(position).addPrice = content;
                customTransportListBean.addPrice = content;
                break;
        }
    }

}
