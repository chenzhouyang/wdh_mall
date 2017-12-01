package com.yskj.wdh.ui.collectmoney;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yskj.wdh.url.Ips;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YSKJ-02 on 2017/5/26.
 */

public class PaymentCodeActivity extends BaseActivity {
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.para_image)
    ImageView paraimage;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_code);
        ButterKnife.bind(this);
        txtTitle.setText("收银付款二维码");
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.paycodeimage)).getBitmap();
        initview();
    }

    //生成收款码
    public void initview() {
        String qrcode = getIntent().getStringExtra("orderid")+"#"+getIntent().getStringExtra("totalamount");
        String  qrcodetrue = Ips.SHAREURL+"/index.php?m=Mobile&c=User&a=reg&spreader="+spreadCode+"Code@#" + qrcode;
        inits(qrcodetrue);
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
    public void onViewClicked() {
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
