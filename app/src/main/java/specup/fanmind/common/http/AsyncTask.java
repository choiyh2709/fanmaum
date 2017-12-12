package specup.fanmind.common.http;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import org.apache.http.NameValuePair;
import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.DialogUtil;
import specup.fanmind.common.Util.Utils;


public class AsyncTask extends android.os.AsyncTask<String, Integer, Integer> {

    public OnTask listener;
    public Context mContext;
    //	private ProgressDialog mProgress;
    private String mResult = null;
    public String mUrl;
    private List<NameValuePair> mParams;
    private boolean isPost;
    public static DialogUtil dialogUtil;
    private HashMap<String, String> header;
    private WeakReference imageViewReference;

    long startTime;
    static String now;
    Calendar calendar = Calendar.getInstance();


    public AsyncTask(Context context, String Url, boolean post) {
        mContext = context;
        mUrl = Url;
        isPost = post;
        now = calendar.getTime().toString();

        startTime = System.currentTimeMillis();
    }

    /**
     * Params가 있는 생성자.
     *
     * @param context
     * @param Url     api 주소
     * @param params  api 값
     * @param post    isPost
     */
    public AsyncTask(Context context, String Url, List<NameValuePair> params, boolean post) {
        mContext = context;
        mUrl = Url;
        mParams = params;
        isPost = post;

        now = calendar.getTime().toString();
        startTime = System.currentTimeMillis();
    }

    /**
     * Params가 있는 생성자.
     *
     * @param context
     * @param header  header 추가
     * @param Url     api 주소
     * @param params  api 값
     * @param post    isPost
     */
    public AsyncTask(Context context, HashMap<String, String> header, String Url, List<NameValuePair> params, boolean post) {
        mContext = context;
        mUrl = Url;
        mParams = params;
        isPost = post;
        this.header = header;

        now = calendar.getTime().toString();
        startTime = System.currentTimeMillis();
    }


    public void setListenr(OnTask listen) {
        listener = listen;
    }


    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        if (isPost) {
            if (header == null) {
                mResult = HttpRequest.post(mUrl, mParams);
                Log.d("doInBackground","doInBackground1");
            } else {
                mResult = HttpRequest.postSetHeader(mUrl, mParams, header);
                Log.d("doInBackground","doInBackground2");
            }
            if (mResult == null) {
                publishProgress(0);
            } else {
                try {
                    setTask(params[0]);
                } catch (Exception e) {
                    setTask("-1");
                }
            }
        } else {
            if (header == null) {
                mResult = HttpRequest.get(mUrl, mParams);
                Log.d("doInBackground","doInBackground3");
            } else {
                mResult = HttpRequest.postSetHeader(mUrl, mParams, header);
                Log.d("doInBackground","doInBackground4");
            }
            if (mResult == null) {
                publishProgress(0);
            } else {
                try {
                    if (params[0].equals("movie")) {
                        publishProgress(1);
                    } else {
                        setTask(params[0]);
                    }
                } catch (Exception e) {
                    setTask("-1");
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        try {
            if (dialogUtil == null) {
                dialogUtil = new DialogUtil();
                dialogUtil.showProgress(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        try {
            Class.forName("android.os.AsyncTask");
        } catch (Throwable ignored) {
        }
        // TODO Auto-generated method stub
        if (values[0] == 0) Utils.setToast(mContext, R.string.networkerror);
        else try {
            timecheck(mUrl,mParams,startTime);
            listener.onTask(values[0], mResult);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        if (dialogUtil != null) {
            dialogUtil.dismissProgress();
            dialogUtil = null;
        }if(imageViewReference != null){
            final ImageView imageView = (ImageView) imageViewReference.get();
            if(imageView != null){
                imageView.setImageBitmap(null);
            }
        }
        super.onPostExecute(result);
    }

    public static void timecheck(String url,List<NameValuePair> params , long startTime){
        String a = "";
        int count = 0;

        if( params != null )
            count = params.size();
        for( int i = 0; i < count; i++ ){
            a = a+"," + params.get(i).getName()+":"+params.get(i).getValue();
        }


        Log.d("timecheck - asynctask", url + "///" + a + "  :  "  + now  + "    :    " +  (System.currentTimeMillis() - startTime)/1000.0);
    }


    private void setTask(String param) {
        if (param.equals(AsyncTaskValue.LOGIN)) publishProgress(AsyncTaskValue.LOGIN_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_EMAIL_CHECK))
            publishProgress(AsyncTaskValue.LOGIN_EMAIL_CHECK_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_CERTI01))
            publishProgress(AsyncTaskValue.LOGIN_CERTI01_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_CERTI02))
            publishProgress(AsyncTaskValue.LOGIN_CERTI02_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_NICK_CHECK))
            publishProgress(AsyncTaskValue.LOGIN_NICK_CHECK_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_STAR))
            publishProgress(AsyncTaskValue.LOGIN_STAR_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_JOIN))
            publishProgress(AsyncTaskValue.LOGIN_JOIN_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_FINDID))
            publishProgress(AsyncTaskValue.LOGIN_FINDID_NUM);
        else if (param.equals(AsyncTaskValue.LOGIN_FINDPW))
            publishProgress(AsyncTaskValue.LOGIN_FINDPW_NUM);
        else if (param.equals(AsyncTaskValue.CONTACT)) publishProgress(AsyncTaskValue.CONTACT_NUM);
        else if (param.equals(AsyncTaskValue.CHANGE_PWD))
            publishProgress(AsyncTaskValue.CHANGE_PWD_NUM);
        else if (param.equals(AsyncTaskValue.SERVER)) publishProgress(AsyncTaskValue.SERVER_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_LIST_DETAIL))
            publishProgress(AsyncTaskValue.SUPPORT_LIST_DETAIL_NUM);
        else if (param.equals(AsyncTaskValue.STAR_ADD))
            publishProgress(AsyncTaskValue.STAR_ADD_NUM);
        else if (param.equals(AsyncTaskValue.MAIN)) publishProgress(AsyncTaskValue.MAIN_NUM);
        else if (param.equals(AsyncTaskValue.CHANGE_PHONE))
            publishProgress(AsyncTaskValue.CHANGE_PHONE_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_LIST))
            publishProgress(AsyncTaskValue.SUPPORT_LIST_NUM);
        else if (param.equals(AsyncTaskValue.STAR_SELECT))
            publishProgress(AsyncTaskValue.STAR_SELECT_NUM);
        else if (param.equals(AsyncTaskValue.HISTORY_LIST))
            publishProgress(AsyncTaskValue.HISTORY_LIST_NUM);
        else if (param.equals(AsyncTaskValue.LIKE)) publishProgress(AsyncTaskValue.LIKE_NUM);
        else if (param.equals(AsyncTaskValue.LIKE_CANCEL))
            publishProgress(AsyncTaskValue.LIKE_CANCEL_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_READY_LIST))
            publishProgress(AsyncTaskValue.SUPPORT_READY_LIST_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_READY_DETAIL))
            publishProgress(AsyncTaskValue.SUPPORT_READY_DETAIL_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_LIST))
            publishProgress(AsyncTaskValue.REPLY_LIST_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_WRITE))
            publishProgress(AsyncTaskValue.REPLY_WRITE_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_REPORT))
            publishProgress(AsyncTaskValue.REPLY_REPORT_NUM);
        else if (param.equals(AsyncTaskValue.LIKE)) publishProgress(AsyncTaskValue.LIKE_NUM);
        else if (param.equals(AsyncTaskValue.LIKE_CANCEL))
            publishProgress(AsyncTaskValue.LIKE_CANCEL_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_LIKE))
            publishProgress(AsyncTaskValue.REPLY_LIKE_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_LIKE_CANCEL))
            publishProgress(AsyncTaskValue.REPLY_LIKE_CANCEL_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE))
            publishProgress(AsyncTaskValue.TIME_LINE_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE2))
            publishProgress(AsyncTaskValue.TIME_LINE_NUM2);
        else if (param.equals(AsyncTaskValue.STAR_MODIFY))
            publishProgress(AsyncTaskValue.STAR_MODIFY_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_MONTH))
            publishProgress(AsyncTaskValue.TIME_LINE_MONTH_NUM);
        else if (param.equals(AsyncTaskValue.MYNEWS_LIST))
            publishProgress(AsyncTaskValue.MYNEWS_LIST_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_LIST))
            publishProgress(AsyncTaskValue.NEWSFEED_LIST_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_DEL))
            publishProgress(AsyncTaskValue.NEWSFEED_DEL_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_ALARM_ON))
            publishProgress(AsyncTaskValue.NEWSFEED_ALARM_ON_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_ALARM_OFF))
            publishProgress(AsyncTaskValue.NEWSFEED_ALARM_OFF_NUM);
        else if (param.equals(AsyncTaskValue.REPLY_DELETE))
            publishProgress(AsyncTaskValue.REPLY_DELETE_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_SEND_MIND))
            publishProgress(AsyncTaskValue.SUPPORT_SEND_MIND_NUM);
        else if (param.equals(AsyncTaskValue.SCHDUEL_ALL))
            publishProgress(AsyncTaskValue.SCHDUEL_ALL_NUM);
        else if (param.equals(AsyncTaskValue.STAR_REQUEST))
            publishProgress(AsyncTaskValue.STAR_REQUEST_NUM);
        else if (param.equals(AsyncTaskValue.STAR_ADD))
            publishProgress(AsyncTaskValue.STAR_ADD_NUM);
        else if (param.equals(AsyncTaskValue.SCHDUEL_INFORM))
            publishProgress(AsyncTaskValue.SCHDUEL_INFORM_NUM);
        else if (param.equals(AsyncTaskValue.EVENT_LIST))
            publishProgress(AsyncTaskValue.EVENT_LIST_NUM);
        else if (param.equals(AsyncTaskValue.HISTORY_DETAIL))
            publishProgress(AsyncTaskValue.HISTORY_DETAIL_NUM);
        else if (param.equals(AsyncTaskValue.EVENT_DETAIL))
            publishProgress(AsyncTaskValue.EVENT_DETAIL_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_DETAIL))
            publishProgress(AsyncTaskValue.NEWSFEED_DETAIL_NUM);
        else if (param.equals(AsyncTaskValue.STAR_VOTE))
            publishProgress(AsyncTaskValue.STAR_VOTE_NUM);
        else if (param.equals(AsyncTaskValue.STAR_VOTE_CANCEL))
            publishProgress(AsyncTaskValue.STAR_VOTE_CANCEL_NUM);
        else if (param.equals(AsyncTaskValue.NEWS_EDIT))
            publishProgress(AsyncTaskValue.NEWS_EDIT_NUM);
        else if (param.equals(AsyncTaskValue.NEWS_EDIT_ALL))
            publishProgress(AsyncTaskValue.NEWS_EDIT_ALL_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_TIME))
            publishProgress(AsyncTaskValue.TIME_LINE_TIME_NUM);
        else if (param.equals(AsyncTaskValue.FAN_CLUB))
            publishProgress(AsyncTaskValue.FAN_CLUB_NUM);
        else if (param.equals(AsyncTaskValue.LOCKSCREEN))
            publishProgress(AsyncTaskValue.LOCKSCREEN_NUM);
        else if (param.equals(AsyncTaskValue.MYPOINT)) publishProgress(AsyncTaskValue.MYPOINT_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_REPORT))
            publishProgress(AsyncTaskValue.TIME_LINE_REPORT_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_DEL))
            publishProgress(AsyncTaskValue.TIME_LINE_DEL_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_ALARMOFF))
            publishProgress(AsyncTaskValue.TIME_LINE_ALARMOFF_NUM);
        else if (param.equals(AsyncTaskValue.TIME_LINE_ALARMON))
            publishProgress(AsyncTaskValue.TIME_LINE_ALARMON_NUM);
        else if (param.equals(AsyncTaskValue.LOCKPOINT))
            publishProgress(AsyncTaskValue.LOCKPOINT_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_ALL))
            publishProgress(AsyncTaskValue.SUPPORT_ALL_NUM);
        else if (param.equals(AsyncTaskValue.SUPPORT_REDAY_ALL))
            publishProgress(AsyncTaskValue.SUPPORT_REDAY_ALL_NUM);
        else if (param.equals(AsyncTaskValue.HISTORY_ALL))
            publishProgress(AsyncTaskValue.HISTORY_ALL_NUM);
        else if (param.equals(AsyncTaskValue.NEWSFEED_REPORT))
            publishProgress(AsyncTaskValue.NEWSFEED_REPORT_NUM);
        else if (param.equals(AsyncTaskValue.SSKEY_INSERT))
            publishProgress(AsyncTaskValue.SSKEY_INSERT_NUM);
        else if (param.equals(AsyncTaskValue.DAILY_SELECT))
            publishProgress(AsyncTaskValue.DAILY_SELECT_NUM);
        else if (param.equals(AsyncTaskValue.DAILY_UPDATE))
            publishProgress(AsyncTaskValue.DAILY_UPDATE_NUM);
        else if (param.equals(AsyncTaskValue.DAILY_INSERT))
            publishProgress(AsyncTaskValue.DAILY_INSERT_NUM);
        else if (param.equals(AsyncTaskValue.DAILY_ADDPOINT))
            publishProgress(AsyncTaskValue.DAILY_ADDPOINT_NUM);
        else if (param.equals(AsyncTaskValue.PAY_ACCOUNT))
            publishProgress(AsyncTaskValue.PAY_ACCOUNT_NUM);
        else if (param.equals(AsyncTaskValue.NAVER_LOGIN))
            publishProgress(AsyncTaskValue.NAVER_LOGIN_NUM);
        else if (param.equals(AsyncTaskValue.NAVER_LOGIN_RESULT))
            publishProgress(AsyncTaskValue.NAVER_LOGIN_RESULT_NUM);
        else if (param.equals(AsyncTaskValue.FACEBOOK_LOGIN))
            publishProgress(AsyncTaskValue.FACEBOOK_LOGIN_NUM);
        else if (param.equals(AsyncTaskValue.FACEBOOK_LOGIN_RESULT))
            publishProgress(AsyncTaskValue.FACEBOOK_LOGIN_RESULT_NUM);
        else if (param.equals(AsyncTaskValue.TWITTER_LOGIN))
            publishProgress(AsyncTaskValue.TWITTER_LOGIN_NUM);
       else if (param.equals(AsyncTaskValue.TWITTER_LOGIN_RESULT))
            publishProgress(AsyncTaskValue.TWITTER_LOGIN_RESULT_NUM);
        else {
            publishProgress(-1);
        }
    }

}
