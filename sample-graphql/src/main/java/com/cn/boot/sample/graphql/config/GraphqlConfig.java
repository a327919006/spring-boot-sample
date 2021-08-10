package com.cn.boot.sample.graphql.config;

import cn.hutool.core.io.FileUtil;
import com.cn.boot.sample.graphql.config.instrumentation.CostTimeInstrumentation;
import com.cn.boot.sample.graphql.config.instrumentation.SampleFieldValidation;
import com.cn.boot.sample.graphql.config.instrumentation.SampleInstrumentation;
import com.cn.boot.sample.graphql.config.wiring.AuthorWiring;
import com.cn.boot.sample.graphql.config.wiring.BookWiring;
import com.cn.boot.sample.graphql.config.wiring.QueryWiring;
import com.cn.boot.sample.graphql.subscribe.SubscriptionWiring;
import com.cn.boot.sample.graphql.subscribe.WebSocketServer;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.com.google.common.base.Charsets;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.AsyncSerialExecutionStrategy;
import graphql.execution.instrumentation.ChainedInstrumentation;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.fieldvalidation.FieldValidationInstrumentation;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import graphql.schema.visibility.BlockedFields;
import graphql.schema.visibility.GraphqlFieldVisibility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Chen Nan
 */
@Slf4j
@Configuration
public class GraphqlConfig {

    private GraphQL graphQL;

    @Autowired
    private QueryWiring queryWiring;
    @Autowired
    private BookWiring bookWiring;
    @Autowired
    private AuthorWiring authorWiring;
    @Autowired
    private SubscriptionWiring subscriptionWiring;

    @Autowired
    private SampleExceptionHandler exceptionHandler;
    @Autowired
    private SampleFieldVisibility fieldVisibility;
    @Autowired
    private AuthDirective authDirective;

    @Autowired
    private CostTimeInstrumentation costTimeInstrumentation;
    @Autowired
    private SampleInstrumentation sampleInstrumentation;
    @Autowired
    private SampleDocumentCache preparsedDocumentProvider;
    @Autowired
    private SampleFieldValidation fieldValidation;


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
        chainedList.add(costTimeInstrumentation);
//        chainedList.add(sampleInstrumentation);
        chainedList.add(new TracingInstrumentation());
        chainedList.add(new FieldValidationInstrumentation(fieldValidation));
        ChainedInstrumentation chainedInstrumentation = new ChainedInstrumentation(chainedList);

        RuntimeWiring runtimeWiring = buildWiring();
        GraphQLSchema schema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);

        graphQL = GraphQL.newGraphQL(schema)
                // 自定义异常处理器
                .defaultDataFetcherExceptionHandler(exceptionHandler)
                // 自定义查询缓存
                .preparsedDocumentProvider(preparsedDocumentProvider)
                // 默认Query执行策略
                .queryExecutionStrategy(new AsyncExecutionStrategy())
                // 默认Mutation执行策略
                .mutationExecutionStrategy(new AsyncSerialExecutionStrategy())
                // 类似拦截器，在执行GraphQL各个流程前后定义自己的业务流程
                .instrumentation(chainedInstrumentation)
                .build();

        log.info("===初始化GraphQL完成===");
        WebSocketServer.graphQL = graphQL;
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", queryWiring)
                .type("Author", authorWiring)
                .type("Book", bookWiring)
                .type("Subscription", subscriptionWiring)
                .directive("auth", authDirective)
                // 字段可见性，方式一：
//                .fieldVisibility(getBlockedFields())
                // 字段可见性，方式二：
//                .fieldVisibility(fieldVisibility)
                .build();
    }

    /**
     * 获取graphql配置文件内容
     */
    private String getResourceContent(String resource) throws IOException {
        URL url = Resources.getResource(resource);
        return Resources.toString(url, Charsets.UTF_8);
    }

    /**
     * 获取字段可见性配置
     */
    private GraphqlFieldVisibility getBlockedFields() {
        GraphqlFieldVisibility blockedFields = BlockedFields.newBlock()
                .addPattern("Book.updateTime")
                .addPattern(".*\\.updateTime") // it uses regular expressions
                .build();
        return blockedFields;
    }
}
