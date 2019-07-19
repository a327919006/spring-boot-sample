package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.po.Client;

/**
 * @author Chen Nan
 */
public interface ClientService extends BaseService<Client, String> {

    /**
     * 添加商户
     * @param client 商户信息
     */
    void insertClients(Client client);
}
