package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DEV-06 on 2016-04-19.
 */
public class ProjectVO implements Parcelable {

    private String project_srl;
    private String title;
    private String slogun;
    private String thumbnail;
    private String thumbnail_base;
    private String begin_time;
    private String close_time;
    private String heart_goal;
    private String heart_now;
    private String total_attends;
    private String review_srl;
    private String star_srl;
    private String star_name;
    private String host;
    private String isHistory;

    private String payment_user_point;
    private String payment_is_paid;
    private String payment_address_pri= "";
    private String payment_address_ext= "";
    private String payment_time;
    private String payment_name = "";
    private String payment_order_srl;
    private String payment_mobile= "";
    private String payment_email= "";
    private String payment_is_extra;
    private String payment_is_global;
    private String payment_note= "";
    private String payment_note_extra= "";

    public ProjectVO() {

    }


    public String getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(String isHistory) {
        this.isHistory = isHistory;
    }

    public String getPayment_is_global() {
        return payment_is_global;
    }

    public void setPayment_is_global(String payment_is_global) {
        this.payment_is_global = payment_is_global;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getPayment_order_srl() {
        return payment_order_srl;
    }

    public void setPayment_order_srl(String payment_order_srl) {
        this.payment_order_srl = payment_order_srl;
    }

    public String getPayment_mobile() {
        return payment_mobile;
    }

    public void setPayment_mobile(String payment_mobile) {
        this.payment_mobile = payment_mobile;
    }

    public String getPayment_email() {
        return payment_email;
    }

    public void setPayment_email(String payment_email) {
        this.payment_email = payment_email;
    }

    public String getPayment_is_extra() {
        return payment_is_extra;
    }

    public void setPayment_is_extra(String payment_is_extra) {
        this.payment_is_extra = payment_is_extra;
    }

    public String getPayment_note() {
        return payment_note;
    }

    public void setPayment_note(String payment_note) {
        this.payment_note = payment_note;
    }

    public String getPayment_note_extra() {
        return payment_note_extra;
    }

    public void setPayment_note_extra(String payment_note_extra) {
        this.payment_note_extra = payment_note_extra;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getPayment_user_point() {
        return payment_user_point;
    }

    public void setPayment_user_point(String payment_user_point) {
        this.payment_user_point = payment_user_point;
    }

    public String getPayment_is_paid() {
        return payment_is_paid;
    }

    public void setPayment_is_paid(String payment_is_paid) {
        this.payment_is_paid = payment_is_paid;
    }

    public String getPayment_address_pri() {
        return payment_address_pri;
    }

    public void setPayment_address_pri(String payment_address_pri) {
        this.payment_address_pri = payment_address_pri;
    }

    public String getPayment_address_ext() {
        return payment_address_ext;
    }

    public void setPayment_address_ext(String payment_address_ext) {
        this.payment_address_ext = payment_address_ext;
    }

    public String getStar_name() {
        return star_name;
    }

    public void setStar_name(String star_name) {
        this.star_name = star_name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProject_srl() {
        return project_srl;
    }

    public void setProject_srl(String project_srl) {
        this.project_srl = project_srl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlogun() {
        return slogun;
    }

    public void setSlogun(String slogun) {
        this.slogun = slogun;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail_base() {
        return thumbnail_base;
    }

    public void setThumbnail_base(String thumbnail_base) {
        this.thumbnail_base = thumbnail_base;
    }

    public String getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getClose_time() {
        return close_time;
    }

    public void setClose_time(String close_time) {
        this.close_time = close_time;
    }

    public String getHeart_goal() {
        return heart_goal;
    }

    public void setHeart_goal(String heart_goal) {
        this.heart_goal = heart_goal;
    }

    public String getHeart_now() {
        return heart_now;
    }

    public void setHeart_now(String heart_now) {
        this.heart_now = heart_now;
    }

    public String getTotal_attends() {
        return total_attends;
    }

    public void setTotal_attends(String total_attends) {
        this.total_attends = total_attends;
    }

    public String getReview_srl() {
        return review_srl;
    }

    public void setReview_srl(String review_srl) {
        this.review_srl = review_srl;
    }

    public String getStar_srl() {
        return star_srl;
    }

    public void setStar_srl(String star_srl) {
        this.star_srl = star_srl;
    }


    public ProjectVO(Parcel in) {
        readFromParcel(in);
    }

    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {

        @Override
        public ProjectVO createFromParcel(Parcel in) {
            return new ProjectVO(in);
        }

        @Override
        public ProjectVO[] newArray(int size) {
            return new ProjectVO[size];
        }

    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(project_srl);
        dest.writeString(title);
        dest.writeString(slogun);
        dest.writeString(thumbnail);
        dest.writeString(thumbnail_base);
        dest.writeString(begin_time);
        dest.writeString(close_time);
        dest.writeString(heart_goal);
        dest.writeString(heart_now);
        dest.writeString(total_attends);
        dest.writeString(review_srl);
        dest.writeString(star_srl);
        dest.writeString(star_name);
        dest.writeString(host);
        dest.writeString(isHistory);

        dest.writeString(payment_user_point);
        dest.writeString(payment_is_paid);
        dest.writeString(payment_address_pri);
        dest.writeString(payment_address_ext);
        dest.writeString(payment_time);
        dest.writeString(payment_name);
        dest.writeString(payment_order_srl);
        dest.writeString(payment_mobile);
        dest.writeString(payment_email);
        dest.writeString(payment_is_global);
        dest.writeString(payment_is_extra);
        dest.writeString(payment_note);
        dest.writeString(payment_note_extra);
    }

    public void readFromParcel(Parcel in) {

        project_srl = in.readString();
        title = in.readString();
        slogun = in.readString();
        thumbnail = in.readString();
        thumbnail_base = in.readString();
        begin_time = in.readString();
        close_time = in.readString();
        heart_goal = in.readString();
        heart_now = in.readString();
        total_attends = in.readString();
        review_srl = in.readString();
        star_srl = in.readString();
        star_name = in.readString();
        host = in.readString();
        isHistory = in.readString();

        payment_user_point = in.readString();
        payment_is_paid = in.readString();
        payment_address_pri = in.readString();
        payment_address_ext = in.readString();
        payment_time = in.readString();
        payment_name = in.readString();
        payment_order_srl = in.readString();
        payment_mobile = in.readString();
        payment_email = in.readString();
        payment_is_extra = in.readString();
        payment_is_global = in.readString();
        payment_note = in.readString();
        payment_note_extra = in.readString();
    }


}
