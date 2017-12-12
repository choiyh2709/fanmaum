package specup.fanmind.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.vo.ProjectVO;
import specup.fanmind.vo.SupportList;

/**
 * 프로젝트 리스트
 */
public class ProjectListAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;
    private String sToday;
    boolean isDelivery, isAll;
    OnClickListener mClick;


    public ProjectListAdapter(Context context, List<Object> mList) {
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
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setRoundAsCircle(true);

        if( convertView  == null ){
            convertView = layout_In.inflate(R.layout.list_project_listtest, null);
            holder = new Holder();
            // 메인 이미지
            holder.iamge_main = (SimpleDraweeView) convertView.findViewById(R.id.iamge_main);
            // 프로젝트 타이틀
            holder.textView_projectTitle = (TextView) convertView.findViewById(R.id.textView_projectTitle);
            //퍼센트
            holder.seekbar_projectDetail = (SeekBar) convertView.findViewById(R.id.seekbar_projectDetail);
            // 모인마음
            holder.textview_Heart_now = (TextView) convertView.findViewById(R.id.textview_Heart_now);
            // 퍼센트(글자)
            holder.textview_percent = ((TextView) convertView.findViewById(R.id.textview_percent));
            // 총 참여자수
            holder.textview_Total_attends = ((TextView) convertView.findViewById(R.id.textview_Total_attends));
            // 마감일
            holder.textView_deadline_value = (TextView) convertView.findViewById(R.id.textView_deadline_value);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ProjectVO projectVO = (ProjectVO) mList.get(position);

        holder.textView_projectTitle.setText(projectVO.getTitle());
        holder.seekbar_projectDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        double sum = Utils.getPercent(projectVO.getHeart_now(), projectVO.getHeart_goal());
        holder.seekbar_projectDetail.setProgress((int) sum);
        holder.textview_Heart_now.setText(Html.fromHtml(mContext.getString(R.string.maum_value2).replace("{VALUE}", Utils.getMoney(projectVO.getHeart_now()))) );

        holder.textview_percent.setText(Html.fromHtml(mContext.getString(R.string.percent2).replace("{NUMBER}", String.valueOf((int) sum))) );
        holder.textview_Total_attends.setText(Html.fromHtml(mContext.getString(R.string.joinFan_value2).replace("{VALUE}", projectVO.getTotal_attends())));
        int change = Utils.GetDifferenceOfDate(projectVO.getClose_time(), Utils.getTime());
        if (change >= 0) {
            holder.textView_deadline_value.setText(Html.fromHtml(mContext.getString(R.string.deadline_value2).replace("{VALUE}", String.valueOf(change))));
        } else {
            holder.textView_deadline_value.setText(mContext.getString(R.string.deadline2));
        }


        Uri uri2 = Uri.parse(projectVO.getThumbnail_base() + projectVO.getThumbnail());
        HttpRequest.frescoToproundImage(uri2,holder.iamge_main);

        return convertView;
    }

    public class Holder {
        SimpleDraweeView iamge_main;;
        TextView textview_Heart_now, textView_projectTitle, textview_percent, textview_Total_attends, textView_deadline_value;
        SeekBar seekbar_projectDetail;
    }

    //리스트뷰의 아이템 갯수를 가져와 높이를 설정한다
    public static void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null){
            return;
        }

        int totalHeight = 0;
        int desiredwidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),View.MeasureSpec.AT_MOST);
        int count = listAdapter.getCount();
        for (int i = 0; i<count; i++){
            View listItem = listAdapter.getView(i,null,listView);
            listItem.measure(desiredwidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private static class ViewHolder {

        public final ImageView bananaView;
        public final TextView phoneView;

        public ViewHolder(ImageView bananaView, TextView phoneView) {
            this.bananaView = bananaView;
            this.phoneView = phoneView;
        }
    }
}
