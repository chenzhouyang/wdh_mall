package com.yskj.wdh.ui.providermanager;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yskj.wdh.R;
import com.yskj.wdh.adapter.AddGoodsInfoAdapter;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.AddGoodsInfoToJava;
import com.yskj.wdh.bean.ProviderGoodsInfoBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.dialog.AddGoodsInfoDialog;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.JSONFormat;
import com.yskj.wdh.util.MethodUtils;
import com.yskj.wdh.util.PhotoUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 创建日期 2017/8/3on 10:15.
 * 描述：添加商品详情
 * 作者：姜贺YSKJ-JH
 */

public class AddGoodsInfoActivity extends BaseActivity implements View.OnClickListener, AddGoodsInfoDialogInterface, AddGoodsInfoAdapterInterface, EditExpressCompleteInterface {
    ImageView imgBack;
    TextView txtTitle;
    TextView tvRight;
    ListView listView;
    LinearLayout llNoData;
    LinearLayout llAdd;
    private ArrayList<ProviderGoodsInfoBean> arrayList = new ArrayList<>();
    private AddGoodsInfoAdapter adapter;
    private PopupWindow popupWindow;
    private int imgPosition;//adapter中图片位置
    private LoadingCaches caches = LoadingCaches.getInstance();
    private String token;
    private String goodsId;
    /**
     * 从相册选择照片
     */
    public static final int ALBUM = 1;
    /**
     * 从相机拍照获取照片
     */
    public static final int CAMERA = 2;
    /**
     * 照片来源
     */
    public int pictureFromWhere = 0;

    /**
     * 当前要上传的图片的file路径
     */
    private File mCurrentPhotoFile;
    /**
     * 从相机来的照片路径
     */
    private String path;
    private Bitmap bitMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods_info);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        adapter = new AddGoodsInfoAdapter(context, arrayList);
        listView.setAdapter(adapter);
        adapter.setAddGoodsInfoAdapterInterface(this);
        adapter.setEditExpressCompleteInterface(this);

        imgOrListView();
    }

    /**
     * 类型1为添加文字  类型2为添加图片
     * 添加类型1的列表空元素
     */
    private void addGoodsInfoType1() {
        ProviderGoodsInfoBean providerGoodsInfoBean = new ProviderGoodsInfoBean();
        providerGoodsInfoBean.setContent("");
        providerGoodsInfoBean.setType(1);
        arrayList.add(providerGoodsInfoBean);
        PublicWay.goodsContentList.add(providerGoodsInfoBean);
    }

    /**
     * 类型1为添加文字  类型2为添加图片
     * 添加类型2的列表空元素
     */
    private void addGoodsInfoType2() {
        ProviderGoodsInfoBean providerGoodsInfoBean = new ProviderGoodsInfoBean();
        providerGoodsInfoBean.setContent("");
        providerGoodsInfoBean.setType(2);
        arrayList.add(providerGoodsInfoBean);
        PublicWay.goodsContentList.add(providerGoodsInfoBean);
    }

    private void initView() {
        token = caches.get("access_token");

        Logger.d(token);
        imgBack = (ImageView) findViewById(R.id.img_back);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        listView = (ListView) findViewById(R.id.lv);
        llNoData = (LinearLayout) findViewById(R.id.ll_no_data);
        llAdd = (LinearLayout) findViewById(R.id.ll_add);
        imgBack.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        goodsId =bundle.getString("goodsId");
        arrayList.addAll(PublicWay.goodsContentList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Intent intent = new Intent();
                intent.putExtra("data","");
                setResult(Config.ADDGOODSINFORESULTCODE,intent);
                finish();
                break;
            case R.id.ll_add:
                AddGoodsInfoDialog dialog = new AddGoodsInfoDialog(context);
                dialog.show();
                dialog.setAddGoodsInfoDialogInterface(this);
                break;
            case R.id.tv_right:
                sendToJavaJson();
                break;
        }
    }

    private void sendToJavaJson() {
        AddGoodsInfoToJava addGoodsInfoToJava = new AddGoodsInfoToJava();
        addGoodsInfoToJava.contentDetailVo = new ArrayList<>();
        addGoodsInfoToJava.goodId = goodsId;
        for (int i = 0; i < arrayList.size(); i++) {
            AddGoodsInfoToJava.ContentDetailVoBean contentDetailVoBean = new AddGoodsInfoToJava.ContentDetailVoBean();

            ProviderGoodsInfoBean providerGoodsInfoBean = arrayList.get(i);
            contentDetailVoBean.orders = i;
            contentDetailVoBean.type = providerGoodsInfoBean.getType();
            if (providerGoodsInfoBean.getType() == 1) {
                contentDetailVoBean.content = providerGoodsInfoBean.getContent();
            } else {
                contentDetailVoBean.id = providerGoodsInfoBean.getData();
            }
            addGoodsInfoToJava.contentDetailVo.add(contentDetailVoBean);
        }
        Gson gson = new Gson();
        String json = gson.toJson(addGoodsInfoToJava);
        Logger.d(json);
        OkHttpUtils.postString().url(Urls.PROVIDERGOODSINFO).addHeader(Config.HEADTOKEN, token).mediaType(MediaType.parse("application/json; charset=utf-8")).content(json)
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
                        Intent intent = new Intent();
                        intent.putExtra("data", data);
                        setResult(Config.ADDGOODSINFORESULTCODE, intent);
                        finish();
                        break;
                }
            }
        });
    }

    /**
     * 判断显示默认图或者是list
     */
    private void imgOrListView() {
        if (arrayList.size() == 0) {
            llNoData.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.VISIBLE);
            llNoData.setVisibility(View.GONE);
        }
    }

    /**
     * dialog对话框回掉  用于添加空白类型
     */
    @Override
    public void addTypeOne() {
        addGoodsInfoType1();
        imgOrListView();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addTypeTwo() {
        addGoodsInfoType2();
        imgOrListView();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deleteType(int position) {
        arrayList.remove(arrayList.get(position));
        PublicWay.goodsContentList.remove(PublicWay.goodsContentList.get(position));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void upType(int position) {
        if (position > 0) {
            ProviderGoodsInfoBean goodsInfoBean = arrayList.get(position);
            ProviderGoodsInfoBean goodsInfoBean1 = arrayList.get(position - 1);
            arrayList.set(position, goodsInfoBean1);
            arrayList.set(position - 1, goodsInfoBean);

            PublicWay.goodsContentList.set(position,goodsInfoBean1);
            PublicWay.goodsContentList.set(position-1,goodsInfoBean);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addImg(int position, View view) {
        this.imgPosition = position;
        dialogShow(view);
    }

    @Override
    public void editComplete(String tag, int position, String content) {
        switch (tag) {
            case "etName":
                try {
                    arrayList.get(position).setContent(content);
                    PublicWay.goodsContentList.get(position).setContent(content);
                } catch (IndexOutOfBoundsException e) {
                    e.getStackTrace();
                }
                break;
        }
    }

    /**
     * 选择图片来源
     */
    private void dialogShow(View view) {
        getPhotoPopupWindow(R.layout.popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.anim_popup_dir);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
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

    /***
     * 图片PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    backgroundAlpha(1f);
                    popupWindow = null;
                }
                return false;
            }
        });
    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        pictureFromWhere = ALBUM;
        doSelectImageFromAlbum();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }
    /**
     * 从相册选择照片
     */
    private void doSelectImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    /**
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        pictureFromWhere = CAMERA;
        doTakePhoto();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 调用系统相机拍照
     */
    protected void doTakePhoto() {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/Photo");
            if (!file.exists()) {
                file.mkdirs();
            }
            mCurrentPhotoFile = new File(file, PhotoUtil.getRandomFileName() + ".jpg");
            path = Environment.getExternalStorageDirectory() + "/DCIM/Photo/" + PhotoUtil.getRandomFileName();
            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException d) {
            Toast.makeText(this, "手机中无可用的图片", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                if (pictureFromWhere == ALBUM) {
                    mCurrentPhotoFile = getPicturePathFromAlbum(data);
                    if(bitMap!=null){
                        if (bitMap.getByteCount() > (1024 * 1024 * 3)) {
                            MethodUtils.showToast(AddGoodsInfoActivity.this, "请上传小于3M的图片");
                            break;
                        }
                    }

                } else {
                    getPicturePathFromCamera();
                }
                upDataImg();
                break;
        }
    }

    private void getPicturePathFromCamera() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 5;   // width，hight设为原来的十分一
        try {
            bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(mCurrentPhotoFile)), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private File getPicturePathFromAlbum(Intent data) {
        Uri uri = data.getData();
        uri = getUri(data);//解决方案
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 5;   // width，hight设为原来的十分一
            try {
                bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
        } else {
            return null;
        }
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public Uri getUri(android.content.Intent intent) {
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
     * 图片上传
     *
     */
    private void upDataImg() {
        if (mCurrentPhotoFile == null) {
            MethodUtils.showToast(AddGoodsInfoActivity.this, "获取照片失败，请重试");
        } else {
            arrayList.get(imgPosition).setBitmap(bitMap);
            PublicWay.goodsContentList.get(imgPosition).setBitmap(bitMap);
            adapter.notifyDataSetChanged();
            mCurrentPhotoFile = saveBitmap(bitMap);
            OkHttpUtils.post().url(Urls.PROVIDERGOODSIMGDETAILUPLOAD).addFile("files", System.currentTimeMillis() + ".jpg", mCurrentPhotoFile).build().execute(new StringCallback() {
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
                            arrayList.get(imgPosition).setData(data.replace(",", ""));
                            break;
                    }
                }
            });
        }
    }

    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File  saveBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/lsk/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "share.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return img;
    }
}
