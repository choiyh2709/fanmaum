package specup.fanmind.main.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import specup.fanmind.R;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.NaverAsyncTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.left.EventTwitterWebView;
import specup.fanmind.main.tab0.ProjectDetailWebView;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by DEV-06 on 2016-02-22.
 */
public class OAuthID {


    public static void onFacebookLogout() {

        LoginManager.getInstance().logOut();

    }
    public static LoginResult StaticloginResult;
    public static CallbackManager onFacebookLogin(final Context context, final ResultInterface resultInterface) {

        FacebookSdk.sdkInitialize(context);
        CallbackManager callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        StaticloginResult = loginResult;
                        AccessToken accessToken = loginResult.getAccessToken();

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("code", "OK");
                            jsonObject.put("accessToken", accessToken.getToken());
                            jsonObject.put("getApplicationId", accessToken.getApplicationId());
                            jsonObject.put("getUserId", accessToken.getUserId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resultInterface.onResult(jsonObject);
                    }

                    @Override
                    public void onCancel() {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("code", "CANCEL");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resultInterface.onResult(jsonObject);
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("code", "ERROR");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        resultInterface.onResult(jsonObject);
                        LoginManager.getInstance().logOut();

                    }
                });


        LoginManager.getInstance().logInWithReadPermissions((Activity) context, Arrays.asList("public_profile", "user_friends", "email"));

        return callbackManager;
    }

    /**
     * naver Logout
     *
     * @param context
     */
    public static boolean onNaverLogout(Context context) throws ExecutionException, InterruptedException {
        NaverAsyncTask naverAsyncTask = new NaverAsyncTask(context);
        return (boolean) naverAsyncTask.execute().get();

    }


    /**
     * naver Login
     *
     * @param context
     */


    public static void onNaverLogin(final Context context, final ResultInterface resultInterface) {

        OAuthLogin.getInstance().init(
                context.getApplicationContext()
                , "7VnrMT_vpDrSEezbX9aP"
                , "e_GmkKHUld"
                , "팬마음"// 네이버 앱의 로그인 화면에 표시할 애플리케이션 이름
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.
        );


        OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
            @Override
            public void run(boolean success) {

                OAuthLogin mOAuthLoginModule = OAuthLogin.getInstance();
                if (success) {
                    String accessToken = mOAuthLoginModule.getAccessToken(context);
                    String tokenType = mOAuthLoginModule.getTokenType(context);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("code", tokenType);
                        jsonObject.put("accessToken", accessToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    resultInterface.onResult(jsonObject);
                    /*
                    NEED_INIT: 초기화가 필요한 상태
                    NEED_LOGIN: 로그인이 필요한 상태. 접근 토큰(access token)과 갱신 토큰(refresh token)이 모두 없습니다.
                    NEED_REFRESH_TOKEN: 토큰 갱신이 필요한 상태. 접근 토큰은 없고, 갱신 토큰은 있습니다.
                    OK: 접근 토큰이 있는 상태. 단, 사용자가 네이버의 내정보 > 보안설정 > 외부 사이트 연결 페이지에서 연동을 해제했다면 서버에서는 상태 값이 유효하지 않을 수 있습니다.
                     */


//                              mOauthAT.setText(accessToken);
//                              mOauthRT.setText(refreshToken);
//                              mOauthExpires.setText(String.valueOf(expiresAt));
//                              mOauthTokenType.setText(tokenType);
//                              mOAuthState.setText(mOAuthLoginModule.getState(context).toString());
                } else {
                    String errorCode = mOAuthLoginModule.getLastErrorCode(context).getCode();
                    String errorDesc = mOAuthLoginModule.getLastErrorDesc(context);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("code", errorCode);
                        jsonObject.put("accessToken", errorDesc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mOAuthLoginModule.logout(context);
                    resultInterface.onResult(jsonObject);


                }
            }

            ;
        };

        OAuthLogin.getInstance().startOauthLoginActivity((Activity) context, mOAuthLoginHandler);
    }


    public static Twitter twitter = null;
    public static RequestToken requestToken = null;
    public static String sns_message = "";

    /**
     * 트위터 로그인
     * @param context
     */
    public static final int LOGIN_PAGE = 0 ;
    public static final int PROFILE_PAGE = 1;
    public static void twitter_login(final Context context, final int page) {
        String twitterKey = "mtDTDEtFf6Oe7EetJeygniydG";
        String twitterSecret = "tPoZ1WYTZxs1jUzqrDNWSjqcjma8zDodabSNFy1vvAZmHMbQ8a";
        try {
            {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(twitterKey);
                builder.setOAuthConsumerSecret(twitterSecret);
                twitter4j.conf.Configuration configuration = builder.build();
                twitter = new TwitterFactory(configuration).getInstance();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Uri url = Uri.parse("http://twitterresult.com");
                            requestToken = twitter.getOAuthRequestToken(url.toString());

                            Intent intent = new Intent(context, TwitterLoginWebView.class);
                            intent.putExtra("url", requestToken.getAuthorizationURL());
                            intent.putExtra("pageType", page);
                            context.startActivity(intent);
                        } catch (TwitterException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

    }


    /**
     * 트위터 공유
     *
     * @param context
     * @param flag    0 : project   1:event
     */
    public static void shareTwitter(final Context context, final int flag) {
        String twitterKey = "mtDTDEtFf6Oe7EetJeygniydG";
        String twitterSecret = "tPoZ1WYTZxs1jUzqrDNWSjqcjma8zDodabSNFy1vvAZmHMbQ8a";

        if (FanMindSetting.getIsLinked(context)) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(twitterKey);
            builder.setOAuthConsumerSecret(twitterSecret);
            twitter4j.conf.Configuration configuration = builder.build();
            twitter = new TwitterFactory(configuration).getInstance();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Uri url = Uri.parse("http://twitterresult.com");
                        requestToken = twitter.getOAuthRequestToken(url.toString());
                        if (flag == 0) {
                            Intent intent = new Intent(context, ProjectDetailWebView.class);
                            intent.putExtra("url", requestToken.getAuthorizationURL());
                            context.startActivity(intent);
                        } else if (flag == 1) {
                            Intent intent = new Intent(context, EventTwitterWebView.class);
                            intent.putExtra("url", requestToken.getAuthorizationURL());
                            context.startActivity(intent);
                        }

                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            Utils.setToast(context, context.getString(R.string.sns_please_connect));
        }
    }

    /**
     * 페이스북 공유
     *
     * @param context
     * @param total_Json
     */
    public static LoginManager manager;

    public static void shareFacebook(final Context context, final String total_Json, CallbackManager callbackManager, final int flag) {
        if (FanMindSetting.getIsLinked(context)) {
            switch (flag) {
                case 0: {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(Utils.getJsonData(total_Json.toString(), "title"))
                                .setContentDescription(Utils.getJsonData(total_Json.toString(), "slogun"))
                                .setContentUrl(Uri.parse(URLAddress.NEW_SERVER + "/projects/view/" + Utils.getJsonData(total_Json.toString(), "project_srl")
                                        + "/" + Utils.getLanquageLocal(context)
                                        + "/" + Utils.getGenerateString()))
                                .setImageUrl(Uri.parse(Utils.getJsonData(total_Json.toString(), "thumbnail_base") + Utils.getJsonData(total_Json.toString(), "thumbnail")))
                                .build();


                        ShareDialog shareDialog = new ShareDialog((Activity) context);

                        shareDialog.show(linkContent);
                    }
                }
                break;
                case 1: {
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(Utils.getJsonData(total_Json.toString(), "title"))
                                .setContentDescription(Utils.getJsonData(total_Json.toString(), "present"))
                                .setContentUrl(Uri.parse(URLAddress.NEW_SERVER + "/events/view/" + Utils.getJsonData(total_Json.toString(), "event_srl")
                                        + "/" + Utils.getLanquageLocal(context)
                                        + "/" + Utils.getGenerateString()))
                                .setImageUrl(Uri.parse(Utils.getJsonData(total_Json.toString(), "thumbnail_base") + Utils.getJsonData(total_Json.toString(), "thumbnail")))
                                .build();


                        ShareDialog shareDialog = new ShareDialog((Activity) context);

                        shareDialog.show(linkContent);
                    }
                }
                break;
            }


        } else {
            Utils.setToast(context, context.getString(R.string.sns_please_connect));
        }

    }


    private static KakaoLink kakaoLink;
    private static KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private static String imageSrc = null;

    public static void shareKakaotalk(final Context context, String total_Json, int flag) {

        try {
            kakaoLink = KakaoLink.getKakaoLink(context);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            imageSrc = Utils.getJsonData(total_Json.toString(), "thumbnail_base") + Utils.getJsonData(total_Json.toString(), "thumbnail");

            kakaoTalkLinkMessageBuilder.addText(Utils.getJsonData(total_Json.toString(), "title")
                    + " " + context.getString(R.string.attends_nickname).replace("{nickname}", FanMindSetting.getNICK_NAME(context))
            );

            switch (flag) {
                case 0: {
                    kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                            new AppActionBuilder()
                                    .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("support=" + Utils.getJsonData(total_Json.toString(), "project_srl")).setMarketParam("referrer=kakaotalklink").build())
                                    .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                    );
                }
                break;
                case 1: {
                    kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                            new AppActionBuilder()
                                    .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("support=" + Utils.getJsonData(total_Json.toString(), "event_srl")).setMarketParam("referrer=kakaotalklink").build())
                                    .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                    );
                }
                break;
            }

            kakaoTalkLinkMessageBuilder.addImage(imageSrc, 1000, 586);

            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), context);

            Utils.setShareSnsIndex(context, Utils.getJsonData(total_Json.toString(), "project_srl"), "kakaotalk");


        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }


}
