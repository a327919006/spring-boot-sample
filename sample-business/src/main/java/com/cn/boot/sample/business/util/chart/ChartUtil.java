package com.cn.boot.sample.business.util.chart;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Chen Nan
 */
@Slf4j
public class ChartUtil {

    public static void createPieChart() throws IOException {
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
}
