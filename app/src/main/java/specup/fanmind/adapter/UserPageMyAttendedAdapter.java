package specup.fanmind.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.main.tab4.ChangeDeliveryInfomation;
import specup.fanmind.vo.ProjectVO;
import specup.fanmind.vo.SupportList;

/**
 * 프로젝트 리스트
 */
public class UserPageMyAttendedAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sToday;
    boolean isDelivery, isAll;
    OnClickListener mClick;

    public UserPageMyAttendedAdapter(Context context, List<Object> mList) {
        mContext = context;
        this.mList = mList;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void add(SupportList data) {
        mList.add(data);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        final ProjectVO projectVO = (ProjectVO) mList.get(position);
        if (convertView == null) {
            convertView = layout_In.inflate(R.layout.list_userpage_my_attended, null);
            holder = new Holder();

            holder.iamge_main = (ImageView) convertView.findViewById(R.id.iamge_main);
            holder.imageView_profile = (ImageView) convertView.findViewById(R.id.imageView_profile);
            holder.textView_projectTitle = (TextView) convertView.findViewById(R.id.textView_projectTitle);

            // host
            holder.textView_hostNickName = (TextView) convertView.findViewById(R.id.textView_hostNickName);
            holder.imageView_profile = (ImageView) convertView.findViewById(R.id.imageView_profile);

            //퍼센트
            holder.seekbar_projectDetail = (SeekBar) convertView.findViewById(R.id.seekbar_projectDetail);
            holder.textview_Heart_now = (TextView) convertView.findViewById(R.id.textview_Heart_now);
            holder.textview_percent = ((TextView) convertView.findViewById(R.id.textview_percent));

            //참여한팬
            holder.textview_Total_attends = ((TextView) convertView.findViewById(R.id.textview_Total_attends));

            //마감일
            holder.textView_deadline_value = (TextView) convertView.findViewById(R.id.textView_deadline_value);

            //결제금액
            holder.textview_used_point = ((TextView) convertView.findViewById(R.id.textview_used_point));

            //결제상태
            holder.textview_isPaid = ((TextView) convertView.findViewById(R.id.textview_isPaid));
            holder.textview_payment_time = ((TextView) convertView.findViewById(R.id.textview_payment_time));

            //배송정보
            holder.textview_address_pri = ((TextView) convertView.findViewById(R.id.textview_address_pri));
            holder.textview_address_change = ((TextView) convertView.findViewById(R.id.textview_address_change));

            //숨겨진 layout
            RelativeLayout layout_clickable = (RelativeLayout) convertView.findViewById(R.id.layout_clickable);
            LinearLayout layout_payment = (LinearLayout) convertView.findViewById(R.id.layout_payment);
            layout_clickable.setTag(layout_payment);
            layout_clickable.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout layout_payment = (LinearLayout) v.getTag();
                    if (layout_payment.getVisibility() == View.GONE) {
                        layout_payment.setVisibility(View.VISIBLE);
                    } else {
                        layout_payment.setVisibility(View.GONE);
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.textView_projectTitle.setText(projectVO.getTitle());
        HttpRequest.glideImage(mContext, projectVO.getThumbnail_base() + projectVO.getThumbnail(), holder.iamge_main);
        holder.textView_hostNickName.setText(Utils.getJsonData(projectVO.getHost(), "name"));
        HttpRequest.getNetworkCircleImage(mContext,Utils.getJsonData(projectVO.getHost(), "profile_base") + Utils.getJsonData(projectVO.getHost(), "profile"), holder.imageView_profile);
        holder.seekbar_projectDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        double sum = Utils.getPercent(projectVO.getHeart_now(), projectVO.getHeart_goal());
        holder.seekbar_projectDetail.setProgress((int) sum);
        holder.textview_Heart_now.setText(mContext.getString(R.string.maum_value).replace("{VALUE}", projectVO.getHeart_now()));
        holder.textview_percent.setText(mContext.getString(R.string.listsupport10).replace("{NUMBER}", String.valueOf((int) sum)));
        holder.textview_Total_attends.setText(mContext.getString(R.string.joinFan_value).replace("{VALUE}", projectVO.getTotal_attends()));
        holder.textview_used_point.setText(mContext.getString(R.string.maum_num).replace("{VALUE}", projectVO.getPayment_user_point()));
        int change = Utils.GetDifferenceOfDate(projectVO.getClose_time(), Utils.getTime());
        if (change == 0) {
            holder.textView_deadline_value.setText(mContext.getString(R.string.deadline_value).replace("{VALUE}", "TODAY"));
        } else if (change > 0) {
            holder.textView_deadline_value.setText(mContext.getString(R.string.deadline_value).replace("{VALUE}", "D-" + change));
        } else {
            int temp = Math.abs(change);
            holder.textView_deadline_value.setText(mContext.getString(R.string.deadline2));
        }
        String tempIsPaid = "결제대기";
        if (projectVO.getPayment_is_paid().equals("Y")) {
            tempIsPaid = "결제함";
        }
        holder.textview_isPaid.setText(tempIsPaid);
        holder.textview_payment_time.setText(projectVO.getPayment_time());
        holder.textview_address_pri.setText(projectVO.getPayment_address_pri() + projectVO.getPayment_address_ext());
        holder.textview_address_change.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ChangeDeliveryInfomation.class);
                intent.putExtra("projectVO", projectVO);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    public static CustomDialog mDialog;

    public void showDialog(final ProjectVO projectVO) {
        String title = mContext.getString(R.string.payment_cancel);
        String content = mContext.getString(R.string.payment_cancel_content);
        String left = mContext.getString(R.string.payaccountcancel03);
        String right = mContext.getString(R.string.payment_cancel_ok);
        mDialog = new CustomDialog(mContext, title, content, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                getNetwork(projectVO);
            }
        }, left, right);
        mDialog.show();
    }

    private void getNetwork(ProjectVO projectVO) {


        List<NameValuePair> mParams = new ArrayList<NameValuePair>();

        mParams = Utils.setSession(mContext, mParams);
        mParams.add(new BasicNameValuePair("order_srl", projectVO.getPayment_order_srl()));

        HttpRequest.setHttp1(mContext, URLAddress.PROJECT_ATTENDS_CANCEL(), mParams, new OnTask() {
            @Override
            public void onTask(int output, String result) {
                Utils.setToast(mContext, Utils.getJsonData(result, "message") + " ( " + Utils.getJsonData(result, "code") + " )");
            }
        });
    }


    public class Holder {
        ImageView iamge_main, imageView_profile;
        TextView textview_payment_cancel, textview_address_change, textview_payment_time, textView_hostNickName, textview_Heart_now, textView_projectTitle, textview_percent, textview_Total_attends, textView_deadline_value, textview_address_pri, textview_isPaid, textview_used_point;
        SeekBar seekbar_projectDetail;
        RelativeLayout layout;
    }
}
