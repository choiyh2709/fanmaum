package specup.fanmind.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.vo.SupportList;
import specup.fanmind.R;

public class PayAccountAdapter extends BaseAdapter{
	private ArrayList<SupportList> mList = null;
	private LayoutInflater layout_In = null;
	private Context mContext;
	OnClickListener mClick;
	
	public PayAccountAdapter(Context context,  ArrayList<SupportList> mList, OnClickListener click){
		mContext = context;
		this.mList = mList;
		layout_In = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mClick = click;
	}


	public void add(SupportList data)	{
		mList.add(data);
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public SupportList getItem(int position) {
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
		Holder holder;
		SupportList support = mList.get(position);
		if(convertView ==null){
			holder = new Holder();
			convertView = layout_In.inflate(R.layout.list_payaccount, null);
			holder.tv01 = (TextView)convertView.findViewById(R.id.listpayaccount_tv01);
			holder.tv02 = (TextView)convertView.findViewById(R.id.listpayaccount_tv02);
			holder.tv03 = (TextView)convertView.findViewById(R.id.listpayaccount_tv03);
			holder.tv04 = (TextView)convertView.findViewById(R.id.listpayaccount_tv04);
			holder.tv05 = (TextView)convertView.findViewById(R.id.listpayaccount_tv05);
			holder.tv06 = (TextView)convertView.findViewById(R.id.listpayaccount_tv06);
			holder.tv07 = (TextView)convertView.findViewById(R.id.listpayaccount_tv07);
			holder.mImageBtn =(ImageButton)convertView.findViewById(R.id.listpayaccount_btn01);
			convertView.setTag(holder);
		}else{
			holder = (Holder)convertView.getTag();
		}

		if(support!=null){
			String price = support.getTitle();
			String title =null;
			
			if(price.equals("5500")){
				title = mContext.getString(R.string.buyitem03);
			}else if(price.equals("11000")){
				title = mContext.getString(R.string.buyitem04);
			}else if(price.equals("22000")){
				title = mContext.getString(R.string.buypoint10);
			}else if(price.equals("33000")){
				title = mContext.getString(R.string.buypoint11);
			}else if(price.equals("55000")){
				title = mContext.getString(R.string.buyitem06);
			}else if(price.equals("1100")){
				title = mContext.getString(R.string.buyitem01);
			}else if(price.equals("2500")){
				title = mContext.getString(R.string.buyitem02);
			}else if(price.equals("27500")){
				title = mContext.getString(R.string.buyitem05);
			}
			
			String product = mContext.getString(R.string.mypoint_tv05).replace("{NUMBER}", title);
			String reprice = mContext.getString(R.string.mypoint_tv06).replace("{PRICE}", Utils.getMoney(price));
			
//			holder.tv01.setText(title);
			holder.tv05.setText(product);
			holder.tv06.setText(reprice);
			String mDate = support.getThumImgPath();
			int year = Integer.parseInt(mDate.substring(0, 4));
			int month = Integer.parseInt(mDate.substring(5, 7));
			int day = Integer.parseInt(mDate.substring(8, 10));
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA);
			Calendar today = Calendar.getInstance ();
			today.set(year, month-1, day);
			today.add(Calendar.DATE, 3);
			Date t = today.getTime();
			String future = sdf.format(t);
			
			int reyear = Integer.parseInt(future.substring(0, 4));
			int remonth = Integer.parseInt(future.substring(4, 6));
			int reday = Integer.parseInt(future.substring(6, 8));
			
			String date = mContext.getString(R.string.mypoint_tv07).replace("{YEAR}", String.valueOf(reyear))
					.replace("{MONTH}", String.valueOf(remonth)).replace("{DAY}", String.valueOf(reday));
			holder.tv07.setText(date);
			
			
			if(support.getGoalMoney().equals("03")){
				holder.tv02.setText(mContext.getString(R.string.bank01));
			}else if(support.getGoalMoney().equals("04")){
				holder.tv02.setText(mContext.getString(R.string.bank02));
			}else if(support.getGoalMoney().equals("20")){
				holder.tv02.setText(mContext.getString(R.string.bank03));
			}else if(support.getGoalMoney().equals("26")){
				holder.tv02.setText(mContext.getString(R.string.bank04));
			}else if(support.getGoalMoney().equals("81")){
				holder.tv02.setText(mContext.getString(R.string.bank05));
			}else if(support.getGoalMoney().equals("11")){
				holder.tv02.setText(mContext.getString(R.string.bank06));
			}else if(support.getGoalMoney().equals("71")){
				holder.tv02.setText(mContext.getString(R.string.bank07));
			}			
			
			holder.tv03.setText(support.getWarning());
			holder.tv04.setText(mContext.getString(R.string.specupad));
			
			holder.mImageBtn.setTag(position);
			holder.mImageBtn.setOnClickListener(mClick);
		}
		return convertView;
	}

	public class Holder{
		TextView tv01, tv02, tv03, tv04, tv05, tv06, tv07;
		ImageButton mImageBtn;
	}
}
