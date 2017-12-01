package com.yskj.wdh.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.view.WindowManager;

import com.yskj.wdh.R;
import com.yskj.wdh.util.Util1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 *裁剪头像
 */
@SuppressLint("NewApi")
public class CropImageActivity extends MonitoredActivity {

	final int IMAGE_MAX_SIZE = 1024;

	private static final String TAG = "CropImage";
	public static final String IMAGE_PATH = "image-path";
	public static final String SCALE = "scale";
	public static final String ORIENTATION_IN_DEGREES = "orientation_in_degrees";
	public static final String ASPECT_X = "aspectX";
	public static final String ASPECT_Y = "aspectY";
	public static final String OUTPUT_X = "outputX";
	public static final String OUTPUT_Y = "outputY";
	public static final String SCALE_UP_IF_NEEDED = "scaleUpIfNeeded";
	public static final String CIRCLE_CROP = "circleCrop";
	public static final String RETURN_DATA = "return-data";
	public static final String RETURN_DATA_AS_BITMAP = "data";
	public static final String ACTION_INLINE_DATA = "inline-data";

	// These are various options can be specified in the intent.
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;
	private Uri mSaveUri = null;
	private boolean mDoFaceDetection = true;
	private boolean mCircleCrop = false;
	private final Handler mHandler = new Handler();

	private int mAspectX;
	private int mAspectY;
	private int mOutputX;
	private int mOutputY;
	private boolean mScale;
	private NCropImageView mImageView;
	private ContentResolver mContentResolver;
	private Bitmap mBitmap;
	private String mImagePath;

	public boolean mWaitingToPick; // Whether we are wait the user to pick a
									// face.
	public boolean mSaving; // Whether the "save" button is already clicked.
	public HighlightView mCrop;

	// These options specifiy the output image size and whether we should
	// scale the output to fit it (or just crop it).
	private boolean mScaleUp = true;

	private final BitmapManager.ThreadSet mDecodingThreads = new BitmapManager.ThreadSet();

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("mImagePath", mImagePath);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContentResolver = getContentResolver();
		setContentView(R.layout.activity_cropimage);
		mImageView = (NCropImageView) findViewById(R.id.image);

		showStorageToast(this);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			if (extras.getString(CIRCLE_CROP) != null) {

				if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
					mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
				}

				mCircleCrop = true;
				mAspectX = 1;
				mAspectY = 1;
			}

			mImagePath = extras.getString(IMAGE_PATH);

			mSaveUri = getImageUri(mImagePath);
			mBitmap = getBitmap(mImagePath);

			if (extras.containsKey(ASPECT_X)
					&& extras.get(ASPECT_X) instanceof Integer) {

				mAspectX = extras.getInt(ASPECT_X);
			} else {

				throw new IllegalArgumentException("aspect_x must be integer");
			}
			if (extras.containsKey(ASPECT_Y)
					&& extras.get(ASPECT_Y) instanceof Integer) {

				mAspectY = extras.getInt(ASPECT_Y);
			} else {

				throw new IllegalArgumentException("aspect_y must be integer");
			}
			mOutputX = extras.getInt(OUTPUT_X);
			mOutputY = extras.getInt(OUTPUT_Y);
			mScale = extras.getBoolean(SCALE, true);
			mScaleUp = extras.getBoolean(SCALE_UP_IF_NEEDED, true);
		}

		if (icicle != null) {
			mImagePath =icicle.getString("mImagePath");
		}
		
		if (mBitmap == null) {
			//Utility.toastGolbalMsg(this, "选择的图片有问题，请重新选择！");
			finish();
			return;
		}

		// Make UI fullscreen.
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		findViewById(R.id.discard).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {

						setResult(RESULT_CANCELED);
						finish();
					}
				});

		findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					onSaveClicked();
				} catch (Exception e) {
					finish();
				}
			}
		});
		findViewById(R.id.rotateLeft).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {

						mBitmap = Util1.rotateImage(mBitmap, -90);
						RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
						mImageView.setImageRotateBitmapResetBase(rotateBitmap,
								true);
						mRunFaceDetection.run();
					}
				});

		findViewById(R.id.rotateRight).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {

						mBitmap = Util1.rotateImage(mBitmap, 90);
						RotateBitmap rotateBitmap = new RotateBitmap(mBitmap);
						mImageView.setImageRotateBitmapResetBase(rotateBitmap,
								true);
						mRunFaceDetection.run();
					}
				});
		startFaceDetection();

	}

	private Uri getImageUri(String path) {

		return Uri.fromFile(new File(path));
	}

	private Bitmap getBitmap(String path) {

		Uri uri = getImageUri(path);
		InputStream in = null;
		try {
			in = mContentResolver.openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(in, null, o);
			in.close();

			int scale = 1;
			if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(IMAGE_MAX_SIZE
								/ (double) Math.max(o.outHeight, o.outWidth))
								/ Math.log(0.5)));
			}

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			in = mContentResolver.openInputStream(uri);
			Bitmap b = BitmapFactory.decodeStream(in, null, o2);
			in.close();

			return b;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		return null;
	}

	private void startFaceDetection() {

		if (isFinishing()) {
			return;
		}

		mImageView.setImageBitmapResetBase(mBitmap, true);

		Util1.startBackgroundJob(this, null, "正在载入图片，请稍等...", new Runnable() {
			public void run() {

				final CountDownLatch latch = new CountDownLatch(1);
				final Bitmap b = mBitmap;
				mHandler.post(new Runnable() {
					public void run() {

						if (b != mBitmap && b != null) {
							mImageView.setImageBitmapResetBase(b, true);
							mBitmap.recycle();
							mBitmap = b;
						}
						if (mImageView.getScale() == 1F) {
							mImageView.center(true, true);
						}
						latch.countDown();
					}
				});
				try {
					latch.await();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				mRunFaceDetection.run();
			}
		}, mHandler);
	}

	private void onSaveClicked() throws Exception {
		// TODO this code needs to change to use the decode/crop/encode single
		// step api so that we don't require that the whole (possibly large)
		// bitmap doesn't have to be read into memory
		if (mSaving)
			return;

		if (mCrop == null) {

			return;
		}

		mSaving = true;

		Rect r = mCrop.getCropRect();

		int width = r.width();
		int height = r.height();

		// If we are circle cropping, we want alpha channel, which is the
		// third param here.
		Bitmap croppedImage;
		try {

			croppedImage = Bitmap.createBitmap(width, height,
					mCircleCrop ? Bitmap.Config.ARGB_8888
							: Bitmap.Config.RGB_565);
		} catch (Exception e) {
			throw e;
		}
		if (croppedImage == null) {

			return;
		}

		{
			Canvas canvas = new Canvas(croppedImage);
			Rect dstRect = new Rect(0, 0, width, height);
			canvas.drawBitmap(mBitmap, r, dstRect, null);
		}

		if (mCircleCrop) {

			// OK, so what's all this about?
			// Bitmaps are inherently rectangular but we want to return
			// something that's basically a circle. So we fill in the
			// area around the circle with alpha. Note the all important
			// PortDuff.Mode.CLEAR.
			Canvas c = new Canvas(croppedImage);
			Path p = new Path();
			p.addCircle(width / 2F, height / 2F, width / 2F, Path.Direction.CW);
			c.clipPath(p, Region.Op.DIFFERENCE);
			c.drawColor(0x00000000, PorterDuff.Mode.CLEAR);
		}

		/* If the output is required to a specific size then scale or fill */
		if (mOutputX != 0 && mOutputY != 0) {

			if (mScale) {

				/* Scale the image to the required dimensions */
				Bitmap old = croppedImage;
				croppedImage = Util1.transform(new Matrix(), croppedImage,
						mOutputX, mOutputY, mScaleUp);
				if (old != croppedImage) {

					old.recycle();
				}
			} else {

				/*
				 * Don't scale the image crop it to the size requested. Create
				 * an new image with the cropped image in the center and the
				 * extra space filled.
				 */

				// Don't scale the image but instead fill it so it's the
				// required dimension
				Bitmap b = Bitmap.createBitmap(mOutputX, mOutputY,
						Bitmap.Config.RGB_565);
				Canvas canvas = new Canvas(b);

				Rect srcRect = mCrop.getCropRect();
				Rect dstRect = new Rect(0, 0, mOutputX, mOutputY);

				int dx = (srcRect.width() - dstRect.width()) / 2;
				int dy = (srcRect.height() - dstRect.height()) / 2;

				/* If the srcRect is too big, use the center part of it. */
				srcRect.inset(Math.max(0, dx), Math.max(0, dy));

				/* If the dstRect is too big, use the center part of it. */
				dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

				/* Draw the cropped bitmap in the center */
				canvas.drawBitmap(mBitmap, srcRect, dstRect, null);

				/* Set the cropped bitmap as the new bitmap */
				croppedImage.recycle();
				croppedImage = b;
			}
		}
		if (croppedImage.getWidth() > 480) {
			croppedImage = MaxBitMap(croppedImage);
		}
		// Return the cropped image directly or save it to the specified URI.
		Bundle myExtras = getIntent().getExtras();
		if (myExtras != null
				&& (myExtras.getParcelable("data") != null || myExtras
						.getBoolean(RETURN_DATA))) {

			Bundle extras = new Bundle();
			extras.putParcelable("data", croppedImage);
			setResult(RESULT_OK, (new Intent()).setAction(ACTION_INLINE_DATA)
					.putExtras(extras));
			finish();
		} else {
			Bundle extras = new Bundle();
			extras.putParcelable("data", croppedImage);
			setResult(RESULT_OK, (new Intent()).setAction(ACTION_INLINE_DATA)
					.putExtras(extras));
			finish();
			
			}
	}

	/**
	 * 缩小图片
	 * 
	 * @param bitmap
	 * @return
	 */
	private Bitmap MaxBitMap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		// 获取这个图片的宽和高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		// 定义预转换成的图片的宽度和高度
		int newWidth = 480;
		int newHeight = 480;

		// 计算缩放率，新尺寸除原始尺寸
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();

		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (mBitmap != null) {

			mBitmap.recycle();
		}
	}

	Runnable mRunFaceDetection = new Runnable() {
		@SuppressWarnings("hiding")
		float mScale = 1F;
		Matrix mImageMatrix;
		FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
		int mNumFaces;

		// For each face, we create a HightlightView for it.
		private void handleFace(FaceDetector.Face f) {

			PointF midPoint = new PointF();

			int r = ((int) (f.eyesDistance() * mScale)) * 2;
			f.getMidPoint(midPoint);
			midPoint.x *= mScale;
			midPoint.y *= mScale;

			int midX = (int) midPoint.x;
			int midY = (int) midPoint.y;

			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			RectF faceRect = new RectF(midX, midY, midX, midY);
			faceRect.inset(-r, -r);
			if (faceRect.left < 0) {
				faceRect.inset(-faceRect.left, -faceRect.left);
			}

			if (faceRect.top < 0) {
				faceRect.inset(-faceRect.top, -faceRect.top);
			}

			if (faceRect.right > imageRect.right) {
				faceRect.inset(faceRect.right - imageRect.right, faceRect.right
						- imageRect.right);
			}

			if (faceRect.bottom > imageRect.bottom) {
				faceRect.inset(faceRect.bottom - imageRect.bottom,
						faceRect.bottom - imageRect.bottom);
			}

			hv.setup(mImageMatrix, imageRect, faceRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);

			mImageView.add(hv);
		}

		// Create a default HightlightView if we found no face in the picture.
		private void makeDefault() {

			HighlightView hv = new HighlightView(mImageView);

			int width = mBitmap.getWidth();
			int height = mBitmap.getHeight();

			Rect imageRect = new Rect(0, 0, width, height);

			// make the default size about 4/5 of the width or height
			int cropWidth = Math.min(width, height) ;
			int cropHeight = height;

			/*
			 * if (mAspectX != 0 && mAspectY != 0) {
			 * 
			 * if (mAspectX > mAspectY) {
			 * 
			 * cropHeight = cropWidth * mAspectY / mAspectX; } else {
			 * 
			 * cropWidth = cropHeight * mAspectX / mAspectY; } }
			 * 
			 * int x = (width - cropWidth) / 2; int y = (height - cropHeight) /
			 * 2;
			 */

			int x = (width - cropWidth) / 2;
			int y = (height - cropWidth) / 2;

			RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
			hv.setup(mImageMatrix, imageRect, cropRect, mCircleCrop,
					mAspectX != 0 && mAspectY != 0);

			mImageView.mHighlightViews.clear(); // Thong added for rotate

			mImageView.add(hv);
		}

		// Scale the image down for faster face detection.
		private Bitmap prepareBitmap() {

			if (mBitmap == null) {

				return null;
			}

			// 256 pixels wide is enough.
			if (mBitmap.getWidth() > 256) {

				mScale = 256.0F / mBitmap.getWidth();
			}
			Matrix matrix = new Matrix();
			matrix.setScale(mScale, mScale);
			return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
					mBitmap.getHeight(), matrix, true);
		}

		public void run() {

			mImageMatrix = mImageView.getImageMatrix();
			Bitmap faceBitmap = prepareBitmap();

			mScale = 1.0F / mScale;
			if (faceBitmap != null && mDoFaceDetection) {
				FaceDetector detector = new FaceDetector(faceBitmap.getWidth(),
						faceBitmap.getHeight(), mFaces.length);
				mNumFaces = detector.findFaces(faceBitmap, mFaces);
			}

			if (faceBitmap != null && faceBitmap != mBitmap) {
				faceBitmap.recycle();
			}

			mHandler.post(new Runnable() {
				public void run() {

					mWaitingToPick = mNumFaces > 1;
					if (mNumFaces > 0) {
						for (int i = 0; i < mNumFaces; i++) {
							handleFace(mFaces[i]);
						}
					} else {
						makeDefault();
					}
					mImageView.invalidate();
					if (mImageView.mHighlightViews.size() == 1) {
						mCrop = mImageView.mHighlightViews.get(0);
						mCrop.setFocus(true);
					}

					if (mNumFaces > 1) {

					}
				}
			});
		}
	};

	public static final int NO_STORAGE_ERROR = -1;
	public static final int CANNOT_STAT_ERROR = -2;

	public static void showStorageToast(Activity activity) {

		showStorageToast(activity, calculatePicturesRemaining(activity));
	}

	public static void showStorageToast(Activity activity, int remaining) {

		String noStorageText = null;

		if (remaining == NO_STORAGE_ERROR) {

			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_CHECKING)) {

				noStorageText = "存储卡准备中";
			} else {

				noStorageText = "没有存储设备";
			}
		} else if (remaining < 1) {

			noStorageText = "存储空间不够";
		}

		if (noStorageText != null) {

			//Utility.toastGolbalMsg(activity, noStorageText);
		}
	}

	public static int calculatePicturesRemaining(Activity activity) {

		try {
			/*
			 * if (!ImageManager.hasStorage()) { return NO_STORAGE_ERROR; } else
			 * {
			 */
			String storageDirectory = "";
			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				storageDirectory = Environment.getExternalStorageDirectory()
						.toString();
			} else {
				storageDirectory = activity.getFilesDir().toString();
			}
			StatFs stat = new StatFs(storageDirectory);
			float remaining = ((float) stat.getAvailableBlocks() * (float) stat
					.getBlockSize()) / 400000F;
			return (int) remaining;
			// }
		} catch (Exception ex) {
			// if we can't stat the filesystem then we don't know how many
			// pictures are remaining. it might be zero but just leave it
			// blank since we really don't know.
			return CANNOT_STAT_ERROR;
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		BitmapManager.instance().cancelThreadDecoding(mDecodingThreads);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
}
