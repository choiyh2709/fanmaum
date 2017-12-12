package specup.fanmind.main.tab0;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.adapter.DefaultAdapter;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.vo.ProductVO;

/**
 * 상품 수량 선택
 */
public class AlertDialogPaymentProductMaximum extends DialogFragment {

    private View view;
    private Context context;
    private ProductVO productVO;
    private String title;
    private ResultInterface resultInterface;

    public AlertDialogPaymentProductMaximum(Context context, ProductVO productVO, String title , ResultInterface resultInterface) {
        this.context = context;
        this.productVO = productVO;
        this.resultInterface = resultInterface;
        this.title = title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.alertdialog_number_picker, container);

        setView();

        return view;
    }

    private void setView() {
        ((TextView) view.findViewById(R.id.title)).setText(title);


        ArrayList<Object> arr = new ArrayList<Object>();
        int max = Integer.valueOf(productVO.getAmount_max());
        int min = Integer.valueOf(productVO.getAmount_now());
        int maxIndex = max - min;

        //10개이상일 경우 맥스 10개로
        if(max==0 || maxIndex>10){
            maxIndex = 11;
        }

        for (int i1 = 1; i1 < maxIndex; i1++) {
            arr.add(String.valueOf(i1));
        }
        ListView listView = (ListView) view.findViewById(R.id.listView);
        DefaultAdapter defaultAdapter = new DefaultAdapter(context, arr);
        listView.setTag("AlertDialogPaymentProductMaximum");
        listView.setAdapter(defaultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resultInterface.onResult(position);
                getDialog().cancel();
            }
        });

        ((ImageView) view.findViewById(R.id.button_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
    }

}