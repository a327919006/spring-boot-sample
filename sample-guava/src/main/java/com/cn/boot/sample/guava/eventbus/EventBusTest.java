package com.cn.boot.sample.guava.eventbus;

import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

/**
 * @author Chen Nan
 */
public class EventBusTest {

    @Test
    public void testSimpleListener() {
        EventBus eventBus = new EventBus();
        eventBus.register(new SimpleListener());

        eventBus.post("test1");
        eventBus.post("test2");
        eventBus.post("test3");
    }

    @Test
    public void testMultiListener() {
        EventBus eventBus = new EventBus();
        eventBus.register(new MultiListener());

        eventBus.post("test1");
        eventBus.post(123);
    }
}
