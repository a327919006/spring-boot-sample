package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.model.po.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ChenNan
 */
@Slf4j
@RestController
@RequestMapping("/file")
@Api(tags = "文件上传、下载测试", produces = MediaType.APPLICATION_JSON_VALUE)
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
}
