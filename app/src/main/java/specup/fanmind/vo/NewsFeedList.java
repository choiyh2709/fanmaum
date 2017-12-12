package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsFeedList implements Parcelable{

	String mNewsFeedSrl;
	String mTitle;
	String mText;
	String mNick;
	String mID;
	String mPic;
	String mTime;
	String[] mImg;
	String mLikes;
	String mMyLike;
	String mReplyCnt;
	String mAlarm;
	
	
	public NewsFeedList(String srl, String title, String text, String nick, String id, String pic,
			String time, String[] img, String likes, String mylike, String replycnt, String alarm){
		mNewsFeedSrl = srl;
		mTitle = title;
		mText = text;
		mNick = nick;
		mID = id;
		mPic =pic;
		mTime = time;
		mImg= img;
		mLikes = likes;
		mMyLike = mylike;
		mReplyCnt = replycnt;
		mAlarm = alarm;
	}
	
	public String getNewsFeedSrl() {
		return mNewsFeedSrl;
	}
	public void setNewsFeedSrl(String newsFeedSrl) {
		mNewsFeedSrl = newsFeedSrl;
	}
	public String getText() {
		return mText;
	}
	public void setText(String text) {
		mText = text;
	}
	public String getNick() {
		return mNick;
	}
	public void setNick(String nick) {
		mNick = nick;
	}
	public String getID() {
		return mID;
	}
	public void setID(String iD) {
		mID = iD;
	}
	public String getPic() {
		return mPic;
	}
	public void setPic(String pic) {
		mPic = pic;
	}
	public String getTime() {
		return mTime;
	}
	public void setTime(String time) {
		mTime = time;
	}
	public String[] getImg() {
		return mImg;
	}
	public void setImg(String[] img) {
		mImg = img;
	}
	public String getLikes() {
		return mLikes;
	}
	public void setLikes(String likes) {
		mLikes = likes;
	}
	public String getMyLike() {
		return mMyLike;
	}
	public void setMyLike(String myLike) {
		mMyLike = myLike;
	}
	public String getReplyCnt() {
		return mReplyCnt;
	}
	public void setReplyCnt(String replyCnt) {
		mReplyCnt = replyCnt;
	}
	public String getAlarm() {
		return mAlarm;
	}
	public void setAlarm(String alarm) {
		mAlarm = alarm;
	}
	public String getTitle() {
		return mTitle;
	}
	public void setTitle(String title) {
		mTitle = title;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mNewsFeedSrl);
		dest.writeString(mTitle);
		dest.writeString(mText);
		dest.writeString(mNick);
		dest.writeString(mID);
		dest.writeString(mPic);
		dest.writeString(mTime);
		dest.writeStringArray(mImg);
		dest.writeString(mLikes);
		dest.writeString(mMyLike);
		dest.writeString(mReplyCnt);
		dest.writeString(mAlarm);
	}
	
	private void readFromParcel(Parcel in){
		mNewsFeedSrl = in.readString();
		mTitle = in.readString();
		mText = in.readString();
		mNick = in.readString();
		mID = in.readString();
		mPic =in.readString();
		mTime = in.readString();
		mImg= in.createStringArray();
		mLikes = in.readString();
		mMyLike = in.readString();
		mReplyCnt = in.readString();
		mAlarm = in.readString();
	}
	
	public NewsFeedList(Parcel in){
		readFromParcel(in);
	}
	
	public static final Creator<NewsFeedList> CREATOR = new Creator<NewsFeedList>(){
        public NewsFeedList createFromParcel(Parcel in) {
             return new NewsFeedList(in);
       }
        
       public NewsFeedList[] newArray(int size) {
            return new NewsFeedList[size];
       }
   };
   
}

