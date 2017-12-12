package specup.fanmind.main.setting.extra;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import specup.fanmind.R;
import specup.fanmind.main.tab2_.NewsFeedPopupActivity;

public class LockTalkFragment extends Fragment{

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_locktalk, null);
	}

	RelativeLayout mLockTalk[] = new RelativeLayout[3];

	private void setView(){
		mLockTalk[0] = (RelativeLayout)getActivity().findViewById(R.id.locktalk_btn01);
		mLockTalk[1] = (RelativeLayout)getActivity().findViewById(R.id.locktalk_btn02);
		mLockTalk[2] = (RelativeLayout)getActivity().findViewById(R.id.locktalk_btn03);

		for(int i=0; i<mLockTalk.length; i++){
			mLockTalk[i].setOnClickListener(mClick);
		}
	}

	OnClickListener mClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.locktalk_btn01 :
				break;
			case R.id.locktalk_btn02 :
				Intent intent = new Intent(getActivity(), NewsFeedPopupActivity.class);
				intent.putExtra("where", "0");
				getActivity().startActivity(intent);
				break;
			case R.id.locktalk_btn03 :
				break;
			}
		}
	};

}
