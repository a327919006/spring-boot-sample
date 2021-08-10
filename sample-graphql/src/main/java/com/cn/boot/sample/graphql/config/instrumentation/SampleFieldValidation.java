package com.cn.boot.sample.graphql.config.instrumentation;

import graphql.execution.ResultPath;
import graphql.execution.instrumentation.fieldvalidation.SimpleFieldValidation;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Chen Nan
 */
@Component
public class SampleFieldValidation extends SimpleFieldValidation {

    public SampleFieldValidation() {
        ResultPath fieldPath = ResultPath.parse("/findAuthorById");
        this.addRule(fieldPath, (fieldAndArguments, environment) -> {
            String id = fieldAndArguments.getArgumentValue("id");
            if (id.length() > 10) {
                return Optional.of(environment.mkError("Invalid ID", fieldAndArguments));
            }
            return Optional.empty();
        });
    }
}
