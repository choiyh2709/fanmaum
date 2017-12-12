package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DEV-06 on 2016-04-21.
 */
public class EventVO implements Parcelable {
    private String event_srl;
    private String star_srl;
    private String project_srl;
    private String thumbnail;
    private String thumbnail_base;
    private String title;
    private String present;
    private String begin_time;
    private String close_time;

    public String getEvent_srl() {
        return event_srl;
    }

    public void setEvent_srl(String event_srl) {
        this.event_srl = event_srl;
    }

    public String getStar_srl() {
        return star_srl;
    }

    public void setStar_srl(String star_srl) {
        this.star_srl = star_srl;
    }

    public String getProject_srl() {
        return project_srl;
    }

    public void setProject_srl(String project_srl) {
        this.project_srl = project_srl;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPresent() {
        return present;
    }

    public void setPresent(String present) {
        this.present = present;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(event_srl);
        dest.writeString(star_srl);
        dest.writeString(project_srl);
        dest.writeString(thumbnail);
        dest.writeString(thumbnail_base);
        dest.writeString(title);
        dest.writeString(present);
        dest.writeString(begin_time);
        dest.writeString(close_time);

    }

    public EventVO() {
    }

    public EventVO(Parcel in) {

        event_srl = in.readString();
        star_srl= in.readString();
        project_srl= in.readString();
        thumbnail= in.readString();
        thumbnail_base= in.readString();
        title= in.readString();
        present= in.readString();
        begin_time= in.readString();
        close_time= in.readString();


    }

    public static final Parcelable.Creator<EventVO> CREATOR = new Parcelable.Creator<EventVO>() {
        @Override
        public EventVO createFromParcel(Parcel in) {
            return new EventVO(in);
        }

        @Override
        public EventVO[] newArray(int size) {
            return new EventVO[size];
        }
    };

}
