package com.cn.boot.sample.server.shard.service;

import com.cn.boot.sample.api.model.po.Order;
import com.cn.boot.sample.api.service.OrderService;
import com.cn.boot.sample.dal.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000)
@Slf4j
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order, Long>
        implements OrderService {
}
