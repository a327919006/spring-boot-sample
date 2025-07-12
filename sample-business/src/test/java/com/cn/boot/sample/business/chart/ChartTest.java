package com.cn.boot.sample.business.chart;

import com.cn.boot.sample.business.util.chart.ChartUtil;
import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */

public class ChartTest {

    @Test
    public void createPieChart() {
        ChartUtil.createPieChart();
    }

    @Test
    public void createLineChart() {
        ChartUtil.createLineChart();
    }

    @Test
    public void createXYLineChart() {
        ChartUtil.createXYLineChart();
    }

    @Test
    public void createBarChart() {
        ChartUtil.createBarChart2();
    }

    @Test
    public void createBugDayCountChart() {
        ChartUtil.createBugDayCountChart();
    }

    @Test
    public void createBugPeopleCountChart() {
        ChartUtil.createBugPeopleCountChart();
    }

    @Test
    public void createPeopleTaskChart() {
        ChartUtil.createPeopleTaskChart();
    }

    @Test
    public void createPerformanceChart() {
        JFreeChart performanceChart = ChartUtil.createPerformanceChart();
        // 保存为图片
        ChartUtil.chartToFile(performanceChart, 800, 400);
        // 发送邮件，要使用图片的base64值
        // String chartBase64 = ChartUtil.chartToBase64(performanceChart, 800, 400);
        // String html = "<img src=\"data:image/png;base64," + chartBase64 + "\">";

    }
}
