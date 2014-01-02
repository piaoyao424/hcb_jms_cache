package com.btten.jmsinfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.preference.PreferenceManager.OnActivityResultListener;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btten.Jms.MainActivity;
import com.btten.Jms.R;
import com.btten.account.JmsAccountManager;
import com.btten.network.NetConst;
import com.btten.network.NetSceneBase;
import com.btten.network.OnSceneCallBack;
import com.btten.network.OnUploadCallBack;
import com.btten.network.UploadUtils;
import com.btten.ui.view.BaseView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class BasicInfoView extends BaseView implements OnSceneCallBack,
		OnActivityResultListener {
	private static final String TAG = "BasicInfo";

	Button sendComm, getComm;

	MainActivity root;

	BasicInfoScene scene;

	// boolean isFirstShow = true;
	@Override
	public void OnViewShow() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnViewHide() {

	}

	@Override
	public int GetLayoutId() {
		return R.layout.basic_info;
	}

	public BasicInfoView(MainActivity root) {
		super(root);
		this.root = root;
		init();
	}

	public void init() {

		root.baseListener = this;
		sendComm = (Button) GetView().findViewById(
				R.id.basicinfo_button_send_comment);
		getComm = (Button) GetView().findViewById(
				R.id.basicinfo_button_get_comment);
		sendComm.setOnClickListener(sendListener);
		getComm.setOnClickListener(getListener);
		name = (TextView) GetView().findViewById(R.id.user_real_name);
		sex = (TextView) GetView().findViewById(R.id.user_gender);
		level = (TextView) GetView().findViewById(R.id.user_level);
		qq = (TextView) GetView().findViewById(R.id.user_qq);
		phone = (TextView) GetView().findViewById(R.id.user_phone);
		username = (TextView) GetView().findViewById(R.id.user_name);
		referrer = (TextView) GetView().findViewById(R.id.recommander);
		birthday = (TextView) GetView().findViewById(R.id.user_birthday);
		email = (TextView) GetView().findViewById(R.id.user_email);
		creditnum = (TextView) GetView().findViewById(R.id.user_creditcard);
		identitynum = (TextView) GetView().findViewById(R.id.user_identitycard);
		district = (TextView) GetView().findViewById(R.id.user_zone);
		address = (TextView) GetView().findViewById(R.id.user_address);

		headImage = (ImageView) GetView()
				.findViewById(R.id.baseinfo_head_image);
		headImage.setOnClickListener(imageListener);

		root.ShowProgress("读取数据中", "请稍候……");
		scene = new BasicInfoScene();
		//从服务器请求基本资料
		scene.doScene(this, JmsAccountManager.getInstance().getUserid());
	}

	OnClickListener sendListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
//			root.startActivity(new Intent(root, SendCommActivity.class));
		}
	};
	OnClickListener getListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
//			root.startActivity(new Intent(root, IndividualCommentActivity.class));
		}
	};

	@Override
	public void OnFailed(int status, String info, NetSceneBase netScene) {
		// TODO Auto-generated method stub
		root.HideProgress();
		root.ErrorAlert(status, info);
	}

	TextView name, sex, level, qq, phone, username, referrer, birthday, email,
			creditnum, identitynum, district, address;

	BaseInfoItem item = null;
	
	ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
	
		@Override
		public void onLoadingStarted() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingFailed(FailReason failReason) {
			// TODO Auto-generated method stub
			headImage.setImageResource(R.drawable.head_default);
		}
		
		@Override
		public void onLoadingComplete(Bitmap loadedImage) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingCancelled() {
			// TODO Auto-generated method stub
			headImage.setImageResource(R.drawable.head_default);
		}
	};
	
	DisplayImageOptions displayImageOptions = new DisplayImageOptions
			.Builder()
			.cacheInMemory()
			.imageScaleType(ImageScaleType.POWER_OF_2)
			.showImageForEmptyUri(R.drawable.head_default)
			.build();

	@Override
	public void OnSuccess(Object data, NetSceneBase netScene) {
		root.HideProgress();

		item = (BaseInfoItem) data;
		
		imageLoader.displayImage(item.photourl, headImage, displayImageOptions, imageLoadingListener);
			

		name.setText(item.name);
		if (item.sex != null && !item.sex.equals("")) {
			if (Integer.valueOf(item.sex) == 0)
				sex.setText("男");
			else if (Integer.valueOf(item.sex) == 1)
				sex.setText("女");
			}
		
		level.setText(item.level);
		qq.setText(item.qq);
		phone.setText(item.phone);
		username.setText(item.username);
		referrer.setText(item.referrer);
		birthday.setText(item.birthday);
		email.setText(item.email);
		creditnum.setText(item.creditnum);
		identitynum.setText(item.identitynum);
		district.setText(item.district);
		address.setText(item.address);
	}

	ImageView headImage;

	public static final int TAKE_SMALL_PICTURE = 2;
	public static final int CROP_SMALL_PICTURE = 4;
	public static final int CHOOSE_BIG_PICTURE = 5;
	public static final int CHOOSE_SMALL_PICTURE = 6;
	private static final String IMAGE_FILE_LOCATION = "/sdcard/calltaxi/temp/temp.jpg";
	private Uri photoUri;
	Bitmap bitmap;
	final int size = 80;
	private String IMAGE_TYPE = "image/*";

	OnClickListener imageListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headImage.setClickable(false);
			AlertDialog imageDlg = new AlertDialog.Builder(root)
					.setTitle("选择")
					.setMessage("请选择获取照片的方式!")
					.setPositiveButton("照相",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									photoUri = getOutputMediaFileUri();
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											photoUri);
									root.startActivityForResult(intent,
											TAKE_SMALL_PICTURE);
								}
							})
					.setNegativeButton("相册",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(
											Intent.ACTION_GET_CONTENT, null);
									intent.setType(IMAGE_TYPE);
									intent.putExtra("crop", "true");
									intent.putExtra("aspectX", 1);
									intent.putExtra("aspectY", 1);
									intent.putExtra("outputX", size);
									intent.putExtra("outputY", size);
									intent.putExtra("scale", true);
									intent.putExtra("return-data", true);
									intent.putExtra("outputFormat",
											Bitmap.CompressFormat.JPEG
													.toString());
									intent.putExtra("noFaceDetection", true);
									root.startActivityForResult(intent,
											CHOOSE_SMALL_PICTURE);
								}
							}).show();
			imageDlg.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					// TODO Auto-generated method stub
					headImage.setClickable(true);
				}
			});
		}
	};

	private Uri getOutputMediaFileUri() {
		return Uri.fromFile(getOutputMediaFile());
	}

	private File getOutputMediaFile() {
		File photoDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"calltaxi");
		if (!photoDir.exists()) {
			if (!photoDir.mkdirs()) {
				Log.d("mytag", "file create fail");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File photo = new File(photoDir.getPath() + File.separator + "IMG"
				+ timeStamp + ".jpg");
		return photo;
	}

	ProgressDialog progress;

	@Override
	public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode != Activity.RESULT_OK) {// result is not correct
			Log.e(TAG, "requestCode = " + requestCode);
			Log.e(TAG, "resultCode = " + resultCode);
			Log.e(TAG, "data = " + data);
			return false;
		} else {
			switch (requestCode) {

			case TAKE_SMALL_PICTURE:
				Log.i(TAG, "TAKE_SMALL_PICTURE: data = " + data);
				// TODO sent to crop
				cropImageUri(photoUri, size, size, CROP_SMALL_PICTURE);
				// or decode as bitmap and display it
				// if(imageUri != null){
				// Bitmap bitmap = decodeUriAsBitmap(imageUri);
				// imageView.setImageBitmap(bitmap);
				// }
				break;
			case CROP_SMALL_PICTURE:
				if (photoUri != null) {
					bitmap = decodeUriAsBitmap(photoUri);
					headImage.setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "CROP_SMALL_PICTURE: data = " + data);
				}
				break;
			case CHOOSE_BIG_PICTURE:
				Log.d(TAG, "CHOOSE_BIG_PICTURE: data = " + data);// it seems to
																	// be null
				if (photoUri != null) {
					bitmap = decodeUriAsBitmap(photoUri);
					headImage.setImageBitmap(bitmap);
				}
				break;
			case CHOOSE_SMALL_PICTURE:
				if (data != null) {
					bitmap = data.getParcelableExtra("data");
					headImage.setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "CHOOSE_SMALL_PICTURE: data = " + data);
				}
				break;
			default:
				break;
			}

			if (progress == null)
				progress = ProgressDialog.show(context, "上传图像中", "请稍候……", true,
						true);

			Thread thred = new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (bitmap == null)
						return;
					new UploadUtils(new OnUploadCallBack() {
						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							handler.sendEmptyMessage(ONSUCCESS);
						}

						@Override
						public void onFailure() {
							// TODO Auto-generated method stub
							handler.sendEmptyMessage(ONFAIL);
						}
					}, bitmap, "UploadPic", "doUploadPic", "userid",
					JmsAccountManager.getInstance().getUserid());

				}
			};
			if (requestCode != TAKE_SMALL_PICTURE)
				thred.start();

			return true;
		}
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		root.startActivityForResult(intent, requestCode);
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(root.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	
	public final int ONSUCCESS = 0;
	public final int ONFAIL = 1;
	Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			if (progress != null) {
				progress.dismiss();
				progress = null;
			}

			if (msg.what == ONSUCCESS)
				root.Alert("上传成功！");
			if (msg.what == ONFAIL)
				root.Alert("上传失败！");

			/*
			 * bitmap.recycle(); bitmap=null;
			 */
			super.handleMessage(msg);
		}

	};

	@Override
	public void ReFresh() {
		// TODO Auto-generated method stub
		root.ShowProgress("读取数据中", "请稍候……");
		BasicInfoScene scene = new BasicInfoScene();
		scene.doScene(this, JmsAccountManager.getInstance().getUserid());
	}
}
