package specup.fanmind.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.CommentList;



public class NoticeAdapter extends BaseAdapter{
	private ArrayList<CommentList> mList = null;
	private LayoutInflater layout_In = null;
	Context mContext;

	public NoticeAdapter(Context context,  ArrayList<CommentList> mList){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	public void add(CommentList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public CommentList getItem(int position) {
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
		// TODO Auto-generated method stub
		Holder holder ;
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_notice, null);
			holder.tv01 = (TextView)convertView.findViewById(R.id.listnotice_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listnotice_tv02);
			convertView.setTag(holder);
		}else{
			holder= (Holder)convertView.getTag();
		}

		CommentList rightList = mList.get(position);
		if(rightList!=null){
			holder.tv01.setText(rightList.getContent());
			if(rightList.getLike().equals("1")){ //읽은것
				holder.tv01.setTextColor(Color.parseColor("#adadad"));
			}else{//안읽은것
				holder.tv01.setTextColor(Color.parseColor("#000000"));
			}
			String text="";
			if(rightList.getID().equals("4")){
				text = mContext.getString(R.string.tab03);
			}else if(rightList.getID().equals("5")){
				text = mContext.getString(R.string.tab02);
			}
			String content =mContext.getString(R.string.notice).replace("{KIND}", text).replace("{ID}",
					"by"+rightList.getNickName()).replace("{TIME}", Utils.chageCommentDate(mContext, rightList.getDate()));
			holder.tv02.setText(content);
		}
		return convertView;
	}

	public class Holder{
		ImageView img01;
		TextView tv01, tv02;
		Button btn01;
	}
}
