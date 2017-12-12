package specup.fanmind.main.setting.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.adapter.LockScreenButtonImageAdapter;
import specup.fanmind.adapter.LockScreenImageDelAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.DialogUtil;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.common.http.HttpRequest;
import specup.fanmind.common.http.OnTask;
import specup.fanmind.common.http.URLAddress;
import specup.fanmind.fanmindsetting.FanMindSetting;
import specup.fanmind.fanmindsetting.ImageSaveDB;
import specup.fanmind.left.profile.ProfileActivity;
import specup.fanmind.vo.LockScreenImage;

public class LockScreenButtonImageActivity extends Activity implements OnTask {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		//		Bundle bundle = this.getArguments();
		//		if(bundle!=null)	isBack = bundle.getBoolean("isback", false);
		ActivityManager.getInstance().addActivity(this);
		setContentView(R.layout.fragment_lockscreenimage);
		setView();
	}

	GridView mGrid;
	ArrayList<LockScreenImage> mLockList;
	LockScreenButtonImageAdapter mLockAdapter;
	LockScreenImageDelAdapter mLockDelAdapter;
	ImageSaveDB mImageDB;
	SQLiteDatabase sqlDatabase;
	Button mBtnDel, mBtnDelAll;
	boolean isDel, isAllDel, isBack;
	int mPosition;
	DialogUtil dialogUtil;

	private void setView(){
		TextView mNaviTitle =(TextView)findViewById(R.id.navi_tv01);
		TextView mTitle =(TextView)findViewById(R.id.lockscreenimage_tv01);
		mTitle.setText(R.string.locksetting);
		mNaviTitle.setText(R.string.locksetting02);
		
		mImageDB = new ImageSaveDB(this);
		mGrid =(GridView)findViewById(R.id.lockscreenimage_grid);
		mLockList = new ArrayList<LockScreenImage>();
		mLockAdapter = new LockScreenButtonImageAdapter(this, mLockList);

		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!isDel){ //삭제가 아닐때
					if(position==mLockList.size()-1){
						Utils.makeDir(LockScreenButtonImageActivity.this);
						Intent intent2 = new Intent(Intent.ACTION_PICK);
						intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
						startActivityForResult(intent2, Utils.PICK_FROM_GALLERY_BUTTON);
					}else{
						mPosition = position;
						String title = getString(R.string.imagebtn01);
						String content = getString(R.string.imagebtn02);
						imageDelPopup(title, content, false);
					}
				}else{
					mLockDelAdapter.setChecked(position);
					mLockDelAdapter.notifyDataSetChanged();
				}
			}
		});

		Button mBtnShowDel =(Button)findViewById(R.id.lockscreenimage_btn01);
		mBtnShowDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mLockList.size() >1){
					isDel = true;
					mBtnDel.setVisibility(View.VISIBLE);
					mBtnDelAll.setVisibility(View.VISIBLE);
					getDelImage();
				}else{
					Utils.setToast(LockScreenButtonImageActivity.this, R.string.lockscreen05);
				}
			}
		});

		mBtnDel = (Button)findViewById(R.id.lockscreenimage_btn03);
		mBtnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mLockDelAdapter.getChecked().size()==0){
					Utils.setToast(LockScreenButtonImageActivity.this, R.string.nodatadel);
				}else{
					String title = getString(R.string.imagedel01);
					String content = getString(R.string.imagedel02);
					imageDelPopup(title, content, true);
				}
			}
		});

		mBtnDelAll =(Button)findViewById(R.id.lockscreenimage_btn02);
		mBtnDelAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isAllDel) isAllDel = false;
				else isAllDel =true;
				mLockDelAdapter.setAllChecked(isAllDel);
				mLockDelAdapter.notifyDataSetChanged();
			}
		});

		getImage();
	}

    /**
   	 * 지우는 이미지 얻기.
   	 */
	private void getDelImage(){
		String query =null;
		mLockList.removeAll(mLockList);
		sqlDatabase = mImageDB.getReadableDatabase();

		try{
			sqlDatabase.beginTransaction();
			query = "select * from image where imageType='1' order by _id desc";
			Cursor cursor = sqlDatabase.rawQuery(query, null);
			if(cursor.getCount()!=0){
				while (cursor.moveToNext()) {
					int index = cursor.getInt(0);
					String path = cursor.getString(1);
					mLockList.add(new LockScreenImage(index, path));
				}
			}
			sqlDatabase.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDatabase.endTransaction();
			mImageDB.close();
		}
		mLockDelAdapter = new LockScreenImageDelAdapter(this, mLockList, mLockList.size(), false);
		mGrid.setAdapter(mLockDelAdapter);
	}


	private void delImage(){
		String dirPath =getFilesDir().getAbsolutePath()+"/button/";
		try {
			sqlDatabase = mImageDB.getWritableDatabase();
			sqlDatabase.beginTransaction();
			for(int i=0 ; i<mLockDelAdapter.getChecked().size(); i++){
				int position =  mLockDelAdapter.getChecked().get(i);
				int index = mLockList.get(position).getIndex();
				String path =mLockList.get(position).getPath();
				path = path.substring(dirPath.length(), path.length());
				sqlDatabase.execSQL("delete from image where _id='"+index+"';");
				sqlDatabase.setTransactionSuccessful();
				delBitmap(path);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDatabase.endTransaction();
			mImageDB.close();
		}
		getImage();
		mBtnDel.setVisibility(View.GONE);
		mBtnDelAll.setVisibility(View.GONE);
		Utils.setToast(this, R.string.choice02);
		isDel = false;
	}


	CustomDialog mCustomDialog;
    /**
   	 * 이미지 삭제 팝업
   	 */
	private void imageDelPopup(String title, String content, final boolean isDel){
		String left = getString(R.string.cancel);
		String right = getString(R.string.confirmation);
		mCustomDialog = new CustomDialog(this, title, content, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomDialog.dismiss();
				if(isDel)	 delImage();
				else upLoadImage();
			}
		}, left, right);
		mCustomDialog.show();
	}


    /**내부메모리 bitmap 삭제.
   	 * @param path Path
   	 */
	private void delBitmap(String path){
		try{
			File file = new File("data/data/specup.fanmind/files/button");
			File[] flist = file.listFiles();
			for(int i = 0 ; i < flist.length ; i++){
				String fname = flist[i].getName();
				if(fname.equals(path)){
					flist[i].delete();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//이미지 얻기.
	private void getImage(){
		mLockList.removeAll(mLockList);
		sqlDatabase = mImageDB.getReadableDatabase();
		try{
			sqlDatabase.beginTransaction();
			String query = "select * from image where imageType='1' order by _id desc";
			Cursor cursor = sqlDatabase.rawQuery(query, null);
			if(cursor.getCount()!=0){
				while (cursor.moveToNext()) {
					int index = cursor.getInt(0);
					String path = cursor.getString(1);
					int check =  cursor.getInt(2);
					int type = cursor.getInt(3);
					mLockList.add(new LockScreenImage(index, path, check, false, false));
				}
			}
			sqlDatabase.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDatabase.endTransaction();
			mImageDB.close();
			mLockList.add(new LockScreenImage(0, "", 1, true, false));
			mLockAdapter.notifyDataSetChanged();
			mGrid.setAdapter(mLockAdapter);
		}
	}

	//이미지 추가.
	private void addImage(String path){
		sqlDatabase = mImageDB.getWritableDatabase();
		try{
			String query="insert into image(imagePath, imageCheck, imageType) " +
					"values('"+path+"', '1', '1')";
			sqlDatabase.execSQL(query);
		}catch(Exception e){
			e.printStackTrace();
		}
		getImage();
	}

	//이미지 체크 업데이트.
	private void updateImageCheck(int position, int index, int imagecheck ){
		sqlDatabase = mImageDB.getWritableDatabase();
		sqlDatabase = mImageDB.getReadableDatabase();
		String query=null;
		try{
			sqlDatabase.beginTransaction();
			query = "update image set imageCheck='1' where imageType='1';";
			sqlDatabase.execSQL(query);

			query = "update image set imageCheck='0' where _id='"+index+"';";
			sqlDatabase.execSQL(query);

			for(int i=0; i<mLockList.size(); i++){
				if(i!=position)mLockList.get(i).setCheck(1);
				else mLockList.get(i).setCheck(0);
			}

			sqlDatabase.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDatabase.endTransaction();
			mImageDB.close();
			mLockAdapter.notifyDataSetChanged();
		}
	}

	private Uri mGallaryUri;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if(resultCode != RESULT_OK){
			return;
		}

		if(requestCode==Utils.PICK_FROM_GALLERY_BUTTON){
			mGallaryUri = data.getData();
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mGallaryUri, "image/*");
			intent.putExtra("crop", "true");
            // crop한 이미지를 저장할때 200x200 크기로 저장
			intent.putExtra("outputX", 200); // crop한 이미지의 x축 크기
			intent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
			intent.putExtra("aspectX", 1); // crop 박스의 x축 비율
			intent.putExtra("aspectY", 1); // crop 박스의 y축 비율

			String dirPath = Environment.getExternalStorageDirectory().toString()+"/Fanmaum/test.png";
			File f = new File(dirPath);
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());  
			}

			Uri uri = Uri.fromFile(f);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, Utils.CROP_FROM_CAMERA_BUTTON);

		}else if(requestCode == Utils.CROP_FROM_CAMERA_BUTTON){
			Bundle bundle = data.getExtras();
			String dir = Environment.getExternalStorageDirectory().toString()+"/Fanmaum/test.png";
			//			if (bundle != null) {
			Bitmap photo = BitmapFactory.decodeFile(dir);
			String filename = String.valueOf(System.currentTimeMillis());
			String dirPath = getFilesDir().getAbsolutePath()+"/button/"+filename+".png"; 
			File savefile = new File(dirPath);
			try{
				FileOutputStream fos = new FileOutputStream(savefile);
				photo.compress(CompressFormat.PNG, 100 , fos);          
				fos.flush();     
				fos.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			addImage(dirPath);
			//			}

			// 임시 파일 삭제
			File f = new File(dir);
			if(f.exists()){
				f.delete();
			}
		}else if(requestCode == Utils.BUTTON_DEL){
			getImage();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTask(int output, String result) {
		// TODO Auto-generated method stub
	}

	MultipartEntity mReqEntity;
	private void upLoadImage(){
		try{
			File path = new File(mLockList.get(mPosition).getPath());
			mReqEntity  = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			FileBody bin = new FileBody(path);
			mReqEntity.addPart("pic", bin);
			mReqEntity.addPart("sskey", new StringBody(FanMindSetting.getSESSION_KEY(this)));
			mReqEntity.addPart("ssid", new StringBody(FanMindSetting.getEMAIL_ID(this)));
		}catch(Exception e){
			e.printStackTrace();
		}
		new Async().execute("upload");
	}


	String mResult;
	private class Async extends android.os.AsyncTask<String, Integer, Integer>{
		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0].equals("upload")){
				mResult = HttpRequest.upLoad(URLAddress.CHANGE_PROFILE(LockScreenButtonImageActivity.this), mReqEntity);
				if(mResult ==null) publishProgress(0);
				else publishProgress(1);
			}
			return 1;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if(dialogUtil ==null){
				dialogUtil = new DialogUtil();
				dialogUtil.showProgress(LockScreenButtonImageActivity.this);
			}

			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if(values[0]==0){
				Utils.setToast(LockScreenButtonImageActivity.this, R.string.networkerror);
			}else{
				Log.d("siba - 11", "Fin ? - "+values + "::" + mResult);
				if(Utils.getJsonData(mResult)){
					getJsonData(mResult);
					Log.d("mResult",mResult);
				}else{
					Utils.setToast(LockScreenButtonImageActivity.this, R.string.upload05);
					Log.d("mResult",mResult);
				}
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			if(dialogUtil !=null){
				dialogUtil.dismissProgress();
				dialogUtil=null;
			}

			super.onPostExecute(result);
		}
	}

	public void  getJsonData(String result){
		try{
			JSONObject json = new JSONObject(result);
			String pic_base = json.getString("pic_base");
			String pic = json.getString("pic");
			FanMindSetting.setMY_PIC(this, pic_base+pic);
		}catch(Exception e){
			e.printStackTrace();
		}
		int index = mLockList.get(mPosition).getIndex();
		int check = mLockList.get(mPosition).getCheck();
		updateImageCheck(mPosition, index, check);
		Utils.setToast(this, R.string.profileok);
//		((LeftMenuFragment)LeftMenuFragment.mLeftContext).setProfile();
		if(((ProfileActivity)ProfileActivity.mProfileActivity) !=null){
			((ProfileActivity)ProfileActivity.mProfileActivity).setProfile();	
		}
	}

	public void onReBack(){//백버튼 눌렀을시
		if(mBtnDelAll.getVisibility() == View.VISIBLE){
			getImage();
			mBtnDel.setVisibility(View.GONE);
			mBtnDelAll.setVisibility(View.GONE);
			isDel = false;
		}else{
			ActivityManager.getInstance().deleteActivity(this);
//			if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//			((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
		}
	}


	private void exit(){
		String title = getString(R.string.exit01);
		String content = getString(R.string.exit02);
		String lBtnText = getString(R.string.cancel);
		String rBtnText = getString(R.string.confirmation);

		mCustomDialog = new CustomDialog(this, title, content, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mCustomDialog.dismiss();
			}
		}, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		}, lBtnText, rBtnText);
		mCustomDialog.show();
	}

	public void onBack(View v){
		onReBack();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(mBtnDel.getVisibility() == View.VISIBLE){
				getImage();
				mBtnDel.setVisibility(View.GONE);
				mBtnDelAll.setVisibility(View.GONE);
				isDel = false;
				return true;
			}else{
				ActivityManager.getInstance().deleteActivity(this);
//				if(((BaseActivity)BaseActivity.mBaseActivity) !=null)
//				((BaseActivity)BaseActivity.mBaseActivity).sm.isShow = false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
