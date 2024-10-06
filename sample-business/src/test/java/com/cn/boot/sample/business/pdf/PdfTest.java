package com.cn.boot.sample.business.pdf;

import com.cn.boot.sample.business.util.pdf.PdfUtil;
import lombok.extern.slf4j.Slf4j;
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
}
