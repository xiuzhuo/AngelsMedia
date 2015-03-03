package angels.zhuoxiu.media.audio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DecimalFormat;

import angels.zhuoxiu.media.R;
import angels.zhuoxiu.media.audio.AudioRecorder.AudioRecordListener;

public class AudioRecordView extends FrameLayout {

    static final String TAG = AudioRecorder.class.getSimpleName();
    static final DecimalFormat df = new DecimalFormat("##0.0");

    ImageView mRecordButton;
    TextView mTimeText;
    AudioRecorder mRecorder;
    AudioRecordListener mListener;
    TextView recordTime;

    public AudioRecordView(Context context) {
        this(context, null);

    }

    @SuppressLint("NewApi")
    public AudioRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.audio_record_box, this);
        mRecordButton = (ImageView) findViewById(R.id.record_button);
        mTimeText = (TextView) findViewById(R.id.record_time);
        mRecorder = AudioRecorder.getInstance(context, new AudioRecordListener() {
            @Override
            public void onTiming(long currentTime, int level) {
                setRecordTime(currentTime);
                mListener.onTiming(currentTime, level);
            }

            @Override
            public void onStop(File audioFile) {
                if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    mRecordButton.setAlpha(0.8f);
                }
                setRecordTime(0);
                mListener.onStop(audioFile);
            }

            @Override
            public void onStart() {
                if (VERSION.SDK_INT >= VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    mRecordButton.setAlpha(1f);
                    mListener.onStart();
                }
            }

            @Override
            public void onError(String message) {
                Log.i(TAG, message);
            }
        });
        mRecordButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mRecorder.startRecording();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mRecorder.stopRecording();
                        break;
                }
                return true;
            }
        });
    }

    public void setAudioRecordListener(AudioRecordListener listener) {
        mListener = listener;
    }

    public void setRecordTime(long ms) {
        if (ms == 0) {
            mTimeText.setText(null);
        } else {
            mTimeText.setText(df.format(ms / 1000f) + "s");
        }
    }

}
