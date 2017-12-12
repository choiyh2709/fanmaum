package specup.fanmind.vo;

public class RightList {

	String mPath;
	String mName;
	String mFan;
	String mStarsrl;
	String mReady;
	String mStar;
	boolean isSelect;
	boolean isDel;
	String mTitle;
	boolean isLike;
	
	
    /** 오른쪽 메뉴
   	 * @param path 이미지경로
   	 * @param name 그룹 이름
   	 * @param fan 팬 수
   	 * @param starsrl 연예인 번호
   	 * @param select  현재 선택되어진 연예인인지 아닌지
   	 * @param del  편집버튼이 보여지는지 아닌지
   	 */
	public RightList(String path, String name, String fan, String starsrl, boolean select, boolean del){
		mPath = path;
		mName = name;
		mFan = fan;
		mStarsrl = starsrl;
		isSelect = select;
		isDel = del;
	}
	
	
    /** 오른쪽 메뉴
   	 * @param path 이미지경로
   	 * @param name 그룹 이름
   	 * @param fan 팬 수
   	 * @param starsrl 연예인 번호
   	 * @param select  즐겨찾기 연예인
   	 * @param title section텍스트
   	 * @param like 즐겨찾기
   	 */
	public RightList(String path, String name, String fan, String starsrl, boolean select, String title, boolean like){
		mPath = path;
		mName = name;
		mFan = fan;
		mStarsrl = starsrl;
		isSelect = select;
		mTitle =title;
		isLike = like;
	}
	
	
    /**회원가입시 스타 선택
   	 * @param path 스타 인덱스
   	 * @param name 스타 이름
   	 * @param del 스타 체크상태
   	 */
	public RightList(String path, String name, boolean del){
		mPath = path;
		mName = name;
		isDel = del;
	}
	
    /**적립내역 알려주기
   	 * @param name 종류
   	 * @param point 포인트
   	 */
	public RightList(String name, String point){
		mName = name;
		mFan = point;
	}
	
    /**스케쥴 보기 팝업
   	 * @param path 타이틀
   	 * @param name 시작시간
   	 * @param point 끝나는 시간
   	 */
	public RightList(String path, String starsrl, String name, String point, String ready, String star){
		mPath = path;
		mStarsrl = starsrl;
		mName = name;
		mFan = point;
		mReady = ready;
		mStar = star;
	}
	
    /**스케쥴 보기 팝업
   	 * @param path 타이틀
   	 * @param name 시작시간
   	 * @param point 끝나는 시간
   	 */
	public RightList(String path, String starsrl, String name, String point){
		mPath = path;
		mStarsrl = starsrl;
		mName = name;
		mFan = point;
	}
	
	
	public boolean isLike() {
		return isLike;
	}


	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}


	public String getStar() {
		return mStar;
	}

	public void setStar(String mStar) {
		this.mStar = mStar;
	}
	
	public String getReady() {
		return mReady;
	}


	public void setReady(String mReady) {
		this.mReady = mReady;
	}

	

	public String getTitle() {
		return mTitle;
	}


	public void setTitle(String mTitle) {
		this.mTitle = mTitle;
	}


	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}


	public String getStarsrl() {
		return mStarsrl;
	}

	public void setStarsrl(String starsrl) {
		mStarsrl = starsrl;
	}


	public RightList(String name, boolean del){
		mName = name;
		isDel = del;
	}

	public boolean isDel() {
		return isDel;
	}

	public void setDel(boolean isDel) {
		this.isDel = isDel;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		mPath = path;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getFan() {
		return mFan;
	}

	public void setFan(String fan) {
		mFan = fan;
	}
	
	
	
}
