package chart;

import java.util.List;

/**
 * Created by pc on 2021/9/15.
 */
public class Years {
    private List<MonthCounter> months;


    public List<MonthCounter> getMonths() {
        return months;
    }

    public void setMonths(List<MonthCounter> months) {
        this.months = months;
    }

    @Override
    public String toString() {
        return "Years{" +
                "months=" + months +
                '}';
    }
}
