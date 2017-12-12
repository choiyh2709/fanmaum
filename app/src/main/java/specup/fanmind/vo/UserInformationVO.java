package specup.fanmind.vo;

public class UserInformationVO {

    public String member_srl;
    public String nick;
    public String pic;
    public String pic_base;
    public String heart;
    public String time;


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

    public String getHeart() {
        return heart;
    }

    public void setHeart(String heart) {
        this.heart = heart;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
