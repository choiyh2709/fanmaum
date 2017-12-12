package specup.fanmind.fanmindsetting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ImageSaveDB extends SQLiteOpenHelper{

	private static final String DB_NAME = "image.db";

	
	public ImageSaveDB(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public ImageSaveDB(Context context) {
		super(context, DB_NAME, null, 1);
        Log.e("DB 생성", "DB 생성");
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//imagepath �̹��� ���, imagecheck �̹��� üũ=> 0�� üũ, imagetype=> button���̸� 1�ƴϸ� 0
		db.execSQL("CREATE TABLE image (_id INTEGER PRIMARY KEY AUTOINCREMENT, imagePath TEXT, imageCheck INTEGER, imageType INTEGER);");
        Log.e("table  생성", "table 생성");
		
	}	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS image");
		onCreate(db);
	}
	
}