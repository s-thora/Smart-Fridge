package smart_fridge.application.rdg;

import java.math.BigDecimal;

public class StatsExpenses {
    private Integer month;
    private Integer year;
    private BigDecimal sum;

    public Integer getMonth() {
        return month;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public BigDecimal getSum() {
        return sum;
    }
    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
