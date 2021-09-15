package chart;

import com.alibaba.fastjson.JSON;

import java.util.*;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by pc on 2021/9/15.
 */
public class Test {
    public static void main(String[] args) {
//        int year = 2016;
//        Map<String, Object> maps = new HashMap<String, Object>();
//        List<MonthCounter> monthCounterList = new ArrayList<MonthCounter>();
//
//
//        Calendar c = Calendar.getInstance();
//
//        Years years = new Years();
//        for (int i = 0; i < 12; i++) {
//            MonthCounter monthCounter = new MonthCounter();
//            monthCounter.setCount(0);
//            monthCounter.setMonth(i + 1);
//
//            c.set(year, i, 1);
//            int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
//            List<DayCounter> dayCounterList = new ArrayList<DayCounter>();
//            for (int x = 1; x <= lastDay; x++) {
//
//                DayCounter dayCounter = new DayCounter();
//                dayCounter.setCount(0);
//                dayCounter.setDay(x);
//                dayCounterList.add(dayCounter);
//
//            }
//            monthCounter.setDays(dayCounterList);
//
//            monthCounterList.add(monthCounter);
//        }
//
//
//        years.setMonths(monthCounterList);
//        maps.put(String.valueOf(year), years);
//
//
//        Years fromJson = JSON.parseObject(JSON.toJSONString(maps.get("2016")), Years.class);
//        System.out.println(fromJson);

        List<Integer> list = new ArrayList<Integer>();

        int num = 0;
        for (int i = 0; i < 2; i++) {
            num += 1;

            for (int k = 0; k < 12; k++) {
                list.add(111);
            }
        }
        System.out.println(1);

    }

}
