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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import specup.fanmind.MainActivity;
import specup.fanmind.R;
import specup.fanmind.adapter.LockScreenImageAdapter;
import specup.fanmind.adapter.LockScreenImageDelAdapter;
import specup.fanmind.common.Util.ActivityManager;
import specup.fanmind.common.Util.CustomDialog;
import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.ImageSaveDB;
import specup.fanmind.vo.LockScreenImage;

public class LockScreenImageActivity extends Activity{

	GridView mGrid;
	ArrayList<LockScreenImage> mLockList;
	LockScreenImageAdapter mLockAdapter;
	LockScreenImageDelAdapter mLockDelAdapter;
	ImageSaveDB mImageDB;
	SQLiteDatabase sqlDatabase;
	TextView mTextView;
	Button mBtnDel, mBtnDelAll;
	boolean isDel, isAllDel, isNoti, isLock;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityManager.getInstance().addActivity(this);

		isNoti = getIntent().getBooleanExtra("isNoti", false);
		isLock = getIntent().getBooleanExtra("lock", false);
		setContentView(R.layout.fragment_lockscreenimage);
		setView();
	}

	private void setView(){


		mTextView=(TextView)findViewById(R.id.lockscreenimage_tv01);
		mImageDB = new ImageSaveDB(this);
		mGrid =(GridView)findViewById(R.id.lockscreenimage_grid);
		mLockList = new ArrayList<LockScreenImage>();
		mLockAdapter = new LockScreenImageAdapter(this, mLockList);
		mGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(!isDel){ //삭제가 아닐때
					if(position==mLockList.size()-1){
						Utils.makeDir(LockScreenImageActivity.this);
						Intent intent2 = new Intent(Intent.ACTION_PICK);
						intent2.setType(MediaStore.Images.Media.CONTENT_TYPE);
						startActivityForResult(intent2, Utils.PICK_FROM_GALLERY_IMAGE);
					}else{
						int index = mLockList.get(position).getIndex();
						int check = mLockList.get(position).getCheck();
						updateImageCheck(position, index, check);
					}
				}else{//삭제일때
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
					Utils.setToast(LockScreenImageActivity.this, R.string.lockscreen05);
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

		mBtnDel = (Button)findViewById(R.id.lockscreenimage_btn03);
		mBtnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mLockDelAdapter.getChecked().size()==0){
					Utils.setToast(LockScreenImageActivity.this, R.string.nodatadel);
				}else{
					String title = getString(R.string.imagedel01);
					String content = getString(R.string.imagedel02);
					imageDelPopup(title, content, true, 0);
				}
			}
		});

		getImage();
	}


	//이미지 얻기.
	private void getImage(){
		mLockList.removeAll(mLockList);
		sqlDatabase = mImageDB.getReadableDatabase();
		try{
			sqlDatabase.beginTransaction();
			String query = "select * from image where imageType='0' order by _id desc";
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
		}
		mLockList.add(new LockScreenImage(0, "", 1, true, false));
		mGrid.setAdapter(mLockAdapter);
	}

	//이미지 추가.
	private void addImage(String path){
		sqlDatabase = mImageDB.getWritableDatabase();
		try{
			String query="insert into image(imagePath, imageCheck, imageType) " +
					"values('"+path+"', '0', '0')";
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
		Cursor mCursor=null; String query=null;
		try{
			sqlDatabase.beginTransaction();
			if(imagecheck==0){
				query = "select * from image where imageCheck=0 AND imageType=0;";
				mCursor = sqlDatabase.rawQuery(query, null);
				if(mCursor.getCount()>1){
					query = "update image set imageCheck='1' where _id='"+index+"';";
					sqlDatabase.execSQL(query);
					mLockList.get(position).setCheck(1);
				}else{
					Utils.setToast(this, R.string.choice01);
				}
			}else{
				query = "update image set imageCheck='0' where _id='"+index+"';";
				sqlDatabase.execSQL(query);
				mLockList.get(position).setCheck(0);
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

		if(requestCode==Utils.PICK_FROM_GALLERY_IMAGE){
			mGallaryUri = data.getData();
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(mGallaryUri, "image/*");
			intent.putExtra("crop", "true");
			// crop한 이미지를 저장할때 200x200 크기로 저장
			intent.putExtra("outputX", 900); // crop한 이미지의 x축 크기
			intent.putExtra("outputY", 1600); // crop한 이미지의 y축 크기
			intent.putExtra("aspectX", 9); // crop 박스의 x축 비율
			intent.putExtra("aspectY", 16); // crop 박스의 y축 비율

			String dirPath = Environment.getExternalStorageDirectory().toString()+"/Fanmaum/test.png";
			File f = new File(dirPath);
			try {
				f.createNewFile();
			} catch (IOException ex) {
				Log.e("io", ex.getMessage());  
			}

			Uri uri = Uri.fromFile(f);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			startActivityForResult(intent, Utils.CROP_FROM_CAMERA_IMAGE);

		}else if(requestCode == Utils.CROP_FROM_CAMERA_IMAGE){
			Bundle bundle = data.getExtras();
			String dir = Environment.getExternalStorageDirectory().toString()+"/Fanmaum/test.png";
			//			if (bundle != null) {
			Bitmap photo = BitmapFactory.decodeFile(dir);
			String filename = String.valueOf(System.currentTimeMillis());
			String dirPath = getFilesDir().getAbsolutePath()+"/bg/"+filename+".png"; 
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
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			query = "select * from image where imageType='0' order by _id desc";
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
		mLockDelAdapter = new LockScreenImageDelAdapter(this, mLockList, mLockList.size(), true);
		mGrid.setAdapter(mLockDelAdapter);
	}


    /**
   	 * 이미지 삭제(내부 저장소 및 DB)
   	 */
	private void delImage(){
		String dirPath = getFilesDir().getAbsolutePath()+"/bg/";
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

    /**내부메모리 bitmap 삭제.
   	 * @param path Path
   	 */
	private void delBitmap(String path){
		try{
			File file = new File("data/data/specup.fanmind/files/bg");
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


	CustomDialog mCustomDialog;
    /**
   	 * 이미지 삭제 팝업
   	 */
	private void imageDelPopup(String title, String content, final boolean isDel, final int position){
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
				if(isDel) 	delImage();
			}
		}, left, right);
		mCustomDialog.show();
	}

	public void onBack(View v){
		onBack();
	}


	private void onBack(){
		if(mBtnDelAll.getVisibility() == View.VISIBLE){
			getImage();
			mBtnDel.setVisibility(View.GONE);
			mBtnDelAll.setVisibility(View.GONE);
			isDel = false;
		}else{
			if(isLock){
				setResult(RESULT_OK);
			}else{
				if(isNoti){
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
				}
			}
			ActivityManager.getInstance().deleteActivity(this);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK){
			onBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}



}
