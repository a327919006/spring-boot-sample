package com.cn.boot.sample.server.mp.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author Chen Nan
 */
public class CustomInsertAllBatch extends AbstractMethod {
    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    protected CustomInsertAllBatch(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // 定义SQL语句
        String sql = "INSERT INTO " + tableInfo.getTableName() + "(" + StringUtils.join(tableInfo.getFieldList(), ",") + ") VALUES " +
                "(#{list[0].id}, #{list[0].name}, #{list[0].age}), " +
                "(#{list[1].id}, #{list[1].name}, #{list[1].age})";
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        // 第三个参数必须和baseMapper的自定义方法名一致
        return this.addInsertMappedStatement(mapperClass, modelClass, "mysqlInsertAllBatch", sqlSource, new NoKeyGenerator(), null, null);
    }
}