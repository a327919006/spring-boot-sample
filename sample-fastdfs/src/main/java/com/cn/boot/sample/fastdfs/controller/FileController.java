package com.cn.boot.sample.fastdfs.controller;

import cn.hutool.core.io.IoUtil;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${fdfs.http-prefix}")
    private String prefix;

    @Autowired
    private FastFileStorageClient storageClient;

    @ApiOperation("上传文件")
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

        StorePath storePath = storageClient.uploadFile(file.getInputStream(), size, filename, null);
        storePath.getFullPath();
        log.info("path={}", storePath.getPath());
        log.info("fullPath={}", storePath.getFullPath());
        log.info("group={}", storePath.getGroup());

        return prefix + storePath.getFullPath();
    }

    @ApiOperation("下载文件")
    @GetMapping("/download")
    public void download(String groupName, String path, HttpServletResponse response) throws IOException {
        byte[] bytes = storageClient.downloadFile(groupName, path, IoUtil::readBytes);
        String fileName = path.substring(path.lastIndexOf("/"));

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        response.setHeader("Connection", "close");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
