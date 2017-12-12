package specup.fanmind.vo;

import android.widget.LinearLayout;

/**
 * Created by DEV-06 on 2016-05-10.
 */
public class SelectProductVO {

    private LinearLayout hidenlayout;
    private Integer itemIndex;//리스트에서의 상품index
    private String goodsMax;//상품수량
    private String goodsDelivery;//배송정보
    private String product_srl;// 상품 고유번호
    private String ship_cost;
    private String is_ship_cost_extra;
    private String ship_cost_extra;


    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
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

    public String getGoodsMax() {
        return goodsMax;
    }

    public void setGoodsMax(String goodsMax) {
        this.goodsMax = goodsMax;
    }

    public String getProduct_srl() {
        return product_srl;
    }

    public void setProduct_srl(String product_srl) {
        this.product_srl = product_srl;
    }

    public LinearLayout getHidenlayout() {
        return hidenlayout;
    }

    public void setHidenlayout(LinearLayout hidenlayout) {
        this.hidenlayout = hidenlayout;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }


    @Override
    public String toString() {
        return "SelectProductVO{" +
                "hidenlayout=" + hidenlayout +
                ", itemIndex=" + itemIndex +
                ", goodsMax='" + goodsMax + '\'' +
                ", product_srl='" + product_srl + '\'' +
                ", ship_cost='" + ship_cost + '\'' +
                ", is_ship_cost_extra='" + is_ship_cost_extra + '\'' +
                ", ship_cost_extra='" + ship_cost_extra + '\'' +
                '}';
    }
}
