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

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.adapter.DefaultAdapter2;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.ProductVO;

/**
 * 배송지 선택
 */
public class AlertDialogPaymentDeliveryImformation extends DialogFragment {

    private View view;
    private Context context;
    private ProductVO productVO;
    private ResultInterface resultInterface;
    private String title;
    List<Object> arrayCountryData;

    public AlertDialogPaymentDeliveryImformation(Context context, ProductVO productVO, String title, List<Object> arrayCountryData, ResultInterface resultInterface) {
        this.context = context;
        this.productVO = productVO;
        this.resultInterface = resultInterface;
        this.title = title;
        this.arrayCountryData = arrayCountryData;
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


//        arr.add(String.valueOf(context.getString(R.string.default_delivery) + "(" + productVO.getShip_cost() + "원)"));
//        if (productVO.getIs_ship_cost_extra().equalsIgnoreCase("Y")) {
//            arr.add(String.valueOf(context.getString(R.string.jeju_delivery) + "(" + productVO.getShip_cost_extra() + "원)"));
//        }


        ListView listView = (ListView) view.findViewById(R.id.listView);
        DefaultAdapter2 defaultAdapter = new DefaultAdapter2(context, arrayCountryData);
        listView.setTag("AlertDialogPaymentDeliveryImformation");
        listView.setAdapter(defaultAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                getDialog().cancel();
                resultInterface.onResult(position);
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