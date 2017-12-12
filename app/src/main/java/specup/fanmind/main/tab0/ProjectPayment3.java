package specup.fanmind.main.tab0;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.kakao.AppActionBuilder;
import com.kakao.AppActionInfoBuilder;
import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.kakao.KakaoTalkLinkMessageBuilder;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.login.OAuthID;
import specup.fanmind.main.tab4.UserPageMyAttendedActivity;

/**
 * 프로젝트 참여(결제 3page)
 * 완료 페이지
 */
public class ProjectPayment3 extends AppCompatActivity {

    JSONObject total_Json;
    String sSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_payment3);
        ActivityManager.getInstance().addActivity(this);
        try {
            total_Json = new JSONObject(getIntent().getStringExtra("total_Json"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setView();
    }

    Button button_share_url, button_main_project, button_my_project;

    private void setView() {

        TextView thankyouMessage2 = (TextView) findViewById(R.id.thankyouMessage2);
        thankyouMessage2.setText(getString(R.string.completed_payment));
        TextView textview_thankyouMessage = (TextView) findViewById(R.id.thankyouMessage);
        textview_thankyouMessage.setText(getString(R.string.completed_payment_message).replace("{HOST_NAME}", Utils.getJsonData(Utils.getJsonData(total_Json.toString(), "host"), "name")));


        Button button_share_kakao = (Button) findViewById(R.id.button_share_kakao);
        Button button_share_twitter = (Button) findViewById(R.id.button_share_twitter);
        Button button_share_facebook = (Button) findViewById(R.id.button_share_facebook);
        button_share_url = (Button) findViewById(R.id.button_share_url);
        button_main_project = (Button) findViewById(R.id.button_main_project);
        button_my_project = (Button) findViewById(R.id.button_my_project);

        button_share_kakao.setOnClickListener(onClick);
        button_share_twitter.setOnClickListener(onClick);
        button_share_facebook.setOnClickListener(onClick);
        button_share_url.setOnClickListener(onClick);
        button_main_project.setOnClickListener(onClick);
        button_my_project.setOnClickListener(onClick);

    }

    private KakaoLink kakaoLink;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
    private String imageSrc = null;
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_share_kakao: {
                    try {
                        kakaoLink = KakaoLink.getKakaoLink(ProjectPayment3.this);
                        kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

                        imageSrc = Utils.getJsonData(total_Json.toString(), "thumbnail_base") + Utils.getJsonData(total_Json.toString(), "thumbnail");
                        kakaoTalkLinkMessageBuilder.addText(Utils.getJsonData(total_Json.toString(), "title")
                                + " " + getString(R.string.attends_nickname).replace("{nickname}", FanMindSetting.getNICK_NAME(getApplicationContext()))

                        );
//
                        kakaoTalkLinkMessageBuilder.addAppButton("팬마음",
                                new AppActionBuilder()
                                        .addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("support=" + sSrl).setMarketParam("referrer=kakaotalklink").build())
                                        .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder(AppActionBuilder.DEVICE_TYPE.PHONE).setExecuteParam("execparamkey1=1111").build()).build()
                        );
                        kakaoTalkLinkMessageBuilder.addImage(imageSrc, 1000, 586);

                        kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), ProjectPayment3.this);
                        Utils.setShareSnsIndex(ProjectPayment3.this, Utils.getJsonData(total_Json.toString(), "project_srl"), "kakaotalk");
                    } catch (KakaoParameterException e) {
                        e.printStackTrace();
                    }

                }
                break;
                case R.id.button_share_twitter: {
                    OAuthID.shareTwitter(ProjectPayment3.this,0);
                }
                break;
                case R.id.button_share_facebook: {
                    String pic = "http://app.fanmaum.com/images/fm1.jpeg";

                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                .setContentTitle(Utils.getJsonData(total_Json.toString(), "title"))
                                .setContentDescription(Utils.getJsonData(total_Json.toString(), "slogun"))
                                .setContentUrl(Uri.parse(URLAddress.NEW_SERVER + "/projects/view/" + Utils.getJsonData(total_Json.toString(), "project_srl")
                                        + "/" + Utils.getLanquageLocal(ProjectPayment3.this)
                                        + "/" + Utils.getGenerateString()))
                                .setImageUrl(Uri.parse(Utils.getJsonData(total_Json.toString(), "thumbnail_base") + Utils.getJsonData(total_Json.toString(), "thumbnail")))
                                .build();

                        ShareDialog shareDialog = new ShareDialog(ProjectPayment3.this);
                        FacebookSdk.sdkInitialize(ProjectPayment3.this);
                        shareDialog.show(linkContent);
                        Utils.setShareSnsIndex(ProjectPayment3.this, Utils.getJsonData(total_Json.toString(), "project_srl"), "facebook");
                    }
                }
                break;
                case R.id.button_share_url: {
                    final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    final android.content.ClipData clipData = android.content.ClipData.newPlainText("text label",
                            Utils.getJsonData(total_Json.toString(), "title")
                                    + " " + getString(R.string.attends_nickname).replace("{nickname}", FanMindSetting.getNICK_NAME(getApplicationContext()))
                                    + " " + URLAddress.NEW_SERVER + "/projects/view/" + Utils.getJsonData(total_Json.toString(), "project_srl"));
                    clipboardManager.setPrimaryClip(clipData);
                    Utils.setSnackBar(ProjectPayment3.this, button_share_url, getString(R.string.clipboard_insert));

                }
                break;
                case R.id.button_main_project: {
                    Intent intent = new Intent(ProjectPayment3.this, MainActivity.class);
                    intent.putExtra("noti", true);
                    intent.putExtra("index", 1);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
                break;
                case R.id.button_my_project: {
                    getNetwork();

                }
                break;
            }
        }
    };

    public void  getNetwork() {

        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(this, mParams);

        HttpRequest.setHttp1(this, URLAddress.MAIN_PAGE(), mParams, new OnTask() {
                    @Override
                    public void onTask(int output, String result) throws JSONException {
                        if (Utils.getJsonData(result, "code").equals("SUCCESS") || Utils.getJsonData(result, "code").equals("UNLINKED")) {

                            if (Utils.getJsonData(result, "code").equals("SUCCESS")) {
                                FanMindSetting.setIsLinked(ProjectPayment3.this, true);
                            } else {
                                FanMindSetting.setIsLinked(ProjectPayment3.this, false);
                            }

                            //개설한 프로젝트
                            try {
                                JSONObject jsonObject_projects = new JSONObject(Utils.getJsonData(Utils.getJsonData(result, "data"), "projects"));
                                JSONArray jsonArray_attended = jsonObject_projects.getJSONArray("attended");

                                Intent intent = new Intent(getApplicationContext(), UserPageMyAttendedActivity.class);
                                intent.putExtra("jsonObjectData", Utils.getJsonData(result, "data"));
                                intent.putExtra("jsonArray_attended", jsonArray_attended.toString());
                                startActivity(intent);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Utils.setToast(ProjectPayment3.this, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                        }
                    }
                }

        );
    }


    public void onBack(View v) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityManager.getInstance().deleteActivity(this);
        finish();
    }
}