package com.cn.boot.sample.server.service;

import com.cn.boot.sample.api.model.po.Client;
import com.cn.boot.sample.dal.mapper.ClientMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Chen Nan
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public void listByHandler() {
        clientMapper.listByHandler(new ResultHandler<Client>() {
            @Override
            public void handleResult(ResultContext<? extends Client> resultContext) {
                Client resultObject = resultContext.getResultObject();
                log.info("resultObject: {}", resultObject);
            }
        });
    }
}
