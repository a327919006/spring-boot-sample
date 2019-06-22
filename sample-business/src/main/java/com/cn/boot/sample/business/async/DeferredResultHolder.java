package com.cn.boot.sample.business.async;

import com.cn.boot.sample.api.model.po.Client;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Component
@Data
public class DeferredResultHolder {

    private Map<String, DeferredResult<Client>> map = new HashMap<>();


}
