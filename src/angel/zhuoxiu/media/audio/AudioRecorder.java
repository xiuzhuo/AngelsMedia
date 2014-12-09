package angel.zhuoxiu.media.audio;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import android.R.integer;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;

public class AudioRecorder {
	static final String TAG = AudioRecorder.class.getSimpleName();
	static AudioRecorder mAudioRecorder = null;

	static final int MESSAGE_START = 0;
	static final int MESSAGE_STOP = 1;
	static final int MESSAGE_TIMMING = 2;

	public interface AudioRecordListener { 
		public void onStart();

		public void onTiming(long currentTime, long totalTime);

		public void onStop(File audioFile);

		public void onError(String message);
	}

	boolean isRecording = false, isPlaying = false;
	MediaRecorder mRecorder;
	MediaPlayer mPlayer;
	AudioRecordListener mListener;
	File mAudioFile;
	Context mContext;
	Handler mHandler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_START:
				mListener.onStart();
				break;
			case MESSAGE_TIMMING:
				mListener.onTiming(msg.arg1, msg.arg2);
				break;
			case MESSAGE_STOP:
				mListener.onStop(mAudioFile);
				clean();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public static AudioRecorder getInstance(Context context, AudioRecordListener listener) {
		if (mAudioRecorder == null) {
			mAudioRecorder = new AudioRecorder();
		}
		mAudioRecorder.setAudioRecordListener(listener);
		mAudioRecorder.mContext = context;
		return mAudioRecorder;
	}

	private AudioRecorder() {
	}

	public void setAudioRecordListener(AudioRecordListener listener) {
		mListener = listener;
	}

	public void startRecording() {
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(generateFile().getAbsolutePath());
		Log.i(TAG, mAudioFile.getAbsolutePath());
		try {
			mRecorder.prepare();
			mRecorder.start();
			isRecording = true;
			Message msg = new Message();
			msg.what = MESSAGE_START;
			mHandler.sendMessage(msg);
			startRecordTimerThread();
		} catch (IOException e) {
			Log.e(TAG, "prepare() failed " + e.getMessage());
			mListener.onError("prepare() failed " + e.getMessage());
		}
	}

	public void stopRecording() {
		if (mRecorder != null && isRecording) {
			mRecorder.stop(); 
			mRecorder.release();
			isRecording = false;
			Message msg = new Message();
			msg.what = MESSAGE_STOP;
			mHandler.sendMessage(msg);
		}
	}

	private void startRecordTimerThread() {
		new Thread() {
			long startTime = System.currentTimeMillis();

			public void run() {
				while (isRecording) {
					long currentTime = System.currentTimeMillis() - startTime;
					Message msg = new Message();
					msg.what = MESSAGE_TIMMING;
					msg.arg1 = (int) currentTime;
					msg.arg2 = (int) currentTime;
					mHandler.sendMessage(msg);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	private void clean() {
		mRecorder = null;
		mAudioFile = null;
	}

	private File generateFile() {
		mAudioFile = new File(mContext.getCacheDir(), System.currentTimeMillis() + ".amr");
		return mAudioFile;
	}

}
