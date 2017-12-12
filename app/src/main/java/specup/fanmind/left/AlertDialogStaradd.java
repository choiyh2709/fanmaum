package specup.fanmind.left;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;

public class AlertDialogStaradd extends DialogFragment implements OnTask{

    View view;
    Context context;
    EditText tv_name;
    Button btn_cancel,btn_yes;
    List<NameValuePair> mParams;
    AsyncTask mAsyncTask;

    public AlertDialogStaradd(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.activity_alert_dialog_staradd, container);

        setView();

        return view;

    }

    private void setView() {

        tv_name = (EditText) view.findViewById(R.id.tv_name);
        btn_cancel = (Button) view.findViewById(R.id.cancel);
        btn_yes = (Button) view.findViewById(R.id.yes);

        btn_yes.setOnClickListener(onClick);
        btn_cancel.setOnClickListener(onClick);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(tv_name.getApplicationWindowToken(),  InputMethodManager.SHOW_FORCED, 0);

    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cancel: {
                    getDialog().cancel();
                }
                case R.id.yes: {
                    if (FanMindSetting.getLOGIN_OK(getActivity())) {
                        requestStar();
                        getDialog().cancel();
                    } else {
                        Utils.showDialog(getActivity());
                    }
                }
            }
        }
    };

    private void requestStar(){
        mParams = new ArrayList<NameValuePair>();
        mParams.add(new BasicNameValuePair("text", tv_name.getText().toString().trim()));
        mParams = Utils.setSession(getActivity(), mParams);
        HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.STAR_ADD(getActivity()),
                AsyncTaskValue.STAR_ADD, this);
    }

    @Override
    public void onTask(int output, String result) {
        // TODO Auto-generated method stub
      if (output == AsyncTaskValue.STAR_ADD_NUM) {
            if (Utils.getJsonDataString(result).equals("900")) {//��ǥ����
                Utils.setToast(context, R.string.request05);
            } else if (Utils.getJsonDataString(result).equals("5301")) { //�ߺ���ǥ
                Utils.setToast(context, R.string.request08);
            }
        }
    }
}
