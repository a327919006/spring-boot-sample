package com.cn.boot.sample.business.version;

import com.cn.boot.sample.business.anotation.ApiVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Chen Nan
 */
public class SampleRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    public SampleRequestMappingHandlerMapping() {
    }

    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappinginfo = super.getMappingForMethod(method, handlerType);
        if (mappinginfo != null) {
            RequestMappingInfo apiVersionMappingInfo = this.getApiVersionMappingInfo(method, handlerType);
            return apiVersionMappingInfo == null ? mappinginfo : apiVersionMappingInfo.combine(mappinginfo);
        } else {
            return null;
        }
    }

    @Nullable
    private RequestMappingInfo getApiVersionMappingInfo(Method method, Class<?> handlerType) {
        ApiVersion apiVersion = (ApiVersion) AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
        if (apiVersion == null || StringUtils.isBlank(apiVersion.value())) {
            apiVersion = (ApiVersion) AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
        }

        boolean nonApiVersion = apiVersion == null || StringUtils.isBlank(apiVersion.value());
        if (nonApiVersion) {
            return null;
        } else {
            RequestMappingInfo.Builder mappingInfoBuilder = RequestMappingInfo.paths(new String[]{""});
            String vsersionMediaTypes = (new VersionMediaType(apiVersion.value())).toString();
            mappingInfoBuilder.produces(new String[]{vsersionMediaTypes});

            return mappingInfoBuilder.build();
        }
    }

    protected void handlerMethodsInitialized(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
        if (this.logger.isInfoEnabled()) {
            Iterator var2 = handlerMethods.entrySet().iterator();

            while (var2.hasNext()) {
                Map.Entry<RequestMappingInfo, HandlerMethod> entry = (Map.Entry) var2.next();
                RequestMappingInfo mapping = (RequestMappingInfo) entry.getKey();
                HandlerMethod handlerMethod = (HandlerMethod) entry.getValue();
                this.logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
            }
        }

        super.handlerMethodsInitialized(handlerMethods);
    }
}
