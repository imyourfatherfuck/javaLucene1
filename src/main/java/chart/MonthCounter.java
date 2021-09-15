package chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2021/9/15.
 */
public class MonthCounter {

    private Integer count;

    private Integer month;

    private List<DayCounter> days = new ArrayList<DayCounter>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public List<DayCounter> getDays() {
        return days;
    }

    public void setDays(List<DayCounter> days) {
        this.days = days;
    }

    @Override
    public String toString() {
        return "MonthCounter{" +
                "count=" + count +
                ", month=" + month +
                ", days=" + days +
                '}';
    }
}
