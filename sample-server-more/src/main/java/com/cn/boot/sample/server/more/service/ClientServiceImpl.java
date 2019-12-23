package com.cn.boot.sample.server.more.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import com.cn.boot.sample.dal.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;

/**
 * @author Chen Nan
 */
@Service
@Slf4j
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client, String>
        implements ClientService {
    @Reference
    private UidGeneratorService uidGeneratorService;

    @Override
    public int saveClient(Client client) {
        return mapper.saveClient(client);
    }
}
