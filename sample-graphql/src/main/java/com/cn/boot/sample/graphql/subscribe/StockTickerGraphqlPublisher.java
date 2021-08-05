package com.cn.boot.sample.graphql.subscribe;

import com.cn.boot.sample.graphql.subscribe.data.StockTickerPublisher;
import com.google.common.io.Resources;
import graphql.com.google.common.base.Charsets;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

/**
 * @author Chen Nan
 */
public class StockTickerGraphqlPublisher {
//    private final GraphQLSchema graphQLSchema;

//    public StockTickerGraphqlPublisher() {
//        graphQLSchema = buildSchema();
//    }
//
//    private GraphQLSchema buildSchema() {
//        //
//        // reads a file that provides the schema types
//        //
//
//        String streamReader = null;
//        try {
//            streamReader = getResourceContent("graphql/subscription.graphqls");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(streamReader);
//
//        RuntimeWiring wiring = RuntimeWiring.newRuntimeWiring()
//                .type("Subscription", new SubscriptionWiring())
//                .build();
//
//        return new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
//    }
//
//    public GraphQLSchema getGraphQLSchema() {
//        return graphQLSchema;
//    }
//
//    /**
//     * 获取graphql配置文件内容
//     */
//    private String getResourceContent(String resource) throws IOException {
//        URL url = Resources.getResource(resource);
//        return Resources.toString(url, Charsets.UTF_8);
//    }
}
