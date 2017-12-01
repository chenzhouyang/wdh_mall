package com.yskj.wdh.zxing.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yskj.wdh.R;
import com.yskj.wdh.base.BaseActivity;
import com.yskj.wdh.bean.CapTureBean;
import com.yskj.wdh.cache.LoadingCaches;
import com.yskj.wdh.url.Config;
import com.yskj.wdh.url.Urls;
import com.yskj.wdh.util.Messge;
import com.yskj.wdh.zxing.camera.CameraManager;
import com.yskj.wdh.zxing.decoding.CaptureActivityHandler;
import com.yskj.wdh.zxing.decoding.InactivityTimer;
import com.yskj.wdh.zxing.view.ViewfinderView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.util.Vector;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Initial the camera
 *
 * @author Ryan.Tang
 */
public class CaptureActivity extends BaseActivity implements Callback {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView cancelScanButton;
    private String bundle, token, passwords, mobile, num, anmount;
    private LoadingCaches aCache = LoadingCaches.getInstance();
    private Intent intent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        //ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication(),0,5);
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        cancelScanButton = (ImageView) this.findViewById(R.id.btn_cancel_scan);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        intent = getIntent();
        bundle = intent.getStringExtra("codebundle");
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

        //quit the scan view
        cancelScanButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String json = new String("nodata");
                Intent intent = new Intent();
                intent.putExtra("json", json);
                setResult(RESULT_OK, intent);
                CaptureActivity.this.finish();
            }
        });
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
        String resultString = result.getText();
        boolean bs = resultString.contains("payCode#");
        //FIXME
        if (resultString.equals("")) {
            Toast.makeText(CaptureActivity.this, "扫描失败", Toast.LENGTH_SHORT).show();
        } else if (bundle.equals("1") && bs) {
            //商家扫描
            String[] receivables = resultString.split("#");
            String payCode = receivables[1];
            anmount = intent.getStringExtra("anmount");
            OkHttpUtils.post().url(Urls.QRCODWPAY)
                    .addHeader("Authorization", aCache.get("access_token"))
                    .addParams("payAmount", anmount)
                    .addParams("code", payCode)
                    .build().execute(new CaptureCallBall());
        } else if ((bundle.equals("0") || bundle.equals("3")) && bs) {
            //收款接口
            String[] paracode = resultString.split("#");
            if (bundle.equals("3") && paracode.length == 2) {
                Toast.makeText(getApplicationContext(), "付款码仅支持商家使用", Toast.LENGTH_SHORT).show();
                finish();
            } else if (bundle.equals("3") && paracode.length == 3) {

            }

        } else if (bundle.equals("3") && !bs) {
            String uriStr = result.getText();
            Intent intent = new Intent(Intent.ACTION_VIEW, (Uri.parse(uriStr))).addCategory(Intent.CATEGORY_BROWSABLE)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else if (bundle.equals("4")) {
            startMyDialog();
            OkHttpUtils.post().url(Urls.consumptionInvokeUrl).addHeader("Authorization", aCache.get("access_token"))
                    .addParams("consumePwd", resultString)
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
                    Intent intent = new Intent();
                    intent.putExtra("json", response);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }else if (bundle.equals("5")){
            Intent intent = new Intent();
            intent.putExtra("data", resultString);
            setResult(Config.EXPRESSORDERID, intent);
            finish();
        }
    }
public class CaptureCallBall extends com.zhy.http.okhttp.callback.Callback<CapTureBean>{
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
    public CapTureBean parseNetworkResponse(Response response, int id) throws Exception {
        String s = response.body().string();
        CapTureBean capTureBean = new Gson().fromJson(s,new TypeToken<CapTureBean>(){}.getType());
        return capTureBean;
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        stopMyDialog();
        isLogin(e);
    }

    @Override
    public void onResponse(CapTureBean response, int id) {
        stopMyDialog();
        if(response.getCode() == 0){
            if (bundle.equals("1")) {
                new AlertDialog.Builder(CaptureActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("收款成功")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                finish();
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        finish();
                    }
                }).show();//在按键响应事件中显示此对话框

            } else {
                new AlertDialog.Builder(CaptureActivity.this).setTitle("系统提示")//设置对话框标题
                        .setMessage("支付成功")//设置显示的内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                finish();
                            }
                        }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                    public void onClick(DialogInterface dialog, int which) {//响应事件
                        finish();
                    }
                }).show();//在按键响应事件中显示此对话框
            }
        }else if(response.getCode() == 701){
            new AlertDialog.Builder(CaptureActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage("对方可用资金不足，让您的亲扫您试试哦")//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            finish();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    finish();
                }
            }).show();//在按键响应事件中显示此对话框
        }else {
            new AlertDialog.Builder(CaptureActivity.this).setTitle("系统提示")//设置对话框标题
                    .setMessage(Messge.geterr_code(response.getCode()))//设置显示的内容
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                        public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                            finish();
                        }
                    }).setNegativeButton("返回", new DialogInterface.OnClickListener() {//添加返回按钮
                public void onClick(DialogInterface dialog, int which) {//响应事件
                    finish();
                }
            }).show();//在按键响应事件中显示此对话框
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
            handler = new CaptureActivityHandler(this, decodeFormats,
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






 /*   @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String json = new String("nodata");
            Intent intent = new Intent();
            intent.putExtra("json", json);
            setResult(RESULT_OK, intent);
            return true;
        }
        return false;
    }*/
}