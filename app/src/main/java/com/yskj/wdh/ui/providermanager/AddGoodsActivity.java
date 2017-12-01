package com.yskj.wdh.ui.providermanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.ProviderGoodsAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.AddGoodsKindsBean;
import com.yskj.wdh.bean.GotAddGoodsInfoBean;
import com.yskj.wdh.bean.PostAddGoodsToJava;
import com.yskj.wdh.bean.ProviderGoodsInfoBean;
import com.yskj.wdh.bean.ProviderGoodsPropertyBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.AddGoodsKindsDialog;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.widget.NoScrollListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * 创建日期 2017/8/1on 9:38.
 * 描述：添加商品界面
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsActivity extends BaseActivity implements View.OnClickListener,EditExpressCompleteInterface,AddKindsInterface{
    ImageView imgBack;
    LinearLayout llImgAdd;
    TextView txtTitle;
    TextView tvRight,tvKinds,tvLogistics;
    NoScrollListView listView;
    TextView tvAdd;
    LinearLayout llGoodsLogistics,llGoodsDetail;
    ConvenientBanner convenientBanner;
    EditText etName,etDes;
    FrameLayout flCb;
    ImageView imgAdd;

    private ArrayList<ProviderGoodsPropertyBean> arrayList = new ArrayList<>();
    private ProviderGoodsAdapter adapter;
    private ArrayList image = new ArrayList();//本地轮播集合
    private ArrayList<String> imageList = new ArrayList<>();//获取网络轮播

    private String imageIds;//轮播返回值
    private String contentIds;//商品详情返回值
    private String goodsCategoryId;//分类返回值
    private String transportId ;//运费返回值

    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private LinearLayout llGoodsKinds;
    private ArrayList<AddGoodsKindsBean.DataBean> addGoodsKindsList;
    private String goodsId,millId;
    private int operaType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        initView();
    }

    private void gotGoodsDetail() {
        String json = "{" +"\"millId\": "+millId +",\"goodId\": " +goodsId+ "}";
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.GOTGOODSDETAIL).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new gotGoodsDetailCallBack());
    }

    /**
     * 获取商品详情
     */
    private class gotGoodsDetailCallBack extends Callback<GotAddGoodsInfoBean>{

        @Override
        public GotAddGoodsInfoBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            Logger.d(s);
            GotAddGoodsInfoBean gotAddGoodsInfoBean = new Gson().fromJson(s,new TypeToken<GotAddGoodsInfoBean>(){}.getType());
            return gotAddGoodsInfoBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(GotAddGoodsInfoBean response, int id) {
            if (response.code==0){
                GotAddGoodsInfoBean.DataBean data = response.data;
                etName.setText(data.goodName==null?"": data.goodName);
                etDes.setText(data.keyWord==null?"": data.keyWord);
                tvKinds.setText(data.categoryName==null?"未分类":data.categoryName);
                tvLogistics.setText(data.transportName==null?"":data.transportName);
                for (GotAddGoodsInfoBean.DataBean.ParameterListBean bean : data.parameterList) {
                    ProviderGoodsPropertyBean providerGoodsPropertyBean = new ProviderGoodsPropertyBean();
                    providerGoodsPropertyBean.setSpecification(bean.name);
                    providerGoodsPropertyBean.setPrice(bean.price+"");
                    providerGoodsPropertyBean.setNumber(bean.stock+"");
                    providerGoodsPropertyBean.setmValue(bean.mAccount+"");
                    arrayList.add(providerGoodsPropertyBean);
                }
                initData();
                goodsCategoryId = data.goodsCategoryId;
                StringBuilder contentIdsbuilder = new StringBuilder();
                PublicWay.goodsContentList.clear();
                for (GotAddGoodsInfoBean.DataBean.GoodContentListBean bean : data.goodContentList) {
                    contentIdsbuilder.append(bean.id+",");
                    final ProviderGoodsInfoBean providerGoodsInfoBean = new ProviderGoodsInfoBean();
                    providerGoodsInfoBean.setContent(bean.content);
                    providerGoodsInfoBean.setType(bean.type);
                    providerGoodsInfoBean.setData(bean.id+"");
                    if (bean.type==2){
                       Glide.with(context).load(bean.content).asBitmap().into(new SimpleTarget<Bitmap>() {
                           @Override
                           public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                               providerGoodsInfoBean.setBitmap(resource);
                           }
                       });
                    }
                    PublicWay.goodsContentList.add(providerGoodsInfoBean);
                }
                contentIds = contentIdsbuilder.toString();
                transportId = data.transportId;
                StringBuilder imageIdsbuilder = new StringBuilder();
                for (GotAddGoodsInfoBean.DataBean.GoodImageListBean bean : data.goodImageList) {
                    imageIdsbuilder.append(bean.id+",");
                    imageList.add(bean.thumbnail);
                    initHTTPCB();
                    flCb.setVisibility(View.VISIBLE);
                    llImgAdd.setVisibility(View.GONE);
                    convenientBanner.startTurning(3000);
                }
                imageIds = imageIdsbuilder.toString();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (convenientBanner.isTurning()){
            convenientBanner.stopTurning();
        }
    }

    private void initData() {
        adapter = new ProviderGoodsAdapter(context,arrayList);
        listView.setAdapter(adapter);
        adapter.setEditExpressCompleteInterface(this);
    }
    /**
     * 添加空白商品型号
     */
    private void addProviderGoods() {
        ProviderGoodsPropertyBean providerGoodsPropertyBean = new ProviderGoodsPropertyBean();
        providerGoodsPropertyBean.setSpecification("");
        providerGoodsPropertyBean.setPrice("");
        providerGoodsPropertyBean.setNumber("");
        providerGoodsPropertyBean.setmValue("");
        arrayList.add(providerGoodsPropertyBean);
    }

    private void initView() {
        goodsId = getIntent().getStringExtra("goodsId");
        millId = getIntent().getStringExtra("millId");
        operaType = getIntent().getIntExtra("operaType",1);


        imgBack = (ImageView) findViewById(R.id.img_back);
        flCb = (FrameLayout) findViewById(R.id.fl_cb);
        imgAdd = (ImageView) findViewById(R.id.img_add);
        tvLogistics = (TextView) findViewById(R.id.tv_logistics);
        convenientBanner = (ConvenientBanner) findViewById(R.id.convenientBanner);
        llImgAdd = (LinearLayout) findViewById(R.id.ll_add);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        etName = (EditText) findViewById(R.id.et_name);
        etDes = (EditText) findViewById(R.id.et_des);
        listView = (NoScrollListView) findViewById(R.id.listView);
        llGoodsLogistics = (LinearLayout) findViewById(R.id.ll_goods_logistics);
        llGoodsDetail = (LinearLayout) findViewById(R.id.ll_goods_detail);
        llGoodsKinds =(LinearLayout)  findViewById(R.id.ll_goods_kinds);
        tvAdd = (TextView) findViewById(R.id.tv_add);
        tvKinds = (TextView) findViewById(R.id.tv_kinds);
        tvAdd.setOnClickListener(this);
        llGoodsLogistics.setOnClickListener(this);
        llGoodsDetail.setOnClickListener(this);
        llGoodsKinds.setOnClickListener(this);
        llImgAdd.setOnClickListener(this);
        txtTitle.setText("添加商品");
        imgBack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        token = caches.get("access_token");
        if (operaType==2){
            gotGoodsDetail();
        }else {
            addProviderGoods();
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_add:
                PublicWay.goodsContentList.clear();
                addProviderGoods();
                adapter.notifyDataSetChanged();
                break;
            case R.id.ll_goods_kinds:
                gotGoodsCategory();
                break;
            //运费模板
            case R.id.ll_goods_logistics:
                intent = new Intent(context,LogisticsModeActivity.class);
                intent.putExtra("millId",millId);
                startActivityForResult(intent, Config.LOGISTICSMODERESULTCODE);
                break;
            //添加商品详情
            case R.id.ll_goods_detail:
                intent = new Intent(context,AddGoodsInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("goodsId",goodsId);
                intent.putExtra("bundle",bundle);
                startActivityForResult(intent,Config.ADDGOODSINFORESULTCODE);
                break;
            //添加轮播图
            case R.id.ll_add:
                intent = new Intent(context,AddGoodsImgActivity.class);
                startActivityForResult(intent,Config.ADDGOODSIMGRESULTCODE);
                break;
            case R.id.img_add:
                intent = new Intent(context,AddGoodsImgActivity.class);
                startActivityForResult(intent,Config.ADDGOODSIMGRESULTCODE);
                break;
            case R.id.tv_right:
                sendToJavaJson();
                break;
        }
    }

    private void gotGoodsCategory() {
        OkHttpUtils.get().url(Urls.GETGOODSCATE).build().execute(new GoodsCategoryCallBack());
    }
    @Override
    public void addKinds(int firstPos, int selectPos) {
        goodsCategoryId = addGoodsKindsList.get(firstPos).children.get(selectPos).cateId;
        tvKinds.setText(addGoodsKindsList.get(firstPos).children.get(selectPos).name);
    }

    private class GoodsCategoryCallBack extends Callback<AddGoodsKindsBean>{

        @Override
        public AddGoodsKindsBean parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            AddGoodsKindsBean addGoodsKindsBean = new Gson().fromJson(s, new TypeToken<AddGoodsKindsBean>() {}.getType());
            return addGoodsKindsBean;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            isLogin(e);
        }

        @Override
        public void onResponse(AddGoodsKindsBean response, int id) {
            if (response.code==0){
                addGoodsKindsList = response.data;
                AddGoodsKindsDialog dialog = new AddGoodsKindsDialog(context, addGoodsKindsList);
                dialog.show();
                dialog.setAddKindsInterface(AddGoodsActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        String content = data.getStringExtra("data");
        // 根据上面发送过去的请求别
        switch (requestCode) {
            //轮播
            case Config.ADDGOODSIMGRESULTCODE:
                imageIds = content;
                if ( Bimp.tempSelectBitmap.size()!=0){
                    image.clear();
                    for (ImageItem imgBean: Bimp.tempSelectBitmap){
                        File file = new File(imgBean.getImagePath());
                        image.add(file);
                    }
                    initCB();
                    flCb.setVisibility(View.VISIBLE);
                    llImgAdd.setVisibility(View.GONE);
                    convenientBanner.startTurning(3000);
                }
                Logger.d("imageIds"+imageIds);
                break;
            //详情
            case Config.ADDGOODSINFORESULTCODE:
                contentIds = content;
                break;
            //运费
            case Config.LOGISTICSMODERESULTCODE:
                if (content.contains("-")){
                    String[] strings  = content.split("-");
                    transportId = strings[0];
                    tvLogistics.setText(strings[1]);
                }
                break;
            default:
                break;
        }
    }

    //向后台提交数据
    private void sendToJavaJson() {
        PostAddGoodsToJava postAddGoodsToJava = new PostAddGoodsToJava();
        postAddGoodsToJava.prameterList = new ArrayList<>();
        postAddGoodsToJava.contentIds=contentIds;//详情
        postAddGoodsToJava.imageIds = imageIds;//轮播
        postAddGoodsToJava.name = etName.getText().toString();
        postAddGoodsToJava.profile = etDes.getText().toString();
        postAddGoodsToJava.keyWord = etDes.getText().toString();
        postAddGoodsToJava.goodsCategoryId = goodsCategoryId;//分类
        postAddGoodsToJava.operaType = operaType;//操作类型：1新增 ，2对现有商品进行编辑更新
        postAddGoodsToJava.transportId = transportId;//运费
        postAddGoodsToJava.millId = millId;
        postAddGoodsToJava.goodId = goodsId;
        for (int i = 0; i < arrayList.size(); i++) {
            PostAddGoodsToJava.PrameterListBean prameterBean = new PostAddGoodsToJava.PrameterListBean();
            ProviderGoodsPropertyBean providerGoodsPropertyBean = arrayList.get(i);
            prameterBean.name = providerGoodsPropertyBean.getSpecification();
            prameterBean.price = providerGoodsPropertyBean.getPrice();
            prameterBean.stock = providerGoodsPropertyBean.getNumber();
            prameterBean.mAccount = providerGoodsPropertyBean.getmValue();
            prameterBean.orders = i;
            postAddGoodsToJava.prameterList.add(prameterBean);
        }
        Gson gson = new Gson();
        String json = gson.toJson(postAddGoodsToJava);
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.PROVIDERGOODS).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                isLogin(e);
            }

            @Override
            public void onResponse(String response, int id) {
                Map<String, Object> map = JSONFormat.jsonToMap(response);
                if(map!=null){
                    int code = (int) map.get("code");
                    String message= (String) map.get("message");
                    if (code==0){
                        showToast(Messge.geterr_code(code));
                        finish();
                    }else if(message!=null){
                        showToast(message);
                    }
                }

            }
        });
    }

    /**
     * 添加编辑完成后回掉
     * @param tag
     * @param position
     * @param content
     */
    @Override
    public void editComplete(String tag, int position, String content) {
        switch (tag) {
            case "etName":
                try {
                    arrayList.get(position).setSpecification(content);
                }catch (IndexOutOfBoundsException e){
                    e.getStackTrace();
                }
                break;
            case "etPrice":
                try {
                    arrayList.get(position).setPrice(content);
                }catch (IndexOutOfBoundsException e){
                    e.getStackTrace();
                }
                break;
            case "etNum":
                try {
                    arrayList.get(position).setNumber(content);
                }catch (IndexOutOfBoundsException e){
                    e.getStackTrace();
                }
                break;
            case "etMValue":
                try {
                    arrayList.get(position).setmValue(content);
                }catch (IndexOutOfBoundsException e){
                    e.getStackTrace();
                }
                break;
        }
    }














    private void initCB() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<NetworkImageHolderView>() {
                    @Override
                    public NetworkImageHolderView createHolder() {
                        return new NetworkImageHolderView();
                    }
                }, image)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }
    //轮播图适配图片
    public class NetworkImageHolderView implements Holder<File> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int position, File data) {
//            imageView.setImageResource(data);
            Glide.with(context).load(data).error(R.mipmap.img_error).into(imageView);
        }
    }

    //
    private void initHTTPCB() {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        convenientBanner.setPages(
                new CBViewHolderCreator<HTTPImageHolderView>() {
                    @Override
                    public HTTPImageHolderView createHolder() {
                        return new HTTPImageHolderView();
                    }
                }, imageList)
                //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                .setPageIndicator(new int[]{R.drawable.dot_dark, R.drawable.dot_red})
                //设置指示器的方向
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
    }
    //轮播图适配图片
    public class HTTPImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }
        @Override
        public void UpdateUI(Context context, int position, String data) {
//            imageView.setImageResource(data);
            Glide.with(context).load(data).error(R.mipmap.img_error).into(imageView);
        }
    }
}
