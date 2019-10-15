package com.cn.boot.sample.hazelcast.loader;

import com.hazelcast.core.MapStore;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
public class TestDataLoader implements MapStore<String, String> {
    /**
     * map用来模拟数据从数据库增删改查
     */
    private static Map<String, String> dataMap = new HashMap<>();

    static {
        // 模拟数据库中已经持久化的数据
        log.info("【TestDataLoader】init");
        dataMap.put("test1", "111");
        dataMap.put("test2", "222");
        dataMap.put("test3", "333");
        dataMap.put("test4", "444");
    }

    @Override
    public void store(String key, String value) {
        log.info("【TestDataLoader】store, key={},value={}", key, value);
        dataMap.put(key, value);
    }

    @Override
    public void storeAll(Map<String, String> map) {
        log.info("【TestDataLoader】storeAll, map={}", map);
        dataMap.putAll(map);
    }

    @Override
    public void delete(String key) {
        log.info("【TestDataLoader】delete, key={}", key);
        dataMap.remove(key);
    }

    @Override
    public void deleteAll(Collection<String> keys) {
        log.info("【TestDataLoader】deleteAll, keys={}");
        for (String key : keys) {
            delete(key);
        }
    }

    @Override
    public String load(String key) {
        log.info("【TestDataLoader】load, key={}", key);
        return dataMap.get(key);
    }

    @Override
    public Map<String, String> loadAll(Collection<String> keys) {
        log.info("【TestDataLoader】loadAll, keys={}", keys);
        Map<String, String> data = new HashMap<>(keys.size());
        for (String key : keys) {
            data.put(key, load(key));
        }
        return data;
    }

    @Override
    public Iterable<String> loadAllKeys() {
        log.info("【TestDataLoader】loadAllKeys");
        return dataMap.keySet();
    }
}
