package specup.fanmind.left;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.MovieAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.FanMindApplication;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.AsyncTask;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.StarSetting;
import specup.fanmind.vo.MovieList;


public class MovieActivity extends YouTubeBaseActivity
		implements OnScrollListener, OnTask, YouTubePlayer.OnInitializedListener {

	MovieAdapter mMovieAdapter;
	ArrayList<MovieList> mMovieList;
	ListView mList;
	// EditText mSearchEt;
	// Button mSearchBtn;
	AsyncTask mAsyncTask;
	boolean mLockListView, isFullScreen;
	String mStar;
	YouTubePlayer mPlayer;
	YouTubePlayerView youTubeView;
	boolean isLock = false;
	String mPageId = "";

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);

		Tracker t = ((FanMindApplication) getApplication()).getTracker(FanMindApplication.TrackerName.APP_TRACKER);
		t.setScreenName("Movie");
		t.send(new HitBuilders.AppViewBuilder().build());

		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_movie);
		Log.e("123", "123");
		isLock = getIntent().getBooleanExtra("isLock", false);
		setView();
	}

	private void setView() {
		mLockListView = true;
		mMovieList = new ArrayList<MovieList>();
		mMovieAdapter = new MovieAdapter(this, mMovieList);
		mList = (ListView) findViewById(R.id.movie_list);
		mList.setOnScrollListener(this);
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				try {
					String youtubeid = mMovieList.get(position).getID();
					youTubeView.setVisibility(View.VISIBLE);
					mPlayer.loadVideo(youtubeid);
				} catch (Exception e) {
					Utils.setToast(MovieActivity.this, R.string.alert_replay);
				}
			}
		});
		mList.setAdapter(mMovieAdapter);
		// mSearchEt =(EditText)findViewById(R.id.movie_et);
		// mSearchBtn =(Button)findViewById(R.id.movie_btn01);
		youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
		youTubeView.initialize(Utils.DEVELOPER_KEY, this);

		mStar = StarSetting.getSTAR_SELECT_NAME(this);
		Loading(mStar, mPageId);
	}

	private void Loading(String Name, String Pageid) {
		if (mAsyncTask != null) {
			mAsyncTask.cancel(true);
		}
		mAsyncTask = new AsyncTask(this, URLAddress.getMovie(Name, Pageid), false);
		mAsyncTask.setListenr(this);
		mAsyncTask.execute("movie");
	}

	private void getJsonData(String result) {
		try {
			JSONObject json = new JSONObject(result);
			mPageId = json.getString("nextPageToken");
			JSONArray contacts = json.getJSONArray("items");
			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);
				String Name = c.getJSONObject("snippet").getString("title"); //유튜브 제목을 받아옵니다
				// String changString = "";
				// try {
				// changString = new String(Name.getBytes("8859_1"), "utf-8");
				// //한글이 깨져서 인코딩 해주었습니다
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				String Date = c.getJSONObject("snippet").getString("publishedAt") //등록날짜
						.substring(0, 10);
				String ImagePath = c.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium")
						.getString("url");  //썸내일 이미지 URL값

				String ID = c.getJSONObject("id").getString("videoId"); //유튜브 동영상 아이디 값입니다. 재생시 필요합니다.

				mMovieList.add(new MovieList(Name, ImagePath, Date, Integer.parseInt("0"), ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Loading(mStar, mPageId);
		}
		mMovieAdapter.notifyDataSetChanged();
		mLockListView = false;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		int count = totalItemCount - visibleItemCount;
		if (firstVisibleItem >= count && totalItemCount != 0 && mLockListView == false) {
			mLockListView = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Loading(mStar, mPageId);
				}
			}, 500);
		}
	}

	@Override
	public void onTask(int output, String result) {
		if (output == 1) {
			getJsonData(result);
		}
	}

	@Override
	public void onInitializationFailure(Provider arg0, YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isFullScreen) {
			mPlayer.setFullscreen(false);
		} else {
			if (isLock) {
				Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			} else {
				super.onBackPressed();
			}
		}
	}

	@Override
	public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
		if (!wasRestored) {
			mPlayer = player;
			mPlayer.setOnFullscreenListener(new OnFullscreenListener() {
				@Override
				public void onFullscreen(boolean _isFullScreen) {
					// TODO Auto-generated method stub
					isFullScreen = _isFullScreen;
				}
			});
		}
	}

	public void onBack(View v) {
		if (!isLock) {
			ActivityManager.getInstance().deleteActivity(this);
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityManager.getInstance().deleteActivity(this);
//			if (((BaseActivity) BaseActivity.mBaseActivity) != null)
//				((BaseActivity) BaseActivity.mBaseActivity).sm.isShow = false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

}
