package specup.fanmind.common.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import specup.fanmind.IntroActivity;
import specup.fanmind.R;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.lockscreen.LockService;
import specup.fanmind.main.login.LoginActivity2;


public class Utils {

    //유투브 동영상 키
    public static final String DEVELOPER_KEY = "AIzaSyA3DzpcHzITunHYEr-hCDRc48kpP7j7lfY";
    //잠금화면 이미지 갤러리
    public static final int PICK_FROM_GALLERY_IMAGE = 10000;
    //잠금화면 크롭 이미지
    public static final int CROP_FROM_CAMERA_IMAGE = 20000;
    //잠금화면 버튼 이미지 갤러리
    public static final int PICK_FROM_GALLERY_BUTTON = 30000;
    //잠금화면 버튼 크롭 갤러리
    public static final int CROP_FROM_CAMERA_BUTTON = 40000;
    //타임라인 이미지 업로드
    public static final int TIMELINE_UPLOAD = 50000;
    //뉴스피드 글쓰기
    public static final int NEWSFEED_UPLOAD = 60000;
    //뉴스피드 전체보기 신고
    public static final int NEWSFEED_SINGO_ALL = 60001;
    //타임라인 전체보기 신고
    public static final int TIMELINE_SINGO_ALL = 60002;

    //앱 시작시
    public static final int START = 10001;
    //잠금화면 지우기
    public static final int IMAGE_DEL = 10002;
    //잠금화면 버튼 지우기
    public static final int BUTTON_DEL = 10003;
    //사는곳 얻어오기
    public static final int WHERE_LIVE = 10004;
    //스타리스트
    public static final int STAR_LIST = 10005;
    //생년월일
    public static final int BIRTH_YEAR = 10006;
    //서포트 진행중 디테일
    public static final int SUPPORTING_DETAIL = 10007;
    //이벤트 디테일
    public static final int EVENT_DETAIL = 10008;
    //뉴스피드 디테일
    public static final int NEWSFEED_DETAIL = 10009;
    //히스토리 디테일
    public static final int HISTORY_DETAIL = 10010;
    //뉴스피드 글쓰기
    public static final int NEWSFEED_WRITE = 10011;
    //뉴스피드 글쓰기 수정
    public static final int NEWSFEED_WRITE_INSERT = 10012;
    //타임라인 월별이미지
    public static final int TIMELINE_MONTH = 10013;
    //타임라인 댓글쓰기
    public static final int TIMELINE_DAY_REPLY = 10014;
    //락스크린 댓글쓰기
    public static final int LOCK = 10015;
    //가상계좌
    public static final int PAY_ACCOUNT = 10016;

    /**Tab액티비티 타이틀
     * @param title 타이틀
     */
    // public static void setTitle(String title){
    // if((TabMainActivity)TabMainActivity.mTabMainContext !=null)
    // ((TabMainActivity)TabMainActivity.mTabMainContext).mTitle.setText(title);
    // }

    /**
     * 날짜 2자리 int형 시간을 String형으로 바꿈.
     *
     * @param time 시간 값
     * @return
     */
    public static String chageTime(int time) {
        String StringTime = String.valueOf(time);
        if (StringTime.length() == 1) {
            StringTime = "0" + StringTime;
        }
        return StringTime;
    }

    /**
     * 날짜 2자리 String으로 시간을 바꿈.
     *
     * @param date 시간
     * @return
     */
    public static String changeCal(String date) {
        String mDate = null;
        if (date.length() == 1) {
            mDate = "0" + date;
        } else {
            mDate = date;
        }
        return mDate;
    }

    /**
     * 오늘시간얻기
     *
     * @return String 시간
     */

    public static String getTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.KOREA);
        java.util.Date dDate = new java.util.Date();
        String sToday = sdf.format(dDate);
        return sToday;
    }

    /**
     * 오늘시간얻기
     *
     * @return String 시간
     */

    public static String getCalTime() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.KOREA);
        java.util.Date dDate = new java.util.Date();
        String sToday = sdf.format(dDate);
        return sToday;
    }

    /**
     * 오늘시간얻기
     *
     * @return String 시간
     */

    public static String getCalTime2() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddhhmmss", java.util.Locale.KOREA);
        java.util.Date dDate = new java.util.Date();
        String sToday = sdf.format(dDate);
        return sToday;
    }


    /**
     * 비번숫자체크
     *
     * @param hex 비밀번호
     * @return
     */
    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,16}$";

    public static boolean isPassword(String hex) {
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    /**
     * 돈 자릿수
     *
     * @param result 돈
     * @return
     */
    public static String getMoney(String result) {
        if (result.equals("null") || result == null)
            return "0";
        else {
            DecimalFormat df = new DecimalFormat("#,##0");
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setDecimalFormatSymbols(dfs);

            double inputNum = Double.parseDouble(result);
            result = df.format(inputNum).toString();
            return result;
        }
    }

    /**
     * maum 을 각 나라 금액으로 변경
     * 한국 : :ko_KR
     * 일본 : ja_JP
     * 중국(간체) : zh_CN
     * 미국 : en_US
     *
     * @param context
     * @param maum
     * @return
     */
    public static String getMaum2Money(Context context, String maum) {
        String money = "0";
        String lanquageLocal = getLanquageLocal(context);

        if (maum.equals("") || maum.equals("null")) {
            return "0";
        }

        if (lanquageLocal.equals("ko_KR")) {
            money = maum;
//            money = Double.parseDouble(maum) * 1;

        } else if (lanquageLocal.equals("en_US")) {
            money = String.valueOf(Double.parseDouble(maum) * 0.000909);
        } else if (lanquageLocal.equals("zh_CN")) {
            money = String.valueOf(Double.parseDouble(maum) * 0.1);
        } else if (lanquageLocal.equals("ja_JP")) {
            money = String.valueOf(Double.parseDouble(maum) * 0.005714);
        }
        return money;
    }

    /**
     * 날짜에 점 표시
     *
     * @param date 날짜.
     * @return
     */
    public static String dotDate(String date) {
        String reDate = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
        return reDate;
    }

    /**
     * 날짜에 점 표시
     *
     * @param date 날짜.
     * @return
     */
    public static String dotMinus(String date) {
        String reDate = date.substring(0, 4) + date.substring(5, 7) + date.substring(8, 10);
        return reDate;
    }

    /**
     * EditText 길이 체크
     *
     * @param mEt EditText
     * @return
     */
    public static boolean checkLength(TextView mEt) {
        boolean isZero = false;
        if (mEt.getText().toString().trim().length() == 0) {
            isZero = true;
        } else {
            isZero = false;
        }
        return isZero;
    }

    /**
     * 스트링값을 인트로 분리.
     *
     * @param value String 값
     * @return
     */
    public static int subStringInt(String value) {
        int reValue = Integer.parseInt(value);
        return reValue;
    }

    /**
     * 이메일 체크
     *
     * @param email
     * @return
     */

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 폰번호 유효 체크
     *
     * @param str
     * @return
     */
    public static boolean isCellphone(String str) {
        // 010, 011, 016, 017, 018, 019
        return str.matches("(01[016789])(\\d{3,4})(\\d{4})");
    }

    /**
     * 토스트 띄움
     *
     * @param context
     * @param contents 내용
     */
    public static void setToast(Context context, int contents) {
        Toast.makeText(context, contents, Toast.LENGTH_SHORT).show();
    }

    /**
     * 토스트 띄움
     *
     * @param context
     * @param contents 내용
     */
    public static void setToast(final Context context, final String contents) {
        Toast.makeText(context, contents, Toast.LENGTH_SHORT).show();

    }

    /**
     * 스낵바 띄움
     *
     * @param view
     * @param contents 내용
     */
    public static void setSnackBar(Context context, View view, String contents) {
        try {
            Snackbar sb = Snackbar.make(view, contents, Snackbar.LENGTH_LONG);
            View sbView = sb.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setMaxLines(10);
            sb.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 프래그먼트 뒤로가기
     *
     * @param fm 프래그먼트 액티비티
     */
    public static void backStack(FragmentActivity fm) {
        fm.getSupportFragmentManager().popBackStack();
    }


    /**TimeStamp
     * @param time 시간.
     * @return
     */
    // public static String getDate(long time) {
    // Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    // cal.setTimeInMillis(time);
    // String date = DateFormat.format("yyyy-MM-dd-hh-mm-ss", cal).toString();
    // return date;
    // }

    /**
     * 모든 프래그먼트 제거
     *
     * @param fr 프래그먼트 액티비티
     */
    public static void clearFrag(FragmentActivity fr) {
        FragmentManager fm = fr.getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }


    /**
     * 디데이 계산
     *
     * @param mDay1 마감일
     * @param mDay2 오늘날
     * @return
     */
    public static int GetDifferenceOfDate(String mDay1, String mDay2) {
        try {
            Calendar cal = Calendar.getInstance();
            int nYear1 = Integer.parseInt(mDay1.substring(0, 4));
            int nMonth1 = Integer.parseInt(mDay1.substring(5, 7));
            int nDate1 = Integer.parseInt(mDay1.substring(8, 10));
            int nYear2 = Integer.parseInt(mDay2.substring(0, 4));
            int nMonth2 = Integer.parseInt(mDay2.substring(5, 7));
            int nDate2 = Integer.parseInt(mDay2.substring(8, 10));

            int nTotalDate1 = 0, nTotalDate2 = 0, nDiffOfYear = 0, nDiffOfDay = 0;
            if (nYear1 > nYear2) {
                for (int i = nYear2; i < nYear1; i++) {
                    cal.set(i, 12, 0);
                    nDiffOfYear += cal.get(Calendar.DAY_OF_YEAR);
                }
                nTotalDate1 += nDiffOfYear;
            } else if (nYear1 < nYear2) {
                for (int i = nYear1; i < nYear2; i++) {
                    cal.set(i, 12, 0);
                    nDiffOfYear += cal.get(Calendar.DAY_OF_YEAR);
                }
                nTotalDate2 += nDiffOfYear;
            }
            cal.set(nYear1, nMonth1 - 1, nDate1);
            nDiffOfDay = cal.get(Calendar.DAY_OF_YEAR);
            nTotalDate1 += nDiffOfDay;

            cal.set(nYear2, nMonth2 - 1, nDate2);
            nDiffOfDay = cal.get(Calendar.DAY_OF_YEAR);
            nTotalDate2 += nDiffOfDay;

            return nTotalDate1 - nTotalDate2;
        } catch (Exception e) {
            e.printStackTrace();
            return -100;
        }

    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = listView.getPaddingTop() + listView.getPaddingBottom() + totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * 미디어 스캔
     *
     * @param context
     */
    public static void setScannerFile(Context context, String strPah, String name) {
//        String strPath = Environment.getExternalStorageDirectory().toString() + "/Fanmaum/" + name;
//        File file = new File(strPath);
//        new SingleMediaScanner(context, file);
//        Toast.makeText(context, R.string.okdownload, Toast.LENGTH_SHORT).show();
    }

    /**
     * 세션키, 아이디 공유
     *
     * @param context
     * @param mParams
     * @return
     */
    public static List<NameValuePair> setSession(Context context, List<NameValuePair> mParams) {
        if (FanMindSetting.getLOGIN_OK(context)) {
            mParams.add(new BasicNameValuePair("sskey", FanMindSetting.getSESSION_KEY(context)));
            mParams.add(new BasicNameValuePair("ssid", FanMindSetting.getEMAIL_ID(context)));
        }
        mParams.add(new BasicNameValuePair("locale", Utils.getLanquageLocal(context)));
        mParams.add(new BasicNameValuePair("timezone", Utils.getTimeZone()));
        return mParams;
    }

    /**
     * 댓글달기
     *
     * @param mParams
     * @param board   1. 서포트진행형, 2. 전달예정, 3. 히스토리, 4. 뉴스피드, 5.타임라인 이미지, 6.이벤트
     * @param mode    전체보기 : A, 내댓글보기 : M
     * @param srl     댓글이 달린 글번호.
     * @param begin   리스팅 시작할댓글
     */
    public static List<NameValuePair> setReply(Context context, List<NameValuePair> mParams, String board, String mode,
                                               String srl, String begin) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", board));
        mParams.add(new BasicNameValuePair("mode", mode));
        mParams.add(new BasicNameValuePair("srl", srl));
        mParams.add(new BasicNameValuePair("begin", begin));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 댓글쓰기
     *
     * @param mParams
     * @param board   1. 서포트진행형, 2. 전달예정, 3. 히스토리, 4. 뉴스피드, 5.타임라인 이미지, 6.이벤트
     * @param srl     댓글이 달린 글번호.
     * @param text    내용
     */
    public static List<NameValuePair> writeReply(Context context, List<NameValuePair> mParams, String board, String srl,
                                                 String text) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", board));
        mParams.add(new BasicNameValuePair("srl", srl));
        mParams.add(new BasicNameValuePair("text", text));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 댓글 삭제하기
     *
     * @param context
     * @param mParams
     * @param srl     댓글번호
     * @return
     */
    public static List<NameValuePair> delReply(Context context, List<NameValuePair> mParams, String srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("reply_srl", srl));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 상세보기 페이지
     *
     * @param context
     * @param mParams
     * @param param
     * @param srl     리스트 번호
     * @return
     */
    public static List<NameValuePair> showDetail(Context context, List<NameValuePair> mParams, String param,
                                                 String srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair(param, srl));
        mParams.add(new BasicNameValuePair("star_srl", "1"));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 댓글 신고하기
     *
     * @param mParams
     * @param reply_srl 댓글번호
     * @return
     */
    public static List<NameValuePair> reportReply(Context context, List<NameValuePair> mParams, String reply_srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("reply_srl", reply_srl));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 좋아요
     *
     * @param mParams
     * @param board   댓글(0), 서포트 전달예정(2), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5), 이벤트(6)
     * @param srl     댓글번호
     * @return
     */
    public static List<NameValuePair> likeOK(Context context, List<NameValuePair> mParams, String board, String srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", board));
        mParams.add(new BasicNameValuePair("srl", srl));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 좋아요 취소
     *
     * @param mParams
     * @param board   댓글(0), 히스토리(3), 뉴스피드(4), 타임라인 이미지(5)
     * @param srl     댓글번호
     * @return
     */
    public static List<NameValuePair> likeCancel(Context context, List<NameValuePair> mParams, String board,
                                                 String srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", board));
        mParams.add(new BasicNameValuePair("srl", srl));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 마음보내기
     *
     * @param mParams
     * @param board   서포트(1), 이벤트(6)
     * @param srl     게시글 번호
     * @return
     */
    public static List<NameValuePair> sendMind(Context context, List<NameValuePair> mParams, String board, String srl,
                                               String heart) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("board", board));
        mParams.add(new BasicNameValuePair("srl", srl));
        mParams.add(new BasicNameValuePair("heart", heart));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 타임라인 월별 이미지
     *
     * @param context
     * @param mParams
     * @param year     년도
     * @param month    월
     * @param begin    시작번호
     * @param star_srl 연예인번호
     * @return
     */
    public static List<NameValuePair> TimeLineMonth(Context context, List<NameValuePair> mParams, String year,
                                                    String month, String begin, String star_srl) {
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("year", year));
        mParams.add(new BasicNameValuePair("month", month));
        mParams.add(new BasicNameValuePair("begin", begin));
        mParams.add(new BasicNameValuePair("star_srl", star_srl));
        mParams = setSession(context, mParams);
        return mParams;
    }

    /**
     * 뉴스피드 팝업
     *
     * @param context
     * @param mAsyncTask
     * @param mParams
     * @param srl        뉴스피드 글 번호
     * @param task
     * @param kind       알람끄기(1), 알람켜기(2), 신고하기(3), 삭제(4)
     */
    public static void reportNewsFeed(Context context, AsyncTask mAsyncTask, List<NameValuePair> mParams, String srl,
                                      OnTask task, int kind) {
        String url = null, execute = null;
        if (kind == 1) {
            url = URLAddress.NEWSFEED_ALARM_OFF(context);
            execute = AsyncTaskValue.NEWSFEED_ALARM_OFF;
        } else if (kind == 2) {
            url = URLAddress.NEWSFEED_ALARM_ON(context);
            execute = AsyncTaskValue.NEWSFEED_ALARM_ON;
        } else if (kind == 3) {
            url = URLAddress.NEWSFEED_REPORT(context);
            execute = AsyncTaskValue.NEWSFEED_REPORT;
        } else if (kind == 4) {
            url = URLAddress.NEWSFEED_DEL(context);
            execute = AsyncTaskValue.NEWSFEED_DEL;
        }
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("newsfeed_srl", srl));
        mParams = setSession(context, mParams);
        HttpRequest.setHttp(context, mAsyncTask, mParams, url, execute, task);
    }

    /**
     * 픽셀을 DP로 계산.
     *
     * @param context
     * @param x       PX값
     * @return
     */
    public static int getDP(Context context, int x) {
        Resources r = context.getResources();
        x = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, r.getDisplayMetrics());
        return x;
    }

    static int getDip2Px(Context context, int dip) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, dip, context.getResources().getDisplayMetrics());
        return px;
    }

    static Bitmap scaledBitmap = null;

    public static ImageView scaleImage(Context context, ImageView view, int boundBoxInDp, boolean isMargin) {
        // Get the ImageView and its bitmap
        Drawable drawing = view.getDrawable();

        Bitmap bitmap = ((BitmapDrawable) drawing).getBitmap();

        // Get current dimensions
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        float xScale = ((float) boundBoxInDp) / width;
        float yScale = ((float) boundBoxInDp) / height;
//        float scale = (xScale <= yScale) ? xScale : yScale;

        // Create a matrix for the scaling and add the scaling data

        Matrix matrix = new Matrix();
//         matrix.postScale(scale, scale);
        // jw.kang ���ο� ���߱� ������ ���������� ����.
        matrix.postScale(1, 1);

        // Create a new bitmap and convert it to a format understood by the
        // ImageView
        if (scaledBitmap != null) {
            scaledBitmap.recycle();
            scaledBitmap = null;
        }
        try {
            scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            scaledBitmap.recycle();
            try {
                scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
            } catch (OutOfMemoryError e2) {
                e2.printStackTrace();
            }
        }

        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        width = scaledBitmap.getWidth();
        height = scaledBitmap.getHeight();

        // Apply the scaled bitmap
        view.setImageDrawable(result);

        // Now change ImageView's dimensions to match the scaled image
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        if (isMargin) {
            params.bottomMargin = Utils.getDP(context, 20);
        }
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * -를 .으로 변환.
     *
     * @param date Comment
     * @return
     */
    public static String chageCommentDate(Context context, String date) {
        date = date.replace("-", "").replace(":", "").replace(" ", "");
        Log.e("date", date);
        String sToday = Utils.getCalTime();
        Log.e("sToday", sToday);
        if (sToday.substring(0, 8).equals(date.substring(0, 8))) {
            if (sToday.substring(8, 10).equals(date.substring(8, 10))) {
                int write = Integer.parseInt(date.substring(10, 12));
                int now = Integer.parseInt(sToday.substring(10, 12));
                if (now - write > 0)
                    date = String.valueOf(now - write) + context.getString(R.string.min);
                else
                    date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
            } else {
                int write = Integer.parseInt(date.substring(8, 10));
                int now = Integer.parseInt(sToday.substring(8, 10));
                if (now - write > 0)
                    date = String.valueOf(now - write) + context.getString(R.string.hour);
                else
                    date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
            }
        } else {
            date = date.substring(0, 4) + "." + date.substring(4, 6) + "." + date.substring(6, 8);
        }
        return date;
    }

    /**
     * -를 .으로 변환.
     *
     * @param date
     * @return
     */
    public static String chageDate(String date) {
        date = date.replace("-", ".").substring(0, 10);
        return date;
    }

    /**
     * -를 ""으로 변환.
     *
     * @param date
     * @return
     */
    public static String changebar(String date) {
        date = date.replace("-", "").substring(0, 8);
        return date;
    }

    /**
     * -를 ""으로 변환.
     *
     * @param date
     * @return
     */
    public static String changeDate(String date) {
        date = date.replace("-", "").substring(0, 8);
        date = date.substring(0, 4) + "년 " + date.substring(4, 6) + "월 " + date.substring(6, 8) + "일";
        return date;
    }

    /**
     * -를 ""으로 변환.
     *
     * @param date
     * @return
     */
    public static String changeDate2(String date) {
        date = date.replace("-", "").substring(0, 8);
        date = date.substring(0, 4) + ". " + date.substring(4, 6) + ". " + date.substring(6, 8);
        return date;
    }

    /**
     * 백분율 구하기
     *
     * @param up   분자
     * @param down 분모
     * @return
     */
    public static double getPercent(String up, String down) {
        double goalmoney = (double) Utils.subStringInt(down);
        double now = (double) Utils.subStringInt(up);
        double total = now / goalmoney * 100.0;
        return total;
    }


    /**
     * Json 에러결과
     *
     * @param result 결과
     * @return
     */
    public static String getJsonData(String result, String key) {
        String joinCode = null;
        try {
            JSONObject json = new JSONObject(result);
            joinCode = json.getString(key);
            if (joinCode.equals("null")) {
                joinCode = "";
            }
        } catch (Exception e) {
            joinCode = "";
            e.printStackTrace();
        }

        return joinCode;
    }


    /**
     * Json 에러결과
     *
     * @param result 결과
     * @return
     */
    public static boolean getJsonData(String result) {
        String joinCode = null;
        try {
            System.out.println("result;"+result);
            JSONObject json = new JSONObject(result);
            joinCode = json.getString("errcode");

            if (joinCode.equals("900")) {//   SUCCESS, VALIDATION_FAILURE
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean getJsonData2(String result) {
        String joinCode = null;
        try {
            JSONObject json = new JSONObject(result);
            joinCode = json.getString("code");

            if (joinCode.equals("SUCCESS") || joinCode.equals("UNLINKED")) {//   SUCCESS, VALIDATION_FAILURE
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Json 에러결과
     *
     * @param result 결과
     * @return String 형
     */
    public static String getJsonDataString(String result) {
        String joinCode = null;
        try {
            JSONObject json = new JSONObject(result);
            joinCode = json.getString("errcode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joinCode;
    }

    /**
     * Json 에러결과
     *
     * @param result 결과
     * @return String 형
     */
    public static String getJsonDataString2(String result) {
        String joinCode = null;
        try {
            joinCode = Utils.getJsonData(Utils.getJsonData(result, "data"), "errcode");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return joinCode;
    }


    public static String getSchduel(String right) {
        String timea = "";
        String min = right.substring(14, 16);
        int time = Integer.parseInt(right.substring(11, 13));
        if (time > 11) {
            timea = "PM";
            if (time != 12) {
                if (time != 0) {
                    time = time - 12;
                }
            }
        } else {
            if (time == 0)
                time = 12;
            timea = "AM";
        }

        String atime = Utils.changeCal(String.valueOf(time));
        timea = atime + ":" + min + " " + timea;
        return timea;
    }

    public static CustomDialog mDialog;

    public static void showDialog(final Activity context) {
        String title = context.getString(R.string.start01);
        String content = context.getString(R.string.start02);
        String left = context.getString(R.string.start03);
        String right = context.getString(R.string.start04);
        mDialog = new CustomDialog(context, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Intent intent = new Intent(context, LoginActivity2.class);
                context.startActivity(intent);
            }
        }, left, right);
        mDialog.show();
    }

    public static void loading(final ImageView mLoading) {
        mLoading.post(new Runnable() {
            public void run() {
                AnimationDrawable animation = (AnimationDrawable) mLoading.getBackground();
                animation.stop();
                animation.start();
            }
        });
    }

    /**
     * network 연결 상태 확인
     *
     * @return true : 네트워크 나 와이파이로 연결이 되어 있음
     * false : 깡통폰임
     */
    public static boolean isOnline(Context context) { //
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            State wifi = conMan.getNetworkInfo(1).getState(); // wifi
            if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
                return true;

            }
            State mobile = conMan.getNetworkInfo(0).getState(); // mobile
            // ConnectivityManager.TYPE_MOBILE
            if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
                return true;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    //내부 저장소 디렉토리 생성.
    public static void makeDir(Context context) {
        String dirPath = context.getFilesDir().getAbsolutePath();
        File file = new File(dirPath, "bg");
        // 일치하는 폴더가 없으면 생성
        if (!file.exists()) {
            file.mkdirs();
        }

        file = new File(dirPath, "button");
        // 일치하는 폴더가 없으면 생성
        if (!file.exists()) {
            file.mkdirs();
        }

        dirPath = Environment.getExternalStorageDirectory().toString() + "/Fanmaum";
        file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 국가 비교
     *
     * @param context
     * @param country
     * @return
     */
    public static boolean getLocal(Context context, String country) {
        if (context.getResources().getConfiguration().locale.getDisplayLanguage().equals(country)) {
            return true;
        }
        return false;
    }

    /**
     * 언어코드_국가코드
     *
     * @param context
     * @return
     */
    public static String getLanquageLocal(Context context) {

        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        String country = locale.getCountry();

        return language + "_" + country;
    }

    /**
     * 타임존 정보
     */
    public static String getTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        return tz.getID();
    }

    /**
     * string to md5
     *
     * @param s
     * @return
     */
    public static String getMD5Hash(String s) {
        MessageDigest m = null;
        String hash = null;

        try {
            m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(), 0, s.length());
            hash = new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    /**
     * 댓글 구별 되게 색 변경.
     *
     * @param text
     * @return
     */

    public static Spanned getColorNickName(String text) {

        StringBuffer strings = new StringBuffer();
        strings.append(text);
        try {

            int idx = strings.toString().indexOf("@");
            int idxEnd = 0;

            for (int i = 0; i < getIndexCount('@', text); i++) {

                if (idx >= 0) {

                    if (i == getIndexCount('@', text) - 1) {

                        idxEnd = strings.toString().indexOf(" ", idx);

                        if (idxEnd == -1) {
                            idxEnd = strings.length();
                        }
                    } else {
                        idxEnd = strings.toString().indexOf(" ", idx);

                    }


                    if (idxEnd - idx <= 1) {
                        continue;
                    }

                    strings.insert(idxEnd, "</font></b>");
                    strings.insert(idx, "<b><font color=\"#058dc7\">");

                    idx = strings.toString().lastIndexOf("</b>") + 4;
                    idx = strings.toString().indexOf("@", idx);

                } else {
                    return Html.fromHtml(text);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Html.fromHtml(strings.toString());
    }


    private static int getIndexCount(Character index, String test) {
        int sum = 0;
        for (int i = 0; i < test.length(); i++) {
            if (test.charAt(i) == index) {
                sum++;
            }
        }
        return sum;
    }

    public static void setAnalyticsTrackerScreenName(Context context, String content) {
        Tracker t = ((FanMindApplication) context.getApplicationContext()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);
        t.setScreenName(content);
        t.send(new HitBuilders.AppViewBuilder().build());
    }


    public static void setAnalyticsTracker(Intent intent, Activity activity) {
        try {
            int index = intent.getIntExtra("index", 0);

            Tracker t = ((FanMindApplication) activity.getApplication()).getTracker(
                    FanMindApplication.TrackerName.APP_TRACKER);


            switch (index) {
                case 0:
                    return;
                case 13://공지사항 푸쉬 14
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("PUSH")
                            .setAction("ALRAM CLICK")
                            .setLabel("공지사항 푸쉬")
                            .build());
                    break;
                case 14: //이벤트 푸쉬
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("PUSH")
                            .setAction("ALRAM CLICK")
                            .setLabel("이벤트 푸쉬")
                            .build());
                    break;
                case 15: //서포트 푸쉬
                    t.send(new HitBuilders.EventBuilder()
                            .setCategory("PUSH")
                            .setAction("ALRAM CLICK")
                            .setLabel("서포트 푸쉬 ")
                            .build());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static String getString(Context context, int id) {
        try {
            return context.getString(id);
        } catch (Exception e) {
            if (context == null) {
            }
            Intent intent = new Intent(context, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

        }
//        finally {
//            Intent intent = new Intent(context, IntroActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            context.startActivity(intent);
//
//        }
        return null;
    }


    public static void appDawn(Context context) {
        Uri uri = Uri.parse("market://details?id=specup.fanmind");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }


    public static String getAppVersion(Context context) {
        try {
            PackageInfo i = ((Activity) context).getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return i.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "0.0.0";
        }
    }

    public static void startLockScreen(Context context) {
        if (FanMindSetting.getLOCKSCREEN(context)) {
            context.startService(new Intent(context, LockService.class));
        } else {
            context.stopService(new Intent(context, LockService.class));
        }
    }


    public static int stringToInt(String tempString) {
        try {
            return Integer.valueOf(tempString);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String intToString(int tempInt) {
        try {
            return String.valueOf(tempInt);
        } catch (Exception e) {
            return "0";
        }
    }

    public static float stringToFloat(String tempString) {
        try {
            return Float.parseFloat(tempString);
        } catch (Exception e) {
            return 0f;
        }
    }


    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels, int w, int h, boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR) {

        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels * densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        //draw rectangles over the corners we want to be square
        if (squareTL) {
            canvas.drawRect(0, 0, w / 2, h / 2, paint);
        }
        if (squareTR) {
            canvas.drawRect(w / 2, 0, w, h / 2, paint);
        }
        if (squareBL) {
            canvas.drawRect(0, h / 2, w / 2, h, paint);
        }
        if (squareBR) {
            canvas.drawRect(w / 2, h / 2, w, h, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Bitmap input_1 = Bitmap.createScaledBitmap(input, w, h, true);
        canvas.drawBitmap(input_1, 0, 0, paint);
        return output;


    }


    public static String getUnicodeDecode(String unicode) throws Exception {
        StringBuffer sb = new StringBuffer();
        char ch = 0;

        for (int i = unicode.indexOf("\\u"); i > -1; i = unicode.indexOf("\\u")) {
            ch = (char) Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
            sb.append(unicode.substring(0, i));
            sb.append(ch);
            unicode = unicode.substring(i + 6);
        }
        sb.append(unicode);
        return sb.toString();
    }


    /**
     * 랜덤 스트링 생성
     *
     * @return
     */
    public static String getGenerateString() {
        char[] initRandomChar = {'A', 'B', 'C', 'D', 'F', 'G', 'H', 'I', 'J',
                'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'N', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0',
                '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        String randomString = "";
        int i = 0;
        while (i < 6) {
            int randomInt = (int) (Math.random() * initRandomChar.length);
            randomString += Character.toString(initRandomChar[randomInt]);
            i++;
        }

        return randomString;
    }

    public static void setShareSnsIndex(final Context context, String project_srl, String service) {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(context, mParams);
        mParams.add(new BasicNameValuePair("project_srl", project_srl));
        mParams.add(new BasicNameValuePair("service", service));
        mParams.add(new BasicNameValuePair("access", "app"));
        HttpRequest.setHttp1(context, URLAddress.PROJECT_SHARE(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (!Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    Utils.setToast(context, Utils.getJsonData(result, "message"));
                }

            }
        });
    }

    public static void setEventShareSnsIndex(final Context context, String event_srl, String service) {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();
        mParams = Utils.setSession(context, mParams);
        mParams.add(new BasicNameValuePair("event_srl", event_srl));
        mParams.add(new BasicNameValuePair("service", service));
        mParams.add(new BasicNameValuePair("access", "app"));
        HttpRequest.setHttp1(context, URLAddress.PROJECT_SHARE(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) throws JSONException {
                if (!Utils.getJsonData(result, "code").equals("SUCCESS")) {
                    Utils.setToast(context, Utils.getJsonData(result, "message"));
                }
            }
        });
    }


    /**
     * 이모티콘이 있을경우 "" 리턴, 그렇지 않을 경우 null 리턴
     **/
    static InputFilter specialCharacterFilter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                // 이모티콘 패턴
                Pattern unicodeOutliers = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]+");
                if (unicodeOutliers.matcher(source).matches()) {
                    return "";
                }
            }
            return null;
        }
    };

    public static void setEditTextFilter(EditText editText) {
        editText.setFilters(new InputFilter[]{specialCharacterFilter});
    }



}
