package com.cn.boot.sample.actuator.metric;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

/**
 * @author Chen Nan
 */
@Component
public class TestMetrics implements MeterBinder {
    public Counter counter1;
    public Counter counter2;
    public Counter counter3;

    public AtomicInteger gauge;

    /**
     * http://localhost:10100/sample-actuator/actuator/metrics/cn.demo.counter?tag=name:counter1
     */
    @Override
    public void bindTo(MeterRegistry registry) {
        counter1 = Counter.builder("cn.demo.counter")
                .tags("name", "counter1")
                .description("This is counter1")
                .register(registry);
        counter2 = Counter.builder("cn.demo.counter")
                .tags("name", "counter2")
                .description("This is counter2")
                .register(registry);
        counter3 = Counter.builder("cn.demo.counter")
                .tags("name", "counter3")
                .description("This is counter3")
                .register(registry);

        gauge = registry.gauge("cn.demo.guage", Tags.of("level", "1", "name", "故障"), new AtomicInteger(0));
    }
}
