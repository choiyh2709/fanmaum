package specup.fanmind.common.Util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {


	private OnTextLengthListener onTextLengthListener = null;

	public void setOnTextLengthListener(OnTextLengthListener listener) {
		onTextLengthListener = listener;
	}

	public interface OnTextLengthListener {

		public abstract void onTextLength(int length);
	}
	
	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomEditText(Context context) {
		super(context);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before, int after) {
		if(onTextLengthListener != null)
			onTextLengthListener.onTextLength(text.length());
		super.onTextChanged(text, start, before, after);
	}

}
