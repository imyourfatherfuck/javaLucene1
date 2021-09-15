package chart;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by pc on 2021/9/15.
 */
public class Data {
    private Integer year;

    private Integer[] months = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private Set<Integer> days = new HashSet<Integer>();

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer[] getMonths() {
        return months;
    }

    public void setMonths(Integer[] months) {
        this.months = months;
    }

    public Set<Integer> getDays() {
        return days;
    }

    public void setDays(Set<Integer> days) {
        this.days = days;
    }
}
