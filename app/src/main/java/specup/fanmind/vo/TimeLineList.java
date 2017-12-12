package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeLineList implements Parcelable{

	String mTimeSrl;
	String mThum;
	String mImg;
	String mLikeCnt;
	String mReplyCnt;
	String mMyLike;
	String mWidth;
	String mHeight;
	String mMine;
	String mAlarm;
	
	public TimeLineList(String srl, String thum, String img, String likecnt, String replylike, String mylike,
			String width, String height, String mine, String alarm){
		mTimeSrl = srl;
		mThum= thum;
		mImg=img;
		mLikeCnt = likecnt;
		mReplyCnt = replylike;
		mMyLike = mylike;
		mWidth = width;
		mHeight = height;
		mMine = mine;
		mAlarm = alarm;
	}
	
	
	
	
	public String getMine() {
		return mMine;
	}




	public void setMine(String mMine) {
		this.mMine = mMine;
	}




	public String getAlarm() {
		return mAlarm;
	}




	public void setAlarm(String mAlarm) {
		this.mAlarm = mAlarm;
	}




	public String getTimeSrl() {
		return mTimeSrl;
	}




	public void setTimeSrl(String mTimeSrl) {
		this.mTimeSrl = mTimeSrl;
	}




	public String getThum() {
		return mThum;
	}




	public void setThum(String mThum) {
		this.mThum = mThum;
	}




	public String getImg() {
		return mImg;
	}




	public void setImg(String mImg) {
		this.mImg = mImg;
	}




	public String getLikeCnt() {
		return mLikeCnt;
	}




	public void setLikeCnt(String mLikeCnt) {
		this.mLikeCnt = mLikeCnt;
	}




	public String getReplyCnt() {
		return mReplyCnt;
	}




	public void setReplyCnt(String mReplyCnt) {
		this.mReplyCnt = mReplyCnt;
	}




	public String getMyLike() {
		return mMyLike;
	}




	public void setMyLike(String mMyLike) {
		this.mMyLike = mMyLike;
	}




	public String getWidth() {
		return mWidth;
	}




	public void setWidth(String mWidth) {
		this.mWidth = mWidth;
	}




	public String getHeight() {
		return mHeight;
	}




	public void setHeight(String mHeight) {
		this.mHeight = mHeight;
	}




	public TimeLineList(Parcel in){
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mImg);
		dest.writeString(mTimeSrl);
		dest.writeString(mThum);
		dest.writeString(mLikeCnt);
		dest.writeString(mReplyCnt);
		dest.writeString(mMyLike);
		dest.writeString(mWidth);
		dest.writeString(mHeight);
		dest.writeString(mMine);
		dest.writeString(mAlarm);
	}
	
	private void readFromParcel(Parcel in){
		mImg = in.readString();
		mTimeSrl = in.readString();
		mThum = in.readString();
		mLikeCnt = in.readString();
		mReplyCnt = in.readString();
		mMyLike = in.readString();
		mWidth = in.readString();
		mHeight=in.readString();
		mMine = in.readString();
		mAlarm = in.readString();
	}
	
	
	
	public static final Creator<TimeLineList> CREATOR = new Creator<TimeLineList>(){
        public TimeLineList createFromParcel(Parcel in) {
             return new TimeLineList(in);
       }
        
       public TimeLineList[] newArray(int size) {
            return new TimeLineList[size];
       }
   };
	
	
	
}
