package com.cn.boot.sample.dal.mapper;

import com.cn.boot.sample.api.model.po.Client;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Chen Nan
 */
public interface ClientMapper extends Mapper<Client> {

    /**
     * 保存Client
     *
     * @param client client信息
     * @return 操作结果
     */
    int saveClient(Client client);
}