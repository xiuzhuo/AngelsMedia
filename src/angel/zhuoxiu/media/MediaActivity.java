package angel.zhuoxiu.media;

/**
	
	<uses-feature android:name="android.hardware.camera" android:required="true" />
	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"  android:maxSdkVersion="18"/>
	
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import angel.zhuoxiu.media.api.ChooserType;
import angel.zhuoxiu.media.api.ChosenImage;
import angel.zhuoxiu.media.api.ChosenVideo;
import angel.zhuoxiu.media.api.ImageChooserManager;
import angel.zhuoxiu.media.api.MediaChooserListener;
import angel.zhuoxiu.media.api.MediaChooserManager;
import angel.zhuoxiu.media.api.VideoChooserManager;

public class MediaActivity extends Activity implements MediaChooserListener, ChooserType {
	public static enum MediaMethod {
		PICK, CAPTURE
	};

	public static enum MediaType {
		IMAGE, VIDEO
	}

	static final String TAG = MediaActivity.class.getSimpleName();
	static final String KEY_CHOOSER_TYPE = "chooser_type";
	static final String KEY_CHOOSER_ID = "chooser_id";

	MediaChooserListener mediaChooserListener;
	ImageChooserManager imageChooserManager;
	VideoChooserManager videoChooserManager;
	MediaChooserManager chooserManager;
	long chooserId;
	int chooserType;
	String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);

		chooserType = getIntent().getIntExtra(KEY_CHOOSER_TYPE, chooserType);
		chooserId = getIntent().getLongExtra(KEY_CHOOSER_ID, 0);
		mediaChooserListener = MediaManager.mediaChooserListenerArray.get(chooserId);

		switch (chooserType) {
		case ChooserType.TYPE_PICK_PICTURE:
			pickPicture();
			break;
		case ChooserType.TYPE_CAPTURE_PICTURE:
			capturePicture();
			break;
		case ChooserType.TYPE_PICK_VIDEO:
			pickVideo();
			break;
		case ChooserType.TYPE_CAPTURE_VIDEO:
			captureVideo();
			break;
		case ChooserType.TYPE_PICK_PICTURE_OR_VIDEO:
			pickMedia();
			break;
		default:
			super.finish();
		}
	}

	/*
	 * protected void doImageCapture() { Log.i(TAG, "doImageCapture"); Intent
	 * imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); if
	 * (imageCaptureIntent.resolveActivity(getPackageManager()) != null) { File
	 * photoFile = null; try { photoFile = createImageFile(); } catch
	 * (IOException ex) { ex.printStackTrace(); } if (photoFile != null) {
	 * imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	 * Uri.fromFile(photoFile)); startActivityForResult(imageCaptureIntent,
	 * REQUEST_IMAGE_CAPTURE); } else { finish(); } } }
	 */

	void captureVideo() {
		chooserType = ChooserType.TYPE_CAPTURE_VIDEO;
		videoChooserManager = new VideoChooserManager(this, ChooserType.TYPE_CAPTURE_VIDEO, "myvideofolder");
		videoChooserManager.setVideoChooserListener(this);
		try {
			// pbar.setVisibility(View.VISIBLE);
			filePath = videoChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void pickVideo() {
		chooserType = ChooserType.TYPE_PICK_VIDEO;
		videoChooserManager = new VideoChooserManager(this, ChooserType.TYPE_PICK_VIDEO);
		videoChooserManager.setVideoChooserListener(this);
		try {
			// pbar.setVisibility(View.VISIBLE);
			videoChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void capturePicture() {
		chooserType = ChooserType.TYPE_CAPTURE_PICTURE;
		imageChooserManager = new ImageChooserManager(this, ChooserType.TYPE_CAPTURE_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		try {
			// pbar.setVisibility(View.VISIBLE);
			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void pickPicture() {
		chooserType = ChooserType.TYPE_PICK_PICTURE;
		imageChooserManager = new ImageChooserManager(this, ChooserType.TYPE_PICK_PICTURE, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		try {
			// pbar.setVisibility(View.VISIBLE);
			filePath = imageChooserManager.choose();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pickMedia() {
		chooserManager = new MediaChooserManager(this, ChooserType.TYPE_PICK_PICTURE_OR_VIDEO);
		chooserManager.setMediaChooserListener(this);
		try {
			String path = chooserManager.choose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Should be called if for some reason the VideoChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeVideoChooser() {
		videoChooserManager = new VideoChooserManager(this, chooserType, "myvideofolder", true);
		videoChooserManager.setVideoChooserListener(this);
		videoChooserManager.reinitialize(filePath);
	}

	// Should be called if for some reason the ImageChooserManager is null (Due
	// to destroying of activity for low memory situations)
	private void reinitializeImageChooser() {
		imageChooserManager = new ImageChooserManager(this, chooserType, "myfolder", true);
		imageChooserManager.setImageChooserListener(this);
		imageChooserManager.reinitialize(filePath);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && (requestCode == ChooserType.TYPE_PICK_PICTURE || requestCode == ChooserType.TYPE_CAPTURE_PICTURE)) {
			if (imageChooserManager == null) {
				reinitializeImageChooser();
			}
			imageChooserManager.submit(requestCode, data);
		} else {
			// pbar.setVisibility(View.GONE);
		}
		if (resultCode == RESULT_OK && (requestCode == ChooserType.TYPE_CAPTURE_VIDEO || requestCode == ChooserType.TYPE_PICK_VIDEO)) {
			if (videoChooserManager == null) {
				reinitializeVideoChooser();
			}
			videoChooserManager.submit(requestCode, data);
		} else {
			// pbar.setVisibility(View.GONE);
		}
		if (resultCode == RESULT_OK && requestCode == ChooserType.TYPE_PICK_PICTURE_OR_VIDEO) {
			chooserManager.submit(requestCode, data);
		}

		finish();
	}

	@Override
	public void onVideoChosen(ChosenVideo video) {
		if (mediaChooserListener != null) {
			mediaChooserListener.onVideoChosen(video);
		}
	}

	@Override
	public void onImageChosen(ChosenImage image) {
		if (mediaChooserListener != null) {
			mediaChooserListener.onImageChosen(image);
		}
	}

	@Override
	public void onError(final String reason) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(MediaActivity.this, reason, Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt("chooser_type", chooserType);
		outState.putString("media_path", filePath);
		super.onSaveInstanceState(outState);
	}
}
