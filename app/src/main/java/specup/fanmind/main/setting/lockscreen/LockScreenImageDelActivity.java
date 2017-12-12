package specup.fanmind.main.setting.lockscreen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import specup.fanmind.R;
import specup.fanmind.adapter.LockScreenImageDelAdapter;
import specup.fanmind.fanmindsetting.ImageSaveDB;
import specup.fanmind.vo.LockScreenImage;

public class LockScreenImageDelActivity extends Activity{

	GridView mGrid;
	ArrayList<LockScreenImage> mImageDel;
	LockScreenImageDelAdapter mImageDelAdapter;
	ImageSaveDB mImageDB;
	SQLiteDatabase sqlDatabase;
	CheckBox mCheckBox;
	boolean isImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockscreenimagedel);
		setView();
	}

	private void setView(){
		Intent intent = getIntent();
		isImage = intent.getBooleanExtra("isImage", false);
		mCheckBox =(CheckBox)findViewById(R.id.lockscreenimagedel_check);
		mGrid =(GridView)findViewById(R.id.lockscreenimagedel_grid);
		mImageDel = new ArrayList<LockScreenImage>();
		mImageDB= new ImageSaveDB(this);
		mCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mImageDelAdapter.setAllChecked(mCheckBox.isChecked());
				mImageDelAdapter.notifyDataSetChanged();
			}
		});

		mGrid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mImageDelAdapter.setChecked(position);
				mImageDelAdapter.notifyDataSetChanged();
			}
		});

		Button mBtnDel = (Button)findViewById(R.id.lockscreenimagedel_btn01);
		mBtnDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mImageDelAdapter.getChecked().size()==0){
					Toast.makeText(LockScreenImageDelActivity.this, getString(R.string.nodatadel), Toast.LENGTH_SHORT).show();
				}else{
					delImage();
				}
			}
		});

		getImage();
	}

	//이미지 얻기.
	private void getImage(){
		String query =null;
		mImageDel.removeAll(mImageDel);
		sqlDatabase = mImageDB.getReadableDatabase();
		try{
			sqlDatabase.beginTransaction();
			if(isImage) query = "select * from image where imageType='0'";
			else query = "select * from image where imageType='1'"; 
			Cursor cursor = sqlDatabase.rawQuery(query, null);
			if(cursor.getCount()!=0){
				while (cursor.moveToNext()) {
					int index = cursor.getInt(0);
					String path = cursor.getString(1);
					mImageDel.add(new LockScreenImage(index, path));
				}
			}
			sqlDatabase.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDatabase.endTransaction();
			mImageDB.close();
		}
		mImageDelAdapter = new LockScreenImageDelAdapter(this, mImageDel, mImageDel.size(), isImage);
		mGrid.setAdapter(mImageDelAdapter);
	}


	private void delImage(){
		String dirPath = null;
		if(isImage) dirPath = getFilesDir().getAbsolutePath()+"/bg/";
		else dirPath = getFilesDir().getAbsolutePath()+"/button/";
		try {
			sqlDatabase = mImageDB.getWritableDatabase();
			sqlDatabase.beginTransaction();
			for(int i=0 ; i<mImageDelAdapter.getChecked().size(); i++){
				int position =  mImageDelAdapter.getChecked().get(i);
				int index = mImageDel.get(position).getIndex();
				String path =mImageDel.get(position).getPath();
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
			setResult(RESULT_OK);
			finish();
		}
	}

	
	//내부메모리 bitmap 삭제.
	private void delBitmap(String path){
		File file =null;
		try{
			if(isImage) file = new File("data/data/specup.fanmind/files/bg");
			else file = new File("data/data/specup.fanmind/files/button");
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
}
