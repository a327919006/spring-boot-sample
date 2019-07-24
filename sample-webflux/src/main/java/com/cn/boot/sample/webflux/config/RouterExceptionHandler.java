package com.cn.boot.sample.webflux.config;

import cn.hutool.json.JSONUtil;
import com.cn.boot.sample.api.model.dto.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Router方式的异常处理器
 * @author Chen Nan
 */
@Component
@Order(-99)
@Slf4j
public class RouterExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("error");
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);

        Error error = new Error(ex.getMessage());
        DataBuffer buffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(error).getBytes());

        return response.writeWith(Mono.just(buffer));
    }
}
