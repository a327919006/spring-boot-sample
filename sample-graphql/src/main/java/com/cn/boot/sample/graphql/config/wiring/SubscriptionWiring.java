package com.cn.boot.sample.graphql.config.wiring;

import com.cn.boot.sample.graphql.config.StockTickerPublisher;
import graphql.schema.idl.TypeRuntimeWiring;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

/**
 * @author Chen Nan
 */
@Component
public class SubscriptionWiring implements UnaryOperator<TypeRuntimeWiring.Builder> {

    @Autowired
    private StockTickerPublisher publisher;

    @Override
    public TypeRuntimeWiring.Builder apply(TypeRuntimeWiring.Builder builder) {
        return builder
                .dataFetcher("stockQuotes", environment -> {
                    List<String> arg = environment.getArgument("stockCodes");
                    List<String> stockCodesFilter = arg == null ? Collections.emptyList() : arg;
                    if (stockCodesFilter.isEmpty()) {
                        return publisher.getPublisher();
                    } else {
                        return publisher.getPublisher(stockCodesFilter);
                    }
                });
    }
}
