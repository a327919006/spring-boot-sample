package com.cn.boot.sample.business.pdf;

import com.cn.boot.sample.business.util.pdf.PdfUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.dromara.pdf.fop.core.doc.Document;
import org.dromara.pdf.fop.core.doc.component.block.BlockContainer;
import org.dromara.pdf.fop.core.doc.component.page.CurrentPageNumber;
import org.dromara.pdf.fop.core.doc.component.page.TotalPageNumber;
import org.dromara.pdf.fop.core.doc.component.text.Text;
import org.dromara.pdf.fop.core.doc.page.Page;
import org.dromara.pdf.fop.handler.TemplateHandler;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */
@Slf4j
public class PdfTest {

    @Test
    public void createPdf() {
        PdfUtil.createPdf();
    }

    /**
     * 测试PDF合并，目的：由于分页组件无法跳过首页进行总页数统计
     * 如果首页不希望纳入总页数统计，需将首页单独成一个pdf文件，正文为另一个pdf文件，再进行合并
     */
    @Test
    @SneakyThrows
    public void createPdfMerge() {
        // 定义输出路径
        String outputPath1 = "./pdf/test1.pdf";
        String outputPath2 = "./pdf/test2.pdf";
        String outputPathMerge = "./pdf/testMerge.pdf";

        // 定义页面id
        String pageId = "page";
        // 创建文档
        Document document1 = TemplateHandler.Document.build();
        Page page1 = TemplateHandler.Page.build().setFontFamily("微软雅黑");
        page1.addBodyComponent(TemplateHandler.Text.build().setText("首页"));
        // 添加页面
        document1.addPage(page1);
        // 转换pdf
        document1.transform(outputPath1);

        Document document2 = TemplateHandler.Document.build();
        // 创建页面

        Page page2 = TemplateHandler.Page.build().setFontFamily("微软雅黑");
        // 创建当前页码
        CurrentPageNumber currentPageNumber = TemplateHandler.CurrentPageNumber.build();
        // 创建总页码
        TotalPageNumber totalPageNumber = TemplateHandler.TotalPageNumber.build().setPageId(pageId);
        // 创建普通文本
        Text text1 = TemplateHandler.Text.build().setText("第一页内容");
        // 创建普通文本并分页
        Text text2 = TemplateHandler.Text.build().setText("第二页内容").setBreakBefore("page");
        // 创建容器
        BlockContainer container = TemplateHandler.BlockContainer.build();
        // 创建当前页码文本
        Text currentText = TemplateHandler.Text.build().setText("当前第： ");
        // 创建总页码文本
        Text totalText = TemplateHandler.Text.build().setText("，共： ");
        // 添加容器内组件
        container.addComponent(currentText, currentPageNumber, totalText, totalPageNumber);
        // 设置id
        page2.setId(pageId);
        // 设置页眉高度并添加页眉组件
        page2.setHeaderHeight("20pt").addHeaderComponent(container);
        // 设置页面主体上边距并添加页面主体组件
        page2.setBodyMarginTop("20pt").addBodyComponent(text1, text2);
        // 添加页面
        document2.addPage(page2);
        // 转换pdf
        document2.transform(outputPath2);

        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outputPathMerge);
        pdfMerger.addSource(outputPath1);
        pdfMerger.addSource(outputPath2);
        pdfMerger.mergeDocuments(null);
    }
}
