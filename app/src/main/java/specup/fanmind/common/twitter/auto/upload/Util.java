package specup.fanmind.common.twitter.auto.upload;

import android.app.Activity;
import android.content.SharedPreferences;

public class Util {
	public static void setAppPreferences(Activity context, String key, String value)
	{
		SharedPreferences pref = null;
		pref = context.getSharedPreferences(Model.LOG_TAG, 0);
		SharedPreferences.Editor prefEditor = pref.edit();
		prefEditor.putString(key, value);
	
		prefEditor.commit();
	}

	public static String getAppPreferences(Activity context, String key)
	{
		String returnValue = null;
	
		SharedPreferences pref = null;
		pref = context.getSharedPreferences(Model.LOG_TAG, 0);
	
		returnValue = pref.getString(key, "");
	
		return returnValue;
	}	
}
