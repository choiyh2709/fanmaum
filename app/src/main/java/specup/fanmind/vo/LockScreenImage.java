package specup.fanmind.vo;

public class LockScreenImage {
	
	int mIndex;
	String mPath;
	int mCheck;
	boolean isFirst;
	boolean isDel;
	
    /** 잠금화면
   	 * @param index 잠금화면 번호
   	 * @param path 잠금화면 경로
   	 * @param check 잠금화면 체크상태
   	 * @param first 잠금화면 첫번째 버튼.
   	 * @param Del 삭제버튼
   	 */
	public LockScreenImage(int index, String path, int check, boolean first, boolean del){
		mIndex = index;
		mPath = path;
		mCheck = check;
		isFirst = first; 
		isDel = del;
	}
	
	
	public LockScreenImage(String path, boolean first, boolean del){
		mPath = path;
		isFirst = first; 
		isDel = del;
	}
	
	
    /**잠금화면 경로.
   	 * @param index 잠금화면 번호.
   	 * @param path 잠금화면 경로.
   	 */
	public LockScreenImage(int index, String path){
		mIndex = index;
		mPath = path;
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

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int getCheck() {
		return mCheck;
	}

	public void setCheck(int check) {
		mCheck = check;
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int index) {
		mIndex = index;
	}
	
	

}
