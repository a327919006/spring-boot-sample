package com.cn.boot.sample.graphql.config;

import com.cn.boot.sample.api.model.po.User;
import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 配置自定义注解，实现自定义业务逻辑
 * 如：1、某个字段需要指定权限才能获取，自定义auth注解，要求admin权限才能获取
 * 2、增加自定义注解dateformat，格式化日期数据等等
 *
 * @author Chen Nan
 */
@Slf4j
@Component
public class AuthDirective implements SchemaDirectiveWiring {

    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
        // 获取该字段所需的role
        String targetAuthRole = ((StringValue) environment.getDirective().getArgument("role").getArgumentValue().getValue()).getValue();

        GraphQLFieldDefinition field = environment.getElement();
        GraphQLFieldsContainer parentType = environment.getFieldsContainer();

        // 创建自定义DataFetcher，校验用户角色
        DataFetcher originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
        DataFetcher authDataFetcher = env -> {
            Object context = env.getContext();
            // 校验通过则获取数据，调用不通过则返回空
            if (context instanceof User) {
                User user = (User) context;
                if (user.getUsername().equals(targetAuthRole)) {
                    return originalDataFetcher.get(env);
                }
            }
            return null;
        };

        // 设置使用自定义DataFetcher
        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field;
    }
}