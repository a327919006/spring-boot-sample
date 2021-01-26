package com.cn.boot.sample.actuator.metric;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

/**
 * @author Chen Nan
 */
@Component
public class TestMetrics implements MeterBinder {
    public Counter counter1;
    public Counter counter2;
    public Counter counter3;

    /**
     * http://localhost:10100/sample-actuator/actuator/metrics/cn.demo.counter?tag=name:counter1
     */
    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        counter1 = Counter.builder("cn.demo.counter")
                .tags("name", "counter1")
                .description("This is counter1")
                .register(meterRegistry);
        counter2 = Counter.builder("cn.demo.counter")
                .tags("name", "counter2")
                .description("This is counter2")
                .register(meterRegistry);
        counter3 = Counter.builder("cn.demo.counter")
                .tags("name", "counter3")
                .description("This is counter3")
                .register(meterRegistry);
    }
}
