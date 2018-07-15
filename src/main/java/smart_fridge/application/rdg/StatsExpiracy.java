package smart_fridge.application.rdg;

import java.math.BigDecimal;

public class StatsExpiracy {

    private Integer month;
    private String categoryName;
    private BigDecimal expCount;
    private BigDecimal ttlCount;
    private BigDecimal ratio;

    public Integer getMonth() {
        return month;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public BigDecimal getExpCount() {
        return expCount;
    }
    public void setExpCount(BigDecimal expCount) {
        this.expCount = expCount;
    }
    public BigDecimal getTtlCount() {
        return ttlCount;
    }
    public void setTtlCount(BigDecimal ttlCount) {
        this.ttlCount = ttlCount;
    }
    public BigDecimal getRatio() {
        return ratio;
    }
    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
}
