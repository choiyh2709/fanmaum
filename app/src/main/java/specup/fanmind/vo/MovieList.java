package specup.fanmind.vo;

public class MovieList {

	String mName;
	String mImagePath;
	String mDate;
	int mDuration;
	String mID;

	
    /**
   	 * 유투브 동영상
   	 * @param Name 이름
   	 * @param ImagePath 이미지 경로
   	 * @param Date 날짜
   	 * @param Duration 시간
   	 * @param ID 유투브 아이디
   	 */
	public MovieList(String Name, String ImagePath, String Date, int Duration, String ID){
		mName = Name;
		mImagePath = ImagePath;
		mDate = Date;
		mDuration = Duration;
		mID = ID;
	}
    public MovieList(){

    }

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getImagePath() {
		return mImagePath;
	}

	public void setImagePath(String imagePath) {
		mImagePath = imagePath;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		mDate = date;
	}

	public int getDuration() {
		return mDuration;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}

	public String getID() {
		return mID;
	}

	public void setID(String iD) {
		mID = iD;
	}
	
	
	
	
}
