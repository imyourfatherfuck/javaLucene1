package lucene;

import chart.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LuceneTester {

    String indexDir = "E:\\Lucene\\ectest\\Index";
    String dataDir = "E:\\Lucene\\ectest\\Data";
    String baseImgurl = "E:\\lucene\\image\\";
    static String keyword = "CHANGYU";
    Indexer indexer;
    Searcher searcher;


    public static void main(String[] args) {
        LuceneTester tester;
        try {
            tester = new LuceneTester();
            tester.createIndex();
            tester.search(keyword);
        } catch (Exception e) {
        }
    }


    private void createIndex() throws IOException {
        indexer = new Indexer(indexDir);
        int numIndexed;
        numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
        indexer.close();
        System.out.println("共有文件" + numIndexed + "个");
    }

    private void search(String searchQuery) throws Exception {
        searcher = new Searcher(indexDir);
        TopDocs hits = searcher.search(searchQuery);

        System.out.println("查到到匹配索引文件" + hits.totalHits + "个");
        HashSet<Data> yearList = new HashSet<Data>();

        Map<String, Object> maps = new HashMap<String, Object>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = searcher.getDocument(scoreDoc);
            System.out.println("文件: " + doc.get(LuceneConstants.FILE_PATH));
            File file = new File(doc.get(LuceneConstants.FILE_PATH));
            String[] arr = txtString(file).split("\n");


//            int[] monthCount = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            if (arr.length > 0) {
                for (String str : arr) {
                    if (str.indexOf(keyword) != -1) {
                        System.out.println("索引句子：" + str);
                    }
                    if (isValidDate(str)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                        System.out.println("文本时间：" + str);
                        Date txtdate = format.parse(str);
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(txtdate);

                        int day = ca.get(Calendar.DATE); //天数

                        int month = ca.get(Calendar.MONTH) + 1;//第几个月

                        int year = ca.get(Calendar.YEAR);//年份数值
                        // 标识 生成总折线图
                        boolean flag = false;
                        if (yearList.size() > 0) {
                            for (Data data : yearList) {
                                if (data.getYear() == year) {
                                    Integer[] months = data.getMonths();
                                    months[month - 1] += 1;
                                    data.setMonths(months);
                                    flag = true;
                                }
                            }
                            if (flag == false) {
                                addSet(year, month, yearList);
                            }
                        } else {
                            addSet(year, month, yearList);
                        }


                        //根据月和天 生成折线图
                        Years year1 = addMap(year, maps);
                        year1.getMonths().get(month - 1).setCount(year1.getMonths().get(month - 1).getCount() + 1);
                        year1.getMonths().get(month - 1).getDays().get(day - 1).setCount(year1.getMonths().get(month - 1).getDays().get(day - 1).getCount() + 1);
                        maps.put(String.valueOf(year), year1);

                    }

                }
            }

        }


        System.out.println(yearList);
        searcher.close();


        CreatLineChart creatLineChart = new CreatLineChart();
        try {
            List<String> categorie = new ArrayList<String>();
            List<Serie> series = null;
            //横坐标
            String[] categories = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};
            for (String str : categories) {
                categorie.add(str);
            }
            series = new Vector<Serie>();
            // 柱子名称：柱子所有的值集合
            //纵坐标
//            series.add(new Serie("Tokyo", new Integer[]{49, 71, 106, 129, 144, 176, 135, 148, 216, 194, 95, 54}));
//            series.add(new Serie("New York", new Integer[]{83, 78, 98, 93, 106, 84, 105, 104, 91, 83, 106, 92}));
            if (yearList != null && yearList.size() > 0) {

                for (Data data : yearList) {
                    series.add(new Serie(data.getYear().toString() + "总图", data.getMonths()));

                }
            }


//            ChartPanel chartPanel = new CreatLineChart().createChart("", "", "", categorie, series);
//            frame.getContentPane().add(chartPanel);
//            frame.setVisible(true);
            //将图片保存为png格式
//            saveAsFile(chartPanel.getChart(),"D:\\1\\lol.png",900,500);
            creatLineChart.CreateNewLineChartForPng("折线图.png", "月份", "出现次数", baseImgurl + "折线图.png", categorie, series, 1350, 750);

//            creatLineChart.CreateNewLineChartForPng1("折线图1.png", "月份", "出现次数", imgurl1, categorie, series, 1350, 750);

            int num = 0;
            series = new Vector<Serie>();
            for (Object key : maps.keySet()) {
                List<Serie> dataList = new ArrayList<Serie>();
                num += 1;
                Years fromJson = JSON.parseObject(JSON.toJSONString(maps.get(String.valueOf(key))), Years.class);

                //12个月
                for (int i = 0; i < fromJson.getMonths().size(); i++) {
                    List<String> categorys = new ArrayList<String>();
                    Integer[] days = new Integer[fromJson.getMonths().get(i).getDays().size()];
                    //画图
                    for (int k = 0; k < fromJson.getMonths().get(i).getDays().size(); k++) {
                        categorys.add(fromJson.getMonths().get(i).getDays().get(k).getDay() + "日");
                        days[k] = fromJson.getMonths().get(i).getDays().get(k).getCount();
                    }

                    series.add(new Serie(String.valueOf(key), days));


                    if (num == maps.size()) {
                        for (int a = 0; a < num; a++) {
                            dataList.add(series.get(a * 12 + i));
                        }
                        String fileName = (i + 1) + "月" + "折线图.png";
                        creatLineChart.CreateNewLineChartForPng(fileName, (i + 1) + "月份", "出现次数", baseImgurl + fileName, categorys, dataList, 1350, 750);
                    }
                }


            }
            System.out.println("生成图片成功");
        } catch (
                Exception e1
                )

        {
            e1.printStackTrace();
        }

    }


    public static Years addMap(int year, Map<String, Object> maps) {


        if (maps.get(String.valueOf(year)) == null) {
            List<MonthCounter> monthCounterList = new ArrayList<MonthCounter>();


            Calendar c = Calendar.getInstance();

            Years years = new Years();
            for (int i = 0; i < 12; i++) {
                MonthCounter monthCounter = new MonthCounter();
                monthCounter.setCount(0);
                monthCounter.setMonth(i + 1);

                c.set(year, i, 1);
                int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                List<DayCounter> dayCounterList = new ArrayList<DayCounter>();
                for (int x = 1; x <= lastDay; x++) {

                    DayCounter dayCounter = new DayCounter();
                    dayCounter.setCount(0);
                    dayCounter.setDay(x);
                    dayCounterList.add(dayCounter);

                }
                monthCounter.setDays(dayCounterList);

                monthCounterList.add(monthCounter);
            }


            years.setMonths(monthCounterList);
            maps.put(String.valueOf(year), years);

        }


        Years fromJson = JSON.parseObject(JSON.toJSONString(maps.get(String.valueOf(year))), Years.class);
//        System.out.println(fromJson);
        return fromJson;
    }


    public static void addSet(int year, int month, Set<Data> yearList) {
        Data data = new Data();
        data.setYear(year);
        Integer[] months = data.getMonths();
        months[month - 1] += 1;
        data.setMonths(months);
        yearList.add(data);
    }

    public static String txtString(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result + "\n" + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isValidDate(String str) {
        boolean convertSuccess = true;
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            format.parse(str);
        } catch (Exception e) {
            // e.printStackTrace();
// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

}
