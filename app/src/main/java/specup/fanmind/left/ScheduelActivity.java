package specup.fanmind.left;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.adapter.ScheduelAdapter;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.vo.RightList;
import specup.fanmind.vo.SupportList;


public class ScheduelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_scheduel);
		setView();
	}


	TextView mTitle;
	ListView mList;
	ArrayList<SupportList> mSupport;
	ArrayList<RightList> mRightList;
	ScheduelAdapter mScheduelAdapter;
	String mDate;
	
	private void setView(){
		mTitle = (TextView)findViewById(R.id.sch_tv01);
		mList =(ListView)findViewById(R.id.sch_list01);

		Intent intent = getIntent();
		mDate = intent.getStringExtra("date");
		boolean isSch = intent.getBooleanExtra("issch", false);
		if(isSch){
			String index = intent.getStringExtra("index");
			String array[] = index.split(",");
			mSupport = intent.getParcelableArrayListExtra("sch");
			mRightList = new ArrayList<RightList>();
			mScheduelAdapter = new ScheduelAdapter(this, mRightList);
			for(int i=0; i<array.length; i++){
				String title = mSupport.get(Integer.parseInt(array[i])).getSrl();
				String text = mSupport.get(Integer.parseInt(array[i])).getText();
				String begin = mSupport.get(Integer.parseInt(array[i])).getTitle();
				String end = mSupport.get(Integer.parseInt(array[i])).getThumImgPath();
				mRightList.add(new RightList(title, text, begin, end));
			}
			mList.setAdapter(mScheduelAdapter);
		}

		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ScheduelActivity.this, ScheduelDetailActivity.class);
				intent.putExtra("title", mRightList.get(position).getPath());
				intent.putExtra("text", mRightList.get(position).getStarsrl());
				intent.putExtra("start", mRightList.get(position).getName());
				intent.putExtra("end", mRightList.get(position).getFan());
				intent.putExtra("date", mDate);
				startActivity(intent);
				finish();
			}
		});
		
		String syear = mDate.substring(0, 4);
		String smonth = mDate.substring(5, 7);
		String sday = mDate.substring(8, 10);
		
		
		if(smonth.startsWith("0")) smonth = smonth.substring(1);
		if(sday.startsWith("0")) sday = sday.substring(1);
		String reDate = syear+"."+smonth+"."+sday;
		mTitle.setText(reDate);
	}

	public void onClick(View v){
		if(FanMindSetting.getLOGIN_OK(this)){
			Intent intent = new Intent(this, ScheduelAddActivity.class);
			intent.putExtra("date", mDate);
			startActivity(intent);
			finish();
		}else{
//			Utils.showDialog(this);
		}
	}

}
