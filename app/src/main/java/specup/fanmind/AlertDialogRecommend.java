package specup.fanmind;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;


public class AlertDialogRecommend extends DialogFragment {

    View view;
    Context context;

    public AlertDialogRecommend(Context context) {
        this.context = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.alertdialog_recommend, container);

        setView();

        return view;

    }

    private void setView() {
        TextView textView_content = (TextView) view.findViewById(R.id.textView_content);
        textView_content.setText(getString(R.string.recommend_content).replace("{NICK}", FanMindSetting.getNICK_NAME(getActivity())));

        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);

        button_cancel.setOnClickListener(onClick);
        Button button_registration = (Button) view.findViewById(R.id.button_registration);
        button_registration.setOnClickListener(onClick);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_cancel: {
                    getDialog().cancel();
                }
                break;
                case R.id.button_registration: {
                    final EditText recommendUserName = (EditText) view.findViewById(R.id.recommendUserName);
                    final String tempNickName = recommendUserName.getText().toString();
                    List<NameValuePair> mParams = new ArrayList<NameValuePair>();
                    mParams = Utils.setSession(getActivity(), mParams);
                    mParams.add(new BasicNameValuePair("nick", tempNickName));
                    HttpRequest.setHttp1(getActivity(), URLAddress.RECOMMEND_USER(), mParams, new OnTask() {
                        @Override
                        public void onTask(int output, String result) throws JSONException {
                            if (Utils.getJsonData(result, "code").equals("MEMBER_RECOMMENDED")) {

                                FanMindSetting.setNICK_NAME(getActivity(), tempNickName);
                                Utils.setToast(getActivity(), Utils.getJsonData(result, "message"));
                                getDialog().cancel();
                            } else {
                                Utils.setToast(getActivity(), Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
                            }
                        }
                    });
                }
                break;
            }
        }
    };


}