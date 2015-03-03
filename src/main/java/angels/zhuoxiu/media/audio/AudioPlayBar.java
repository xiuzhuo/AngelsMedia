package angels.zhuoxiu.media.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.IOException;

import angels.zhuoxiu.media.R;

public class AudioPlayBar extends FrameLayout {
    static final String TAG = AudioPlayBar.class.getSimpleName();
    ImageButton playButton, pauseButton;
    MediaPlayer mediaPlayer;
    String path;
    boolean prepared = false;
    OnClickListener mediaButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == playButton ) {
                if (!prepared) {
                    mediaPlayer.reset();
                    try {
                        mediaPlayer.setDataSource(path);
                        prepared = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                }

            } else if (v == pauseButton) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    };

    public AudioPlayBar(Context context) {
        this(context, null);
    }

    public AudioPlayBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.audio_play_bar, this);
        playButton = (ImageButton) findViewById(R.id.audio_button_play);
        pauseButton = (ImageButton) findViewById(R.id.audio_button_pause);
        mediaPlayer = new MediaPlayer();
        playButton.setOnClickListener(mediaButtonOnClickListener);
    }

    public void setAudioDataSource(String path) {
        if (path != null && !path.equals(this.path)) {
            this.path = path;
            prepared = false;
        }

    }

}
