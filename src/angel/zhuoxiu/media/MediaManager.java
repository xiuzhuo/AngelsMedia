package angel.zhuoxiu.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.LongSparseArray;
import android.util.Log;
import angel.zhuoxiu.media.MediaActivity.MediaMethod;
import angel.zhuoxiu.media.MediaActivity.MediaType;
import angel.zhuoxiu.media.api.ChooserType;
import angel.zhuoxiu.media.api.ChosenMedia;
import angel.zhuoxiu.media.api.MediaChooserListener;

public class MediaManager {

	static final String TAG = MediaManager.class.getSimpleName();

	static final LongSparseArray<MediaChooserListener> mediaChooserListenerArray = new LongSparseArray<MediaChooserListener>();

	static Intent getPreparedIntent(Context context) {
		Intent intent = new Intent(context, MediaActivity.class);
		long actionId = 0;
		do {
			actionId = System.currentTimeMillis();
		} while (mediaChooserListenerArray.get(actionId) != null);
		intent.putExtra(MediaActivity.KEY_CHOOSER_ID, actionId);
		return intent;
	}

	public static Intent getImageCaptureIntent(Context context) {
		Intent intent = getPreparedIntent(context);
		// intent.putExtra(MediaActivity.KEY_CHOOSER_TYPE,
		// MediaActivity.REQUEST_IMAGE_CAPTURE);
		return intent;
	}

	public static void chooseMedia(Activity activity, int type, MediaChooserListener listener) {
		Intent intent = new Intent(activity, MediaActivity.class);
		intent.putExtra(MediaActivity.KEY_CHOOSER_TYPE, type);
		if (listener!=null){
			long time=System.currentTimeMillis();
			intent.putExtra(MediaActivity.KEY_CHOOSER_ID, time);
			mediaChooserListenerArray.put(time, listener);
		}
		activity.startActivity(intent);
	}

//	public static void pickPicture(Activity activity, MediaChooserListener listener) {
//		Intent intent = new Intent(activity, MediaActivity.class);
//		intent.putExtra(MediaActivity.KEY_CHOOSER_TYPE, MediaActivity.TYPE_PICK_PICTURE);
//		intent.putExtra(MediaActivity.KEY_CHOOSER_ID, System.currentTimeMillis());
//		activity.startActivity(intent);
//	}
//
//	public static void pickVideo(Activity activity,  MediaChooserListener listener) {
//		Intent intent = new Intent(activity, MediaActivity.class);
//		intent.putExtra(MediaActivity.KEY_CHOOSER_TYPE, MediaActivity.TYPE_PICK_VIDEO);
//		intent.putExtra(MediaActivity.KEY_CHOOSER_ID, System.currentTimeMillis());
//		activity.startActivity(intent);
//	}

}
