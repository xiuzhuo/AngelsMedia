package angels.zhuoxiu.media.audio;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import angels.zhuoxiu.media.R;

/**
 * Created by zxui on 19/02/15.
 */
public class AudioRecordDialog extends Dialog {
    static final String TAG = AudioRecordDialog.class.getSimpleName();

    View[] volumes = new View[6];
    TextView recordTime;
    int lastIndex = 0;

    public AudioRecordDialog(Context context) {
        this(context, R.style.AudioRecordDialog);
        View contentView = getLayoutInflater().inflate(R.layout.audio_record_dialog, null);
        volumes[0] = null;
        volumes[1] = contentView.findViewById(R.id.audio_record_volume_1);
        volumes[2] = contentView.findViewById(R.id.audio_record_volume_2);
        volumes[3] = contentView.findViewById(R.id.audio_record_volume_3);
        volumes[4] = contentView.findViewById(R.id.audio_record_volume_4);
        volumes[5] = contentView.findViewById(R.id.audio_record_volume_5);
        recordTime = (TextView) contentView.findViewById(R.id.record_time);
        setContentView(contentView);
    }

    public AudioRecordDialog(Context context, int theme) {
        super(context, theme);
    }

    public void setRecordVolume(int index) {
        if (index == lastIndex) {
            return;
        }
        lastIndex = index;
        for (int i = 1; i < volumes.length; i++) {
            try {
                volumes[i].setAlpha(i <= index ? 0.8f : 0.2f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static final DecimalFormat df = new DecimalFormat("##0.0");

    public void setRecordTime(long ms) {
        if (ms == 0) {
            recordTime.setText(null);
        } else {
            recordTime.setText(df.format(ms / 1000f) + "s");
        }
    }

}
