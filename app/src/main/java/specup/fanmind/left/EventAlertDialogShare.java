package specup.fanmind.left;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.facebook.CallbackManager;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.main.login.OAuthID;


public class EventAlertDialogShare extends DialogFragment {

    View view;
    Context context;
    String total_Json;
    CallbackManager callbackManager;

    public EventAlertDialogShare(Context context, String total_Json, CallbackManager callbackManager) {
        this.context = context;
        this.total_Json = total_Json;
        this.callbackManager = callbackManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.alertdialog_share, container);

        setView();

        return view;

    }


    Button button_share_url;

    private void setView() {

        Button button_share_kakao = (Button) view.findViewById(R.id.button_share_kakao);
        Button button_share_twitter = (Button) view.findViewById(R.id.button_share_twitter);
        Button button_share_facebook = (Button) view.findViewById(R.id.button_share_facebook);
        button_share_url = (Button) view.findViewById(R.id.button_share_url);

        button_share_kakao.setOnClickListener(onClick);
        button_share_twitter.setOnClickListener(onClick);
        button_share_facebook.setOnClickListener(onClick);
        button_share_url.setOnClickListener(onClick);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_share_kakao: {
                    OAuthID.shareKakaotalk(context, total_Json,1);
                }
                break;
                case R.id.button_share_twitter: {
                    OAuthID.shareTwitter(context,1);
                }
                break;
                case R.id.button_share_facebook: {

                    OAuthID.shareFacebook(context, total_Json, callbackManager, 1);
                }
                break;
                case R.id.button_share_url: {
                    final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    final android.content.ClipData clipData = android.content.ClipData.newPlainText("text label",
                            Utils.getJsonData(total_Json.toString(), "title")
                                    + " " + getString(R.string.attends_nickname).replace("{nickname}", FanMindSetting.getNICK_NAME(context))
                                    + " " + URLAddress.NEW_SERVER + "/events/view/" + Utils.getJsonData(total_Json.toString(), "event_srl"));
                    clipboardManager.setPrimaryClip(clipData);
                    Utils.setSnackBar(context, button_share_url, getString(R.string.clipboard_insert));

                }
                break;
            }
            getDialog().cancel();
        }
    };


    /**
     * 카카오스토리 다운로드 연결.
     */
    private void goKakaoStoryDownload() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("market://details?id=com.kakao.story"));
        startActivity(i);
    }


}