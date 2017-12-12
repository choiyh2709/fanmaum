package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import specup.fanmind.R;
import specup.fanmind.vo.NoticeVO;


public class Project_noticeAdapter extends BaseAdapter {
    private List<Object> mList = null;
    private LayoutInflater layout_In = null;
    private Context mContext;

    public Project_noticeAdapter(Context context, List<Object> list) {
        this.mList = list;
        mContext = context;
        layout_In = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = layout_In.inflate(R.layout.list_project_notice, null);
            holder.notice_srl = (TextView)convertView.findViewById(R.id.notice_srl);
            holder.subject = (TextView)convertView.findViewById(R.id.subject);
            holder.written_time = (TextView)convertView.findViewById(R.id.written_time);


            holder.notice_srl.setText( "# "+((NoticeVO) mList.get(position)).getmEvent_srl() );
            holder.subject.setText(((NoticeVO) mList.get(position)).getSubject());
            holder.written_time.setText(((NoticeVO) mList.get(position)).getWritten_time());

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        return convertView;
    }

    public class Holder {
        TextView notice_srl,subject,written_time;
    }
}
