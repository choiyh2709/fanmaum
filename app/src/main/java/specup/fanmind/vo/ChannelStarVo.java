package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DEV-06 on 2016-04-18.
 */
public class ChannelStarVo implements Parcelable {
    private String star_srl;
    private String name;
    private String icon;
    private String icon_base;
    private String fan_cnt;
    private String is_added;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_base() {
        return icon_base;
    }

    public void setIcon_base(String icon_base) {
        this.icon_base = icon_base;
    }

    public String getFan_cnt() {
        return fan_cnt;
    }

    public void setFan_cnt(String fan_cnt) {
        this.fan_cnt = fan_cnt;
    }

    public String getIs_added() {
        return is_added;
    }

    public void setIs_added(String is_added) {
        this.is_added = is_added;
    }

    public ChannelStarVo() {
    }

    public ChannelStarVo(Parcel in) {
        readFromParcel(in);
    }


    public static final Creator<ChannelStarVo> CREATOR = new Creator<ChannelStarVo>(){
           public ChannelStarVo createFromParcel(Parcel in) {
                return new ChannelStarVo(in);
          }

          public ChannelStarVo[] newArray(int size) {
               return new ChannelStarVo[size];
          }
      };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(star_srl);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(icon_base);
        dest.writeString(fan_cnt);
        dest.writeString(is_added);


    }

    private void readFromParcel(Parcel in) {

        star_srl = in.readString();
        name = in.readString();
        icon = in.readString();
        icon_base = in.readString();
        fan_cnt = in.readString();
        is_added = in.readString();


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelStarVo that = (ChannelStarVo) o;

        return star_srl != null ? star_srl.equals(that.star_srl) : that.star_srl == null;

    }

    @Override
    public int hashCode() {
        return star_srl != null ? star_srl.hashCode() : 0;
    }
}
