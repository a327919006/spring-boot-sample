package com.cn.boot.sample.business.util.chart;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.title.Title;
import org.jfree.chart.ui.*;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.awt.List;
import java.awt.geom.Ellipse2D;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Chen Nan
 */
@Slf4j
public class ChartUtil {
    private static final String FONT_NAME = "文泉驿正黑";
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
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 20));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 15));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 15));
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
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.PLAIN, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.PLAIN, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.PLAIN, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createLineChart(null, null, null, dataset);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);  // 设置绘图区背景颜色为白色

        // 自定义标题
        TextTitle customTitle = new TextTitle("风险点趋势", new Font(FONT_NAME, Font.BOLD, 12));
        customTitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        customTitle.setMargin(new RectangleInsets(5, 5, 5, 5)); // 可以根据需要调整这些值
        chart.setTitle(customTitle);

        // 设置图例位置-右上角（默认下方中间）
        LegendTitle legend = chart.getLegend();
        legend.setPadding(0, 300, 0, 0);
        legend.setPosition(RectangleEdge.TOP);

        // 增加y轴单位说明文字
        Title title = new TextTitle("单位：个", new Font(FONT_NAME, Font.BOLD, 10), COLOR_GRAY,
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
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
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
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createBarChart("部门人数曲线", null, null, dataset);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/barChart.png"), chart, 500, 200);
    }

    @SneakyThrows
    public static void createBugPeopleCountChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(10, "BUG", "张三");
        dataset.setValue(30, "BUG", "李四");
        dataset.setValue(55, "BUG", "王五");
        dataset.setValue(32, "BUG", "马六");
        dataset.setValue(44, "BUG", "马六1");
        dataset.setValue(55, "BUG", "马六2");
        dataset.setValue(66, "BUG", "马六3");
        dataset.setValue(77, "BUG", "马六4");
        dataset.setValue(11, "BUG", "马六5");
        dataset.setValue(22, "BUG", "马六6");
        dataset.setValue(33, "BUG", "马六7");
        dataset.setValue(123, "BUG", "马六8");
        dataset.setValue(31, "BUG", "马六9");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createBarChart("本周新增BUG数",
                null,                // 分类轴标签
                null,                // 数值轴标签
                dataset,             // 数据集
                PlotOrientation.VERTICAL,  // 图表方向
                false,               // 不显示图例
                false,               // 不生成工具提示
                false                // 不生成URL
        );

        // 移除灰色背景（核心修改部分）
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);                 // 绘图区域背景
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);         // 设置横向网格线颜色（可选）
        // 设置纯蓝色柱形（核心修改1）
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 100, 255)); // RGB纯蓝色
        // 显示数据标签（关键修改点）
        renderer.setDefaultItemLabelsVisible(true); // 全局启用标签
        renderer.setSeriesItemLabelsVisible(0, true); // 针对系列0启用（双保险）
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelFont(new Font(FONT_NAME, Font.BOLD, 12));
        renderer.setDefaultItemLabelPaint(Color.BLACK); // 白色标签确保可读性
        // 设置标签显示在柱形上方（核心修改）
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12,     // 标签锚点：柱形外侧顶部
                TextAnchor.BOTTOM_CENTER       // 文本对齐：底部居中
        ));

        // 设置X轴标签显示优化（核心修改部分）
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 标签旋转45度

        // 调整绘图区域边距确保标签完整显示
        plot.setAxisOffset(new RectangleInsets(10, 0, 0, 0)); // 增加10px上边距

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/研发BUG数.png"), chart, 500, 400);
    }

    @SneakyThrows
    public static void createPeopleTaskChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(33, "任务", "张三(1.5)");
        dataset.setValue(22, "任务", "赵四(1.5)");
        dataset.setValue(44, "任务", "李四(1.5)");
        dataset.setValue(55, "任务", "王五(1.5)");
        dataset.setValue(66, "任务", "马六(1.5)");
        dataset.setValue(10, "BUG", "张三(1.5)");
        dataset.setValue(10, "BUG", "赵四(1.5)");
        dataset.setValue(30, "BUG", "李四(1.5)");
        dataset.setValue(55, "BUG", "王五(1.5)");
        dataset.setValue(32, "BUG", "马六(1.5)");

        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createBarChart("研发任务情况",
                null,                // 分类轴标签
                null,                // 数值轴标签
                dataset,             // 数据集
                PlotOrientation.VERTICAL,  // 图表方向
                true,               // 不显示图例
                false,               // 不生成工具提示
                false                // 不生成URL
        );

        // 移除灰色背景
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);                 // 绘图区域背景
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);         // 设置横向网格线颜色（可选）
        // 设置纯蓝色柱形
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 100, 255)); // RGB纯蓝色
        // 显示数据标签
        renderer.setDefaultItemLabelsVisible(true); // 全局启用标签
        renderer.setSeriesItemLabelsVisible(0, true); // 针对系列0启用（双保险）
        renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelFont(new Font(FONT_NAME, Font.BOLD, 12));
        renderer.setDefaultItemLabelPaint(Color.BLACK); // 白色标签确保可读性
        // 设置标签显示在柱形上方
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12,     // 标签锚点：柱形外侧顶部
                TextAnchor.BOTTOM_CENTER       // 文本对齐：底部居中
        ));

        // 设置X轴标签显示优化
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); // 标签旋转45度
        domainAxis.setCategoryMargin(0.30); // 调整分类间距，0.2表示20%的空隙
        ((BarRenderer) plot.getRenderer()).setItemMargin(0); // 设置同一分类内不同系列柱形间距为0

        // 配置Y轴扩展显示空间
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setUpperMargin(0.15);  // 增加顶部边距15%

        // 调整绘图区域边距确保标签完整显示
        plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0)); // 增加10px上边距

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/研发任务情况.png"), chart, 500, 300);
    }

    @SneakyThrows
    public static void createBugDayCountChart() {
        // 创建时间序列数据集
        TimeSeries series = new TimeSeries("日新增BUG数量");

        // 生成模拟数据（30天）
        Calendar calendar = Calendar.getInstance();
        Random rand = new Random();

        // 从30天前到今天生成数据
        for (int i = 29; i >= 0; i--) {
            calendar.add(Calendar.DAY_OF_MONTH, -1); // 递减天数
            Day day = new Day(calendar.getTime());

            // 生成随机BUG数量（0-20之间）
            int bugCount = rand.nextInt(20);
            series.add(day, bugCount);
        }

        // 创建数据集
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        // 将序列添加到数据集
        dataSet.addSeries(series);

        // 创建图表
        StandardChartTheme chartTheme = new StandardChartTheme("CN");
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
        ChartFactory.setChartTheme(chartTheme);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "BUG趋势图",  // 标题
                null,             // X轴标签
                null, // Y轴标签
                dataSet,           // 数据集
                false,              // 显示图例
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
        axis.setDateFormatOverride(new SimpleDateFormat("dd"));  // 日期格式

        // 配置左侧Y轴
        NumberAxis leftAxis = (NumberAxis) plot.getRangeAxis();
        leftAxis.setAutoRangeIncludesZero(true); // 自动调整范围
        leftAxis.setAutoRange(true);

        // 设置折线样式
        XYLineAndShapeRenderer rendererRate = new XYLineAndShapeRenderer();
        rendererRate.setSeriesPaint(0, Color.BLUE);    // 蓝色
//        rendererRate.setDefaultShapesVisible(true); // 显示形状
        rendererRate.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
        rendererRate.setSeriesShapesVisible(0, true);
//        rendererRate.setDefaultItemLabelsVisible(true); // 显示每个点的标签
        rendererRate.setSeriesItemLabelsVisible(0, true);
        rendererRate.setSeriesItemLabelGenerator(0, new StandardXYItemLabelGenerator(
                StandardXYItemLabelGenerator.DEFAULT_ITEM_LABEL_FORMAT,
                new SimpleDateFormat("MM-dd"),
                new java.text.DecimalFormat("0")));
        rendererRate.setSeriesItemLabelFont(0, new Font(FONT_NAME, Font.PLAIN, 10));
        rendererRate.setSeriesItemLabelPaint(0, Color.DARK_GRAY);
        plot.setRenderer(0, rendererRate);

        // 6. 设置边距
        plot.setAxisOffset(RectangleInsets.ZERO_INSETS); // 坐标轴与边框无间距
        // chart.setPadding(new RectangleInsets(10, 10, 10, 10));

        // 设置抗锯齿
        chart.setAntiAlias(true);
        chart.setTextAntiAlias(true);

        ChartUtils.saveChartAsPNG(new File("./pdf/chart/BUG趋势.png"), chart, 800, 400);
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
        chartTheme.setExtraLargeFont(new Font(FONT_NAME, Font.BOLD, 13));
        chartTheme.setRegularFont(new Font(FONT_NAME, Font.BOLD, 10));
        chartTheme.setLargeFont(new Font(FONT_NAME, Font.BOLD, 10));
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
        rightAxis.setLabelFont(new Font(FONT_NAME, Font.BOLD, 10));
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
