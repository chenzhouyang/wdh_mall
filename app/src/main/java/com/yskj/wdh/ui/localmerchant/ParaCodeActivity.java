package com.yskj.wdh.ui.localmerchant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.BusinessInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.entity.UserInfoEntity;
import com.yskj.wdh.url.Ips;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

import static android.R.attr.path;

/**
 * Created by 陈宙洋 on 2016/9/7.
 * 收款界面
 */
public class ParaCodeActivity extends BaseActivity {

    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.para_image)
    ImageView paraimage;
    @Bind(R.id.para_ll)
    LinearLayout paraLl;
    @Bind(R.id.para_name)
    TextView paraName;
    // 图片宽度的一般
    private static final int IMAGE_HALFWIDTH = 30;
    // 需要插图图片的大小 这里设定为40*40
    int[] pixels = new int[2 * IMAGE_HALFWIDTH * 2 * IMAGE_HALFWIDTH];
    // 插入到二维码里面的图片对象
    private Bitmap mBitmap;
    private Bitmap bitmap = null;
    private LoadingCaches caches = LoadingCaches.getInstance();
    private BusinessInfoBean bean;
    private String type;
    private String spreadCode;
    private UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.para_layout);
        ButterKnife.bind(this);
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.paycodeimage)).getBitmap();
        type = getIntent().getStringExtra("type");
        userInfoEntity = new Gson().fromJson(caches.get("userinfo"), new TypeToken<UserInfoEntity>() {
        }.getType());
        spreadCode = userInfoEntity.data.userVo.spreadCode+"";
        if(type.equals("1")){
            txtTitle.setText("收款码");
            if(!caches.get("business").equals("null")){
                bean = new Gson().fromJson(caches.get("business"),new TypeToken<BusinessInfoBean>(){}.getType());
                paraName.setText(bean.getShopName()+"的店铺");
            }
        }else {
            txtTitle.setText("检测码");
            paraName.setText("检测收费二维码");
        }

        initview();
        paraLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                        .setMessage("是否保存收款码？")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                Bitmap bitmap = createViewBitmap(v);
                                saveBitmap(context, bitmap);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                    }
                }).show();//在按键响应事件中显示此对话框

                return false;
            }
        });


    }

    private void saveBitmap(Context context, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "/wdh");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
    }

    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    //收款二维码1、收款吗，3、仪器检测
    public void initview() {
        OkHttpUtils.get().url(Urls.QRCODEFK)
                .addHeader("Authorization", caches.get("access_token"))
                .addParams("codeType", type)
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
                    String qrcode = (String) map.get("data");
                    String qrcodetrue = null;
                    if(type.equals("1")){
                        qrcodetrue = Ips.SHAREURL+"/index.php?m=Mobile&c=User&a=reg&spreader="+spreadCode+"&Code#" + qrcode;
                    }else {
                        qrcodetrue = Ips.SHAREURL+"/index.php?m=Mobile&c=User&a=reg&spreader="+spreadCode+"$Code#" + qrcode;
                    }

                    inits(qrcodetrue);
                }
            }
        });
    }

    private void inits(String url) {

        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
        m.setScale(sx, sy);
        // 重新构造一个40*40的图片
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
                mBitmap.getHeight(), m, false);

        try {
            bitmap = cretaeBitmap(new String(url.getBytes(), "UTF-8"));
            paraimage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成二维码
     *
     * @param
     * @return Bitmap
     * @throws WriterException
     */
    public Bitmap cretaeBitmap(String str) throws WriterException {
        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);//设置容错级别,H为最高
        hints.put(EncodeHintType.MAX_SIZE, IMAGE_HALFWIDTH);// 设置图片的最大值
        hints.put(EncodeHintType.MIN_SIZE, IMAGE_HALFWIDTH/2);// 设置图片的最小值
        hints.put(EncodeHintType.MARGIN, 2);//设置白色边距值
        BitMatrix matrix = new MultiFormatWriter().encode(str,
                BarcodeFormat.QR_CODE, 500, 500);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        // 二维矩阵转为一维像素数组,也就是一直横着排了
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                        && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] = mBitmap.getPixel(x - halfW+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }

            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }


    }
}
