package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class NoticeVO implements Parcelable {

    public String data;
    public String code;
    public String message;
    public String total;
    public String[] items;
    public String next_srl;
    public String notice_srl;
    public String subject;
    public String written_time;
    public String mText;
    public String mWarning;
    public String mSendTime;
    public String mStarName;
    public String mStarSrl;
    public String mEvent_srl;
    public String mEvent_end_date;
    public String contents;
    public String host;
    public String member_srl;
    public String name;
    public String profile;
    public String profile_base;
    public String website;
    public String relation;


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String[] getItems() {
        return items;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    public String getNext_srl() {
        return next_srl;
    }

    public void setNext_srl(String next_srl) {
        this.next_srl = next_srl;
    }

    public String getNotice_srl() {
        return notice_srl;
    }

    public void setNotice_srl(String notice_srl) {
        this.notice_srl = notice_srl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getWritten_time() {
        return written_time;
    }

    public void setWritten_time(String written_time) {
        this.written_time = written_time;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public String getmWarning() {
        return mWarning;
    }

    public void setmWarning(String mWarning) {
        this.mWarning = mWarning;
    }

    public String getmSendTime() {
        return mSendTime;
    }

    public void setmSendTime(String mSendTime) {
        this.mSendTime = mSendTime;
    }

    public String getmStarName() {
        return mStarName;
    }

    public void setmStarName(String mStarName) {
        this.mStarName = mStarName;
    }

    public String getmStarSrl() {
        return mStarSrl;
    }

    public void setmStarSrl(String mStarSrl) {
        this.mStarSrl = mStarSrl;
    }

    public String getmEvent_srl() {
        return mEvent_srl;
    }

    public void setmEvent_srl(String mEvent_srl) {
        this.mEvent_srl = mEvent_srl;
    }

    public String getmEvent_end_date() {
        return mEvent_end_date;
    }

    public void setmEvent_end_date(String mEvent_end_date) {
        this.mEvent_end_date = mEvent_end_date;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getMember_srl() {
        return member_srl;
    }

    public void setMember_srl(String member_srl) {
        this.member_srl = member_srl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile_base() {
        return profile_base;
    }

    public void setProfile_base(String profile_base) {
        this.profile_base = profile_base;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public NoticeVO(String data, String code, String message, String total, String[] items, String next_srl, String notice_srl, String subject, String written_time, String mText, String mWarning, String mSendTime, String mStarName, String mStarSrl, String mEvent_srl, String mEvent_end_date, String contents, String host, String member_srl, String name, String profile, String profile_base, String website, String relation) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.total = total;
        this.items = items;
        this.next_srl = next_srl;
        this.notice_srl = notice_srl;
        this.subject = subject;
        this.written_time = written_time;
        this.mText = mText;
        this.mWarning = mWarning;
        this.mSendTime = mSendTime;
        this.mStarName = mStarName;
        this.mStarSrl = mStarSrl;
        this.mEvent_srl = mEvent_srl;
        this.mEvent_end_date = mEvent_end_date;
        this.contents = contents;
        this.host = host;
        this.member_srl = member_srl;
        this.name = name;
        this.profile = profile;
        this.profile_base = profile_base;
        this.website = website;
        this.relation = relation;
    }

    private NoticeVO(Parcel source) {
        this.data = source.readString();
        this.code = source.readString();
        this.message = source.readString();
        this.total = source.readString();
        source.readStringArray(this.items);
        this.next_srl = source.readString();
        this.notice_srl = source.readString();
        this.subject = source.readString();
        this.written_time = source.readString();
        this.mText = source.readString();
        this.mWarning = source.readString();
        this.mSendTime = source.readString();
        this.mStarName = source.readString();
        this.mStarSrl = source.readString();
        this.mEvent_srl = source.readString();
        this.mEvent_end_date = source.readString();
        this.contents = source.readString();
        this.host = source.readString();
        this.member_srl = source.readString();
        this.name = source.readString();
        this.profile = source.readString();
        this.profile_base = source.readString();
        this.website = source.readString();
        this.relation = source.readString();


    }

    public NoticeVO() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(data);
        dest.writeString(code);
        dest.writeString(message);
        dest.writeString(total);
        dest.writeStringArray(items);
        dest.writeString(next_srl);
        dest.writeString(notice_srl);
        dest.writeString(subject);
        dest.writeString(written_time);
        dest.writeString(mText);
        dest.writeString(mWarning);
        dest.writeString(mSendTime);
        dest.writeString(mStarName);
        dest.writeString(mStarSrl);
        dest.writeString(mEvent_srl);
        dest.writeString(mEvent_end_date);
        dest.writeString(contents);
        dest.writeString(host);
        dest.writeString(member_srl);
        dest.writeString(name);
        dest.writeString(profile);
        dest.writeString(profile_base);
        dest.writeString(website);
        dest.writeString(relation);
    }

    public void readFromParcel(Parcel in) {

        this.data = in.readString();
        this.code = in.readString();
        this.message = in.readString();
        this.total = in.readString();
        in.readStringArray(this.items);
        this.next_srl = in.readString();
        this.notice_srl = in.readString();
        this.subject = in.readString();
        this.written_time = in.readString();
        this.mText = in.readString();
        this.mWarning = in.readString();
        this.mSendTime = in.readString();
        this.mStarName = in.readString();
        this.mStarSrl = in.readString();
        this.mEvent_srl = in.readString();
        this.mEvent_end_date = in.readString();
        this.contents = in.readString();
        this.host = in.readString();
        this.member_srl = in.readString();
        this.name = in.readString();
        this.profile = in.readString();
        this.profile_base = in.readString();
        this.website = in.readString();
        this.relation = in.readString();

    }

    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {

        @Override
        public NoticeVO createFromParcel(Parcel in) {
            return new NoticeVO(in);
        }

        @Override
        public NoticeVO[] newArray(int size) {
            // TODO Auto-generated method stub
            return new NoticeVO[size];
        }

    };


    @Override
    public String toString() {
        return "NoticeVO{" +
                "data='" + data + '\'' +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", total='" + total + '\'' +
                ", items=" + Arrays.toString(items) +
                ", next_srl='" + next_srl + '\'' +
                ", notice_srl='" + notice_srl + '\'' +
                ", subject='" + subject + '\'' +
                ", written_time='" + written_time + '\'' +
                ", mText='" + mText + '\'' +
                ", mWarning='" + mWarning + '\'' +
                ", mSendTime='" + mSendTime + '\'' +
                ", mStarName='" + mStarName + '\'' +
                ", mStarSrl='" + mStarSrl + '\'' +
                ", mEvent_srl='" + mEvent_srl + '\'' +
                ", mEvent_end_date='" + mEvent_end_date + '\'' +
                ", contents='" + contents + '\'' +
                ", host='" + host + '\'' +
                ", member_srl='" + member_srl + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                ", profile_base='" + profile_base + '\'' +
                ", website='" + website + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
