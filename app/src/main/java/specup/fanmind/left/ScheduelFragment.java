package specup.fanmind.left;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import specup.fanmind.R;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.AsyncTaskValue;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.MainDayInfo;
import specup.fanmind.vo.SupportList;


public class ScheduelFragment extends Fragment implements OnTask {


	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		Tracker t = ((FanMindApplication)getActivity().getApplication()).getTracker(
				FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Scheduel");
		t.send(new HitBuilders.AppViewBuilder().build());
		
		
		setView();
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
		Log.e("123", result);
		if(output == AsyncTaskValue.SCHDUEL_ALL_NUM){
			if(Utils.getJsonData(result)){
				setJsonData(result);
			}
		}
	}

	
	private void setJsonData(String result){
		mSupportList.removeAll(mSupportList);
		try{
			JSONObject json = new JSONObject(result);
			String list = json.getString("list");
			JSONArray jsonArray = new JSONArray(list);
			for(int i=0; i<jsonArray.length(); i++){
				String title = jsonArray.getJSONObject(i).getString("title");
				String text = jsonArray.getJSONObject(i).getString("text");
				String begin_time = jsonArray.getJSONObject(i).getString("begin_time");
				String end_time = jsonArray.getJSONObject(i).getString("end_time");
				mSupportList.add(new SupportList(title, text, begin_time, end_time));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		getCalendar(mThisMonthCalendar);
	}


	private void getSchduel(Calendar cal){

		Log.e("YEAR", String.valueOf(cal.get(Calendar.YEAR)));
		Log.e("month", String.valueOf(cal.get(Calendar.MONTH)+1));

		mParams = new ArrayList<NameValuePair>();
		mParams.add(new BasicNameValuePair("star_srl", StarSetting.getSTAR_SELECT_INDEX(getActivity())));
		mParams.add(new BasicNameValuePair("year", String.valueOf(cal.get(Calendar.YEAR))));
		mParams.add(new BasicNameValuePair("month",  Utils.changeCal(String.valueOf(cal.get(Calendar.MONTH)+1))));
		HttpRequest.setHttp(getActivity(), mAsyncTask, mParams, URLAddress.SCHDULE_ALL(getActivity()), AsyncTaskValue.SCHDUEL_ALL, this);
	}



	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_scheduel, null);
	}


	GridView mGvCalendar; 
	public static int SUNDAY 		= 1;
	public static int MONDAY 		= 2;
	public static int TUESDAY 		= 3;
	public static int WEDNSESDAY 	= 4;
	public static int THURSDAY 		= 5;
	public static int FRIDAY 		= 6;
	public static int SATURDAY 		= 7;

	private ArrayList<MainDayInfo> mDayList;
	Calendar mThisMonthCalendar;
	String sToday;
	private CalendarAdapter mCalendarAdapter;
	ViewPager mPager;
	int mPosition;
	private TextView mTvCalendarTitle;
	SwipePagerAdapter mSwipePager;
	boolean isPager =true;

	AsyncTask mAsyncTask;
	List<NameValuePair> mParams;
	ArrayList<SupportList> mSupportList;
	String strYear, strMonth;

	private void setView(){
//		if(((TabMainActivity)TabMainActivity.mTabMainContext) != null){
//		((TabMainActivity)TabMainActivity.mTabMainContext).mTextTitle.setText(R.string.left01);
//		}
		mSupportList = new ArrayList<SupportList>();
		mTvCalendarTitle =(TextView)getActivity().findViewById(R.id.scheduel_tv01);
		mPager = (ViewPager)getActivity().findViewById(R.id.scheduel_pager);
		mSwipePager = new SwipePagerAdapter(getFragmentManager());
		mPager.setAdapter(mSwipePager);
		mPager.setCurrentItem(50, false);
		mPosition = mPager.getCurrentItem();
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(isPager){
					if(mPosition<position){
						mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
						int year =mThisMonthCalendar.get(Calendar.YEAR);
						int month = mThisMonthCalendar.get(Calendar.MONTH);
						int date = mThisMonthCalendar.get(Calendar.DAY_OF_MONTH);
						Log.e("Position",String.valueOf(mPosition)+"/"+ String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(date));
					}else{
						mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
						int year =mThisMonthCalendar.get(Calendar.YEAR);
						int month = mThisMonthCalendar.get(Calendar.MONTH);
						int date = mThisMonthCalendar.get(Calendar.DAY_OF_MONTH);
						Log.e("Position",String.valueOf(mPosition)+"/"+ String.valueOf(year)+"/"+String.valueOf(month+1)+"/"+String.valueOf(date));
					}
					//					getSchduel(mThisMonthCalendar);
					getSchduel(mThisMonthCalendar);
					mPosition = position;
				}else{
					isPager = true;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {}
			@Override
			public void onPageScrollStateChanged(int arg0) {}
		});

		ImageButton mLastBtn = (ImageButton)getActivity().findViewById(R.id.scheduel_btn01);
		ImageButton mNextBtn = (ImageButton)getActivity().findViewById(R.id.scheduel_btn02);

		mLastBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				isPager = false;
//				mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
//				getSchduel(mThisMonthCalendar);
//				mGvCalendar.setAdapter(mCalendarAdapter);
//				mPager.setCurrentItem(mPager.getCurrentItem()-1);
//				mPosition = mPosition-1;
			}
		});

		mNextBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				isPager = false;
//				mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
//				getSchduel(mThisMonthCalendar);
//				mGvCalendar.setAdapter(mCalendarAdapter);
//				mPager.setCurrentItem(mPager.getCurrentItem()+1);
//				mPosition = mPosition+1;
			}
		});

		sToday = Utils.getCalTime();
		mDayList = new ArrayList<MainDayInfo>();
		mCalendarAdapter = new CalendarAdapter(getActivity(), R.layout.day, mDayList);
		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
		if(Utils.getLocal(getActivity(), "English")){
			strYear = getString(R.string.starterm04);
		}else{
			strYear = getString(R.string.starterm04)+" ";
		}
		
		strMonth = getString(R.string.starterm05);
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + strYear
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + strMonth);
	}


    /**
   	 * 지난달의 Calendar 객체를 반환합니다.
   	 *
   	 * @param calendar
   	 * @return LastMonthCalendar
   	 */
	private Calendar getLastMonth(Calendar calendar){
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, -1);
		return calendar;
	}

    /**
   	 * 다음달의 Calendar 객체를 반환합니다.
   	 *
   	 * @param calendar
   	 * @return NextMonthCalendar
   	 */
	private Calendar getNextMonth(Calendar calendar){
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, +1);
		return calendar;
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void getCalendar(Calendar calendar){
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;
		int allSumDay;
		MainDayInfo day = null;

		mDayList.clear();
		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + strYear
				+ (mThisMonthCalendar.get(Calendar.MONTH) + 1) + strMonth);

		// 이번달 시작일의 요일을 구한다. 시작일이 일요일인 경우 인덱스를 1(일요일)에서 8(다음주 일요일)로 바꾼다.)
		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);
        Log.e("지난달 마지막일", calendar.get(Calendar.DAY_OF_MONTH)+"");

		// 지난달의 마지막 일자를 구한다.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);
        Log.e("이번달 시작일", calendar.get(Calendar.DAY_OF_MONTH)+"");

		boolean mCheck = false;
		if(dayOfMonth == SUNDAY)	{
			dayOfMonth= + 7;
			mCheck = true;
		}


		lastMonthStartDay -= (dayOfMonth-1)-1;
		allSumDay = dayOfMonth+thisMonthLastDay-1;


		Log.e("allSumDay", String.valueOf(calendar.get(Calendar.MONTH)+1));

		if(allSumDay>35){
			if(!mCheck){
				allSumDay = 42;
				FanMindSetting.setCAL_WEEK(getActivity(), true);
			}else{
				allSumDay = 35;
				FanMindSetting.setCAL_WEEK(getActivity(), false);
			}
		}else{
			allSumDay = 35;
			FanMindSetting.setCAL_WEEK(getActivity(), false);
		}

		//		String sMonth, sPreMonth, sNextMonth, mNextDate, sPreYear=null;
		//		if(String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1).length()==1){
		//			sMonth = "0"+String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1);
		//			if(sMonth.equals("01")){
		//				sPreMonth= "12";
		//				sPreYear = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)-1);
		//			}else{
		//				sPreMonth= "0"+String.valueOf(mThisMonthCalendar.get(Calendar.MONTH));	
		//				sPreYear = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
		//			}
		//			sNextMonth= "0"+String.valueOf(mThisMonthCalendar.get(Calendar.MONTH)+2);
		//		}else{
		//			sMonth = String.valueOf(mThisMonthCalendar.get(Calendar.MONTH) + 1);
		//			sPreMonth = String.valueOf(mThisMonthCalendar.get(Calendar.MONTH));
		//			if(sMonth.equals("12")){
		//				sNextMonth= "01";
		//				sPreYear = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
		//			}else{
		//				sNextMonth= String.valueOf(mThisMonthCalendar.get(Calendar.MONTH)+2);
		//				sPreYear = String.valueOf(mThisMonthCalendar.get(Calendar.YEAR));
		//			}
		//		}
		//
		String sMonth = Utils.changeCal(String.valueOf((mThisMonthCalendar.get(Calendar.MONTH) + 1)));
		String mDate = mThisMonthCalendar.get(Calendar.YEAR)+sMonth;
		//		String mPreDate = sPreYear+Utils.changeCal(sPreMonth);
		//		if(sMonth.equals("12")){
		//			mNextDate= String.valueOf(mThisMonthCalendar.get(Calendar.YEAR)+1)+sNextMonth;
		//		}else{
		//			mNextDate= mThisMonthCalendar.get(Calendar.YEAR)+sNextMonth;
		//		}
		// Ķ���� Ÿ��Ʋ(��� ǥ��)�� �����Ѵ�. 
		//		mTvCalendarTitle.setText(mThisMonthCalendar.get(Calendar.YEAR) + "." +sMonth);


		Log.e("DayOfMOnth", dayOfMonth+"");

		if(!mCheck){
			for(int i=0; i<dayOfMonth-1; i++){
				int date = lastMonthStartDay+i;
				day = new MainDayInfo();
				day.setDay(Integer.toString(date));
				day.setInMonth(false);
				mDayList.add(day);
			}
		}else{
			dayOfMonth =1;
		}

		for(int i=1; i <= thisMonthLastDay; i++){
			day = new MainDayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);
			mDayList.add(day);
			String dDay = Utils.changeCal(Integer.toString(i));
			String nTime = mDate+dDay+"000000";
			if(nTime.substring(0, 8).equals(sToday.substring(0, 8)))
				day.setisToday(true);
			else 
				day.setisToday(false);
			int count = 0;
			for(int j=0; j<mSupportList.size(); j++){
				String bar = Utils.changebar(mSupportList.get(j).getTitle());
				if(bar.equals(nTime.substring(0, 8))){
					count++;
				}
			}
			day.setTitle1(String.valueOf(count));
		}

		for(int i=1; i<allSumDay-(thisMonthLastDay+dayOfMonth-1)+1; i++){
			day = new MainDayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			mDayList.add(day);
		}

		initCalendarAdapter();
	}





	public class SwipePagerAdapter extends FragmentStatePagerAdapter {
		Context mContext;

		public SwipePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new CalendarFragment(getActivity());
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 100;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}


	private void initCalendarAdapter(){
		mGvCalendar.setAdapter(mCalendarAdapter);
		mCalendarAdapter.notifyDataSetChanged();
	}

	public class CalendarFragment extends Fragment {
		Context mContext;

		public CalendarFragment(Context context){
			mContext = context;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mGvCalendar = new GridView(getActivity());
			mGvCalendar.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT
					, LayoutParams.MATCH_PARENT));
			mGvCalendar.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			mGvCalendar.setNumColumns(7);
			mGvCalendar.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String day = mDayList.get(position).getDay();
					getSch(day);
				}
			});
			mGvCalendar.setSelector(new StateListDrawable());
			//			getCalendar(mThisMonthCalendar);
			getSchduel(mThisMonthCalendar);
			mGvCalendar.setAdapter(mCalendarAdapter);
			return mGvCalendar;
		}
	}

	private void getSch(String day){
		String calyear = mTvCalendarTitle.getText().toString();
		String[] spyear = calyear.split(" ");
		String year  =null, month=null; 	int month_idx=0;
		if(Utils.getLocal(getActivity(), "English")){
		    year = spyear[0].substring(0, 4);
		    month = spyear[0].substring(5, spyear[0].length());
		}else{
			   year = spyear[0].substring(0, 4);
			   month_idx= spyear[1].lastIndexOf(getString(R.string.timeline_tv02));
			   month = spyear[1].substring(0, month_idx);
		}
		month = Utils.changeCal(month);
		day = Utils.changeCal(day);
		String fullDay = year.trim()+month.trim()+day.trim();
		String append="";
		for(int i=0; i<mSupportList.size(); i++){
			String bar = Utils.changebar(mSupportList.get(i).getTitle());
			if(bar.equals(fullDay)){
				append = append+String.valueOf(i)+",";
			}
		}

		if(append.length()!=0){
			String index = append.substring(0, append.length()-1);
			Intent intent = new Intent(getActivity(), ScheduelActivity.class);
			intent.putExtra("date", year.trim()+"."+month.trim()+"."+day.trim());
			intent.putExtra("issch", true);
			intent.putExtra("sch", mSupportList);
			intent.putExtra("index", index);
			startActivity(intent);
		}else{
			Intent intent = new Intent(getActivity(), ScheduelActivity.class);
			intent.putExtra("date", year.trim()+"."+month.trim()+"."+day.trim());
			intent.putExtra("issch", false);
			startActivity(intent);
		}
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStart(getActivity());
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(getActivity()).reportActivityStop(getActivity());
	}
	
}
