package angel.zhuoxiu.media.video;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;
import angel.zhuoxiu.media.ui.MediaButton;
import angels.zhuoxiu.media.R;

public class VideoPlayView extends FrameLayout {
	static final String TAG = VideoPlayView.class.getSimpleName();
	public VideoView videoView;
	MediaButton playButton, pauseButton;
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i(TAG, "onClick");
			if (v == playButton) {
				play();
			} else if (v == pauseButton) {
				pause();
			}
		}
	};
	OnCompletionListener onCompletionListener=new OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			pause();
		}
	};
	public VideoPlayView(Context context) { 
		this(context, null);
	}
  
	public VideoPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.video_play_view, this);
		videoView = (VideoView) findViewById(R.id.video_play_videoView);
		playButton = (MediaButton) findViewById(R.id.video_play_playButton);
		pauseButton = (MediaButton) findViewById(R.id.video_play_pauseButton);
		playButton.setOnClickListener(onClickListener);
		pauseButton.setOnClickListener(onClickListener); 
		videoView.setOnCompletionListener(onCompletionListener);

	}
 
	void play() {
		videoView.start();
		playButton.setVisibility(INVISIBLE);
		pauseButton.setVisibility(VISIBLE);
		pauseButton.bringToFront();
	}

	void pause() {
		videoView.pause();
		pauseButton.setVisibility(INVISIBLE);
		playButton.setVisibility(VISIBLE);
		playButton.bringToFront();
	}

	public void setVideoPath(String path) {
		videoView.setVideoPath(path);
	}

	public void setVideoUri(Uri uri) {
		videoView.setVideoURI(uri);
	}
	
	public void stopPlayBack(){
		videoView.stopPlayback();
	}

}
