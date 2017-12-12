package specup.fanmind.fanmindsetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class StarSetting {

    public static final String STAR_SETTING = "STAR_SETTING";

    /**
     * 스타리스트 이름
     *
     * @param context
     * @param STAR_LIST 이름+, 값으로 저장.
     */
    public static final String PREF_KEY_STAR_LIST = "STAR_LIST";

    public static void setSTAR_LIST(Context context, String STAR_LIST) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_LIST, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_LIST);
        editor.commit();
    }

    public static String getSTAR_LIST(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_LIST, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }

    /**
     * 스타리스트 인덱스
     *
     * @param context
     * @param STAR_LIST_INDEX 인덱스+, 값으로 저장.
     */
    public static final String PREF_KEY_STAR_LIST_INDEX = "STAR_LIST_INDEX";

    public static void setSTAR_LIST_INDEX(Context context, String STAR_LIST_INDEX) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_LIST_INDEX, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_LIST_INDEX);
        editor.commit();
    }

    public static String getSTAR_LIST_INDEX(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_LIST_INDEX, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }

    /**
     * 현재 선택되어진 스타리스트 인덱스
     *
     * @param context
     * @param STAR_SELECT_INDEX 인덱스+, 값으로 저장.
     */
    public static final String PREF_KEY_STAR_SELECT_INDEX = "STAR_SELECT_INDEX";

    public static void setSTAR_SELECT_INDEX(Context context, String STAR_SELECT_INDEX) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_INDEX);
        editor.commit();
    }

    public static String getSTAR_SELECT_INDEX(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "1");
    }

    /**
     * 현재 선택되어진 스타리스트 이름
     *
     * @param context
     * @param 이름
     */
    public static final String PREF_KEY_STAR_SELECT_NAME = "STAR_SELECT_NAME";

    public static void setSTAR_SELECT_NAME(Context context, String STAR_SELECT_NAME) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_NAME);
        editor.commit();
    }

    public static String getSTAR_SELECT_NAME(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }


    /**
     * newsfeed 선택되어진 스타리스트 인덱스
     *
     * @param context
     * @param STAR_SELECT_INDEX 인덱스+, 값으로 저장.
     */
    public static final String PREF_KEY_Newsfeed_STAR_SELECT_INDEX = "NewsfeedSTAR_SELECT_INDEX";

    public static void setNewsfeedSTAR_SELECT_INDEX(Context context, String STAR_SELECT_INDEX) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_Newsfeed_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_INDEX);
        editor.commit();
    }

    public static String getNewsfeedSTAR_SELECT_INDEX(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_Newsfeed_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }

    /**
     * newsfeed 선택되어진 스타리스트 이름
     *
     * @param context
     * @param 이름
     */
    public static final String PREF_Newsfeed_KEY_STAR_SELECT_NAME = "NewsfeedSTAR_SELECT_NAME";

    public static void setNewsfeedSTAR_SELECT_NAME(Context context, String STAR_SELECT_NAME) {
        SharedPreferences pref = context.getSharedPreferences(PREF_Newsfeed_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_NAME);
        editor.commit();
    }

    public static String getNewsfeedSTAR_SELECT_NAME(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_Newsfeed_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }


    /**
     * project 선택되어진 스타리스트 인덱스
     *
     * @param context
     * @param STAR_SELECT_INDEX 인덱스+, 값으로 저장.
     */
    public static final String PREF_KEY_PROJECT_STAR_SELECT_INDEX = "PROJECT_STAR_SELECT_INDEX";

    public static void setProjectSTAR_SELECT_INDEX(Context context, String STAR_SELECT_INDEX) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PROJECT_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_INDEX);
        editor.commit();
    }

    public static String getProjectSTAR_SELECT_INDEX(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PROJECT_STAR_SELECT_INDEX, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }

    /**
     * newsfeed 선택되어진 스타리스트 이름
     *
     * @param context
     * @param 이름
     */
    public static final String PREF_PROJECT_KEY_STAR_SELECT_NAME = "PROJECT_STAR_SELECT_NAME";

    public static void setProjectSTAR_SELECT_NAME(Context context, String STAR_SELECT_NAME) {
        SharedPreferences pref = context.getSharedPreferences(PREF_PROJECT_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(STAR_SETTING, STAR_SELECT_NAME);
        editor.commit();
    }

    public static String getProjectSTAR_SELECT_NAME(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_PROJECT_KEY_STAR_SELECT_NAME, Activity.MODE_PRIVATE);
        return pref.getString(STAR_SETTING, "");
    }
}
