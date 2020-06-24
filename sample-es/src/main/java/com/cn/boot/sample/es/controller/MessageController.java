package com.cn.boot.sample.es.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.es.model.po.Message;
import com.cn.boot.sample.es.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Api(tags = "消息管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Autowired
    private MessageService messageService;

    @ApiOperation("添加")
    @PostMapping("")
    public String insert(@RequestBody Message req) {
        messageService.save(req);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取列表")
    @GetMapping("/all")
    public Iterator<Message> find() {
        return messageService.findAll();
    }

    @ApiOperation("根据content获取")
    @GetMapping("/{content}")
    public List<Message> find(@PathVariable String content) {
        return messageService.findByContent(content).getContent();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{index}")
    public String delete(@PathVariable String index) {
        messageService.deleteIndex(index);
        return Constants.MSG_SUCCESS;
    }

}
