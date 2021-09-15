package chart;

/**
 * Created by pc on 2021/9/15.
 */
public class DayCounter {
    private Integer count;

    private Integer day;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "DayCounter{" +
                "count=" + count +
                ", day=" + day +
                '}';
    }
}
