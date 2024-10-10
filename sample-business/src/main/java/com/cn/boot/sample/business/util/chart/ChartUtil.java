package com.cn.boot.sample.business.util.chart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;

/**
 * @author Chen Nan
 */
@Slf4j
public class ChartUtil {

    /**
     * 创建饼图
     */
    @SneakyThrows
    public static void createPieChart() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("技术部", 200);
        dataset.setValue("人事部", 15);
        dataset.setValue("销售部", 50);

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 20));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.BOLD, 15));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.BOLD, 15));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createPieChart("部门人数分布", dataset, true, false, false);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/pieChart.png"), chart, 400, 300);
    }

    /**
     * 创建折线图
     */
    @SneakyThrows
    public static void createLineChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(10, "技术部", "2021年");
        dataset.setValue(30, "技术部", "2022年");
        dataset.setValue(55, "技术部", "2023年");
        dataset.setValue(200, "技术部", "2024年");
        dataset.setValue(1, "人事部", "2021年");
        dataset.setValue(5, "人事部", "2022年");
        dataset.setValue(10, "人事部", "2023年");
        dataset.setValue(15, "人事部", "2024年");
        dataset.setValue(10, "销售部", "2021年");
        dataset.setValue(20, "销售部", "2022年");
        dataset.setValue(35, "销售部", "2023年");
        dataset.setValue(50, "销售部", "2024年");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 13));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.BOLD, 10));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createLineChart(null, null, null, dataset);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);  // 设置绘图区背景颜色为白色

        // 自定义标题
        TextTitle customTitle = new TextTitle("部门人数曲线", new Font("微软雅黑", Font.BOLD, 13));
        customTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 减少标题周围的边距，使其更靠近左侧
        customTitle.setMargin(new RectangleInsets(5, 5, 5, 5)); // 可以根据需要调整这些值
        chart.setTitle(customTitle);

        // 网格线
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 边框
        plot.setOutlineVisible(false);

        // 获取X轴和Y轴
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLineVisible(false);
        domainAxis.setTickMarksVisible(false);
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(false);
        rangeAxis.setTickMarksVisible(false);

        // 设置图例位置
        LegendTitle legend = chart.getLegend();
        legend.setPadding(0, 300, 0, 0);
        legend.setPosition(RectangleEdge.TOP);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/lineChart.png"), chart, 500, 200);
    }

    /**
     * 创建折线图
     */
    @SneakyThrows
    public static void createXYLineChart() {
        // 创建数据集
        XYSeries series1 = new XYSeries("Bug数");
        series1.add(1.0, 1.0);
        series1.add(2.0, 4.0);
        series1.add(3.0, 3.0);
        series1.add(4.0, 5.0);
        XYSeries series2 = new XYSeries("漏洞数");
        series2.add(1.0, 4.0);
        series2.add(2.0, 2.0);
        series2.add(3.0, 3.0);
        series2.add(4.0, 1.0);
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 13));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.BOLD, 10));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createXYLineChart(null, null, null, dataset);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/xyLineChart.png"), chart, 500, 200);
    }

    /**
     * 创建条形图
     */
    @SneakyThrows
    public static void createBarChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(10, "技术部", "2021年");
        dataset.setValue(30, "技术部", "2022年");
        dataset.setValue(55, "技术部", "2023年");
        dataset.setValue(200, "技术部", "2024年");
        dataset.setValue(1, "人事部", "2021年");
        dataset.setValue(5, "人事部", "2022年");
        dataset.setValue(10, "人事部", "2023年");
        dataset.setValue(15, "人事部", "2024年");
        dataset.setValue(10, "销售部", "2021年");
        dataset.setValue(20, "销售部", "2022年");
        dataset.setValue(35, "销售部", "2023年");
        dataset.setValue(50, "销售部", "2024年");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 13));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.BOLD, 10));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createBarChart("部门人数曲线", null, null, dataset);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/barChart.png"), chart, 500, 200);
    }
}
