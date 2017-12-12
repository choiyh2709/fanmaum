package specup.fanmind.vo;

/**
 * 프로젝트 st
 * Created by DEV-06 on 2016-04-21.
 */
public class ProjectStackVO {
    private String srl;
    private boolean review = false;
    private String isHistory;

    public String getSrl() {
        return srl;
    }

    public void setSrl(String srl) {
        this.srl = srl;
    }

    public boolean isReview() {
        return review;
    }

    public void setReview(boolean review) {
        this.review = review;
    }

    public String getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(String isHistory) {
        this.isHistory = isHistory;
    }
}
