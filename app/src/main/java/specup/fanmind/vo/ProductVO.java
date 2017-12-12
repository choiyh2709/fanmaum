package specup.fanmind.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductVO implements Parcelable {

    public String srl;
    public String point;
    public String name;
    public String component;
    public String note;
    public String amount_max;
    public String amount_now;
    public String begin_time;
    public String close_time;
    public String hearts_min;

    public String ship_cost;
    public String is_ship_cost_extra;
    public String ship_cost_extra;
    public String ship_over_unit;
    public String ship_over_cost;
    public String is_ship_global;

    private String goodsMax;//상품수량
    private String goodsDelivery;//배송정보
    public String goods_ship_over_cost_;//over cost
    public String goods_total_cost;//total cost
    private String isSelected = "false";//선택 상품
    private String ship_weight;//선택 상품

    public String getGoods_total_cost() {
        return goods_total_cost;
    }

    public void setGoods_total_cost(String goods_total_cost) {
        this.goods_total_cost = goods_total_cost;
    }

    public String getGoods_ship_over_cost_() {
        return goods_ship_over_cost_;
    }

    public void setGoods_ship_over_cost_(String goods_ship_over_cost_) {
        this.goods_ship_over_cost_ = goods_ship_over_cost_;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getGoodsMax() {
        return goodsMax;
    }

    public void setGoodsMax(String goodsMax) {
        this.goodsMax = goodsMax;
    }

    public String getGoodsDelivery() {
        return goodsDelivery;
    }

    public void setGoodsDelivery(String goodsDelivery) {
        this.goodsDelivery = goodsDelivery;
    }

    public String getShip_cost() {
        return ship_cost;
    }

    public void setShip_cost(String ship_cost) {
        this.ship_cost = ship_cost;
    }

    public String getIs_ship_cost_extra() {
        return is_ship_cost_extra;
    }

    public void setIs_ship_cost_extra(String is_ship_cost_extra) {
        this.is_ship_cost_extra = is_ship_cost_extra;
    }

    public String getShip_cost_extra() {
        return ship_cost_extra;
    }

    public void setShip_cost_extra(String ship_cost_extra) {
        this.ship_cost_extra = ship_cost_extra;
    }

    public String getShip_over_unit() {
        return ship_over_unit;
    }

    public void setShip_over_unit(String ship_over_unit) {
        this.ship_over_unit = ship_over_unit;
    }

    public String getShip_over_cost() {
        return ship_over_cost;
    }

    public void setShip_over_cost(String ship_over_cost) {
        this.ship_over_cost = ship_over_cost;
    }

    public String getIs_ship_global() {
        return is_ship_global;
    }

    public void setIs_ship_global(String is_ship_global) {
        this.is_ship_global = is_ship_global;
    }

    public String getShip_weight() {
        return ship_weight;
    }

    public void setShip_weight(String ship_weight) {
        this.ship_weight = ship_weight;
    }

    public String getSrl() {
        return srl;
    }

    public void setSrl(String srl) {
        this.srl = srl;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount_max() {
        return amount_max;
    }

    public void setAmount_max(String amount_max) {
        this.amount_max = amount_max;
    }

    public String getAmount_now() {
        return amount_now;
    }

    public void setAmount_now(String amount_now) {
        this.amount_now = amount_now;
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

    public String getHearts_min(){
        return hearts_min;
    }

    public void setHearts_min(String hearts_min){
        this.hearts_min = hearts_min;
    }

    public ProductVO() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(srl);
        dest.writeString(point);
        dest.writeString(name);
        dest.writeString(component);
        dest.writeString(note);
        dest.writeString(amount_max);
        dest.writeString(amount_now);
        dest.writeString(begin_time);
        dest.writeString(close_time);

        dest.writeString(ship_cost);
        dest.writeString(is_ship_cost_extra);
        dest.writeString(ship_cost_extra);
        dest.writeString(ship_over_unit);
        dest.writeString(ship_over_cost);
        dest.writeString(is_ship_global);

        dest.writeString(goodsMax);
        dest.writeString(goodsDelivery);
        dest.writeString(goods_ship_over_cost_);
        dest.writeString(goods_total_cost);
        dest.writeString(isSelected);
        dest.writeString(ship_weight);
    }

    public void readFromParcel(Parcel in) {
        srl = in.readString();
        point = in.readString();
        name = in.readString();
        component = in.readString();
        note = in.readString();
        amount_max = in.readString();
        amount_now = in.readString();
        begin_time = in.readString();
        close_time = in.readString();

        ship_cost = in.readString();
        is_ship_cost_extra = in.readString();
        ship_cost_extra = in.readString();
        ship_over_unit = in.readString();
        ship_over_cost = in.readString();
        is_ship_global = in.readString();

        goodsMax = in.readString();
        goodsDelivery = in.readString();
        goods_ship_over_cost_ = in.readString();
        goods_total_cost = in.readString();
        isSelected = in.readString();
        ship_weight = in.readString();
    }

    public ProductVO(Parcel in) {
        readFromParcel(in);
    }

    @SuppressWarnings("rawtypes")
    public static final Creator CREATOR = new Creator() {

        @Override
        public ProductVO createFromParcel(Parcel in) {
            return new ProductVO(in);
        }

        @Override
        public ProductVO[] newArray(int size) {
            return new ProductVO[size];
        }

    };

    @Override
    public String toString() {
        return "ProductVO{" +
                "srl='" + srl + '\'' +
                ", point='" + point + '\'' +
                ", name='" + name + '\'' +
                ", component='" + component + '\'' +
                ", note='" + note + '\'' +
                ", amount_max='" + amount_max + '\'' +
                ", amount_now='" + amount_now + '\'' +
                ", begin_time='" + begin_time + '\'' +
                ", close_time='" + close_time + '\'' +
                ", ship_cost='" + ship_cost + '\'' +
                ", is_ship_cost_extra='" + is_ship_cost_extra + '\'' +
                ", ship_cost_extra='" + ship_cost_extra + '\'' +
                ", ship_over_unit='" + ship_over_unit + '\'' +
                ", ship_over_cost='" + ship_over_cost + '\'' +
                ", is_ship_global='" + is_ship_global + '\'' +
                ", goodsMax='" + goodsMax + '\'' +
                ", goodsDelivery='" + goodsDelivery + '\'' +
                ", goods_ship_over_cost_='" + goods_ship_over_cost_ + '\'' +
                ", goods_total_cost='" + goods_total_cost + '\'' +
                ", isSelected='" + isSelected + '\'' +
                ", ship_weight='" + ship_weight + '\'' +
                '}';
    }
}
