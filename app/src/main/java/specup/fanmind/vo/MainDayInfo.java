package specup.fanmind.vo;

/**
 * 하루의 날짜정보를 저장하는 클래스
 *
 * @author croute
 * @since 2011.03.08
 */
public class MainDayInfo{
	private String day;
	private boolean inMonth;
	
	private boolean isToday;
	private boolean inOne;
	
	private int mCount;
	private int mKind, mKind2, mKind3;
	private String mTitle1;
	private String mTitle2;
	private String mTitle3;
	
	
    /**
   	 * 날짜를 반환한다.
   	 *
   	 * @return day 날짜
   	 */
	public String getDay(){
		return day;
	}

    /**
   	 * 날짜를 저장한다.
   	 *
   	 * @param day 날짜
   	 */
	public void setDay(String day){
		this.day = day;
	}

    /**
   	 * 이번달의 날짜인지 정보를 반환한다.
   	 *
   	 * @return inMonth(true/false)
   	 */
	public boolean isInMonth(){
		return inMonth;
	}

    /**
   	 * 이번달의 날짜인지 정보를 저장한다.
   	 *
   	 * @param inMonth(true/false)
   	 */
	public void setInMonth(boolean inMonth){
		this.inMonth = inMonth;
	}
	
	public int getKind(){
		return mKind;
	}
	
	public void setKind(int mKind){
		this.mKind = mKind;
	}
	
	public int getKind2(){
		return mKind2;
	}
	
	public void setKind2(int mKind2){
		this.mKind2 = mKind2;
	}
	
	public int getKind3(){
		return mKind3;
	}
	
	public void setKind3(int mKind3){
		this.mKind3 = mKind3;
	}
	
	public void setTitle1(String mTitle1){
		this.mTitle1 = mTitle1;
	}
	
	public String getTitle1(){
		return mTitle1;
	}
	
	public void setTitle2(String mTitle2){
		this.mTitle2 = mTitle2;
	}
	
	public String getTitle2(){
		return mTitle2;
	}
	
	public void setTitle3(String mTitle3){
		this.mTitle3 = mTitle3;
	}
	
	public String getTitle3(){
		return mTitle3;
	}
	
	public boolean isOne(){
		return inOne;
	}
	
	public void setisOne(boolean inOne){
		this.inOne = inOne;
	}
	

	public int getCount(){
		return mCount;
	}
	
	public void setCount(int mCount){
		this.mCount = mCount;
	}
	
	public boolean isToday(){
		return isToday;
	}
	
	public void setisToday(boolean isToday){
		this.isToday = isToday;
	}
}