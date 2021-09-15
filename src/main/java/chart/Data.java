package chart;

/**
 * Created by pc on 2021/9/15.
 */
public class Data {
    private Integer year;

    private Integer[] month = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer[] getMonth() {
        return month;
    }

    public void setMonth(Integer[] month) {
        this.month = month;
    }
}
