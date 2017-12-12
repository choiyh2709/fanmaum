package specup.fanmind.vo;

public class CommentList {

	String mReplySrl;
	String mID;
	String mProfilePath;
	String mNickName;
	String mContent;
	String mDate;
	String mMyLike;
	String mLikeCount;

    /**댓글
   	 * @param reply 댓글번호
   	 * @param id 아이디
   	 * @param nickname 닉네임
   	 * @param profile 프로필 이미지
   	 * @param date 날짜
   	 * @param content 내용
   	 * @param likecount 좋아요 수
   	 * @param mylike 내가 좋아했으면 1 아니면 0
   	 */
	public CommentList(String reply, String id, String nickname, String profile , String date,
			String content, String likecount , String mylike){
		mReplySrl =  reply;
		mID = id;
		mProfilePath = profile;
		mNickName = nickname;
		mContent = content;
		mDate = date;
		mMyLike = mylike;
		mLikeCount = likecount;
	}


    /**내댓글 보기
   	 * @param board 게시판종류
   	 * @param srl 글번호
   	 * @param reply 댓글번호
   	 * @param notify 읽은 댓글은 1, 아니면 0
   	 * @param nick 글쓴이 닉네임
   	 * @param text 글내용
   	 * @param time 글쓴시간
   	 */
	public CommentList(String board, String srl, String reply, String notify, String nick, String text, String time){
		mReplySrl  = reply;
		mNickName= nick;
		mMyLike = notify;
		mContent = text;
		mID = board;
		mProfilePath = srl;
		mDate = time;
	}



	public String getID() {
		return mID;
	}


	public void setID(String iD) {
		mID = iD;
	}


	public String getReplySrl() {
		return mReplySrl;
	}

	public void setReplySrl(String replySrl) {
		mReplySrl = replySrl;
	}

	public String getProfilePath() {
		return mProfilePath;
	}

	public void setProfilePath(String profilePath) {
		mProfilePath = profilePath;
	}

	public String getNickName() {
		return mNickName;
	}

	public void setNickName(String nickName) {
		mNickName = nickName;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		mContent = content;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(String date) {
		mDate = date;
	}

	public String getLike() {
		return mMyLike;
	}

	public void setLike(String like) {
		mMyLike = like;
	}

	public String getLikeCount() {
		return mLikeCount;
	}

	public void setLikeCount(String likeCount) {
		mLikeCount = likeCount;
	}



}
