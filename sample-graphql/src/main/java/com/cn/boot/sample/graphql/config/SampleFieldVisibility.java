package com.cn.boot.sample.graphql.config;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLFieldsContainer;
import graphql.schema.visibility.GraphqlFieldVisibility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class SampleFieldVisibility implements GraphqlFieldVisibility {

    public String getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("username");
    }

    @Override
    public List<GraphQLFieldDefinition> getFieldDefinitions(GraphQLFieldsContainer fieldsContainer) {
        log.info("name:{}", fieldsContainer.getName());

//        String typeName = fieldsContainer.getName();
//        if (StringUtils.equalsIgnoreCase(typeName, "Author")) {
//            String user = getUser();
//            if (!StringUtils.equalsIgnoreCase(user, "admin")) {
//                return Collections.emptyList();
//            }
//        }
        return fieldsContainer.getFieldDefinitions();
    }

    @Override
    public GraphQLFieldDefinition getFieldDefinition(GraphQLFieldsContainer fieldsContainer, String fieldName) {
//        log.info("name:{} fieldName:{}", fieldsContainer.getName(), fieldName);

        String typeName = fieldsContainer.getName();
        if (StringUtils.equalsIgnoreCase(typeName, "Book") && StringUtils.equalsIgnoreCase(fieldName, "createTime")) {
            String user = getUser();
            if (!StringUtils.equalsIgnoreCase(user, "admin")) {
                return null;
            }
        }
        return fieldsContainer.getFieldDefinition(fieldName);
    }
}
