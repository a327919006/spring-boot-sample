package com.cn.boot.sample.business.util.chart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Chen Nan
 */
@Slf4j
public class ChartUtil {

    private static final Color COLOR_GRAY = new Color(146, 146, 146);

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
        dataset.setValue(10, "BUG", "01月");
        dataset.setValue(25, "BUG", "02月");
        dataset.setValue(30, "BUG", "03月");
        dataset.setValue(40, "BUG", "04月");
        dataset.setValue(55, "BUG", "05月");
        dataset.setValue(60, "BUG", "06月");
        dataset.setValue(70, "BUG", "07月");
        dataset.setValue(87, "BUG", "08月");
        dataset.setValue(93, "BUG", "09月");
        dataset.setValue(15, "漏洞", "01月");
        dataset.setValue(24, "漏洞", "02月");
        dataset.setValue(35, "漏洞", "03月");
        dataset.setValue(46, "漏洞", "04月");
        dataset.setValue(55, "漏洞", "05月");
        dataset.setValue(68, "漏洞", "06月");
        dataset.setValue(79, "漏洞", "07月");
        dataset.setValue(83, "漏洞", "08月");
        dataset.setValue(94, "漏洞", "09月");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.PLAIN, 13));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.PLAIN, 10));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.PLAIN, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createLineChart(null, null, null, dataset);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);  // 设置绘图区背景颜色为白色

        // 自定义标题
        TextTitle customTitle = new TextTitle("风险点趋势", new Font("微软雅黑", Font.BOLD, 12));
        customTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        customTitle.setMargin(new RectangleInsets(5, 5, 5, 5)); // 可以根据需要调整这些值
        chart.setTitle(customTitle);

        // 设置图例位置-右上角（默认下方中间）
        LegendTitle legend = chart.getLegend();
        legend.setPadding(0, 300, 0, 0);
        legend.setPosition(RectangleEdge.TOP);

        // 增加y轴单位说明文字
        Title title = new TextTitle("单位：个", new Font("微软雅黑", Font.BOLD, 10), COLOR_GRAY,
                RectangleEdge.TOP, HorizontalAlignment.LEFT, VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS);
        title.setPadding(0, 20, 0, 0);
        chart.addSubtitle(title);

        // 增加网格线
        plot.setRangeGridlinePaint(COLOR_GRAY);

        // 隐藏边框
        plot.setOutlineVisible(false);

        // 隐藏x轴和y轴的标尺
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setAxisLineVisible(false);
        domainAxis.setTickMarksVisible(false);
        domainAxis.setTickLabelPaint(COLOR_GRAY);
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setAxisLineVisible(false);
        rangeAxis.setTickMarksVisible(false);
        rangeAxis.setTickLabelPaint(COLOR_GRAY);

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

    @SneakyThrows
    public static JFreeChart createPerformanceChart() {
        // 创建数据集
        TimeSeries cpuSeries = new TimeSeries("CPU使用率");
        TimeSeries memorySeries = new TimeSeries("内存使用率");
        TimeSeries tempSeries = new TimeSeries("CPU温度");

        // 生成高密度测试数据（每5分钟一条）
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -30); // 30天前

        // 数据生成参数
        double memoryBase = 40.0; // 内存基准值

        int i = 0;
        while (calendar.getTime().before(now)) {
            Minute minute = new Minute(calendar.getTime());

            // 生成带时间特征的模拟数据
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            // CPU使用率（白天高夜间低）
            double cpuUsage = 30 + 50 * Math.abs(Math.sin(hour * Math.PI / 24))
                    + 5 * Math.random();

            // 内存使用率（缓慢上升趋势）
            double memoryUsage = memoryBase + 0.1 * (calendar.get(Calendar.DAY_OF_MONTH));
            memoryUsage += 5 * Math.random();

            // CPU温度（包含负值波动）
            double temperature = 20 + 15 * Math.sin(calendar.get(Calendar.DAY_OF_MONTH))
                    + 10 * Math.random() - 5;

            // 添加数据点
            if (i % 3 != 0) {
                cpuSeries.add(minute, cpuUsage);
                memorySeries.add(minute, Math.min(100, memoryUsage));
                tempSeries.add(minute, temperature);
            }

            // 每5分钟增加一次
            calendar.add(Calendar.MINUTE, 5);
            memoryBase += 0.0001; // 模拟内存缓慢增长
            i++;
        }

        // 创建数据集
        TimeSeriesCollection dataSetRate = new TimeSeriesCollection();
        TimeSeriesCollection dataSetTemp = new TimeSeriesCollection();
        // 将序列添加到数据集
        dataSetRate.addSeries(cpuSeries);
        dataSetRate.addSeries(memorySeries);
        dataSetTemp.addSeries(tempSeries);

        // 创建图表
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font("微软雅黑", Font.BOLD, 13));
        chartTheme.setRegularFont(new Font("微软雅黑", Font.BOLD, 10));
        chartTheme.setLargeFont(new Font("微软雅黑", Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "边缘控制器-1",  // 标题
                null,             // X轴标签
                "使用率(%)", // Y轴标签
                dataSetRate,           // 数据集
                true,              // 显示图例
                true,              // 显示工具提示
                false              // 不显示URL
        );

        // 获取图表绘图对象
        XYPlot plot = chart.getXYPlot();
        // 设置背景颜色
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // 设置坐标轴格式
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MM-dd"));  // 日期格式
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 5)); // 每5天显示一个标签

        // 配置左侧Y轴（使用率）
        NumberAxis leftAxis = (NumberAxis) plot.getRangeAxis();
        leftAxis.setAutoRangeIncludesZero(false); // 自动调整范围
        leftAxis.setAutoRange(true);
        // 强制设置刻度单位为10
        // rangeAxis.setTickUnit(new NumberTickUnit(10));

        // 配置右侧Y轴（温度）
        NumberAxis rightAxis = new NumberAxis("温度(℃)");
        rightAxis.setLabelFont(new Font("微软雅黑", Font.BOLD, 10));
        rightAxis.setTickLabelPaint(Color.RED);   // 新增：刻度值红色
        rightAxis.setAutoRangeIncludesZero(false);
        rightAxis.setAutoRange(true);
        plot.setRangeAxis(1, rightAxis);
        plot.setDataset(1, dataSetTemp);
        plot.mapDatasetToRangeAxis(1, 1);

        // 设置折线样式
        XYLineAndShapeRenderer rendererRate = new XYLineAndShapeRenderer();
        rendererRate.setSeriesPaint(0, Color.BLUE);    // CPU使用率 - 蓝色
        rendererRate.setSeriesPaint(1, Color.GREEN);   // 内存使用率 - 绿色
        rendererRate.setDefaultShapesVisible(false); // 不显示形状
        XYLineAndShapeRenderer rendererTemp = new XYLineAndShapeRenderer();
        rendererTemp.setSeriesPaint(0, Color.RED);    // CPU温度 - 红色
        rendererTemp.setSeriesShapesVisible(0, false);

        // 6. 设置边距
        plot.setAxisOffset(RectangleInsets.ZERO_INSETS); // 坐标轴与边框无间距
        // chart.setPadding(new RectangleInsets(10, 10, 10, 10));

        plot.setRenderer(0, rendererRate);
        plot.setRenderer(1, rendererTemp);

        // ChartUtils.saveChartAsPNG(new File("./pdf/chart/performance.png"), chart, 800, 400);

        // 设置抗锯齿
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);
        return chart;
    }

    public static String chartToBase64(JFreeChart chart, int width, int height) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            // 设置高DPI渲染（解决字体模糊问题）
            ChartUtils.writeChartAsPNG(bos, chart, width, height);

            // 转换为Base64字符串
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        } catch (Exception e) {
            log.error("创建异常：", e);
            return null;
        }
    }

    public static void chartToFile(JFreeChart chart, int width, int height) {
        try {
            ChartUtils.saveChartAsPNG(new File("./pdf/chart/performance.png"), chart, width, height);
        } catch (IOException e) {
            log.error("创建异常：", e);
        }
    }
}
