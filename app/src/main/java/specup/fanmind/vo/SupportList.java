package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class SupportList extends Object implements Parcelable{

	public String mSrl;
	public String mTitle;
	public String mThumImgPath;
	public String[] mImgPath;
	public String mRegisterDate;
	public String mDeadDate;
	public String mGoalMoney;
	public String mNowMoney;
	public String mEntryPeople;
	public String mText;
	public String mWarning;
	public String mSendTime;
	public String mStarName;
	public String mStarSrl;
	public String event_srl;
	public String event_end_date;
	public String mHeartmin;
	private String isHistory;
	
	
    /** 서포트 리스트 진행중
   	 * @param srl : 서포트 제목
   	 * @param title 제목
   	 * @param registerdate 등록일
   	 * @param deaddate 마감일
   	 * @param goalmoney 목표금액
   	 * @param nowmoney 현재 금액
   	 * @param thumimgpath 썸네일이미지 경로
   	 * @param entrypeople 참여한팬
   	 * @param imgpath 이미지 경로
   	 * @param text 상세설명
   	 * @param warning 주의사항
   	 * @param delivery 진행중 flase, 전달예정 true
   	 */
	public SupportList(String srl, String title, String registerdate, String deaddate, String goalmoney,
			String nowmoney, String thumimgpath, String entrypeople, String[] imgpath, String text, String warning ){
		mSrl = srl;
		mImgPath = imgpath;
		mTitle = title;
		mThumImgPath = thumimgpath;
		mRegisterDate = registerdate;
		mDeadDate = deaddate;
		mGoalMoney = goalmoney;
		mNowMoney = nowmoney;
		mEntryPeople = entrypeople;
		mText =  text;
		mWarning = warning;

	}
	
	public SupportList(String srl, String title, String registerdate, String deaddate, String goalmoney,
			String nowmoney, String thumimgpath, String entrypeople, String[] imgpath, String text, String warning ,
			String event_srl, String event_end_date, String temp){
		mSrl = srl;
		mImgPath = imgpath;
		mTitle = title;
		mThumImgPath = thumimgpath;
		mRegisterDate = registerdate;
		mDeadDate = deaddate;
		mGoalMoney = goalmoney;
		mNowMoney = nowmoney;
		mEntryPeople = entrypeople;
		mText =  text;
		mWarning = warning;
		this.event_srl = event_srl;
		this.event_end_date = event_end_date;
	}

	
	public String getEvent_end_date() {
		return event_end_date;
	}

	public void setEvent_end_date(String event_end_date) {
		this.event_end_date = event_end_date;
	}

	public String getEvent_srl() {
		return event_srl;
	}

	public void setEvent_srl(String event_srl) {
		this.event_srl = event_srl;
	}

	public SupportList(String srl, String title, String registerdate, String deaddate, String goalmoney,
			String nowmoney, String thumimgpath, String entrypeople, String[] imgpath, String text,
			String warning, String starname, String starsrl){
		mSrl = srl;
		mImgPath = imgpath;
		mTitle = title;
		mThumImgPath = thumimgpath;
		mRegisterDate = registerdate;
		mDeadDate = deaddate;
		mGoalMoney = goalmoney;
		mNowMoney = nowmoney;
		mEntryPeople = entrypeople;
		mText =  text;
		mWarning = warning;
		mStarName = starname;
		mStarSrl = starsrl;
	}
	
	public SupportList(String srl, String title, String registerdate, String deaddate, String goalmoney,
			String nowmoney, String thumimgpath, String entrypeople, String[] imgpath, String text,
			String warning, String starname, String starsrl,String event_srl, String event_end_date){
		mSrl = srl;
		mImgPath = imgpath;
		mTitle = title;
		mThumImgPath = thumimgpath;
		mRegisterDate = registerdate;
		mDeadDate = deaddate;
		mGoalMoney = goalmoney;
		mNowMoney = nowmoney;
		mEntryPeople = entrypeople;
		mText =  text;
		mWarning = warning;
		mStarName = starname;
		mStarSrl = starsrl;
		this.event_srl = event_srl;
		this.event_end_date = event_end_date;
	}
	
	
	
    /** 서포트 리스트 전달예정
   	 * @param srl : 서포트 제목
   	 * @param title 제목
   	 * @param registerdate 등록일
   	 * @param deaddate 마감일
   	 * @param sendtime 전달예정일
   	 * @param goalmoney 목표금액
   	 * @param nowmoney 현재 금액
   	 * @param thumimgpath 썸네일이미지 경로
   	 * @param entrypeople 참여한팬
   	 * @param imgpath 이미지 경로
   	 * @param text 상세설명
   	 * @param warning 주의사항
   	 * @param delivery 진행중 flase, 전달예정 true
   	 */
	public SupportList(String srl, String title, String registerdate, String deaddate, String sendtime, String goalmoney,
			String nowmoney, String thumimgpath, String entrypeople, String[] imgpath, String text, String warning){
		mSrl = srl;
		mImgPath = imgpath;
		mTitle = title;
		mThumImgPath = thumimgpath;
		mRegisterDate = registerdate;
		mDeadDate = deaddate;
		mGoalMoney = goalmoney;
		mNowMoney = nowmoney;
		mEntryPeople = entrypeople;
		mText =  text;
		mWarning = warning;
		mSendTime = sendtime;
	}
	
    /**스케쥴
   	 * @param srl 타이틀
   	 * @param text 내용
   	 * @param title 시작시간
   	 * @param thum 끝나는시간
   	 */
	public SupportList(String srl, String text, String title, String thum){
		mSrl = srl;
		mText = text;
		mTitle = title;
		mThumImgPath = thum;
	}
	
    /**스케쥴
   	 * @param srl 타이틀
   	 * @param text 내용
   	 * @param title 시작시간
   	 * @param thum 끝나는시간
   	 */
	public SupportList(String srl, String text, String title, String thum, String hit){
		mSrl = srl;
		mText = text;
		mTitle = title;
		mThumImgPath = thum;
		mNowMoney = hit;
	}
	

    /**스케쥴
   	 * @param srl 타이틀
   	 * @param text 내용
   	 * @param title 시작시간
   	 * @param thum 끝나는시간
   	 */
	public SupportList(String srl, String text){
		mSrl = srl;
		mText = text;
	}
	
    /** 연예인 요청
	   	 * @param srl 번호
	   	 * @param title 이름
	   	 * @param thum 이미지 url
	   	 * @param goal 목표요청수
	   	 * @param vote 현재요청수
	   	 * @param vote 좋아요
	   	 */
		public SupportList(String srl, String title, String thum, String goal, String vote, String like){
			mSrl = srl;
			mTitle = title;
			mThumImgPath = thum;
			mGoalMoney = goal;
			mNowMoney = vote;
			mWarning = like;
		}


    /** project content
       	 * @param srl 번호
       	 * @param title 타이틀
       	 * @param thum 이미지 url
       	 * @param goal 목표요청수
       	 * @param vote 현재요청수
       	 * @param begin_time 시작일
       	 * @param close_time 종료일
       	 */
    	public SupportList(String srl, String title, String thum, String goal, String vote, String begin_time, String close_time ){
    		mSrl = srl;
    		mTitle = title;
    		mThumImgPath = thum;
    		mGoalMoney = goal;
    		mNowMoney = vote;
            mRegisterDate = begin_time;
            mDeadDate = close_time;
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


	public SupportList(Parcel in){
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
		dest.writeStringArray(mImgPath);
		dest.writeString(mTitle);
		dest.writeString(mRegisterDate);
		dest.writeString(mDeadDate);
		dest.writeString(mEntryPeople);
		dest.writeString(mGoalMoney);
		dest.writeString(mNowMoney);
		dest.writeString(mText);
		dest.writeString(mWarning);
		dest.writeString(mSrl);
		dest.writeString(mThumImgPath);
		dest.writeString(mSendTime);
		dest.writeString(event_srl);
		dest.writeString(event_end_date);
		dest.writeString(isHistory);
	}
	
	private void readFromParcel(Parcel in){
		mImgPath = in.createStringArray();
		mTitle = in.readString();
		mRegisterDate = in.readString();
		mDeadDate = in.readString();
		mEntryPeople = in.readString();
		mGoalMoney = in.readString();
		mNowMoney = in.readString();
		mText=in.readString();
		mWarning=in.readString();
		mSrl=in.readString();
		mThumImgPath=in.readString();
		mSendTime = in.readString();
		event_srl = in.readString();
		event_end_date = in.readString();
		isHistory = in.readString();
	}
	
	public static final Creator<SupportList> CREATOR = new Creator<SupportList>(){
        public SupportList createFromParcel(Parcel in) {
             return new SupportList(in);
       }
        
       public SupportList[] newArray(int size) {
            return new SupportList[size];
       }
   };

	public String getSrl() {
		return mSrl;
	}

	public void setSrl(String srl) {
		mSrl = srl;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public String getThumImgPath() {
		return mThumImgPath;
	}

	public void setThumImgPath(String thumImgPath) {
		mThumImgPath = thumImgPath;
	}

	public String[] getImgPath() {
		return mImgPath;
	}

	public void setImgPath(String imgPath[]) {
		mImgPath = imgPath;
	}

	public String getRegisterDate() {
		return mRegisterDate;
	}

	public void setRegisterDate(String registerDate) {
		mRegisterDate = registerDate;
	}

	public String getDeadDate() {
		return mDeadDate;
	}

	public void setDeadDate(String deadDate) {
		mDeadDate = deadDate;
	}

	
	public String getWarning() {
		return mWarning;
	}

	public void setWarning(String warning) {
		mWarning = warning;
	}

	public String getGoalMoney() {
		return mGoalMoney;
	}

	public void setGoalMoney(String goalMoney) {
		mGoalMoney = goalMoney;
	}

	public String getNowMoney() {
		return mNowMoney;
	}

	public void setNowMoney(String nowMoney) {
		mNowMoney = nowMoney;
	}

	public String getEntryPeople() {
		return mEntryPeople;
	}

	public void setEntryPeople(String entryPeople) {
		mEntryPeople = entryPeople;
	}

	public String getText() {
		return mText;
	}

	public void setText(String text) {
		mText = text;
	}

	public String getSendTime() {
		return mSendTime;
	}

	public void setSendTime(String sendTime) {
		mSendTime = sendTime;
	}

	public String getIsHistory() {
		return isHistory;
	}

	public void setIsHistory(String isHistory) {
		this.isHistory = isHistory;
	}
}
