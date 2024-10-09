package com.cn.boot.sample.business.util.chart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.charts.LegendPosition;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

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
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);  // 设置绘图区背景颜色为白色

        // 自定义标题
        TextTitle customTitle = new TextTitle("部门人数曲线", new Font("微软雅黑", Font.BOLD, 13));
        customTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        // 减少标题周围的边距，使其更靠近左侧
        customTitle.setMargin(new RectangleInsets(5, 5, 5, 5)); // 可以根据需要调整这些值
        chart.setTitle(customTitle);

        // 假设你已经有了 JFreeChart 对象 chart
        LegendTitle legend = chart.getLegend();
        legend.setPosition(RectangleEdge.TOP);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/lineChart.png"), chart, 500, 200);
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
