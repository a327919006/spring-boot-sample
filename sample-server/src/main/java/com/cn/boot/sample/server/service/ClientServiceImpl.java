package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.IClientService;
import com.cn.boot.sample.dal.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client, Long>
        implements IClientService {
}
