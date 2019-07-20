package com.cn.boot.sample.server.service;

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
@Service(timeout = 300000)
@Slf4j
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client, String>
        implements ClientService {
    @Reference
    private UidGeneratorService uidGeneratorService;

    @Override
    public void insertClients(Client client) {
        for(int i = 0; i < 100000; i++){
            client.setId(uidGeneratorService.generate());
            client.setName("测试商户" + i);
            mapper.insertSelective(client);
        }
    }
}
