package com.cn.boot.sample.graphql.config;

import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.execution.instrumentation.fieldvalidation.FieldAndArguments;
import graphql.execution.instrumentation.fieldvalidation.FieldValidationEnvironment;
import graphql.execution.instrumentation.fieldvalidation.SimpleFieldValidation;

import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Chen Nan
 */
public class TestSimpleFieldValidation extends SimpleFieldValidation {

    @Override
    public SimpleFieldValidation addRule(ResultPath fieldPath, BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> rule) {

        return this;
    }

}
