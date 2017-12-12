package specup.fanmind.common.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

/**
 * Created by DEV-06 on 2016-05-10.
 */
public class AdapterLinearLayout  extends LinearLayout {

    private Adapter adapter;
    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            reloadChildViews();
        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AdapterLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public AdapterLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public AdapterLinearLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);
    }

    public void setAdapter(Adapter adapter) {
        if (this.adapter == adapter) return;
        this.adapter = adapter;
        if (adapter != null) adapter.registerDataSetObserver(dataSetObserver);
        reloadChildViews();
    }

    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adapter != null) adapter.unregisterDataSetObserver(dataSetObserver);
    }

    private void reloadChildViews() {
        removeAllViews();

        if (adapter == null) return;

        int count = adapter.getCount();


        for (int position = 0; position < count; ++position) {
            View v = adapter.getView(position, null, this);
            if (v != null) addView(v);
        }
        requestLayout();
    }
}
