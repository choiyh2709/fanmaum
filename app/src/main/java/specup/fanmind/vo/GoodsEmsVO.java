package specup.fanmind.vo;

/**
 * Created by DEV-06 on 2016-05-18.
 */
public class GoodsEmsVO {
    private String weight;
    private String region;
    private String cost;
    private String cost_won;

    public String getCost_won() {
        return cost_won;
    }

    public void setCost_won(String cost_won) {
        this.cost_won = cost_won;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "GoodsEmsVO{" +
                "weight='" + weight + '\'' +
                ", region='" + region + '\'' +
                ", cost='" + cost + '\'' +
                ", cost_won='" + cost_won + '\'' +
                '}';
    }
}
