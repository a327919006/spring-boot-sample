package com.cn.boot.sample.graphql.config;

import cn.hutool.core.io.FileUtil;
import com.cn.boot.sample.graphql.config.wiring.AuthorWiring;
import com.cn.boot.sample.graphql.config.wiring.BookWiring;
import com.cn.boot.sample.graphql.config.wiring.QueryWiring;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.com.google.common.base.Charsets;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Component
public class GraphqlConfig {

    private GraphQL graphQL;

    @Autowired
    private QueryWiring queryWiring;
    @Autowired
    private BookWiring bookWiring;
    @Autowired
    private AuthorWiring authorWiring;
    @Autowired
    private GraphqlExceptionHandler exceptionHandler;
    @Autowired
    private GraphqlInstrumentation instrumentation;

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() {
        String dir = "graphql";
        List<String> list = FileUtil.listFileNames(dir);

        SchemaParser schemaParser = new SchemaParser();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

        list.forEach(filename -> {
            String resource = dir + "/" + filename;
            try {
                String resourceContent = getResourceContent(resource);
                typeRegistry.merge(schemaParser.parse(resourceContent));
            } catch (IOException e) {
                log.error("get schema error:", e);
            }
        });

        List<Instrumentation> chainedList = new ArrayList<>();
        chainedList.add(instrumentation);
        chainedList.add(new TracingInstrumentation());
        ChainedInstrumentation chainedInstrumentation = new ChainedInstrumentation(chainedList);

        RuntimeWiring runtimeWiring = buildWiring();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
        graphQL = GraphQL.newGraphQL(schema)
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                .instrumentation(chainedInstrumentation)
                .build();
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", queryWiring)
                .type("Author", authorWiring)
                .type("Book", bookWiring)
                .build();
    }

    /**
     * 获取graphql配置文件内容
     */
    private String getResourceContent(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, Charsets.UTF_8);
    }

}