package specup.fanmind.common.http;

import android.content.Context;

import specup.fanmind.common.Util.Utils;
import specup.fanmind.fanmindsetting.FanMindSetting;


/**
 * @author user
 */
public class URLAddress {


    //    public static final String NEW_SERVER = "http://apis.fanmaum.com/fanmaum/index.php";//test server
    public static final String NEW_SERVER = "https://fanmaum.com";//real server

    //    public static final String NEW_SERVER = "Http://52.193.205.176/fanmaum/index.php/service/v1";


    public static String BASE_URL = "";

    /**
     * 서버 URL
     */
//    public static String SERVER = "https://app.fanmaum.com/fanmaum.php";
    public static String SERVER = NEW_SERVER+"/service/v1/version";

    /**
     * 팬마음 연예인 추가문의사항
     */
    public static String FANMIND_STARURL = "http://goto.kakao.com/@%ED%8C%AC%EB%A7%88%EC%9D%8C";


    /**
     * 회원가입 아이디 중복확인
     */
    public static String LOGIN_CHECK_ID(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/check_id";
    }

    /**
     * 휴대폰 인증 번호 발송 요청
     */
    public static String LOGIN_SEND_CERT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/join/send_cert";
    }


    /**
     * 휴대폰 인증 번호 확인
     */
    public static String LOGIN_CHECK_CERT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/join/check_cert";
    }


    /**
     * 연예인 리스트 조회
     */
    public static String LOGIN_STAR_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/star/all";
    }


    /**
     * 폰번호 중복검사
     */
    public static String CHECK_PHONE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/join_jw/phone";
    }

    /**
     * 기기번호 중복검사
     */
    public static String CHECK_DEVICE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/join_jw2/imei";
    }


    /**
     * 아이디 찾기
     */
    public static String LOGIN_FIND_ID(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account_jbink/find_id";
    }

    /**
     * 비밀번호 찾기
     */
    public static String LOGIN_FIND_PW(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/find_passwd";
    }

    /**
     * 제휴문의
     */
    public static String CONTACT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/contact";
    }

    /**
     * 팬클럽
     */
    public static String FAN_CLUB(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/contact_fanclub";
    }

    /**
     * 서포트 리스트 요청
     */
    public static String SUPPORT_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support/all";
    }

    /**
     * 서포트 리스트 요청
     */
    public static String SUPPORT_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support_jbink/all";
    }


    /**
     * 서포트 마음보내기
     */
    public static String SUPPORT_SEND_MIND(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/send_heart";
    }

    /**
     * 댓글 리스트
     */
    public static String REPLY_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/all";
    }


    /**
     * 댓글 좋아요
     */
    public static String REPLY_LIKE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/like";
    }


    /**
     * 댓글신고하기
     */
    public static String REPLY_REPORT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/report";
    }

    /**
     * 댓글 쓰기
     */
    public static String REPLY_WRITE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/write";
    }

    /**
     * 댓글 수정하기
     */
    public static String REPLY_MODIFY(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/modify";
    }

    /**
     * 댓글 삭제
     */
    public static String REPLY_DELETE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/reply/delete";
    }


    /**
     * 서포트 전달 예정 리스트
     */
    public static String SUPPORT_READY_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support/ready";
    }

    /**
     * 서포트 전달 예정 상세보기
     */
    public static String SUPPORT_READY_LIST_DETAIL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support/ready_detail";
    }

    /**
     * 서포트 전달 예정 전체보기
     */
    public static String SUPPORT_READY_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support_jbink/ready";
    }

    /**
     * 서포트 히스토리 리스트
     */
    public static String SUPPORT_HISTORY_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support/history";
    }

    /**
     * 서포트 히스토리 리스트
     */
    public static String SUPPORT_HISTORY_DETAIL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support/history_detail";
    }

    /**
     * 서포트 히스토리 전체 리스트
     */
    public static String SUPPORT_HISTORY_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/support_jbink/history";
    }


    /**
     * 타임라인 리스트
     */
    public static String TIMELINE_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/all";
    }

    /**
     * 타임라인 리스트
     */
    public static String TIMELINE_LOAD(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/load_image";
    }


    /**
     * 타임라인 월별 리스트
     */
    public static String TIMELINE_MONTH_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/detail";
    }

    /**
     * 이미지 날짜 제보
     */
    public static String TIMELINE_DATE_SEND(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/tag";
    }

    /**
     * 이미지 업로드
     */
    public static String TIMELINE_IMAGE_UPLOAD(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/upload";
    }

    /**
     * 타임라인 글 삭제
     */
    public static String TIMELINE_IMAGE_DEL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/delete";
    }

    /**
     * 타임라인 글 신고
     */
    public static String TIMELINE_IMAGE_SINGO(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/report";
    }

    /**
     * 타임라인 글 알람 켜기
     */
    public static String TIMELINE_ALARM_ON(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/alarm_on";
    }

    /**
     * 타임라인 글 알람 끄기
     */
    public static String TIMELINE_ALARM_OFF(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/timeline/alarm_off";
    }

    /**
     * 뉴스피드 리스트
     */
    public static String NEWSFEED_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/all";
    }

    /**
     * 뉴스피드 디테일
     */
    public static String NEWSFEED_DETAIL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/detail";
    }


    /**
     * 뉴스피드 신고하기
     */
    public static String NEWSFEED_REPORT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/report";
    }

    /**
     * 뉴스피드 알림끄기
     */
    public static String NEWSFEED_ALARM_OFF(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/alarm_off";
    }

    /**
     * 뉴스피드 알림켜기
     */
    public static String NEWSFEED_ALARM_ON(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/alarm_on";
    }

    /**
     * 뉴스피드 삭제하기
     */
    public static String NEWSFEED_DEL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/delete";
    }

    /**
     * 뉴스피드 좋아요
     */
    public static String NEWSFEED_LIKE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/like";
    }

    /**
     * 뉴스피드 글쓰기 수정하기
     */
    public static String NEWSFEED_MODIFY(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/modify";
    }

    /**
     * 뉴스피드 글쓰기
     */
    public static String NEWSFEED_WRITE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/newsfeed/write";
    }


    /**
     * 비밀번호 변경
     */
    public static String CHANGE_PWD(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/passwd";
    }

    /**
     * 휴대폰 번호 변경
     */
    public static String CHANGE_PHONE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/phone";
    }

    /**
     * 이벤트 리스트 보기
     */
    public static String EVENT_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/event/all";
    }

    /**
     * 이벤트 리스트 상세보기
     */
    public static String EVENT_DETAIL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/event/detail";
    }

    /**
     * 연예인 요청 리스트
     */
    public static String STAR_REQUEST_LIST(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/standby/all";
//		return "https://115.68.181.203/fanmaum1.0/index.php?/standby/all";
    }

    /**
     * 내 스타 추가 요청
     */
    public static String STAR_ADD(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/standby/add";
    }

    /**
     * 내 스타 추가 투표
     */
    public static String STAR_VOTE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/standby/vote";
    }

    /**
     * 내 스타 추가 투표 취소
     */
    public static String STAR_VOTE_CANCEL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/standby/vote_cancel";
    }


    /**
     * 연예인 추가(변경)
     */
    public static String STAR_STAR(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/star";
    }

    /**
     * 내 소식 보기
     */
    public static String MYNEWS_SEE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/news";
    }

    /**
     * 닉네임 체크
     */
    public static String NICKNAME_CHECK(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/check_nick";
    }


    /**
     * 연예인 선택.(우측 슬라이드메뉴)
     */
    public static String STAR_SELECT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/mystar";
    }

    /**
     * 연예인 수정(우측 슬라이드 메뉴)
     */
    public static String STAR_MODIFY(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account_jbink/star";
    }

    /**
     * 나의즐겨찾기 연예인
     */
    public static String MY_PAGE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/mypage";
    }

    /**
     * 프로필사진 변경
     */
    public static String CHANGE_PROFILE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/pic";
    }

    /**
     * 세션키 서버에 저장
     */
    public static String SSKEY_INSERT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/sskey_insert/ssk_add";
    }


    /**
     * 스케쥴 보기
     */
    public static String SCHDULE_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/schedule/all";
    }

    /**
     * 스케쥴 제보하기
     */
    public static String SCHDULE_INFORM(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/schedule/inform";
    }

    /**
     * 내소식편집
     */
    public static String NEWS_EDIT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/news_edit";
    }

    /**
     * 내소식편집전체
     */
    public static String NEWS_EDIT_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/news_edit_all";
    }

    /**
     * 공지사항
     */
    public static String NOTICE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/webview/notice";
    }

    /**
     * 잠금화면
     */
    public static String LOCKSCREEN(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/lockscreen";
    }

    /**
     * 적립내역
     */
    public static String LOCKSCREEN_HIT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/lockscreen/hit";
    }


    /**
     * 적립내역
     */
    public static String MY_POINT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/account/heart_log";
    }

    /**
     * 출석체크 삽입
     */
    public static String DAILY_INSERT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/daily_insert_jw/daily_add";
    }

    /**
     * 출석체크 검색
     */
    public static String DAILY_SELECT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/daily_insert_jw/daily_select";
    }

    /**
     * 출석체크 검색
     */
    public static String DAILY_TIME(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/daily_insert_jw/daily_time";
    }

    /**
     * 출석체크 업데이트
     */
    public static String DAILY_UPDATE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/daily_insert_jw/daily_update";
    }

    /**
     * 출석체크 업데이트
     */
    public static String DAILY_SAVE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/daily_insert_jw/addpoint";
    }

    /**
     * 가상계좌 얻어오기 전체
     */
    public static String PAY_ACCOUNT_SELECT_ALL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/payaccount_select";
    }

    /**
     * 가상계좌 얻어오기 하나
     */
    public static String PAY_ACCOUNT_SELECT_ONE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/payaccount_select_one";
    }

    /**
     * 마음 구매내역
     */
    public static String PAY_ACCOUNT_LOG(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/paylog_select";
    }

//	/**
//	 *구매내역 인앱 추가
//	 */
//	public static String PAY_ACCOUNT_INSERT(Context context){
//		return FanMindSetting.getBASE_URL(context)+"/pay_account_jw/paylog_add";
//	}

    public static String PAY_ACCOUNT_INSERT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/paylog_add_jw";
    }


//	/**
//	 *구매내역 reword에 추가
//	 */
//	public static String PAY_ACCOUNT_ADDPOINT(Context context){
//		return FanMindSetting.getBASE_URL(context)+"/pay_account_jw/addpoint";
//	}

    public static String PAY_ACCOUNT_ADDPOINT(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/addpoint_jw";
    }

    /**
     * 구매내역 취소
     */
    public static String PAY_ACCOUNT_DEL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/pay_account_jw/delete";
    }

    /**
     * 유투브 동영상 주소
     *
     * @param name 연예인 이름
     * @param idx  비디오 인덱스
     * @return
     */
    public static String getMovie(String name, String idx) {
        name = name.replace(" ", "");
        String url = "https://www.googleapis.com/youtube/v3/search?"
                + "part=snippet&maxResults=20&q=" + name
                + "&key=" + Utils.DEVELOPER_KEY + "&pageToken=" + idx;
        return url;
    }


    /**
     * 결제 호출  URL
     *
     * @param context
     * @param sort    결제종류
     * @param point   포인트
     * @return
     */
    public static String getPayURL(Context context, int sort, String point) {
        String url = null;
        if (sort == 0) {
            url = "https:///bill.fanmaum.com/billing/request.php?uid=" + FanMindSetting.getSESSION_KEY(context)
                    + "&point=" + point;
        } else if (sort == 1) {
            url = "https:///bill.fanmaum.com/billing/mc_request.php?uid=" + FanMindSetting.getSESSION_KEY(context)
                    + "&point=" + point;
        } else if (sort == 2) {
            url = "https:///bill.fanmaum.com/billing/cn_request.php?uid=" + FanMindSetting.getSESSION_KEY(context)
                    + "&point=" + point;
        }
        return url;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    버전 3.0.0 부터 바뀌는 api 아래로 정렬 시킴.
// ///////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 로그인
     */
    public static String LOGIN() {
        return NEW_SERVER + "/service/v1/member/signin";
    }

    /**
     * 네이버 로그인
     *
     * @return
     */
    public static String LOGIN_Naver() {
        return "https://openapi.naver.com/v1/nid/me";
    }


    /**
     * 페이스북북 로그인
     *
     * @return
     */
    public static String LOGIN_Facebook() {
//        return "https://graph.facebook.com/me";
        return "https://graph.facebook.com/bgolub";
    }

    /**
     * 회원가입요청
     */
    public static String LOGIN_JOIN() {
        return NEW_SERVER + "/service/v1/member/signup";
    }
    /**
     * 회원sns연동요청
     */
    public static String MEMBER_UPDATE_LINKAGE() {
        return NEW_SERVER + "/service/v1/member/update/linkage";
    }


    /**
     * 메인페이지
     */
    public static String MAIN_PAGE() {

        return NEW_SERVER + "/service/v1/member/me";


    }

    /**
     * home 페이지
     */
    public static String HOME_PROJECT() {
        return NEW_SERVER + "/service/v1/project/main";
    }


    /**
     * 좋아요 (3 :히스토리, 4:뉴스피드, 5:타임라인이미지, 0:댓글)
     */
    public static String LIKE(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/like";
    }

    /**
     * 좋아요 (3 :히스토리, 4:뉴스피드, 5:타임라인이미지, 0:댓글)
     */
    public static String LIKE_CANCEL(Context context) {
        return FanMindSetting.getBASE_URL(context) + "/like/cancel";
    }

    /**
     * 서포트 상세보기
     */
    public static String SUPPORT_LIST_DETAIL() {
        return NEW_SERVER + "/service/v1/project/get";
    }

    /**
     * project 좋아요
     */
    public static String PROJECT_LIKE() {
        return NEW_SERVER + "/service/v1/project/like";
    }

    /**
     * project 좋아요
     */
    public static String PROJECT_UNLIKE() {
        return NEW_SERVER + "/service/v1/project/unlike";
    }


    /**
     * project 관심갖기
     */
    public static String PROJECT_FEED() {
        return NEW_SERVER + "/service/v1/project/feed";
    }

    /**
     * project 관심갖기 취소
     */
    public static String PROJECT_UNFEED() {
        return NEW_SERVER + "/service/v1/project/unfeed";
    }


    /**
     * 친구 추가
     */
    public static String FOLLOWING() {
        return NEW_SERVER + "/service/v1/member/follow";
    }

    /**
     * 친구 삭제
     */
    public static String UNFOLLOWING() {
        return NEW_SERVER + "/service/v1/member/unfollow";
    }

    /**
     * ems 정보
     */
    public static String EMS() {
        return NEW_SERVER + "/service/v1/ems";
    }

    /**
     * 배송정보 (프로젝트 참여 검사)
     */
    public static String PROJECT_ATTEND_CHECK() {
        return NEW_SERVER + "/service/v1/project/attend/check";
    }


    /**
     * ems 정보
     */
    public static String NOTICE_ALL() {

        return NEW_SERVER + "/service/v1/project/notice/all";
    }

    /**
     * 닉네임 변경
     */
    public static String UPDATE_NICKNAME() {
        return NEW_SERVER + "/service/v1/member/update/nickname";
    }

    /**
     * 주소 변경
     */
    public static String UPDATE_ADDRESS() {
        return NEW_SERVER + "/service/v1/member/update/address";
    }


    /**
     * 추천인 입력
     */
    public static String RECOMMEND_USER() {
        return NEW_SERVER + "/service/v1/member/update/recommender";
    }

    /**
     * 함께하는 사람들
     */
    public static String ASSIST_USER() {
        return NEW_SERVER + "/service/v1/project/assists";
    }

    /**
     * 참여한 팬
     */
    public static String ATTENDS_FAN() {
        return NEW_SERVER + "/service/v1/project/attends";
    }

    /**
     * 댓글 목록
     */
    public static String REPLY_ALL() {
        return NEW_SERVER + "/service/v1/reply/all";
    }

    /**
     * 댓글 작성 (답글 포함)
     */
    public static String REPLY_WRITE() {
        return NEW_SERVER + "/service/v1/reply/write";
    }

    /**
     * 답글목록
     */
    public static String REPLY_REPLIES() {
        return NEW_SERVER + "/service/v1/reply/replies";
    }

    /**
     * 답글삭제
     */
    public static String REPLY_DELETE() {
        return NEW_SERVER + "/service/v1/reply/delete";
    }

    /**
     * 내 댓글 목록
     */
    public static String REPLY_MINE() {
        return NEW_SERVER + "/service/v1/reply/mine";
    }

    /**
     * 뉴스 피드 리스트
     */
    public static String NEWSFEED_ALL() {
        return NEW_SERVER + "/service/v1/newsfeed/all";
    }
    /**
     * 뉴스 피드 get
     */
    public static String NEWSFEED_GET() {
        return NEW_SERVER + "/service/v1/newsfeed/get";
    }

    /**
     * 뉴스 피드 인기 리스트
     */
    public static String NEWSFEED_TOP10() {
        return NEW_SERVER + "/service/v1/newsfeed/top10";
    }

    /**
     * 개설한 프로젝트 목록
     */
    public static String MEMBER_PROJECT_HOST() {
        return NEW_SERVER + "/service/v1/member/project/hosts";
    }

    /**
     * 타 회원 정보
     */
    public static String MEMBER_GET() {

        return NEW_SERVER + "/service/v1/member/get";
    }

    /**
     * ~를 좋아하는 친구
     */
    public static String MEMBER_FOLLOWERS() {
        return NEW_SERVER + "/service/v1/member/followers";
    }

    /**
     * ~가 좋아하는 친구
     */
    public static String MEMBER_FOLLOWINGS() {
        return NEW_SERVER + "/service/v1/member/followings";
    }

    /**
     * #27. 커뮤니티(스타) 목록
     */
    public static String STAR_ALL() {
        return NEW_SERVER + "/service/v1/star/all";
    }

    /**
     * #27. 프로젝트  목록
     */
    public static String PROJECT_ALL() {
        return NEW_SERVER + "/service/v1/project/all";
    }

    /**
     * #35.  프로젝트 참여하기
     */
    public static String PROJECT_ATTEND() {
        return NEW_SERVER + "/service/v1/project/attend";
    }

    /**
     * #35.  프로젝트 참여하기
     */
    public static String PROJECT_ATTENDS() {
        return NEW_SERVER + "/service/v1/project/attends";
    }


    /**
     * #35.  프로젝트 주문 정보 수정
     */
    public static String PROJECT_ATTEND_MODIFY() {
        return NEW_SERVER + "/service/v1/member/order/update";
    }

    /**
     * 다음 api 주소 가져오기
     */
    public static String GET_ADDRESS() {
        return "https://apis.daum.net/local/v1/search/keyword.json";
    }

    /**
     * #43. 유저 랭킹
     */
    public static String USER_RANKING() {
        return NEW_SERVER + "/service/v1/member/rankings";
    }

    /**
     * #44.  개설자 순위
     */
    public static String PROJECT_RANKING() {
        return NEW_SERVER + "/service/v1/project/host/rankings";
    }

    /**
     * #44.  참여한 프로젝트 목록
     */
    public static String MEMBER_PROJECT_ATTENDS() {
        return NEW_SERVER + "/service/v1/member/project/attends";
    }


    /**
     * #44.  참여한 프로젝트 목록
     */
    public static String PROJECT_ATTENDS_CANCEL() {
        return NEW_SERVER + "/service/v1/member/order/refund";
    }

    public static String PROJECT_DETAIL(String srl, int width) {
        return NEW_SERVER + "/projects/view/" + srl + "?access=app&appwidth=" + width;
    }


    public static String PROJECT_AFFTER_DETAIL(String srl, int width) {
        return NEW_SERVER + "/projects/review/" + srl + "?access=app&appwidth=" + width;
    }

    public static String NOTICE_DETAIL(String srl, String position) {
        return NEW_SERVER + "/projects/notices/" + srl + "/" + position + "?access=app";
    }

    public static String PROJECT_SHARE() {
        return NEW_SERVER + "/service/v1/project/share";
    }

    public static String EVENT_SHARE() {
        return NEW_SERVER + "service/v1/event/share";
    }


    /**
     * 이벤트 리스트 보기(웹이벤트)
     */
    public static String EVENT_LIST2() {
        return NEW_SERVER + "/service/v1/event/all";
    }

    /**
     * 이벤트 리스트 상세보기
     */
    public static String EVENT_DETAIL2() {
        return NEW_SERVER + "/service/v1/event/get";
    }


    public static String EVENT_DETAIL_EVENTS(String srl, int width) {
        return NEW_SERVER + "/events/view/" + srl + "?access=app&appwidth=" + width;
    }
    public static String EVENT_DETAIL_EVENTS_RESULT(String srl, int width) {
        return NEW_SERVER + "/events/result/" + srl + "?access=app&appwidth=" + width;
    }

    public static String REPLY_LIKE() {
        return NEW_SERVER + "/service/v1/reply/like";

    }

    public static String REPLY_UNLIKE() {
        return NEW_SERVER + "/service/v1/reply/unlike";

    }

    /**
     * 댓글 쓰기
     */
    public static String REPLY_WRITE2() {
        return NEW_SERVER + "/service/v1/reply/write";
    }

    /**
     * 댓글 삭제
     */
    public static String REPLY_DELETE2() {
        return NEW_SERVER + "/service/v1/reply/delete";
    }


    /**
     * 결제
     *
     * @return
     */
    public static String MEMBERS_PURCHASE() {
//           return NEW_SERVER;
        return NEW_SERVER + "/members/purchase/?access=app";
    }

    /**
     * 탈퇴
     *
     * @return
     */
    public static String MEMBERS_SIGNOUT() {
        return NEW_SERVER + "/service/v1/member/signout";
    }


    /**
     * 출석체크 삽입
     */
    public static String MEMBER_ATTEND() {
        return NEW_SERVER +"/service/v1/member/attend";
    }

    /**
       * 비밀번호 찾기
       */
      public static String LOGIN_FIND_PW2() {
          return NEW_SERVER +"/service/v1/member/findpassword";
      }
}