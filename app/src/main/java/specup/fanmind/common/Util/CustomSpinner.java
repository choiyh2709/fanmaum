package specup.fanmind.common.Util;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;

/**
 * Created by DEV-06 on 2016-02-04.
 */
public class CustomSpinner extends AppCompatSpinner {


    public CustomSpinner(Context context) {
        super(context);
    }

    @Override
    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
    }
}
