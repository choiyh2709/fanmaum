package specup.fanmind.common.http;

import org.json.JSONException;

public interface OnTask {
	void onTask(int output, String result) throws JSONException;
}
