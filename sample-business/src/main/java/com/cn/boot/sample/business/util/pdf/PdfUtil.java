package com.cn.boot.sample.business.util.pdf;

import org.dromara.pdf.fop.core.doc.Document;
import org.dromara.pdf.fop.core.doc.bookmark.Bookmark;
import org.dromara.pdf.fop.core.doc.component.block.BlockContainer;
import org.dromara.pdf.fop.core.doc.component.image.Image;
import org.dromara.pdf.fop.core.doc.component.page.CurrentPageNumber;
import org.dromara.pdf.fop.core.doc.component.page.TotalPageNumber;
import org.dromara.pdf.fop.core.doc.component.table.Table;
import org.dromara.pdf.fop.core.doc.component.table.TableBody;
import org.dromara.pdf.fop.core.doc.component.table.TableCell;
import org.dromara.pdf.fop.core.doc.component.table.TableRow;
import org.dromara.pdf.fop.core.doc.component.text.Text;
import org.dromara.pdf.fop.core.doc.component.text.TextExtend;
import org.dromara.pdf.fop.core.doc.page.Page;
import org.dromara.pdf.fop.core.doc.watermark.Watermark;
import org.dromara.pdf.fop.handler.TemplateHandler;

import java.util.Date;

/**
 * @author Chen Nan
 */
public class PdfUtil {
    /**
     * 定义页面页码ID
     */
    private static final String PAGE_ID = "page";

    private static final String COLOR_GRAY = "#919292";

    public static void createPdf() {
        // 创建文档
        Document document = createDocument();

        // 创建首页
        Page homePage = createHomePage();
        Page contentPage = createContentPage();

        // 添加页眉
        addHeader(homePage);
        addHeader(contentPage);
        // 添加水印
        addPageWatermark(homePage);
        addPageWatermark(contentPage);
        // 添加页脚
        addFooter(contentPage);

        document.addPage(homePage, contentPage)
                // 添加书签
                // .addBookmark(createBookmark())
                .transform("./pdf/hello-world.pdf");
    }

    /**
     * 创建文档
     */
    private static Document createDocument() {
        return TemplateHandler.Document.build()
                .setTitle("标题-测试")
                .setAuthor("作者-测试")
                .setSubject("主题-测试")
                .setKeywords("关键词-测试")
                .setCreator("创建人-测试")
                .setCreationDate(new Date());
    }

    /**
     * 创建首页
     */
    private static Page createHomePage() {
        Page page = TemplateHandler.Page.build()
                .setWidth("21cm")// 默认页面大小：A4 21cm/29.7cm
                .setHeight("29.7cm")
                .setFontFamily("微软雅黑")
                // .setHeaderHeight("30pt") // 页眉高度
                .setBodyMarginTop("30pt")// 顶部边距，防止与页眉重叠
                .setFooterHeight("50pt") // 页脚高度
                // .setBodyMarginBottom("30pt") // 底部边距，防止与页脚重叠
                ;

        // 创建主体文本
        Text body1 = TemplateHandler.Text.build()
                .setText("代理质量系统")
                .setMarginLeft("33%")
                .setMarginTop("30%")
                .setLetterSpacing("5px")
                .setFontSize("30pt");
        Text body2 = TemplateHandler.Text.build()
                .setText("检测报告")
                .setMarginLeft("36%")
                .setLetterSpacing("15px")
                .setFontSize("30pt");
        Text body3 = TemplateHandler.Text.build()
                .setText("CN代码质量检测中心")
                .setHorizontalStyle("center")
                .setMarginTop("30%")
                .setFontSize("13pt");

        // 表格
        Table table = TemplateHandler.Table.build()
                .setWidth("300px")
                // .setLeading("30px")
                .setHorizontalStyle("center")
                .setMargin("25% 0 0 25%");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();
        // 表格行
        TableRow rowNum = TemplateHandler.Table.Row.build().setMinRowHeight("30px").setVerticalStyle("after");
        TableRow rowType = TemplateHandler.Table.Row.build().setMinRowHeight("30px").setVerticalStyle("after");
        TableRow rowDate = TemplateHandler.Table.Row.build().setMinRowHeight("30px").setVerticalStyle("after");
        TableRow rowPlace = TemplateHandler.Table.Row.build().setMinRowHeight("30px").setVerticalStyle("after");
        // 表格单元格
        TableCell cellNumName = TemplateHandler.Table.Cell.build();
        TableCell cellNumValue = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black")
                .setWidth("240px");
        TableCell cellTypeName = TemplateHandler.Table.Cell.build();
        TableCell cellTypeValue = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black");
        TableCell cellDateName = TemplateHandler.Table.Cell.build();
        TableCell cellDateValue = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black");
        TableCell cellPlaceName = TemplateHandler.Table.Cell.build();
        TableCell cellPlaceValue = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black");
        table.setBody(body);
        body.addRow(rowNum, rowType, rowDate, rowPlace);
        rowNum.addCell(cellNumName, cellNumValue);
        rowType.addCell(cellTypeName, cellTypeValue);
        rowDate.addCell(cellDateName, cellDateValue);
        rowPlace.addCell(cellPlaceName, cellPlaceValue);
        cellNumName.addComponent(TemplateHandler.Text.build().setText("检测编号:"));
        cellNumValue.addComponent(TemplateHandler.Text.build().setText("1234567890123456789"));
        cellTypeName.addComponent(TemplateHandler.Text.build().setText("检测项目:"));
        cellTypeValue.addComponent(TemplateHandler.Text.build().setText("spring-boot-sample"));
        cellDateName.addComponent(TemplateHandler.Text.build().setText("检测日期:"));
        cellDateValue.addComponent(TemplateHandler.Text.build().setText("2024-10-01"));
        cellPlaceName.addComponent(TemplateHandler.Text.build().setText("检测类型:"));
        cellPlaceValue.addComponent(TemplateHandler.Text.build().setText("安全分析检测"));

        BlockContainer container = TemplateHandler.BlockContainer.build()
                .setHeight("300px")
                .setHorizontalStyle("center")
                .setBackgroundImage("./pdf/stamp.png")
                .setBackgroundImageWidth("80px")
                .setBackgroundImageHeight("80px")
                .setBackgroundRepeat("no-repeat")
                .setBackgroundPosition("280px 210px");
        container.addComponent(table);

        // 添加主体
        page.addBodyComponent(body1, body2, container, body3);
        return page;
    }

    /**
     * 创建内容页
     */
    private static Page createContentPage() {
        Page page = TemplateHandler.Page.build()
                .setId(PAGE_ID)
                .setFontFamily("微软雅黑")
                .setBodyMarginTop("60pt")
                .setBodyMarginLeft("40pt")
                .setBodyMarginRight("40pt")
                .setFooterHeight("50pt") // 页脚高度
                ;

        // 大标题
        Text title = TemplateHandler.Text.build().setText("代理质量系统检测报告").setHorizontalStyle("center").setFontSize("18pt");
        // 标题-引用
        Text titleQuote = TemplateHandler.Text.build().setText("一、规范性引用文件").setMarginTop("10pt").setFontSize("15pt");
        Text quote1 = createQuoteText("GB 12345-2024《代码质量规范》");
        Text quote2 = createQuoteText("GB 00001-2024《代码开发规范》");
        // 标题-报告
        Text titleReport = TemplateHandler.Text.build().setText("二、检测报告").setMarginTop("10pt").setFontSize("15pt");

        Table infoTable = createContentInfoTable();
        Table stateTable = createContentStateTable();
        BlockContainer resultTable = createContentResultTable();
        Table chartTable = createContentChartTable();

        // 添加主体
        page.addBodyComponent(title, titleQuote, quote1, quote2, titleReport, infoTable, stateTable, resultTable, chartTable);
        return page;
    }

    /**
     * 创建表格-基本信息
     */
    private static Table createContentInfoTable() {
        // 表格
        Table table = TemplateHandler.Table.build()
                .setBorder("1 solid black")
                .setVerticalStyle("center")
                .setMinRowHeight("30px")
                .setMarginTop("5pt");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();

        // 表格行
        TableRow rowTitle = TemplateHandler.Table.Row.build();
        TableRow rowInfo1 = TemplateHandler.Table.Row.build();
        TableRow rowInfo2 = TemplateHandler.Table.Row.build();

        // 表格单元格
        TableCell cellInfoTitle = TemplateHandler.Table.Cell.build().setBorder("1 solid black").setColumnSpan(6);
        TableCell cellInfoNumName = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoNumValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black").setColumnSpan(2);
        TableCell cellInfoProjectName = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoProjectValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black").setColumnSpan(2);
        TableCell cellInfoPeopleName = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoPeopleValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoDateName = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoDateValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoTypeName = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellInfoTypeValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black");

        cellInfoTitle.addComponent(TemplateHandler.Text.build().setText("1.基本信息").setMarginLeft("5pt"));
        cellInfoNumName.addComponent(createTableNameText("检测编号"));
        cellInfoNumValue.addComponent(createTableValueText("1234567890123456789"));
        cellInfoProjectName.addComponent(createTableNameText("检测项目"));
        cellInfoProjectValue.addComponent(createTableValueText("spring-boot-sample"));
        cellInfoPeopleName.addComponent(createTableNameText("送检人"));
        cellInfoPeopleValue.addComponent(createTableValueText("张三"));
        cellInfoDateName.addComponent(createTableNameText("送检日期"));
        cellInfoDateValue.addComponent(createTableValueText("2024-10-01"));
        cellInfoTypeName.addComponent(createTableNameText("检测类型"));
        cellInfoTypeValue.addComponent(createTableValueText("安全分析检测"));

        rowTitle.addCell(cellInfoTitle);
        rowInfo1.addCell(cellInfoNumName, cellInfoNumValue, cellInfoProjectName, cellInfoProjectValue);
        rowInfo2.addCell(cellInfoPeopleName, cellInfoPeopleValue, cellInfoDateName, cellInfoDateValue,
                cellInfoTypeName, cellInfoTypeValue);
        body.addRow(rowTitle, rowInfo1, rowInfo2);
        table.setBody(body);
        return table;
    }

    /**
     * 创建表格-检测状态
     */
    private static Table createContentStateTable() {
        // 表格
        Table table = TemplateHandler.Table.build()
                .setBorder("1 solid black")
                .setVerticalStyle("center")
                // .setMinColumnWidth("20px")
                .setMinRowHeight("30px");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();

        // 表格行
        TableRow rowTitle = TemplateHandler.Table.Row.build();
        TableRow rowItem = TemplateHandler.Table.Row.build();
        // TableRow rowResult = TemplateHandler.Table.Row.build();

        // 表格单元格
        TableCell cellTitle = TemplateHandler.Table.Cell.build();
        TableCell cellEmpty2 = TemplateHandler.Table.Cell.build().setWidth("50px");
        TableCell cellEmpty3 = TemplateHandler.Table.Cell.build();
        TableCell cellEmpty4 = TemplateHandler.Table.Cell.build().setWidth("80px");
        TableCell cellEmpty5 = TemplateHandler.Table.Cell.build();
        TableCell cellEmpty6 = TemplateHandler.Table.Cell.build().setWidth("120px");
        TableCell cellStateItem = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateUnit = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateResult = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateRange = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateStandard = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        // TableCell cellResult = TemplateHandler.Table.Cell.build().setBorder("1 solid black").setColumnSpan(6).setVerticalStyle("top");


        cellTitle.addComponent(TemplateHandler.Text.build().setText("2.检测表现").setMarginLeft("5pt"));
        cellEmpty2.addComponent(TemplateHandler.Text.build().setText(""));
        cellEmpty3.addComponent(TemplateHandler.Text.build().setText(""));
        cellEmpty4.addComponent(TemplateHandler.Text.build().setText(""));
        cellEmpty5.addComponent(TemplateHandler.Text.build().setText(""));
        cellEmpty6.addComponent(TemplateHandler.Text.build().setText(""));
        cellStateItem.addComponent(createTableNameText("检测项目"));
        cellStateUnit.addComponent(createTableNameText("单位"));
        cellStateValue.addComponent(createTableNameText("检测值"));
        cellStateResult.addComponent(createTableNameText("检测结果"));
        cellStateRange.addComponent(createTableNameText("参考值"));
        cellStateStandard.addComponent(createTableNameText("参考标准"));


        rowTitle.addCell(cellTitle, cellEmpty2, cellEmpty3, cellEmpty4, cellEmpty5, cellEmpty6);
        rowItem.addCell(cellStateItem, cellStateUnit, cellStateValue, cellStateResult, cellStateRange, cellStateStandard);

        body.addRow(rowTitle, rowItem);
        body.addRow(createStateRow("漏洞数", "个", "1", "不合格", "=0", "GB 12345-2024"));
        body.addRow(createStateRow("BUG数", "个", "3", "合格", "≤5", "GB 12345-2024"));
        table.setBody(body);
        return table;
    }

    /**
     * 创建表格-检测结论
     */
    private static BlockContainer createContentResultTable() {
        BlockContainer resultContainer = TemplateHandler.BlockContainer.build()
                .setHeight("135px")
                // .setVerticalStyle("top")
                .setBackgroundImage("./pdf/stamp.png")
                .setBackgroundImageWidth("80px")
                .setBackgroundImageHeight("80px")
                .setBackgroundRepeat("no-repeat")
                .setBackgroundPosition("410px 50px");

        // 表格
        Table table = TemplateHandler.Table.build().setBorder("1 solid black");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();

        // 表格行
        TableRow rowResult = TemplateHandler.Table.Row.build();
        TableRow rowResultCenter = TemplateHandler.Table.Row.build();
        TableRow rowResultDate = TemplateHandler.Table.Row.build();

        // 表格单元格
        TableCell cellResult = TemplateHandler.Table.Cell.build().setHeight("80px");
        TableCell cellResultCenter = TemplateHandler.Table.Cell.build();
        TableCell cellResultDate = TemplateHandler.Table.Cell.build().setHeight("40px");

        Text textResultTitle = TemplateHandler.Text.build().setText("检测结论：");
        Text textResultContent = TemplateHandler.Text.build().setText("您的代码本次评分为90分，代码质量优秀，请继续保持。").setFontColor(COLOR_GRAY);
        TextExtend textResult = TemplateHandler.TextExtend.build().addText(textResultTitle, textResultContent)
                .setMargin("20pt 0 0 5pt");
        Text textResultCenter = TemplateHandler.Text.build().setText("CN代码质量检测中心")
                .setHorizontalStyle("right")
                .setMarginRight("5pt")
                .setFontColor(COLOR_GRAY);
        Text textResultDate = TemplateHandler.Text.build().setText("2024-10-01")
                .setHorizontalStyle("right")
                .setMarginRight("30pt")
                .setFontColor(COLOR_GRAY);
        cellResult.addComponent(textResult);
        cellResultCenter.addComponent(textResultCenter);
        cellResultDate.addComponent(textResultDate);

        rowResult.addCell(cellResult);
        rowResultCenter.addCell(cellResultCenter);
        rowResultDate.addCell(cellResultDate);

        body.addRow(rowResult, rowResultCenter, rowResultDate);

        table.setBody(body);

        resultContainer.addComponent(table);
        return resultContainer;
    }

    /**
     * 创建表格-图表分析
     */
    private static Table createContentChartTable() {
        // 表格
        Table table = TemplateHandler.Table.build()
                .setBorder("1 solid black")
                .setVerticalStyle("center")
                // .setMinColumnWidth("20px")
                .setMinRowHeight("30px");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();

        // 表格行
        TableRow rowTitle = TemplateHandler.Table.Row.build();
        TableRow rowChart = TemplateHandler.Table.Row.build();

        // 表格单元格
        TableCell cellTitle = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellChart = TemplateHandler.Table.Cell.build().setBorder("1 solid black");

        cellTitle.addComponent(TemplateHandler.Text.build().setText("3.图表分析").setMarginLeft("5pt"));

        // 创建图像
        Image image = TemplateHandler.Image.build()
                // 设置图像路径（绝对路径）
                .setPath("./pdf/chart/lineChart.png")
                // 设置图像宽度
                .setWidth("500px")
                // 设置图像高度
                .setHeight("200px")
                // 设置水平居中
                .setHorizontalStyle("center");
        cellChart.addComponent(image);

        rowTitle.addCell(cellTitle);
        rowChart.addCell(cellChart);

        body.addRow(rowTitle, rowChart);
        table.setBody(body);
        return table;
    }

    /**
     * 添加页眉
     */
    private static void addHeader(Page page) {
        // 页眉-logo图片
        Image logoImage = TemplateHandler.Image.build()
                .setPath("./pdf/logo.png")
                .setWidth("144px")
                .setHeight("18px");
        // 页眉-报告编号
        Text headerNum = TemplateHandler.Text.build()
                .setText("报告编号：1234567890123456789")
                .setFontSize("8pt")
                .setFontColor(COLOR_GRAY)
                .setPaddingTop("6px");

        // 表格
        Table table = TemplateHandler.Table.build()
                .setMargin("25pt 10% 0 10%");
        // 表格体
        TableBody body = TemplateHandler.Table.Body.build();
        // 表格行
        TableRow row = TemplateHandler.Table.Row.build();
        // 表格单元格-logo
        TableCell cellLogo = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black");
        // 表格单元格-编号
        TableCell cellNum = TemplateHandler.Table.Cell.build()
                .setBorderBottom("1 solid black")
                .setHorizontalStyle("right");

        table.setBody(body);
        body.addRow(row);
        row.addCell(cellLogo, cellNum);
        cellLogo.addComponent(logoImage);
        cellNum.addComponent(headerNum);

        page.addHeaderComponent(table);
    }

    /**
     * 添加页脚
     */
    private static void addFooter(Page page) {
        // 创建当前页码
        CurrentPageNumber currentPageNumber = TemplateHandler.CurrentPageNumber.build();
        // 创建总页码
        TotalPageNumber totalPageNumber = TemplateHandler.TotalPageNumber.build().setPageId(PAGE_ID);
        Text currentText = TemplateHandler.Text.build().setText("第 ");
        Text totalText = TemplateHandler.Text.build().setText(" 页 共 ");
        Text pageText = TemplateHandler.Text.build().setText(" 页");
        // 创建容器,添加容器内组件
        BlockContainer container = TemplateHandler.BlockContainer.build()
                .setHorizontalStyle("center")
                .setFontSize("8pt")
                .setFontColor(COLOR_GRAY)
                .addComponent(currentText, currentPageNumber, totalText, totalPageNumber, pageText);
        // 设置id
        page.addFooterComponent(container);
    }

    /**
     * 添加水印
     */
    private static void addPageWatermark(Page page) {
        // 创建多行水印
        Watermark watermark = TemplateHandler.Watermark.build()
                // 设置水印id
                .setId("watermark")
                // 设置水印文件临时目录
                .setTempDir("./pdf")
                // 设置水印文本，支持多行
                .setText("ChenNan")
                // 设置字体大小
                .setFontSize("20pt")
                // 设置字体
                .setFontFamily("微软雅黑")
                // 设置水印图片宽度
                .setWidth("300pt")
                // 设置水印图片高度
                .setHeight("200pt")
                // 设置水印显示宽度
                .setShowWidth("200pt")
                // 设置旋转弧度
                .setRadians("-20")
                // 设置透明度
                .setFontAlpha("50")
                // 开启文件覆盖
                .enableOverwrite();

        page.setBodyWatermark(watermark);
    }

    /**
     * 创建书签
     */
    private static Bookmark createBookmark() {
        Bookmark bookmark = TemplateHandler.Bookmark.build()
                // 设置标题
                .setTitle("目录-标题")
                // 设置内部地址（对应组件id）
                .setInternalDestination("title");
        Bookmark childBookmark = TemplateHandler.Bookmark.build()
                // 设置标题
                .setTitle("目录-子标题")
                .setInternalDestination("content");
        bookmark.addChild(childBookmark);
        return bookmark;
    }

    /**
     * 创建检测状态行
     */
    private static TableRow createStateRow(String item, String unit, String value, String result, String range, String standard) {
        TableRow rowState = TemplateHandler.Table.Row.build();

        TableCell cellStateItem = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateUnit = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateValue = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateResult = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateRange = TemplateHandler.Table.Cell.build().setBorder("1 solid black");
        TableCell cellStateStandard = TemplateHandler.Table.Cell.build().setBorder("1 solid black");

        cellStateItem.addComponent(createTableStateText(item));
        cellStateUnit.addComponent(createTableStateText(unit));
        cellStateValue.addComponent(createTableStateText(value));
        cellStateResult.addComponent(createTableStateText(result));
        cellStateRange.addComponent(createTableStateText(range));
        cellStateStandard.addComponent(createTableStateText(standard));

        rowState.addCell(cellStateItem, cellStateUnit, cellStateValue, cellStateResult, cellStateRange, cellStateStandard);
        return rowState;
    }

    /**
     * 创建Text-引用
     */
    private static Text createQuoteText(String value) {
        return TemplateHandler.Text.build()
                .setText(value)
                .setMarginLeft("20pt")
                .setMarginTop("10pt")
                .setFontSize("12pt")
                .setFontColor(COLOR_GRAY);
    }

    /**
     * 创建Text-表格值
     */
    private static Text createTableValueText(String value) {
        return TemplateHandler.Text.build()
                .setText(value)
                .setMarginLeft("5pt")
                .setFontColor(COLOR_GRAY);
    }

    /**
     * 创建Text-表格值-状态
     */
    private static Text createTableStateText(String value) {
        return TemplateHandler.Text.build()
                .setText(value)
                .setHorizontalStyle("center")
                .setFontColor(COLOR_GRAY);
    }

    /**
     * 创建Text-表格名
     */
    private static Text createTableNameText(String name) {
        return TemplateHandler.Text.build()
                .setText(name)
                .setHorizontalStyle("center");
    }
}
