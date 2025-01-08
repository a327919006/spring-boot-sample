package com.cn.boot.sample.business.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.business.excel.ClientCsv;
import com.cn.boot.sample.business.excel.ClientExcel;
import com.cn.boot.sample.business.excel.listener.ClientImportListener;
import com.cn.boot.sample.business.util.pdf.PdfUtil;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.dromara.pdf.fop.core.base.Template;
import org.dromara.pdf.fop.core.doc.Document;
import org.dromara.pdf.fop.core.doc.bookmark.Bookmark;
import org.dromara.pdf.fop.core.doc.component.text.Text;
import org.dromara.pdf.fop.core.doc.page.Page;
import org.dromara.pdf.fop.handler.TemplateHandler;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author ChenNan
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Api(tags = "测试08-文件上传、下载", produces = MediaType.APPLICATION_JSON_VALUE)
public class FileController {

    @ApiOperation("单文件上传")
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename) throws IOException {
        String originalFilename = file.getOriginalFilename(); //上传的文件名
        String contentType = file.getContentType();// 文件类型
        String name = file.getName(); // 字段名
        long size = file.getSize(); // 大小 字节
        byte[] bytes = file.getBytes(); // 内容

        log.info("originalFilename={}", originalFilename);
        log.info("contentType={}", contentType);
        log.info("name={}", name);
        log.info("size={}", size);
        log.info("bytes={}", new String(bytes));
        return filename;
    }

    @ApiOperation("多文件上传-用postman测试，swagger有BUG，会报错")
    @PostMapping("/upload/more")
    public String upload(@RequestParam("files") MultipartFile[] files, @RequestParam("filename") String filename) throws IOException {
        for (MultipartFile file : files) {
            log.info("originalFilename={}", file.getOriginalFilename());
        }
        return filename;
    }

    @ApiOperation("下载txt文件")
    @GetMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String data = "123456";
        String fileName = "test.txt";

        response.setContentType("text/plain; charset=GBK");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Connection", "close");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @ApiOperation("下载Excel文件")
    @GetMapping("/download/excel/hssf")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("查询结果");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式

        Font font = wb.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setBold(true);  // 粗体
        style.setFont(font);

        HSSFCell cell = row.createCell(0);
        cell.setCellValue("商户ID");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("商户名称");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("创建时间");
        cell.setCellStyle(style);


        // 准备excel数据
        String filename = "商户信息";
        List<Client> list = new ArrayList<>();
        list.add(new Client().setId("1").setName("商户1").setCreateTime(LocalDateTime.now()));
        list.add(new Client().setId("2").setName("商户2").setCreateTime(LocalDateTime.now()));
        list.add(new Client().setId("3").setName("商户3").setCreateTime(LocalDateTime.now()));

        int i = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Client client : list) {
            row = sheet.createRow(i + 1);

            // 第四步，创建单元格，并设置值
            row.createCell(0).setCellValue(client.getId());
            row.createCell(1).setCellValue(client.getName());
            row.createCell(2).setCellValue(client.getCreateTime().format(formatter));
            i++;
        }

        // 第六步，输出Excel文件
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String(filename.getBytes(), StandardCharsets.ISO_8859_1) + ".xls");
        response.setHeader("Connection", "close");

        wb.write(response.getOutputStream());
    }

    @ApiOperation("下载ZIP")
    @GetMapping("/download/zip")
    public void downloadZip(HttpServletResponse response) throws IOException {
        String data1 = "111111";
        String data2 = "222222";
        String fileName = "test.zip";

        response.setContentType("text/plain; charset=GBK");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Connection", "close");
        ServletOutputStream writer = response.getOutputStream();

        ZipOutputStream outputStream = new ZipOutputStream(writer);
        ZipEntry zipFile1 = new ZipEntry("test1.txt");
        ZipEntry zipFile2 = new ZipEntry("test2.txt");

        outputStream.putNextEntry(zipFile1);
        outputStream.write(data1.getBytes());
        outputStream.closeEntry();

        outputStream.putNextEntry(zipFile2);
        outputStream.write(data2.getBytes());
        outputStream.closeEntry();

        outputStream.flush();
        outputStream.close();
    }

    @ApiOperation("下载Excel文件-EasyExcel")
    @GetMapping("/download/excel/easyexcel")
    public void downloadEasyExcel(HttpServletResponse response) throws IOException {
        String filename = "商户信息";
        List<ClientExcel> list = new ArrayList<>();
        list.add(new ClientExcel("1", "商户1", LocalDateTime.now()));
        list.add(new ClientExcel("2", "商户2", LocalDateTime.now()));
        list.add(new ClientExcel("3", "商户3", LocalDateTime.now()));

        // 输出Excel文件
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename="
                + URLEncoder.encode(filename, StandardCharsets.UTF_8.name()) + ".xlsx");
        response.setHeader("Connection", "close");

        // (可选) 动态指定某些列不导出，列可根据前端或数据库配置动态指定
        List<String> excludeList = new ArrayList<>();
        excludeList.add("id");

        EasyExcel.write(response.getOutputStream(), ClientExcel.class)
                .excludeColumnFiledNames(excludeList)
                .sheet(filename)
                .doWrite(list);
    }

    @ApiOperation("上传并解析Excel")
    @PostMapping("/upload/excel")
    public String uploadExcel(@RequestParam("file") MultipartFile file) {
        String filename = file.getOriginalFilename(); //上传的文件名
        if (StringUtils.isEmpty(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if (!StringUtils.endsWithIgnoreCase(filename, ".xls")
                && !StringUtils.endsWithIgnoreCase(filename, ".xlsx")
                && !StringUtils.endsWithIgnoreCase(filename, ".csv")) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        String contentType = file.getContentType();// 文件类型
        String name = file.getName(); // 字段名
        long size = file.getSize(); // 大小 字节

        log.info("filename={}", filename);
        log.info("contentType={}", contentType);
        log.info("name={}", name);
        log.info("size={}", size);

        try {
            ClientImportListener importListener = new ClientImportListener();
            InputStream inputStream = new BufferedInputStream(file.getInputStream());

            ExcelReaderBuilder builder = EasyExcel.read(inputStream, ClientExcel.class, importListener);
            builder.sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    @ApiOperation("上传并解析Csv")
    @PostMapping("/upload/csv")
    public String uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));

        HeaderColumnNameMappingStrategy<ClientCsv> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ClientCsv.class);

        List<ClientCsv> beans = new CsvToBeanBuilder<ClientCsv>(reader)
                .withSeparator(',')
                .withMappingStrategy(strategy)
                .build()
                .parse();
        log.info("data:{}", beans);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("下载Csv")
    @GetMapping("/download/csv")
    public void downloadCsv(int dataCount, HttpServletResponse response) throws Exception {
        String filename = "test";
        List<ClientCsv> list = generateData(dataCount);

        // 输出CSV文件
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");

//        StatefulBeanToCsv<ClientCsv> beanToCsv = new StatefulBeanToCsvBuilder<ClientCsv>(response.getWriter()).build();
//        beanToCsv.write(list);

        HeaderColumnNameMappingStrategy<ClientCsv> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(ClientCsv.class);

        PrintWriter writer = response.getWriter();
        StatefulBeanToCsv<ClientCsv> btcsv = new StatefulBeanToCsvBuilder<ClientCsv>(writer)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(strategy)
                .withSeparator(',')
                .build();

        btcsv.write(list);
        writer.close();
    }

    @ApiOperation("下载csv且ZIP")
    @GetMapping("/download/csv/zip")
    public void downloadCsvZip(int dataCount, HttpServletResponse response) throws Exception {
        String fileName = "test";
        List<ClientCsv> list = generateData(dataCount);

        writeDate(list, fileName, ClientCsv.class, response);
    }

    @ApiOperation("下载PDF")
    @GetMapping("/download/pdf")
    public void downloadPdf(HttpServletResponse response) throws Exception {
        PdfUtil.createPdf();
    }


    private <T> void writeDate(List<T> list, String fileName, Class<T> clazz, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream(1024 * 1024);
        OutputStreamWriter outWriter = new OutputStreamWriter(byteArrayOut, "UTF-8");

        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);
        StatefulBeanToCsv<T> btcsv = new StatefulBeanToCsvBuilder<T>(outWriter)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(strategy)
                .withSeparator(',')
                .build();
        btcsv.write(list);
        outWriter.close();
        byte[] data = byteArrayOut.toByteArray();

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");
        ServletOutputStream writer = response.getOutputStream();

        ZipOutputStream outputStream = new ZipOutputStream(writer);
        ZipEntry zipFile = new ZipEntry(fileName + ".csv");

        outputStream.putNextEntry(zipFile);
        outputStream.write(data);
        outputStream.closeEntry();

        outputStream.flush();
        outputStream.close();
    }

    public List<ClientCsv> generateData(int count) {
        List<ClientCsv> list = new LinkedList<>();

        for (int i = 0; i < count; i++) {
            list.add(new ClientCsv("1234567890000" + i, "商户" + i, new Date(), LocalDateTime.now()));
        }
        return list;
    }
}
