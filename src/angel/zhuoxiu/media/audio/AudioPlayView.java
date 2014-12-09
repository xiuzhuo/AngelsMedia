package angel.zhuoxiu.media.audio;

import java.io.File;
import java.text.DecimalFormat;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import angels.zhuoxiu.media.R;

public class AudioPlayView extends FrameLayout {
	static final String TAG = AudioPlayView.class.getSimpleName();
	static final DecimalFormat df = new DecimalFormat("##0.0");
	ImageButton playButton, pauseButton;
	MediaPlayer mediaPlayer; 
	File mAudioFile;
	SeekBar seekBar;
	boolean isPrepared = false;
	boolean isAlive = true;
	Thread timingThread;
	TextView currentTime, totalTime;
	Handler handler;
	OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == playButton) {
				play();
			} else if (v == pauseButton) {
				pause();
			}
		}
	};

	public AudioPlayView(Context context) {
		this(context, null);
	}

	public AudioPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler=new Handler();
		inflate(context, R.layout.audio_play_view, this);
		playButton = (ImageButton) findViewById(R.id.audio_button_play);
		pauseButton = (ImageButton) findViewById(R.id.audio_button_pause);
		seekBar = (SeekBar) findViewById(R.id.audio_seekerbar);
		currentTime = (TextView) findViewById(R.id.audio_current_time);
		totalTime = (TextView) findViewById(R.id.audio_total_time);

		playButton.setOnClickListener(onClickListener);
		pauseButton.setOnClickListener(onClickListener);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser) {
					changeProgress(progress);
				}
			}
		});
	}

	public void setAudioFile(File file) {
		if (mAudioFile == file) {
			return;
		}
		isPrepared = false;
		if (mediaPlayer != null) {
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.release();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mediaPlayer = null;
			seekBar.setMax(0);
		}
		Log.i(TAG, "setAudioFile " + file);

		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					pause();
					mediaPlayer.seekTo(0);
				}
			});
			seekBar.setProgress(0);
			seekBar.setMax(mediaPlayer.getDuration());
			setTotalTime(mediaPlayer.getDuration());
			isPrepared = true;
			timingThread = new Thread() {
				public void run() {
					while (isPrepared) {
						seekBar.setProgress(mediaPlayer.getCurrentPosition());
						handler.post(new Runnable() {
							@Override
							public void run() {
								setCurrentTime(mediaPlayer.getCurrentPosition());
							}
						});
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

				};
			};
			timingThread.start();

		} catch (Exception e) {
			if (mediaPlayer != null) {
				mediaPlayer.release();
				mediaPlayer = null;
			}
			e.printStackTrace();
		}
		pause();
	}

	void play() {
		if (isPrepared) {
			mediaPlayer.start();
		}
		playButton.setVisibility(GONE);
		pauseButton.setVisibility(VISIBLE);
	}

	void pause() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.pause(); 
			playButton.bringToFront();
		}
		playButton.setVisibility(VISIBLE);
		pauseButton.setVisibility(GONE);
	}

	void changeProgress(int progress) {
		if (mediaPlayer != null && isPrepared) {
			mediaPlayer.seekTo(progress);
			if (progress != 0 && !mediaPlayer.isPlaying()) {
				mediaPlayer.start();
			}
		}
	}

	void setCurrentTime(int ms){
		currentTime.setText(df.format(ms/1000f)+"s");
	}
	
	void setTotalTime(int ms){
		totalTime.setText(df.format(ms/1000f));
	}
	
	void destroy() {
		isAlive = false;
	}

}
