package com.cn.boot.sample.server.mp.config.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 实现自定义通用方法，比如批量插入数据
 * 使用场景
 * 1、自定义查询方法：当标准的CRUD操作无法满足复杂的查询需求时，可以通过SQL注入器添加自定义的查询方法。
 * 2、复杂数据处理：在需要进行复杂的数据处理，如多表联结、子查询、聚合函数等时，SQL注入器可以帮助生成相应的SQL语句。
 * 3、性能优化：通过自定义SQL语句，可以针对特定的查询场景进行性能优化。
 * 4、数据权限控制：在需要根据用户权限动态生成SQL语句时，SQL注入器可以用来实现数据权限的控制。
 * 5、遗留系统迁移：在将遗留系统迁移到MyBatis-Plus时，可能需要保留原有的SQL语句结构，SQL注入器可以帮助实现这一过渡。
 * <p>
 * 功能
 * 1、注入自定义SQL方法：通过实现ISqlInjector接口，可以注入自定义的SQL方法到MyBatis容器中，这些方法可以是任何复杂的SQL查询。
 * 2、扩展BaseMapper：可以在继承BaseMapper的基础上，通过SQL注入器添加额外的查询方法，这些方法将自动被MyBatis-Plus识别和使用。
 * 3、灵活的SQL生成：SQL注入器提供了灵活的SQL生成机制，可以根据业务需求生成各种SQL语句，包括但不限于SELECT、INSERT、UPDATE、DELETE等。
 * 4、集成第三方数据库功能：如果需要使用数据库的特定功能，如存储过程、触发器等，SQL注入器可以帮助生成调用这些功能的SQL语句。
 * 5、动态SQL支持：在某些场景下，SQL语句需要根据运行时的条件动态生成，SQL注入器可以支持这种动态SQL的生成。
 *
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