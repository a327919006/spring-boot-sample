package com.cn.boot.sample.business.util.pdf;

import org.dromara.pdf.fop.core.doc.Document;
import org.dromara.pdf.fop.core.doc.bookmark.Bookmark;
import org.dromara.pdf.fop.core.doc.component.block.BlockContainer;
import org.dromara.pdf.fop.core.doc.component.image.Image;
import org.dromara.pdf.fop.core.doc.component.page.CurrentPageNumber;
import org.dromara.pdf.fop.core.doc.component.page.TotalPageNumber;
import org.dromara.pdf.fop.core.doc.component.table.*;
import org.dromara.pdf.fop.core.doc.component.text.Text;
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

    private static Page createContentPage() {
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
                .setBackgroundImage("./pdf/stamp.png")
                .setBackgroundImageWidth("60px")
                .setBackgroundImageHeight("60px")
                .setBackgroundRepeat("no-repeat")
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

        // 添加主体
        page.addBodyComponent(body1, body2, table, body3);
        return page;
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

        // 添加主体
        page.addBodyComponent(body1, body2, table, body3);
        return page;
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
                .setFontColor("#919292")
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
                .setFontColor("#919292")
                .addComponent(currentText, currentPageNumber, totalText, totalPageNumber, pageText);
        // 设置id
        page.setId(PAGE_ID).addFooterComponent(container);
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
}
