package com.cn.boot.sample.webflux.router;

import com.cn.boot.sample.webflux.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author Chen Nan
 */
@Configuration
public class StudentRouter {

    @Bean
    public RouterFunction<ServerResponse> customerRouter(StudentHandler handler) {
        return RouterFunctions
                .nest(RequestPredicates.path("/router/student"),
                        RouterFunctions.route(RequestPredicates.GET("/"), handler::findAll)
                                .andRoute(RequestPredicates.POST("/")
                                                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)),
                                        handler::save)
                                .andRoute(RequestPredicates.DELETE("/{id}"), handler::delete)
                                .andRoute(RequestPredicates.PUT("/{id}"), handler::update)
                                .andRoute(RequestPredicates.GET("/{id}"), handler::findById)
                                .andRoute(RequestPredicates.GET("/age/{below}/{top}"), handler::findByAge)

                );
    }
}
