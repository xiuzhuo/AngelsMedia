package angels.zhuoxiu.media.audio;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import angels.zhuoxiu.media.R;

public class AudioPlayBar extends FrameLayout {
	static final String TAG = AudioPlayBar.class.getSimpleName();

	public AudioPlayBar(Context context) {
		this(context, null);
	}

	public AudioPlayBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.i(TAG, "AudioPlayBar 0");
		inflate(context, R.layout.audio_play_bar, this);
		Log.i(TAG, "AudioPlayBar 1");
	}

}
