package com.cn.boot.sample.dal.mapper;

import com.cn.boot.sample.api.model.po.Client;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.session.ResultHandler;
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

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = Integer.MIN_VALUE)
    void listByHandler(ResultHandler<Client> handler);
}