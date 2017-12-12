package specup.fanmind;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;

public class DailyActivity extends Activity {

    ImageView mImage[] = new ImageView[25];

    String networkResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_daily);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        networkResult = getIntent().getStringExtra("result");

        setView();
    }

    private void setView() {
        mImage[0] = (ImageView) findViewById(R.id.img1);
        mImage[1] = (ImageView) findViewById(R.id.img2);
        mImage[2] = (ImageView) findViewById(R.id.img3);
        mImage[3] = (ImageView) findViewById(R.id.img4);
        mImage[4] = (ImageView) findViewById(R.id.img5);
        mImage[5] = (ImageView) findViewById(R.id.img6);
        mImage[6] = (ImageView) findViewById(R.id.img7);
        mImage[7] = (ImageView) findViewById(R.id.img8);
        mImage[8] = (ImageView) findViewById(R.id.img9);
        mImage[9] = (ImageView) findViewById(R.id.img10);
        mImage[10] = (ImageView) findViewById(R.id.img11);
        mImage[11] = (ImageView) findViewById(R.id.img12);
        mImage[12] = (ImageView) findViewById(R.id.img13);
        mImage[13] = (ImageView) findViewById(R.id.img14);
        mImage[14] = (ImageView) findViewById(R.id.img15);
        mImage[15] = (ImageView) findViewById(R.id.img16);
        mImage[16] = (ImageView) findViewById(R.id.img17);
        mImage[17] = (ImageView) findViewById(R.id.img18);
        mImage[18] = (ImageView) findViewById(R.id.img19);
        mImage[19] = (ImageView) findViewById(R.id.img20);
        mImage[20] = (ImageView) findViewById(R.id.img21);
        mImage[21] = (ImageView) findViewById(R.id.img22);
        mImage[22] = (ImageView) findViewById(R.id.img23);
        mImage[23] = (ImageView) findViewById(R.id.img24);
        mImage[24] = (ImageView) findViewById(R.id.img25);


        int attend_continuity = Utils.stringToInt(Utils.getJsonData(Utils.getJsonData(networkResult, "data"), "attend_continuity"));
        attend_continuity-=1;

        for (int i = 0; i < attend_continuity; i++) {
            if (i == 2) {
                mImage[i].setBackgroundResource(R.drawable.daily_10_gary);
            } else if (i == 4) {
                mImage[i].setBackgroundResource(R.drawable.daily_25_gary);
            } else if (i == 9) {
                mImage[i].setBackgroundResource(R.drawable.daily_50_gary);
            } else if (i == 12) {
                mImage[i].setBackgroundResource(R.drawable.daily_30_gary);
            } else if (i == 14) {
                mImage[i].setBackgroundResource(R.drawable.daily_100_gary);
            } else if (i == 19) {
                mImage[i].setBackgroundResource(R.drawable.daily_200_gary);
            } else if (i == 22) {
                mImage[i].setBackgroundResource(R.drawable.daily_35_gary);
            } else if (i == 24) {
                mImage[i].setBackgroundResource(R.drawable.daily_400_gary);
            } else {
                mImage[i].setBackgroundResource(R.drawable.daily_check_gray);
            }
        }

        if (attend_continuity == 2) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_10_red);
        } else if (attend_continuity == 4) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_25_red);
        } else if (attend_continuity == 9) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_50_red);
        } else if (attend_continuity == 12) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_30_red);
        } else if (attend_continuity == 14) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_100_red);
        } else if (attend_continuity == 19) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_200_red);
        } else if (attend_continuity == 22) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_35_red);
        } else if (attend_continuity == 24) {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_400_red);
        } else {
            mImage[attend_continuity].setBackgroundResource(R.drawable.daily_check_red);
        }
        resultDialog(Utils.getJsonData(networkResult, "message").replace("<br>", "\n").replace(":", "\n"));

    }

    CustomDialog mDialog;

    private void resultDialog(String content) {
        String title = getString(R.string.alert);
        String btn = getString(R.string.confirmation);
        mDialog = new CustomDialog(this, title, content,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                }, btn);
        mDialog.show();
    }

    public void onClose(View v) {
        finish();
    }

}
