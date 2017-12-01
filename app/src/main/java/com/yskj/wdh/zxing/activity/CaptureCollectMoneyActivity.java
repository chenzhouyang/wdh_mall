package com.yskj.wdh.zxing.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yskj.wdh.AppManager;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.CollectMoneyAdapter;
import com.yskj.wdh.adapter.InventoryListAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.CalculatorDialog;
import com.yskj.wdh.dialog.HintDialog;
import com.yskj.wdh.entity.CaptureCollectEntity;
import com.yskj.wdh.entity.OrderDetailsEntity;
import com.yskj.wdh.entity.ProvisionalOrderEntivity;
import com.yskj.wdh.entity.ShoppingDetailEntity;
import com.yskj.wdh.ui.collectmoney.PaymentCodeActivity;
import com.yskj.wdh.ui.collectmoney.ProsceniumActivity;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.util.StringUtils;
import com.yskj.wdh.zxing.camera.CameraManager;
import com.yskj.wdh.zxing.decoding.CaptureCollectMoneyActivityHandler;
import com.yskj.wdh.zxing.decoding.InactivityTimer;
import com.yskj.wdh.zxing.view.ViewfinderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureCollectMoneyActivity extends BaseActivity implements Callback, View.OnClickListener, CollectMoneyAdapter.ModifyCountInterface ,CalculatorDialog.CalcuatorInterface{
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.preview_view)
    SurfaceView previewView;
    @Bind(R.id.input_price)
    EditText inputPrice;
    @Bind(R.id.amount_price)
    TextView amountPrice;
    @Bind(R.id.total_amount)
    TextView totalAmount;
    @Bind(R.id.collecti_monety_lv)
    ListView collectiMonetyLv;
    @Bind(R.id.direct_payment)
    TextView directPayment;
    @Bind(R.id.collect_save)
    TextView collectSave;
    @Bind(R.id.bottom_mask)
    RelativeLayout bottomMask;
    @Bind(R.id.capture_container)
    FrameLayout captureContainer;
    private int scanMode;//扫描模型（条形，二维码，全部）
    SurfaceView scanPreview = null;
    private List<ShoppingDetailEntity> shoppingDetailEntities = new ArrayList<>();
    private CollectMoneyAdapter collectMoneyAdapter;
    private String orderName, type;//0、生成新的临时订单，1、修改临时订单
    private String orderId;
    private OrderDetailsEntity orderDetailsEntity;
    private CaptureCollectMoneyActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private int count = 1;
    private int types = 0;//判断点击那个按钮
    private double trueprices = 0.00;
    private double totalamount;
    private CalculatorDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collectmoney);
        ButterKnife.bind(this);
        txtTitle.setText("收银台");
        type = getIntent().getStringExtra("type");
        orderName = getIntent().getStringExtra("ordername");
        if (type.equals("1")) {
            orderId = getIntent().getStringExtra("orderid");
            getdata();
        }
        CameraManager.init(getApplication(),450,2);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        collectMoneyAdapter = new CollectMoneyAdapter(context);
        collectMoneyAdapter.setModifyCountInterface(CaptureCollectMoneyActivity.this);
        collectiMonetyLv.setAdapter(collectMoneyAdapter);
        collectMoneyAdapter.setShoppingCartBeanList(shoppingDetailEntities);
        collectSave.setOnClickListener(this);
        directPayment.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        inputPrice.setOnClickListener(this);
        dialog = new CalculatorDialog(context);
        dialog.setCalcuatorInterface(this);

    }

    /**
     * 获取商品列表
     */
    private void getdata() {
        OkHttpUtils.get().url(Urls.DETAIL).addHeader("Authorization", caches.get("access_token"))
                .addParams("orderId", orderId)
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
                orderDetailsEntity = new Gson().fromJson(response, new TypeToken<OrderDetailsEntity>() {
                }.getType());
                if (orderDetailsEntity.code == 0) {
                    for (int i = 0; i < orderDetailsEntity.data.relations.size(); i++) {
                        ShoppingDetailEntity shoppingDetailEntity = new ShoppingDetailEntity();
                        shoppingDetailEntity.setPrice(orderDetailsEntity.data.relations.get(i).price);
                        shoppingDetailEntity.setGoodname(orderDetailsEntity.data.relations.get(i).goodName);
                        shoppingDetailEntity.setGoodsid(orderDetailsEntity.data.relations.get(i).goodId);
                        shoppingDetailEntity.setCount(orderDetailsEntity.data.relations.get(i).count);
                        shoppingDetailEntities.add(shoppingDetailEntity);
                    }
                    trueprices = 0.00;
                    for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
                        trueprices += detailEntity.getPrice()*detailEntity.getCount();
                    }
                    totalamount = orderDetailsEntity.data.extAmount;//非直供商品金额
                    if(totalamount>0){
                        inputPrice.setText(StringUtils.getStringtodouble(totalamount));
                        inputPrice.setSelection(StringUtils.getStringtodouble(totalamount).length());//将光标移至文字末尾
                    }


                    amountPrice.setText(StringUtils.getStringtodouble(trueprices));
                    if (inputPrice.length() != 0) {
                        totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices + Double.parseDouble(inputPrice.getText().toString().replace(",",""))));
                    } else {
                        totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices));
                    }

                    collectMoneyAdapter = new CollectMoneyAdapter(context);
                    collectMoneyAdapter.setModifyCountInterface(CaptureCollectMoneyActivity.this);
                    collectiMonetyLv.setAdapter(collectMoneyAdapter);
                    collectMoneyAdapter.setShoppingCartBeanList(shoppingDetailEntities);
                } else {
                    showToast(Messge.geterr_code(orderDetailsEntity.code));
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();

    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        final String resultString = result.getText();
        final List<ShoppingDetailEntity> shoppingDetailEntityList = DataSupport.where("shapecode = ?", resultString).find(ShoppingDetailEntity.class);
        if (shoppingDetailEntityList != null && shoppingDetailEntityList.size() != 0) {
            shoppingDetailEntities.addAll(shoppingDetailEntityList);
            double trueprice = 0.00;
            for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
                trueprice += detailEntity.getPrice()*detailEntity.getCount();
            }
            amountPrice.setText(StringUtils.getStringtodouble(trueprice));
            if (inputPrice.length() != 0) {
                totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice + Double.parseDouble(inputPrice.getText().toString())));
            } else {
                totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice));
            }
            Map<Integer, ShoppingDetailEntity> map = new HashMap<>();
            for (ShoppingDetailEntity item : shoppingDetailEntities) {
                if (map.containsKey(item.getGoodsid())) {
                    ShoppingDetailEntity old = map.get(item.getGoodsid());
                    old.setCount(old.getCount() + item.getCount());
                    map.put(old.getGoodsid(), old);
                } else {
                    map.put(item.getGoodsid(), item);
                }
            }
            shoppingDetailEntities = new ArrayList<>(map.values());
            collectMoneyAdapter = new CollectMoneyAdapter(context);
            collectMoneyAdapter.setModifyCountInterface(this);
            collectiMonetyLv.setAdapter(collectMoneyAdapter);
            collectMoneyAdapter.setShoppingCartBeanList(shoppingDetailEntities);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

                    SurfaceHolder surfaceHolder = surfaceView.getHolder();

                    initCamera(surfaceHolder);

                    if (handler != null)

                        handler.restartPreviewAndDecode();
                }

            }, 3000);
            HintDialog dialog = new HintDialog(context,"商品添加成功....");
            dialog.setCancelable(false);
            dialog.show();
        } else {
            OkHttpUtils.get().url(Urls.INFOR).addHeader("Authorization", caches.get("access_token"))
                    .addParams("barCode", resultString)
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
                    CaptureCollectEntity collectEntity = new Gson().fromJson(response, new TypeToken<CaptureCollectEntity>() {
                    }.getType());
                    if (collectEntity.code == 0) {
                        CaptureCollectEntity.DataBean shdeta = collectEntity.data;

                        ShoppingDetailEntity data = new ShoppingDetailEntity();
                        data.setGoodname(shdeta.goodName);
                        data.setGoodsid(shdeta.goodId);
                        data.setPrice(shdeta.price);
                        data.setCount(1);
                        data.setShapecode(resultString);
                        shoppingDetailEntityList.add(data);
                        data.save();

                        double trueprice = 0.00;
                        for (ShoppingDetailEntity detailEntity : shoppingDetailEntityList) {
                            trueprice += detailEntity.getPrice()*detailEntity.getCount();
                        }
                        amountPrice.setText(StringUtils.getStringtodouble(trueprice));
                        if (inputPrice.length() != 0) {
                            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice + Double.parseDouble(inputPrice.getText().toString())));
                        } else {
                            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice));
                        }

                        shoppingDetailEntities.addAll(shoppingDetailEntityList);
                        Map<Integer, ShoppingDetailEntity> map = new HashMap<>();
                        for (ShoppingDetailEntity item : shoppingDetailEntities) {
                            if (map.containsKey(item.getGoodsid())) {
                                ShoppingDetailEntity old = map.get(item.getGoodsid());
                                old.setCount(old.getCount() + item.getCount());
                                map.put(old.getGoodsid(), old);
                            } else {
                                map.put(item.getGoodsid(), item);
                            }
                        }
                        shoppingDetailEntities = new ArrayList<>(map.values());
                        collectMoneyAdapter = new CollectMoneyAdapter(context);
                        collectMoneyAdapter.setModifyCountInterface(CaptureCollectMoneyActivity.this);
                        collectiMonetyLv.setAdapter(collectMoneyAdapter);
                        collectMoneyAdapter.setShoppingCartBeanList(shoppingDetailEntities);
                        HintDialog dialog = new HintDialog(context,"商品添加成功......");
                        dialog.setCancelable(false);
                        dialog.show();
                    } else {
                        showToast(Messge.geterr_code(collectEntity.code));

                    }
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);

                            SurfaceHolder surfaceHolder = surfaceView.getHolder();

                            initCamera(surfaceHolder);

                            if (handler != null)

                                handler.restartPreviewAndDecode();
                        }

                    }, 3000);
                }
            });
        }


    }

    /**
     * 处理按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.direct_payment:
                //直接跳转到付款码
                types = 0;
                if (type.equals("0")) {
                    //创建临时订单
                    Billing();
                } else {
                    //修改临时订单
                    RevampBilling();
                }
                break;
            case R.id.collect_save:
                types = 1;
                //保存按钮
                if (type.equals("0")) {
                    //创建临时订单
                    Billing();
                } else {
                    //修改临时订单
                    RevampBilling();
                }
                break;
            case R.id.img_back:
                AppManager.getInstance().killActivity(CaptureCollectMoneyActivity.class);
                break;
            case R.id.input_price:
                dialog.show();
                break;

        }
    }

    /**
     * 创建临时订单
     */
    private void Billing() {
        if (!orderName.equals("null") && inputPrice.length() != 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("orderName", orderName);
            ((PostFormBuilder) builder).addParams("extAmount", inputPrice.getText().toString());
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (orderName.equals("null") && inputPrice.length() != 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("extAmount", inputPrice.getText().toString());
            ((PostFormBuilder) builder).addParams("orderName", "");
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (!orderName.equals("null") && inputPrice.length() == 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("orderName", orderName);
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (orderName.equals("null") && inputPrice.length() == 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        }

    }

    /**
     * 修改临时订单
     */
    private void RevampBilling() {
        if (!orderName.equals("null") && inputPrice.length() != 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderId", orderId);
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("orderName", orderName);
            ((PostFormBuilder) builder).addParams("extAmount", inputPrice.getText().toString());
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (orderName.equals("null") && inputPrice.length() != 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderId", orderId);
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("extAmount", inputPrice.getText().toString());
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (!orderName.equals("null") && inputPrice.length() == 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderId", orderId);
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).addParams("orderName", orderName);
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        } else if (orderName.equals("null") && inputPrice.length() == 0) {
            String list = "null";
            for (int i = 0; i < shoppingDetailEntities.size(); i++) {
                list = list + "&goods=" + shoppingDetailEntities.get(i).getGoodsid() + "-" + shoppingDetailEntities.get(i).getCount();
            }
            PostFormBuilder builder = OkHttpUtils.post().url(Urls.BILLING + "?" + list.replace("null&", "")).addHeader("Authorization", caches.get("access_token"));
            ((PostFormBuilder) builder).addParams("orderId", orderId);
            ((PostFormBuilder) builder).addParams("orderType", "1");
            ((PostFormBuilder) builder).build().execute(new CollectMoneyCallBack());
        }
    }


    /**
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     */
    @Override
    public void doIncrease(int position, View showCountView, View showview) {
        ShoppingDetailEntity shoppingDetailEntity = shoppingDetailEntities.get(position);
        int currentCount = shoppingDetailEntity.getCount();
        currentCount++;
        count = currentCount;
        shoppingDetailEntity.setCount(currentCount);
        ((EditText) showCountView).setText(currentCount + "");
        trueprices = 0.00;
        for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
            trueprices += detailEntity.getPrice() * detailEntity.getCount();
        }
        amountPrice.setText(StringUtils.getStringtodouble(trueprices));
        if (inputPrice.length() != 0) {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices + Double.parseDouble(inputPrice.getText().toString())));
        } else {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices));
        }
        collectMoneyAdapter.notifyDataSetChanged();
    }

    /**
     * @param position      组元素位置
     * @param showCountView 用于展示变化后数量的View
     */
    @Override
    public void doDecrease(int position, View showCountView, View showview) {
        ShoppingDetailEntity shoppingDetailEntity = shoppingDetailEntities.get(position);
        int currentCount = shoppingDetailEntity.getCount();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        count = currentCount;
        shoppingDetailEntity.setCount(currentCount);
        trueprices = 0.00;
        for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
            trueprices += detailEntity.getPrice() * detailEntity.getCount();
        }
        amountPrice.setText(StringUtils.getStringtodouble(trueprices));
        if (inputPrice.length() != 0) {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices + Double.parseDouble(inputPrice.getText().toString())));
        } else {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices));
        }
        ((EditText) showCountView).setText(currentCount + "");
        ((TextView) showview).setText(StringUtils.getStringtodouble(currentCount * shoppingDetailEntity.getPrice()));
        collectMoneyAdapter.notifyDataSetChanged();
    }

    /**
     * @param position
     */
    @Override
    public void childDelete(int position) {

    }

    @Override
    public void caluator(String exp) {
        double trueprice = 0.00;
        for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
            trueprice += detailEntity.getPrice()*detailEntity.getCount();
        }
        amountPrice.setText(StringUtils.getStringtodouble(trueprice));
        inputPrice.setText(StringUtils.getStringtodouble(Double.parseDouble(exp)));
        if (inputPrice.length() != 0) {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice + Double.parseDouble(inputPrice.getText().toString().replace(",",""))));
        } else {
            totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprice));
        }
    }

    /**
     * 创建临时订单的数据解析
     */
    private class CollectMoneyCallBack extends com.zhy.http.okhttp.callback.Callback<ProvisionalOrderEntivity> {
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
        public ProvisionalOrderEntivity parseNetworkResponse(Response response, int id) throws Exception {
            String s = response.body().string();
            ProvisionalOrderEntivity provisionalOrderEntivity = new Gson().fromJson(s, new TypeToken<ProvisionalOrderEntivity>() {
            }.getType());
            return provisionalOrderEntivity;
        }

        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(ProvisionalOrderEntivity response, int id) {
            if (response.code == 0) {
                if (types == 1) {
                    startActivity(new Intent(context, ProsceniumActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    if(response.data.totalAmount>0){
                        startActivity(new Intent(context, PaymentCodeActivity.class).putExtra("orderid", response.data.orderId + "")
                                .putExtra("totalamount", response.data.totalAmount + "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else {
                        showToast("金额不能为零哦");
                    }

                }
            }else if(response.code == 1011){
                InventoryDialog dialog = new InventoryDialog(context,response.data.list);
                dialog.show();
            }else {
                showToast(Messge.geterr_code(response.code));
            }
        }
    }

    /**
     * 库存不足弹出框
     */
    public class InventoryDialog extends Dialog implements View.OnClickListener{
        private Context context;
        private ListView inventory_lv;
        private TextView abolish_inventory,confirm_inventory;
        private List<ProvisionalOrderEntivity.DataBean.ListBean> list;
        private ProvisionalOrderEntivity.DataBean.ListBean provisional ;
        public InventoryDialog(Context context,  List<ProvisionalOrderEntivity.DataBean.ListBean> list) {
            super(context, R.style.ShareDialog);
            this.context = context;
            this.list = list;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_inventory);
            inventory_lv = (ListView) findViewById(R.id.inventory_lv);
            abolish_inventory = (TextView) findViewById(R.id.abolish_inventory);
            confirm_inventory = (TextView) findViewById(R.id.confirm_inventory);
            abolish_inventory.setOnClickListener(this);
            confirm_inventory.setOnClickListener(this);
            InventoryListAdapter adapter = new InventoryListAdapter(context,list);
            inventory_lv.setAdapter(adapter);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.abolish_inventory:
                    //取消
                    dismiss();
                    break;
                case R.id.confirm_inventory:
                    //确定
                    Iterator<ShoppingDetailEntity> iterator = shoppingDetailEntities.iterator();
                    while (iterator.hasNext()) {
                        ShoppingDetailEntity item = iterator.next();
                        for (ProvisionalOrderEntivity.DataBean.ListBean bean: list) {
                            if (item.getGoodsid() == bean.goodId) {
                                iterator.remove();
                            }
                        }
                    }
                    double trueprice = 0.00;
                    for (ShoppingDetailEntity detailEntity : shoppingDetailEntities) {
                        trueprice += detailEntity.getPrice()*detailEntity.getCount();
                    }
                    amountPrice.setText(StringUtils.getStringtodouble(trueprice + trueprices));
                    if (inputPrice.length() != 0) {
                        totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices + trueprice + Double.parseDouble(inputPrice.getText().toString())));
                    } else {
                        totalAmount.setText("合计：" + StringUtils.getStringtodouble(trueprices + trueprice));
                    }
                    collectMoneyAdapter = new CollectMoneyAdapter(context);
                    collectMoneyAdapter.setModifyCountInterface(CaptureCollectMoneyActivity.this);
                    collectiMonetyLv.setAdapter(collectMoneyAdapter);
                    collectMoneyAdapter.setShoppingCartBeanList(shoppingDetailEntities);
                    dismiss();
                    break;
            }
        }
    }
    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureCollectMoneyActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);
            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;


    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };


}