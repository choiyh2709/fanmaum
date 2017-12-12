package specup.fanmind;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import specup.fanmind.adapter.Alert_address_search_listAdapter;
import specup.fanmind.common.Util.ResultInterface;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.vo.AddressVO;
import twitter4j.HttpResponse;

/**
 * 주소 검색하기
 */
public class AlertAddressSearch extends DialogFragment {

    View view;
    Context context;
    EditText edittext_address_search;
    ListView listView;
    ResultInterface resultInterface;
    private String key = "0415a0f9489947fd61487753710615";
    private ArrayAdapter<String> addressListAdapter;
    private ArrayList<String> addressSearchResultArr = new ArrayList<>();
    private String putAddress;

    public AlertAddressSearch(Context context) {
           this.context = context;
       }

    public AlertAddressSearch(Context context, ResultInterface resultInterface) {
        this.context = context;
        this.resultInterface = resultInterface;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.alert_address_search, container);

        setView();

        return view;

    }

    private specup.fanmind.common.http.AsyncTask mAsyncTask = null;

    private void getNetwork(String search_address_name) {
        //https://apis.daum.net/local/v1/search/keyword.json?apikey={apikey}&query=카카오프렌즈
        putAddress = search_address_name;
        new GetAddressDataTask().execute();

/*        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams.add(new BasicNameValuePair("apikey", "3adec65f556829b64a180d17bfd973fb"));
        mParams.add(new BasicNameValuePair("query", search_address_name));
        mParams.add(new BasicNameValuePair("sort", "2"));

        HttpRequest.setHttpGet1(getActivity(), URLAddress.GET_ADDRESS(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {
                if (Utils.getJsonData(Utils.getJsonData(Utils.getJsonData(result, "channel"), "info"), "count").compareTo("0") > 0) {

                    String string_item = Utils.getJsonData(Utils.getJsonData(result, "channel"), "item");

                    List<Object> list = new ArrayList<Object>();
                    try {
                        JSONArray jsonArray = new JSONArray(string_item);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AddressVO addressVo = new AddressVO();

                            addressVo.setNewAddress(jsonObject.getString("newAddress"));
                            addressVo.setOldAddress(jsonObject.getString("address"));
                            addressVo.setZipcode(jsonObject.getString("zipcode"));

                            list.add(addressVo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Alert_address_search_listAdapter alert_address_search_listAdapter = new Alert_address_search_listAdapter(getActivity(), list);
                    listView.setAdapter(alert_address_search_listAdapter);
                    LinearLayout layout_list = (LinearLayout) view.findViewById(R.id.layout_list);
                    layout_list.setVisibility(View.VISIBLE);
                } else {
                    Utils.setToast(getActivity(), getActivity().getString(R.string.no_data));
                }
            }
        });*/
    }

    private class GetAddressDataTask extends AsyncTask<String, Void, HttpResponse> {
        @Override
        protected HttpResponse doInBackground(String... urls) {
            HttpResponse response = null;
            final String apiurl = "http://biz.epost.go.kr/KpostPortal/openapi";
            ArrayList<String> addressInfo = new ArrayList<String>();
            HttpURLConnection conn = null;
            try {
                StringBuffer sb = new StringBuffer(3);
                sb.append(apiurl);
                sb.append("?regkey=" + key + "&target=post&query=");
                sb.append(URLEncoder.encode(putAddress, "EUC-KR"));
                String query = sb.toString();
                URL url = new URL(query);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("accept-language", "ko");
                DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                byte[] bytes = new byte[4096];
                InputStream in = conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (true) {
                    int red = in.read(bytes);
                    if (red < 0) break;
                    baos.write(bytes, 0, red);
                }
                String xmlData = baos.toString("utf-8");
                baos.close();
                in.close();
                conn.disconnect();
                Document doc = docBuilder.parse(new InputSource(new StringReader(xmlData)));
                Element el = (Element) doc.getElementsByTagName("itemlist").item(0);
                for (int i = 0; i < ((Node) el).getChildNodes().getLength(); i++) {
                    Node node = ((Node) el).getChildNodes().item(i);
                    if (!node.getNodeName().equals("item")) {
                        continue;
                    }
                    String address = node.getChildNodes().item(1).getFirstChild().getNodeValue();
                    String post = node.getChildNodes().item(3).getFirstChild().getNodeValue();
                    Log.d("address value", "address = " + address);
                    addressInfo.add(address + "\n우편번호:" + post.substring(0, 3) + "-" + post.substring(3));
                }
                addressSearchResultArr = addressInfo;
                publishProgress();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) conn.disconnect();
                } catch (Exception e) {
                }
            }
            return response;
        }
        @Override
        protected void onProgressUpdate(Void... values) {

            //TODO Auto-generated method stub

            super.onProgressUpdate(values);
            String[] addressStrArray = new String[addressSearchResultArr.size()];
            addressStrArray = addressSearchResultArr.toArray(addressStrArray);
            addressListAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, addressStrArray);
            listView.setAdapter(addressListAdapter);
        }
    }

    private void setView() {
        edittext_address_search = (EditText) view.findViewById(R.id.edittext_address_search);
        Button button_search = (Button) view.findViewById(R.id.button_search);
        button_search.setOnClickListener(onClickListener);

        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);
        button_cancel.setOnClickListener(onClickListener);
        Button button_search_cancel = (Button) view.findViewById(R.id.button_search_cancel);
        button_search_cancel.setOnClickListener(onClickListener);


        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddressVO addressVO = ((AddressVO) parent.getAdapter().getItem(position));
                if (addressVO.getNewAddress().equals("")) {
                    resultInterface.onResult(addressVO.getOldAddress());

                } else {
                    resultInterface.onResult(addressVO.getNewAddress());

                }
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_search: {
                    getNetwork(edittext_address_search.getText().toString().trim());
                    break;
                }
                case R.id.button_cancel: {
                    getDialog().cancel();
                    break;
                }
                case R.id.button_search_cancel: {
                    edittext_address_search.setText("");
                    break;
                }
            }
        }
    };


}