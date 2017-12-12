package specup.fanmind.main.tab1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import specup.fanmind.R;
import specup.fanmind.adapter.AlertDialog_Adapter;
import specup.fanmind.common.Util.ResultInterface;


public class AlertDialogSort extends DialogFragment {

    View view;
    ResultInterface resultInterface;
    String title ;
    ArrayList<Object> content;
    Context context;
    String selectSortValue;

    public AlertDialogSort(Context context, String title, ResultInterface resultInterface, String[] content , String selectSortValue) {
        this.resultInterface = resultInterface;
        this.content = new ArrayList<Object>(Arrays.asList(content));
        this.context = context;
        this.title = title;
        this.selectSortValue = selectSortValue;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.alertdialog_sort_popular, container);

        setView();

        return view;

    }

    private void setView() {

        TextView textview_title = (TextView) view.findViewById(R.id.title);
        textview_title.setText(title);

        ListView listView = (ListView) view.findViewById(R.id.listView);
        AlertDialog_Adapter adapter = new AlertDialog_Adapter(context, content,selectSortValue);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultInterface.onResult(position);
                getDialog().cancel();
            }
        });
    }
}