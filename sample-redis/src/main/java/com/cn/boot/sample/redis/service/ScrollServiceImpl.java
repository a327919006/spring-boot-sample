package com.cn.boot.sample.redis.service;

import com.cn.boot.sample.redis.dto.ScrollResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Chen Nan
 */
@Service
public class ScrollServiceImpl {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @param min 已返回数据的最小score
     * @param offset 如果能保证score不重复，则固定为1，如果不能保障，如前端传已返回最小score的个数作为跳过offset
     * @return 滚动分页结果
     */
    public ScrollResult scrollPage(Long min, Integer offset) {
        // 1.获取当前用户
        Long userId = 1L;
        // 2.查询redis ZREVRANGEBYSCORE key Max Min LIMIT offset count
        String key = "feed:" + userId;
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet()
                .reverseRangeByScoreWithScores(key, 0, min, offset, 2);
        // 3.非空判断
        if (typedTuples == null || typedTuples.isEmpty()) {
            return null;
        }
        // 4.解析数据：blogId、minTime（时间戳）、offset
        List<String> ids = new ArrayList<>(typedTuples.size());
        long minTime = 0; // 2
        int os = 1; // 2
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) { // 5 4 4 2 2
            // 4.1.获取id
            ids.add(tuple.getValue());
            // 4.2.获取分数(时间戳）
            long time = tuple.getScore().longValue();
            if(time == minTime){
                os++;
            }else{
                minTime = time;
                os = 1;
            }
        }
        os = minTime == min ? os : os + offset;

        // 5.封装并返回
        ScrollResult scrollResult = new ScrollResult();
        scrollResult.setList(ids);
        scrollResult.setOffset(os);
        scrollResult.setMinTime(minTime);

        return scrollResult;
    }
}
