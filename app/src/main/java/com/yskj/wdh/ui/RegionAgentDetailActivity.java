package com.yskj.wdh.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.yskj.wdh.AppManager;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ItemRegionAgentAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CityTestEntity;
import com.yskj.wdh.bean.RegionAgentBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.ChangeOtherDialog;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.AppJsonFileReader;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

import static com.yskj.wdh.R.id.et_province;

/**
 * 创建日期 2017/5/26on 16:58.
 * 描述：
 * 作者：姜贺YSKJ-JH
 */

public class RegionAgentDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.image_right)
    ImageView imageRight;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_number)
    EditText etNumber;
    @Bind(et_province)
    TextView etProvince;
    @Bind(R.id.et_city)
    TextView etCity;
    //    @Bind(R.id.et_county)
//    TextView etCounty;
    @Bind(R.id.tv_apply)
    TextView tvApply;
    @Bind(R.id.no_scroll_view)
    NoScrollListView countyListView;
    @Bind(R.id.ll_add)
    LinearLayout llAddCounty;

    private final static String fileName = "city_full.json";

    private ArrayList<CityTestEntity> mArrProvinces = new ArrayList<>();

    private Map<String, List<CityTestEntity.CityBeanX>> mCitisDatasMap = new HashMap<>();
    private Map<String, List<CityTestEntity.CityBeanX.CityBean>> mDistrictDatasMap = new HashMap<>();

    private ArrayList<String> listProvince = new ArrayList<>();
    private ArrayList<String> listCitys = new ArrayList<>();
    private ArrayList<String> listDistricts = new ArrayList<>();
    private ChangeOtherDialog changeOtherDialog;
    Handler dataHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            for (int i = 0; i < mArrProvinces.size(); i++) {
                listProvince.add(mArrProvinces.get(i).name);
                mCitisDatasMap.put(mArrProvinces.get(i).name, mArrProvinces.get(i).city);
            }
        }
    };
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String areaId; //最终区域id;
    private String city;//用于查询地区id

    private ArrayList<String> areaIdList = new ArrayList<>();//最终区域id集合;

    private ArrayList<String> countyList = new ArrayList<>();
    private ItemRegionAgentAdapter agentAdapter;

    boolean isAdded = false;//判断是否添加的是相同城市

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_agent_detail);
        ButterKnife.bind(this);
        initView();
        initCityData();
    }

    private void initCityData() {
        new DataThread().start();
    }

    class DataThread extends Thread {
        @Override
        public void run() {
            String jsonStr = AppJsonFileReader.getJson(context, fileName);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<CityTestEntity>>() {
            }.getType();
            mArrProvinces = gson.fromJson(jsonStr, type);
            dataHandler.sendMessage(dataHandler.obtainMessage());
        }
    }

    private void initView() {
        txtTitle.setText("区域代理申请");
        imgBack.setOnClickListener(this);
        etProvince.setOnClickListener(this);
        etCity.setOnClickListener(this);
        llAddCounty.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        changeOtherDialog = new ChangeOtherDialog(context);
        //先添加一个空值，用于适配器显示
        countyList.add("");
        areaIdList.add("");
        agentAdapter = new ItemRegionAgentAdapter(context, countyList);
        countyListView.setAdapter(agentAdapter);
        countyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (null == listDistricts || 0 == listDistricts.size()) {
                    showToast("请选择城市");
                    return;
                }
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(listDistricts);
                isAdded = false;
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        for (int i = 0; i < countyList.size(); i++) {
                            if (data.equals(countyList.get(i))) {
                                showToast("您已添加过该城市");
                                isAdded = true;
                                return;
                            }
                        }
                        if (!isAdded) {
                            countyList.set(position, data);
                            agentAdapter.notifyDataSetChanged();
                            Iterator<CityTestEntity.CityBeanX.CityBean> it = mDistrictDatasMap.get(city).iterator();
                            while (it.hasNext()) {
                                CityTestEntity.CityBeanX.CityBean bean = it.next();
                                if (data.equals(bean.name)) {
                                    areaId = bean.id + "";
                                    //判断是否是修改县 如果是替换，如果不是添加
                                    areaIdList.set(position,areaId);
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_province:
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(listProvince);
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        etProvince.setText(data);
                        etCity.setText("");
                        areaIdList.clear();
                        countyList.clear();
                        countyList.add("");
                        areaIdList.add("");
                        agentAdapter.notifyDataSetChanged();
                        listCitys.clear();
                        listDistricts.clear();
                        for (int i = 0; i < mCitisDatasMap.get(data).size(); i++) {
                            listCitys.add(mCitisDatasMap.get(data).get(i).name);
                            mDistrictDatasMap.put(mCitisDatasMap.get(data).get(i).name, mCitisDatasMap.get(data).get(i).city);
                        }
                    }
                });
                break;
            case R.id.et_city:
                if (null == listCitys || 0 == listCitys.size()) {
                    showToast("请选择省份");
                    return;
                }
                changeOtherDialog.show();
                changeOtherDialog.setAddressData(listCitys);
                changeOtherDialog.setAddresskListener(new ChangeOtherDialog.OnAddressCListener() {
                    @Override
                    public void onClick(String data) {
                        etCity.setText(data);
                        city = data;
                        areaIdList.clear();
                        countyList.clear();
                        countyList.add("");
                        areaIdList.add("");
                        agentAdapter.notifyDataSetChanged();
                        listDistricts.clear();
                        for (int i = 0; i < mDistrictDatasMap.get(data).size(); i++) {
                            listDistricts.add(mDistrictDatasMap.get(data).get(i).name);
                        }
                    }
                });
                break;
            case R.id.tv_apply:
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    showToast("请填写名字");
                    return;
                }
                if (TextUtils.isEmpty(etNumber.getText().toString())) {
                    showToast("请填写简介");
                    return;
                }
                if (TextUtils.isEmpty(areaId)) {
                    showToast("请选择城市");
                    return;
                }
                StringBuilder areaIdBuilder = new StringBuilder();
//                areaIdBuilder.append("[");
                for (int i = 0; i < areaIdList.size(); i++) {
                    if (!TextUtils.isEmpty(areaIdList.get(i))){
                        areaIdBuilder.append(areaIdList.get(i) + ",");
                    }
                }
                StringBuilder newAearId = null;
                if (null != areaIdBuilder.toString() && areaIdBuilder.toString().contains(",")) {
                    newAearId = new StringBuilder(areaIdBuilder.toString().substring(0, areaIdBuilder.toString().length() - 1));
                }
//                newAearId.append("]");
                OkHttpUtils.post().url(Urls.REGIONAGENT).addHeader(Config.HEADTOKEN, caches.get("access_token"))
                        .addParams("name", etName.getText().toString()).addParams("profile", etNumber.getText().toString()).addParams("areaId", newAearId.toString()).build()
                        .execute(new regionAgentCallBack());
                break;
            case R.id.ll_add:
                countyList.add("");
                areaIdList.add("");
                agentAdapter.notifyDataSetChanged();
                break;
        }
    }

    private class regionAgentCallBack extends Callback<RegionAgentBean> {
        @Override
        public void onBefore(Request request, int id) {
            startMyDialog();
            super.onBefore(request, id);
        }

        @Override
        public void onAfter(int id) {
            stopMyDialog();
            super.onAfter(id);
        }

        @Override
        public RegionAgentBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            RegionAgentBean regionAgentBean = new Gson().fromJson(s, new TypeToken<RegionAgentBean>() {
            }.getType());
            return regionAgentBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("请检查网络");
            isLogin(e);
        }

        @Override
        public void onResponse(RegionAgentBean response, int id) {
            switch (response.code) {
                case 0:
                    showToast("申请成功");
                    AppManager.getInstance().killActivity(RegionAgentActivity.class);
                    finish();
                    break;
                case 935:
                    showToast("申请区域已被申请代理");
                    break;
                case 936:
                    showToast("申请区域必须为同级地区");
                    break;
                case 1016:
                    showToast("申请代理区域过大");
                    break;
                case 1017:
                    showToast("申请的代理区域不在同一地区");
                    break;
                case 1018:
                    showToast("代理申请地区必须包含自己商家地址");
                    break;
            }
        }
    }
}
