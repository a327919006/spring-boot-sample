package com.cn.boot.sample.business.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @ApiOperation("下载")
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
