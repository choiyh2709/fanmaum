package specup.fanmind.fanmindsetting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class FanMindSetting {
    public static final String FANMIND_SETTING = "FANMIND_SETTING";

    /**
     * 기본 URL저장
     *
     * @param context
     * @param BASE_URL
     */
    public static final String PREF_KEY_BASE_URL = "BASE_URL";

    public static void setBASE_URL(Context context, String BASE_URL) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BASE_URL, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, BASE_URL);
        editor.commit();
    }

    public static String getBASE_URL(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BASE_URL, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }


    /**
     * 이메일 아이디 저장
     *
     * @param context
     * @param EMAIL_ID 이메일
     */
    public static final String PREF_KEY_EMAIL_ID = "EMAIL_ID";

    public static void setEMAIL_ID(Context context, String EMAIL_ID) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_EMAIL_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, EMAIL_ID);
        editor.commit();
    }

    public static String getEMAIL_ID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_EMAIL_ID, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }


    /**
     * 패스워드 설정
     *
     * @param context
     * @param PASSWORD 패스워드
     */

    public static final String PREF_KEY_PASSWORD = "PASSWORD";

    public static void setPASSWORD(Context context, String PASSWORD) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PASSWORD, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, PASSWORD);
        editor.commit();
    }

    public static String getPASSWORD(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PASSWORD, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    /**
     * 팬 마음 로그인 상태
     *
     * @param context
     * @param LOGIN_OK
     */
    public static final String PREF_KEY_LOGIN_OK = "LOGIN_OK";

    public static void setLOGIN_OK(Context context, boolean LOGIN_OK) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOGIN_OK, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOGIN_OK);
        editor.commit();
    }

    public static boolean getLOGIN_OK(Context context) {
        try {

            SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOGIN_OK, Activity.MODE_PRIVATE);
            return pref.getBoolean(FANMIND_SETTING, false);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }
    /**
     * 팬 마음 로그인 처음
     *
     * @param context
     * @param LOGIN_OK
     */
    public static final String PREF_KEY_LOGIN_FIRST = "LOGIN_FIRST";

    public static void setLOGIN_FIRST(Context context, boolean LOGIN_FIRST) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOGIN_FIRST, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOGIN_FIRST);
        editor.commit();
    }

    public static boolean getLOGIN_FIRST(Context context) {
        try {

            SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOGIN_FIRST, Activity.MODE_PRIVATE);
            return pref.getBoolean(FANMIND_SETTING, false);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }


    /**
     * 앱  첫번째
     *
     * @param context
     * @param APP_FIRST
     */
    public static final String PREF_KEY_APP_FIRST = "APP_FIRST";

    public static void setAPP_FIRST(Context context, boolean APP_FIRST) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_APP_FIRST, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, APP_FIRST);
        editor.commit();
    }

    public static boolean getAPP_FIRST(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_APP_FIRST, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }


    /**
     * 달력뷰 크기
     *
     * @param context
     * @param OK true = > 6줄, false =>5줄
     */
    public static final String PREF_CAL_WEEK = "PREF_CAL_WEEK ";

    public static void setCAL_WEEK(Context context, boolean OK) {
        SharedPreferences pref = context.getSharedPreferences(PREF_CAL_WEEK, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, OK);
        editor.commit();
    }

    public static boolean getCAL_WEEK(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_CAL_WEEK, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }


    /**
     * 세션키 저장.
     *
     * @param context
     * @param SESSION_KEY 세션키
     */
    public static final String PREF_KEY_SESSION_KEY = "SESSION_KEY";

    public static void setSESSION_KEY(Context context, String SESSION_KEY) {

        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_SESSION_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, SESSION_KEY);
        editor.commit();
    }

    public static String getSESSION_KEY(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_SESSION_KEY, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    /**
     * 닉네임 저장
     *
     * @param context
     * @param NICK_NAME
     */
    public static final String PREF_KEY_NICK_NAME = "NICK_NAME";

    public static void setNICK_NAME(Context context, String NICK_NAME) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_NICK_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, NICK_NAME);
        editor.commit();
    }

    public static String getNICK_NAME(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_NICK_NAME, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "GUEST");
    }

    /**
     * 회원 번호 저장
     *
     * @param context
     * @param NICK_NAME
     */
    public static final String PREF_KEY_MEMBER_SRL = "MEMBER_SRL";

    public static void setMEMBER_SRL(Context context, String MEMBER_SRL) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MEMBER_SRL, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, MEMBER_SRL);
        editor.commit();
    }

    public static String getMEMBER_SRL(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MEMBER_SRL, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "0");
    }

    /**
     * 나의 마음 보유수
     *
     * @param context
     * @param MY_HEART
     */
    public static final String PREF_KEY_MY_HEART = "MY_HEART";

    public static void setMY_HEART(Context context, String MY_HEART) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MY_HEART, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, MY_HEART);
        editor.commit();
    }

    public static String getMY_HEART(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MY_HEART, Activity.MODE_PRIVATE);

        return pref.getString(FANMIND_SETTING, "0");
    }

    /**
     * 나의 프로필 URL
     *
     * @param context
     * @param MY_PIC
     */
    public static final String PREF_KEY_MY_PIC = "MY_PIC";

    public static void setMY_PIC(Context context, String MY_PIC) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MY_PIC, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, MY_PIC);
        editor.commit();
    }

    public static String getMY_PIC(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_MY_PIC, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "null");
    }


    /**
     * 잠금화면 설정
     *
     * @param context
     * @param LOCKSCREEN
     */
    public static final String PREF_KEY_LOCKSCREEN = "LOCKSCREEN";

    public static void setLOCKSCREEN(Context context, boolean LOCKSCREEN) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOCKSCREEN);
        editor.commit();
    }

    public static boolean getLOCKSCREEN(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN, Activity.MODE_PRIVATE);
//        return false;
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 잠금화면 이미지 섞기
     *
     * @param context
     * @param RANDOM_IMAGE
     */
    public static final String PREF_KEY_RANDOM_IMAGE = "RANDOM_IMAGE";

    public static void setRANDOM_IMAGE(Context context, boolean RANDOM_IMAGE) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_RANDOM_IMAGE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, RANDOM_IMAGE);
        editor.commit();
    }

    public static boolean getRANDOM_IMAGE(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_RANDOM_IMAGE, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }

    /**
     * 잠금화면 메시지
     *
     * @param context
     * @param TALK_MSG
     */
    public static final String PREF_KEY_TALK_MSG = "TALK_MSG";

    public static void setTALK_MSG(Context context, boolean TALK_MSG) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_TALK_MSG, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, TALK_MSG);
        editor.commit();
    }

    public static boolean getTALK_MSG(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_TALK_MSG, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }

    /**
     * 내소식알림
     *
     * @param context
     * @param ALERT_MYNEWS
     */
    public static final String PREF_KEY_ALERT_MYNEWS = "ALERT_MYNEWS";

    public static void setALERT_MYNEWS(Context context, boolean ALERT_MYNEWS) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_MYNEWS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, ALERT_MYNEWS);
        editor.commit();
    }

    public static boolean getALERT_MYNEWS(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_MYNEWS, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }

    /**
     * 팬마음 알림
     *
     * @param context
     * @param ALERT_FANMIND
     */
    public static final String PREF_KEY_ALERT_FANMIND = "ALERT_FANMIND";

    public static void setALERT_FANMIND(Context context, boolean ALERT_FANMIND) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_FANMIND, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, ALERT_FANMIND);
        editor.commit();
    }

    public static boolean getALERT_FANMIND(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_FANMIND, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }

    /**
     * 적립 알림
     *
     * @param context
     * @param ALERT_SAVE
     */
    public static final String PREF_KEY_ALERT_SAVE = "ALERT_SAVE";

    public static void setALERT_SAVE(Context context, boolean ALERT_SAVE) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_SAVE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, ALERT_SAVE);
        editor.commit();
    }

    public static boolean getALERT_SAVE(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_ALERT_SAVE, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, true);
    }


    /**
     * 메인 헬프팝업
     *
     * @param context
     * @param HELP_POPUP
     */
    public static final String PREF_KEY_HELP_POPUP = "HELP_POPUP";

    public static void setHELP_POPUP(Context context, boolean HELP_POPUP) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_HELP_POPUP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, HELP_POPUP);
        editor.commit();
    }

    public static boolean getHELP_POPUP(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_HELP_POPUP, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 팬마음 잠금화면 헬프화면
     *
     * @param context
     * @param LOCKSCREEN_HELP
     */
    public static final String PREF_KEY_LOCKSCREEN_HELP = "LOCKSCREEN_HELP";

    public static void setLOCKSCREEN_HELP(Context context, boolean LOCKSCREEN_HELP) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_HELP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOCKSCREEN_HELP);
        editor.commit();
    }

    public static boolean getLOCKSCREEN_HELP(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_HELP, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }


    /**
     * 팬마음 잠금화면 헬프화면광고
     *
     * @param context
     * @param LOCKSCREEN_HELPAD
     */
    public static final String PREF_KEY_LOCKSCREEN_HELPAD = "LOCKSCREEN_HELPAD";

    public static void setLOCKSCREEN_HELPAD(Context context, boolean LOCKSCREEN_HELPAD) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_HELPAD, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOCKSCREEN_HELPAD);
        editor.commit();
    }

    public static boolean getLOCKSCREEN_HELPAD(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_HELPAD, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }


    /**
     * 팬마음 잠금화면 광고화면
     *
     * @param context
     * @param LOCKSCREEN_AD
     */
    public static final String PREF_KEY_LOCKSCREEN_AD = "LOCKSCREEN_AD";

    public static void setLOCKSCREEN_AD(Context context, boolean LOCKSCREEN_AD) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_AD, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOCKSCREEN_AD);
        editor.commit();
    }

    public static boolean getLOCKSCREEN_AD(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCKSCREEN_AD, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }


    /**
     * 푸쉬 아이디
     *
     * @param context
     * @param GCM_ID
     */
    public static final String PREF_KEY_GCM_ID = "GCM_ID";

    public static void setGCM_ID(Context context, String GCM_ID) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_GCM_ID, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, GCM_ID);
        editor.commit();
    }

    public static String getGCM_ID(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_GCM_ID, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    /**
     * 푸쉬 지나가다.
     *
     * @param context
     * @param PUSH_STAY
     */
    public static final String PREF_KEY_PUSH_STAY = "PUSH_STAY";

    public static void setPUSH_STAY(Context context, boolean PUSH_STAY) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PUSH_STAY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, PUSH_STAY);
        editor.commit();
    }

    public static boolean getPUSH_STAY(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_PUSH_STAY, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 배너팝업
     *
     * @param context
     * @param BANNER
     */
    public static final String PREF_KEY_BANNER = "BANNER";

    public static void setBANNER(Context context, boolean BANNER) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BANNER, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, BANNER);
        editor.commit();
    }

    public static boolean getBANNER(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BANNER, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 푸쉬 아이디
     *
     * @param context
     * @param BANNER_INFO
     */
    public static final String PREF_KEY_BANNER_INFO = "BANNER_INFO";

    public static void setBANNER_INFO(Context context, String BANNER_INFO) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BANNER_INFO, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, BANNER_INFO);
        editor.commit();
    }

    public static String getBANNER_INFO(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_BANNER_INFO, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    /**
     * 리뷰팝업
     *
     * @param context
     * @param REVIEW_POPUP
     */
    public static final String PREF_KEY_REVIEW_POPUP = "REVIEW_POPUP";

    public static void setREVIEW_POPUP(Context context, boolean REVIEW_POPUP) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_REVIEW_POPUP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, REVIEW_POPUP);
        editor.commit();
    }

    public static boolean getREVIEW_POPUP(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_REVIEW_POPUP, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 락팝업
     *
     * @param context
     * @param LOCK_POPUP
     */
    public static final String PREF_KEY_LOCK_POPUP = "LOCK_POPUP";

    public static void setLOCK_POPUP(Context context, boolean LOCK_POPUP) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCK_POPUP, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, LOCK_POPUP);
        editor.commit();
    }

    public static boolean getLOCK_POPUP(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_LOCK_POPUP, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
     * 마음적립하러가기창
     *
     * @param context
     * @param SAVEMIND_EXIT
     */
    public static final String PREF_KEY_SAVEMIND_EXIT = "SAVEMIND_EXIT";

    public static void setSAVEMIND_EXIT(Context context, boolean SAVEMIND_EXIT) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_SAVEMIND_EXIT, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, SAVEMIND_EXIT);
        editor.commit();
    }

    public static boolean getSAVEMIND_EXIT(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_KEY_SAVEMIND_EXIT, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }


    /**
     * 출석체크 저장
     *
     * @param context
     * @param DAILY_CHECK
     */

    public static void setDAILY_CHECK(Context context, String DAILY_CHECK, boolean index) {
        String strNAME = "DAILY_CHECK" + FanMindSetting.getEMAIL_ID(context) + DAILY_CHECK;
        SharedPreferences pref = context.getSharedPreferences(strNAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(strNAME, index);
        editor.commit();
    }


    public static boolean getDAILY_CHECK(Context context, String index) {
        String strNAME = "DAILY_CHECK" + FanMindSetting.getEMAIL_ID(context) + index;
        SharedPreferences pref = context.getSharedPreferences(strNAME, Activity.MODE_PRIVATE);
        return pref.getBoolean(strNAME, false);
    }


    /**
     * 사용자 주소
     *
     * @param context
     * @param SAVEMIND_EXIT
     */
    public static final String ADDRESS = "ADDRESS";

    public static void setADDRESS(Context context, String address) {
        SharedPreferences pref = context.getSharedPreferences(ADDRESS, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, address);
        editor.commit();
    }

    public static String getADDRESS(Context context) {
        SharedPreferences pref = context.getSharedPreferences(ADDRESS, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    /**
     * 연동 유무
     *
     * @param context
     * @param SAVEMIND_EXIT
     */
    public static final String IS_LINK = "IS_LINK";

    public static void setIsLinked(Context context, boolean isLink) {
        SharedPreferences pref = context.getSharedPreferences(IS_LINK, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(FANMIND_SETTING, isLink);
        editor.commit();
    }

    public static boolean getIsLinked(Context context) {
        SharedPreferences pref = context.getSharedPreferences(IS_LINK, Activity.MODE_PRIVATE);
        return pref.getBoolean(FANMIND_SETTING, false);
    }

    /**
    * 연예인 리스트 저장
    * */
    public static final String STAR_LIST = "STAR_LIST";

    public static void setStarList(Context context, String date){
        SharedPreferences pref = context.getSharedPreferences(STAR_LIST, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(FANMIND_SETTING, date);
        editor.commit();
    }

    public static String getStarList(Context context){
        SharedPreferences pref = context.getSharedPreferences(STAR_LIST, Activity.MODE_PRIVATE);
        return pref.getString(FANMIND_SETTING, "");
    }

    public static void setLogout(Context context) {
        FanMindSetting.setLOGIN_OK(context, false);
        FanMindSetting.setMY_PIC(context, "null");
        FanMindSetting.setADDRESS(context, "null");
        StarSetting.setNewsfeedSTAR_SELECT_NAME(context, "");
        StarSetting.setNewsfeedSTAR_SELECT_INDEX(context, "");
        StarSetting.setProjectSTAR_SELECT_INDEX(context, "");
        StarSetting.setProjectSTAR_SELECT_NAME(context, "");
    }
}
