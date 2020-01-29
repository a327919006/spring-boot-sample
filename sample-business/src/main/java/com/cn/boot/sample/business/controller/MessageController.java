package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.mongo.api.model.Message;
import com.cn.boot.sample.mongo.api.model.MessageCount;
import com.cn.boot.sample.mongo.api.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/message")
@Api(tags = "消息管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {

    @Reference
    private MessageService messageService;

    @ApiOperation("消息-添加")
    @PostMapping("")
    public Message insert(@RequestBody Message req) {
        req.setCreateTime(LocalDateTime.now());
        req.setUpdateTime(LocalDateTime.now());
        return messageService.insert(req);
    }

    @ApiOperation("消息-列表")
    @GetMapping("/list")
    public List<Message> find(@ModelAttribute Message req) {
        return messageService.list(req);
    }

    @ApiOperation("消息-获取")
    @GetMapping("/{id}")
    public Message find(@PathVariable String id) {
        return messageService.selectByPrimaryKey(id);
    }

    @ApiOperation("消息-删除")
    @DeleteMapping("/{id}")
    public int delete(@PathVariable String id) {
        return messageService.deleteByPrimaryKey(id);
    }

    @ApiOperation("消息-更新")
    @PutMapping("/")
    public int update(@RequestBody Message req) {
        req.setUpdateTime(LocalDateTime.now());
        return messageService.updateByPrimaryKey(req);
    }

    @ApiOperation("消息-数量")
    @GetMapping("/count/{day}")
    public List<MessageCount> count(@PathVariable Long day) {
        return messageService.findMessageCount(LocalDateTime.now().minusDays(day), 1);
    }
}
