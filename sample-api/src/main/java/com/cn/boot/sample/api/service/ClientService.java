package com.cn.boot.sample.api.service;

import com.cn.boot.sample.api.model.po.Client;

/**
 * @author Chen Nan
 */
public interface ClientService extends BaseService<Client, String> {
    /**
     * 保存Client
     *
     * @param client client信息
     * @return 操作结果
     */
    int saveClient(Client client);

}
