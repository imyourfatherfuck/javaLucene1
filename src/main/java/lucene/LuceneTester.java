package lucene;

import chart.CreatLineChart;
import chart.Data;
import chart.Serie;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.jfree.chart.ChartPanel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class LuceneTester {

    String indexDir = "E:\\Lucene\\ectest\\Index";
    String dataDir = "E:\\Lucene\\ectest\\Data";
    String imgurl = "E:\\lucene\\折线图.png";
//    String imgurl1 = "E:\\lucene\\折线图1.png";
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
                        int month = ca.get(Calendar.MONTH) + 1;//第几个月

                        int year = ca.get(Calendar.YEAR);//年份数值
                        // 标识
                        boolean flag = false;
                        if (yearList.size() > 0) {
                            for (Data data : yearList) {
                                if (data.getYear() == year) {
                                    Integer[] months = data.getMonth();
                                    months[month - 1] += 1;
                                    data.setMonth(months);
                                    flag = true;
                                }
                            }
                            if (flag == false) {
                                addSet(year, month, yearList);
                            }
                        } else {
                            addSet(year, month, yearList);
                        }


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
            series.add(new Serie("Tokyo", new Integer[]{49, 71, 106, 129, 144, 176, 135, 148, 216, 194, 95, 54}));
            series.add(new Serie("New York", new Integer[]{83, 78, 98, 93, 106, 84, 105, 104, 91, 83, 106, 92}));
            if (yearList != null && yearList.size() > 0) {

                for (Data data : yearList) {
                    series.add(new Serie(data.getYear().toString(), data.getMonth()));

                }
            }


//            ChartPanel chartPanel = new CreatLineChart().createChart("", "", "", categorie, series);
//            frame.getContentPane().add(chartPanel);
//            frame.setVisible(true);
            //将图片保存为png格式
//            saveAsFile(chartPanel.getChart(),"D:\\1\\lol.png",900,500);
            creatLineChart.CreateNewLineChartForPng("折线图.png", "月份", "出现次数", imgurl, categorie, series, 1350, 750);

//            creatLineChart.CreateNewLineChartForPng1("折线图1.png", "月份", "出现次数", imgurl1, categorie, series, 1350, 750);
            System.out.println("生成图片成功");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public static void addSet(int year, int month, Set<Data> yearList) {
        Data data = new Data();
        data.setYear(year);
        Integer[] months = data.getMonth();
        months[month - 1] += 1;
        data.setMonth(months);
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
