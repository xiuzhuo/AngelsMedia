package angel.zhuoxiu.media.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.ImageButton;
import angels.zhuoxiu.media.R;

public class MediaButton extends ImageButton {

	static final String TAG = MediaButton.class.getSimpleName();

	int imageResPressed, imageResActive, imageResNormal;

	public MediaButton(Context context) {
		this(context, null);
	}

	public MediaButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MediaButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			TypedArray styled = context.obtainStyledAttributes(attrs, R.styleable.MediaButton);
			imageResActive = styled.getResourceId(R.styleable.MediaButton_srcActive, 0);
			imageResPressed = styled.getResourceId(R.styleable.MediaButton_srcPressed, 0);
			imageResNormal = styled.getResourceId(R.styleable.MediaButton_srcNormal, 0);
			styled.recycle();
			StateListDrawable stateListDrawable = new StateListDrawable();
			if (imageResActive != 0) {
				stateListDrawable.addState(new int[] { android.R.attr.state_active },
						getResources().getDrawable(imageResActive));
			}
			if (imageResPressed != 0) {
				stateListDrawable.addState(new int[] { android.R.attr.state_pressed },
						getResources().getDrawable(imageResPressed));
			}
			if (imageResNormal != 0) {
				stateListDrawable.addState(new int[] {}, getResources().getDrawable(imageResNormal));
			}
			setImageDrawable(stateListDrawable);
		}
	}

}
