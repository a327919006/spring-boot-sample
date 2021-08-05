package com.cn.boot.sample.graphql.subscribe.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
public class QueryParameters {
    private String query;
    private String operationName;
    private Map<String, Object> variables = Collections.emptyMap();

    public String getQuery() {
        return query;
    }

    public String getOperationName() {
        return operationName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public static QueryParameters from(String queryMessage) {
        QueryParameters parameters = new QueryParameters();
        Map<String, Object> json = JsonKit.toMap(queryMessage);
        parameters.query = (String) json.get("query");
        parameters.operationName = (String) json.get("operationName");
        parameters.variables = getVariables(json.get("variables"));
        return parameters;
    }


    private static Map<String, Object> getVariables(Object variables) {
        if (variables instanceof Map) {
            Map<?, ?> inputVars = (Map) variables;
            Map<String, Object> vars = new HashMap<>();
            inputVars.forEach((k, v) -> vars.put(String.valueOf(k), v));
            return vars;
        }
        return JsonKit.toMap(String.valueOf(variables));
    }
}
