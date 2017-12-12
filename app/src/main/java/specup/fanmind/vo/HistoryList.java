package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryList implements Parcelable{
	
	public String mHistorySrl;
	public String mTitle;
	public String mNick;
	public String mPic;
	public String mTime;
	public String mThum;
	public String[] mImg;
	public String mText;
	public String mLike;
	public String mMyLike;
	public String mReplyCount;
	public String mStarName;
	public String mStarSrl;
	
	
    /** 히스토리
   	 * @param srl 히스토리 넘버
   	 * @param title 제목
   	 * @param nick 관리자명
   	 * @param pic 관리자 프로필 url
   	 * @param time 서포트 시간
   	 * @param thum 썸네일 이미지
   	 * @param img 본문 이미지
   	 * @param text 내용
   	 * @param like 좋아요 수
   	 * @param mylike 내가 좋아요 or not
   	 * @param reply 댓글 수
   	 */
	public HistoryList(String srl, String title, String nick, String pic, String time, String thum, String img[],
			String text, String like, String mylike, String reply){
		mHistorySrl = srl;
		mText = text;
		mThum = thum;
		mMyLike = mylike;
		mTime = time;
		mTitle = title;
		mImg = img;
		mNick = nick;
		mPic = pic;
		mLike = like;
		mReplyCount = reply;
	}
	
    /** 히스토리
   	 * @param srl 히스토리 넘버
   	 * @param title 제목
   	 * @param nick 관리자명
   	 * @param pic 관리자 프로필 url
   	 * @param time 서포트 시간
   	 * @param thum 썸네일 이미지
   	 * @param img 본문 이미지
   	 * @param text 내용
   	 * @param like 좋아요 수
   	 * @param mylike 내가 좋아요 or not
   	 * @param reply 댓글 수
   	 */
	public HistoryList(String srl, String title, String nick, String pic, String time, String thum, String img[],
			String text, String like, String mylike, String reply, String starname, String starsrl){
		mHistorySrl = srl;
		mText = text;
		mThum = thum;
		mMyLike = mylike;
		mTime = time;
		mTitle = title;
		mImg = img;
		mNick = nick;
		mPic = pic;
		mLike = like;
		mReplyCount = reply;
		mStarName = starname;
		mStarSrl = starsrl;
	}

	
	
	
	public String getStarName() {
		return mStarName;
	}

	public void setStarName(String mStarName) {
		this.mStarName = mStarName;
	}

	public String getStarSrl() {
		return mStarSrl;
	}

	public void setStarSrl(String mStarSrl) {
		this.mStarSrl = mStarSrl;
	}

	public String getHistorySrl() {
		return mHistorySrl;
	}




	public void setHistorySrl(String historySrl) {
		mHistorySrl = historySrl;
	}




	public String getTitle() {
		return mTitle;
	}




	public void setTitle(String title) {
		mTitle = title;
	}




	public String getNick() {
		return mNick;
	}




	public void setNick(String nick) {
		mNick = nick;
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




	public String getThum() {
		return mThum;
	}




	public void setThum(String thum) {
		mThum = thum;
	}

	public String[] getImg() {
		return mImg;
	}

	public void setImg(String[] img) {
		mImg = img;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public String getLike() {
		return mLike;
	}

	public void setLike(String like) {
		mLike = like;
	}

	public String getMyLike() {
		return mMyLike;
	}

	public void setMyLike(String myLike) {
		mMyLike = myLike;
	}

	public String getReplyCount() {
		return mReplyCount;
	}

	public void setReplyCount(String replyCount) {
		mReplyCount = replyCount;
	}

	public HistoryList(Parcel in) {
		// TODO Auto-generated constructor stub
		readFromParcel(in);
	}

	private void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		mText = in.readString();
		mHistorySrl = in.readString();
		mThum= in.readString();
		mTime = in.readString();
		mTitle = in.readString();
		mMyLike = in.readString();
		mLike = in.readString();
		mPic = in.readString();
		mReplyCount = in.readString();
		mNick = in.readString();
		mImg = in.createStringArray();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mText);
		dest.writeString(mHistorySrl);
		dest.writeString(mThum);
		dest.writeString(mTime);
		dest.writeString(mTitle);
		dest.writeString(mMyLike);
		dest.writeString(mLike);
		dest.writeString(mPic);
		dest.writeString(mReplyCount);
		dest.writeString(mNick);
		dest.writeStringArray(mImg);
	}
	
	public static final Creator<HistoryList> CREATOR = new Creator<HistoryList>(){
		   public HistoryList createFromParcel(Parcel in) {
	             return new HistoryList(in);
	       }
	        
	       public HistoryList[] newArray(int size) {
	            return new HistoryList[size];
	       }
	};
	
}
