package com.cn.boot.sample.business.controller;

import com.cn.boot.sample.api.model.po.Order;
import com.cn.boot.sample.api.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(tags = "订单管理", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    @Reference
    private OrderService orderService;

    @ApiOperation("订单-添加")
    @PostMapping("")
    public int insert(@RequestBody Order req) {
        return orderService.insertSelective(req);
    }

    @ApiOperation("订单-列表")
    @GetMapping("")
    public List<Order> list(@ModelAttribute Order req) {
        return orderService.list(req);
    }
}
