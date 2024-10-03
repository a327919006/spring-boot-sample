package com.cn.boot.sample.business.util.pdf;

import org.dromara.pdf.fop.core.doc.Document;
import org.dromara.pdf.fop.core.doc.bookmark.Bookmark;
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
     * 页眉文本
     */
    private static final Text TEXT_HEADER = TemplateHandler.Text.build()
            .setText("页眉").setFontFamily("微软雅黑");
    /**
     * 页脚文本
     */
    private static final Text TEXT_FOOTER = TemplateHandler.Text.build()
            .setText("页脚").setFontFamily("微软雅黑");


    public static void createPdf() {
        // 创建文档
        Document document = createDocument();

        Page page = TemplateHandler.Page.build()
                .setWidth("21cm")// 默认页面大小：A4 21cm/29.7cm
                .setHeight("29.7cm")
                .setHeaderHeight("30pt") // 页眉高度
                .setBodyMarginTop("30pt")// 顶部边距，防止与页眉重叠
                .setFooterHeight("30pt") // 页脚高度
                .setBodyMarginBottom("30pt") // 底部边距，防止与页脚重叠
                ;

        // 创建主体文本
        Text bodyTitle = TemplateHandler.Text.build()
                .setId("title")
                .setText("hello world")
                .setFontFamily("微软雅黑")
                .setFontSize("20pt");
        Text bodyContent = TemplateHandler.Text.build()
                .setId("content")
                .setText("111111111111111");

        page.addBodyComponent(bodyTitle, bodyContent);
        // 添加页眉页脚
        addHeaderAndFooter(page);
        // 添加水印
        addPageWatermark(page);

        document.addPage(page)
                .addBookmark(createBookmark()) // 添加书签
                .transform("./hello-world.pdf");
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
     * 添加页眉页脚
     */
    private static void addHeaderAndFooter(Page page) {
        page.addHeaderComponent(TEXT_HEADER)
                .addFooterComponent(TEXT_FOOTER);
    }

    /**
     * 添加水印
     */
    private static void addPageWatermark(Page page) {
        // 创建多行水印
        Watermark watermark = TemplateHandler.Watermark.build()
                // 设置水印id
                .setId("logo")
                // 设置水印文本
                .setText("ChenNan")
                // 设置字体大小
                .setFontSize("12pt")
                // 设置字体
                .setFontFamily("微软雅黑")
                // 设置水印图片宽度
                .setWidth("300pt")
                // 设置水印图片高度
                .setHeight("200pt")
                // 设置水印显示宽度
                .setShowWidth("200pt")
                // 设置旋转弧度
                .setRadians("10")
                // 设置透明度
                .setFontAlpha("100")
                // 开启文件覆盖
                .enableOverwrite();

        page.setBodyWatermark(watermark);
    }
}
