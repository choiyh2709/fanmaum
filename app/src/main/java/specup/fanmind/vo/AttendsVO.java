package specup.fanmind.vo;

/**
 * Created by DEV-06 on 2016-05-03.
 */
public class AttendsVO {
    private String member_srl;
    private String nick;
    private String pic;
    private String pic_base;

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
}