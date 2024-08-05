package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.exceptions.BusinessException;
import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.api.service.ClientService;
import com.cn.boot.sample.api.service.UidGeneratorService;
import com.cn.boot.sample.dal.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.aop.framework.AopContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * @author Chen Nan
 */
@Service(timeout = 300000, token = "true")
@Slf4j
public class ClientServiceImpl extends BaseServiceImpl<ClientMapper, Client, String>
        implements ClientService {
    @Reference
    private UidGeneratorService uidGeneratorService;

    @Override
    // 父方法添加事务，子方法不加，事务
    // @Transactional(rollbackFor = Exception.class)
    public int saveClient(Client client) {
        // return mapper.saveClient(client);
        return this.testAop(client);
        // ClientService clientService = (ClientService) AopContext.currentProxy();
        // return clientService.testAop(client);
    }

    @Override
    // @Transactional(rollbackFor = Exception.class)
    public int testAop(Client client) {
        int result = mapper.saveClient(client);
        if(StringUtils.equals("error", client.getName())){
            throw new BusinessException("error");
        }
        return result;
    }
}
