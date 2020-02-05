package com.cn.boot.sample.hazelcast.controller;

import com.cn.boot.sample.api.model.Constants;
import com.cn.boot.sample.api.model.po.User;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MultiMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * @author Chen Nan
 */
@Slf4j
@RestController
@RequestMapping("/multimap")
@Api(tags = "MultiMap", produces = MediaType.APPLICATION_JSON_VALUE)
public class MultiMapController {

    @Autowired
    private HazelcastInstance hzInstance;

    @ApiOperation("保存数据")
    @PostMapping("/{map}")
    public String set(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        MultiMap<Object, Object> multiMap = hzInstance.getMultiMap(map);
        multiMap.put(key, value);
        return Constants.MSG_SUCCESS;
    }

    @ApiOperation("获取")
    @GetMapping("/{map}")
    public Collection<Object> get(@PathVariable String map, @RequestParam String key) {
        MultiMap<Object, Object> multiMap = hzInstance.getMultiMap(map);
        return multiMap.get(key);
    }

    @ApiOperation("所有")
    @GetMapping("/{map}/all")
    public MultiMap<Object, Object> all(@PathVariable String map) {
        return hzInstance.getMultiMap(map);
    }


    @ApiOperation("测试-保存用户")
    @PostMapping("/{map}/test/user")
    public boolean putUser(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        User user = new User().setUsername(value);

        MultiMap<String, User> multiMap = hzInstance.getMultiMap(map);
        return multiMap.put(key, user);
    }

    /**
     * 测试结果：MultiMap可以放入自定义对象，只要实现Serializable接口
     * 需要判断对象是否存在，不需要遍历，只要对象的属性值全部相同，调用containsEntry方法即可。
     */
    @ApiOperation("测试-查找用户")
    @GetMapping("/{map}/test/user")
    public boolean findUser(@PathVariable String map, @RequestParam String key, @RequestParam String value) {
        User user = new User().setUsername(value);

        MultiMap<String, User> multiMap = hzInstance.getMultiMap(map);
        return multiMap.containsEntry(key, user);
    }
}
