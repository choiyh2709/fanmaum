package specup.fanmind.left;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

import specup.fanmind.R;


/**
 * 그리드뷰를 이용한 달력 예제
 *
 * @blog http://croute.me
 * @link http://croute.me/335
 *
 * @author croute
 * @since 2011.03.08
 */
public class GVCalendarActivity extends FragmentActivity {


	Calendar mThisMonthCalendar;
	int mPosition;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.main);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager)findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setCurrentItem(51);
		mPosition = mViewPager.getCurrentItem();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if(mPosition<arg0){
					mThisMonthCalendar = getNextMonth(mThisMonthCalendar);
				}else{
					mThisMonthCalendar = getLastMonth(mThisMonthCalendar);
				}
//				mSectionsPagerAdapter = new SectionsPagerAdapter(
//						getSupportFragmentManager());
				getCalendar(mThisMonthCalendar);
				mPosition = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		mDayList = new ArrayList<DayInfo>();
		mThisMonthCalendar = Calendar.getInstance();
		mThisMonthCalendar.set(Calendar.DAY_OF_MONTH, 1);
	}


	private ViewPager mViewPager;
	private SectionsPagerAdapter mSectionsPagerAdapter;

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		Context mContext;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			//			switch(position%3){
			//			case 0 :
			//				return new GVCalendarFragment(GVCalendarActivity.this, mThisMonthCalendar);
			//			case 1:
			//				return new GVCalendarFragment2(GVCalendarActivity.this, mThisMonthCalendar);
			//			case 2:
			//				return new GVCalendarFragment3(GVCalendarActivity.this, mThisMonthCalendar);
			//			}
			Fragment fragment = new ScheduelFragment(GVCalendarActivity.this, mThisMonthCalendar);
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


	//ȸ���� 1�ܰ�
	/*private void showStep1(){
		GVCalendarFragment step1 = new GVCalendarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.remove(step1);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.replace(R.id.frame, step1, "step1");
		ft.commitAllowingStateLoss();
	}*/

	/**
	 * �������� Calendar ��ü�� ��ȯ�մϴ�.
	 *
	 * @param calendar
	 * @return LastMonthCalendar
	 */
	private Calendar getLastMonth(Calendar calendar)
	{
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, -1);
		return calendar;
	}

	/**
	 * �������� Calendar ��ü�� ��ȯ�մϴ�.
	 *
	 * @param calendar
	 * @return NextMonthCalendar
	 */
	private Calendar getNextMonth(Calendar calendar){
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
		calendar.add(Calendar.MONTH, +1);
		return calendar;
	}

	public GridView mGvCalendar ;
	public ArrayList<DayInfo> mDayList;

	public  int SUNDAY 		= 1;
	public  int MONDAY 		= 2;
	public  int TUESDAY 		= 3;
	public  int WEDNSESDAY 	= 4;
	public  int THURSDAY 		= 5;
	public  int FRIDAY 		= 6;
	public  int SATURDAY 		= 7;


	@SuppressLint("ValidFragment")
    public class ScheduelFragment extends Fragment {

		Context mContext;
		Calendar mThisPosition;

		public ScheduelFragment(Context context, Calendar position){
			mContext = context;
			mThisPosition = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mGvCalendar = new GridView(getActivity());
			mGvCalendar.setNumColumns(7);
//			if(!isFirst){
//			Log.e("isFirst", "isFirst");
			getCalendar(mThisPosition);
//			isFirst = true;
//			}
			return mGvCalendar;
		}
	}

	public void getCalendar(Calendar calendar)
	{
		int lastMonthStartDay;
		int dayOfMonth;
		int thisMonthLastDay;

		mDayList.clear();

		// �̹�� �������� ������ ���Ѵ�. �������� �Ͽ����� ��� �ε����� 1(�Ͽ���)���� 8(������ �Ͽ���)�� �ٲ۴�.)
		dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
		thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, -1);
		Log.e("������ ��������", calendar.get(Calendar.DAY_OF_MONTH)+"");

		// �������� ������ ���ڸ� ���Ѵ�.
		lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		calendar.add(Calendar.MONTH, 1);
		Log.e("�̹�� ������", calendar.get(Calendar.DAY_OF_MONTH)+"");

		if(dayOfMonth == SUNDAY)
		{
			dayOfMonth += 7;
		}

		lastMonthStartDay -= (dayOfMonth-1)-1;



		DayInfo day;

		Log.e("DayOfMOnth", dayOfMonth+"");

		for(int i=0; i<dayOfMonth-1; i++)
		{
			int date = lastMonthStartDay+i;
			day = new DayInfo();
			day.setDay(Integer.toString(date));
			day.setInMonth(false);

			mDayList.add(day);
		}

		for(int i=1; i <= thisMonthLastDay; i++){
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(true);
			mDayList.add(day);
		}

		for(int i=1; i<42-(thisMonthLastDay+dayOfMonth-1)+1; i++){
			day = new DayInfo();
			day.setDay(Integer.toString(i));
			day.setInMonth(false);
			mDayList.add(day);
		}
		initAdapter();
	}

	CalendarAdapter mCalendarAdapter;
	boolean isFirst;
	private void initAdapter(){
//		mCalendarAdapter = new CalendarAdapter(this, R.layout.day, mDayList);
		mCalendarAdapter.notifyDataSetChanged();
		mGvCalendar.setAdapter(mCalendarAdapter);
	}


}