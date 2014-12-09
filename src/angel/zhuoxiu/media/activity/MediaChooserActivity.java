package angel.zhuoxiu.media.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import angels.zhuoxiu.media.R;
import angel.zhuoxiu.media.api.ChooserType;
import angel.zhuoxiu.media.api.ChosenImage;
import angel.zhuoxiu.media.api.ChosenVideo;
import angel.zhuoxiu.media.api.MediaChooserListener;
import angel.zhuoxiu.media.api.MediaChooserManager;

public class MediaChooserActivity extends Activity implements
		MediaChooserListener {

	private final static String TAG = "MediaChooserActivity";

	private MediaChooserManager chooserManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_chooser);
	}

	public void pickMedia(View view) {
		chooserManager = new MediaChooserManager(this,
				ChooserType.TYPE_PICK_PICTURE_OR_VIDEO);
		chooserManager.setMediaChooserListener(this);
		try {
			String path = chooserManager.choose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK
				&& requestCode == ChooserType.TYPE_PICK_PICTURE_OR_VIDEO) {
			chooserManager.submit(requestCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onImageChosen(ChosenImage image) {
		Log.i(TAG, "Image chosen: " + image.getFilePathOriginal());
	}

	@Override
	public void onError(String reason) {
		Log.i(TAG, "Error: " + reason);
	}

	@Override
	public void onVideoChosen(ChosenVideo video) {
		Log.i(TAG, "Video chosen: " + video.getVideoFilePath());
	}
}
