package angels.zhuoxiu.media.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AudioRecorder {
    static final String TAG = AudioRecorder.class.getSimpleName();
    static AudioRecorder mAudioRecorder = null;

    static final int MESSAGE_START = 0;
    static final int MESSAGE_STOP = 1;
    static final int MESSAGE_TIMMING = 2;

    public interface AudioRecordListener {
        public void onStart();

        public void onTiming(long currentTime, int level);

        public void onStop(File audioFile);

        public void onError(String message);
    }

    boolean isRecording = false, isPlaying = false;
    MediaRecorder mediaRecorder;
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
        if (mediaRecorder==null){
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        }else{
            stopRecording();
        }
        mediaRecorder.setOutputFile(generateFile().getAbsolutePath());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            mListener.onError("prepare() failed " + e.getMessage());
            return;
        }
        mediaRecorder.start();
        isRecording = true;
        Message msg = new Message();
        msg.what = MESSAGE_START;
        mHandler.sendMessage(msg);
        startRecordTimerThread();
    }



    public void stopRecording() {
        if (mediaRecorder != null && isRecording) {
            mediaRecorder.stop();
            mediaRecorder.reset();
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
                    msg.arg2 = mediaRecorder.getMaxAmplitude();
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    private void clean() {
        mediaRecorder = null;
        mAudioFile = null;
    }

    private File generateFile() {
        mAudioFile = new File(Environment.getExternalStorageDirectory(),  System.currentTimeMillis() + ".aac");
        return mAudioFile;
    }

    public boolean isRecording(){
        return (mediaRecorder!=null)&&(isRecording);
    }
}
