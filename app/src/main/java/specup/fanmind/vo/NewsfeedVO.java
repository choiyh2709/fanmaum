package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DEV-06 on 2016-04-25.
 */
public class NewsfeedVO implements Parcelable {
    private String newsfeed_srl;
    private String title;
    private String text;
    private String likes;
    private String reply_cnt;
    private String time;
    private String alarm;
    private String member_srl;
    private String nick;
    private String pic;
    private String pic_base;
    private String star_srl;
    private String name;
    private String is_liked;
    private String images[];


    public String getNewsfeed_srl() {
        return newsfeed_srl;
    }

    public void setNewsfeed_srl(String newsfeed_srl) {
        this.newsfeed_srl = newsfeed_srl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getReply_cnt() {
        return reply_cnt;
    }

    public void setReply_cnt(String reply_cnt) {
        this.reply_cnt = reply_cnt;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getMember_srl() {
        return member_srl;
    }

    public void setMember_srl(String member_srl) {
        this.member_srl = member_srl;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic_base() {
        return pic_base;
    }

    public void setPic_base(String pic_base) {
        this.pic_base = pic_base;
    }

    public String getStar_srl() {
        return star_srl;
    }

    public void setStar_srl(String star_srl) {
        this.star_srl = star_srl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(newsfeed_srl);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(likes);
        dest.writeString(reply_cnt);
        dest.writeString(time);
        dest.writeString(alarm);
        dest.writeString(member_srl);
        dest.writeString(nick);
        dest.writeString(pic);
        dest.writeString(pic_base);
        dest.writeString(star_srl);
        dest.writeString(name);
        dest.writeString(is_liked);
        dest.writeStringArray(images);
    }

    public NewsfeedVO() {

    }


    protected NewsfeedVO(Parcel in) {
        newsfeed_srl = in.readString();
        title = in.readString();
        text = in.readString();
        likes = in.readString();
        reply_cnt = in.readString();
        time = in.readString();
        alarm = in.readString();
        member_srl = in.readString();
        nick = in.readString();
        pic = in.readString();
        pic_base = in.readString();
        star_srl = in.readString();
        name = in.readString();
        is_liked = in.readString();
        images = in.createStringArray();
    }

    public static final Creator<NewsfeedVO> CREATOR = new Creator<NewsfeedVO>() {
        @Override
        public NewsfeedVO createFromParcel(Parcel in) {
            return new NewsfeedVO(in);
        }

        @Override
        public NewsfeedVO[] newArray(int size) {
            return new NewsfeedVO[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsfeedVO that = (NewsfeedVO) o;

        return newsfeed_srl != null ? newsfeed_srl.equals(that.newsfeed_srl) : that.newsfeed_srl == null;

    }

    @Override
    public int hashCode() {
        return newsfeed_srl != null ? newsfeed_srl.hashCode() : 0;
    }
}
