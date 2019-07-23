package com.cn.boot.sample.webflux.repository;

import com.cn.boot.sample.webflux.model.Student;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * @author Chen Nan
 */
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

    /**
     * 根据年龄范围查找
     *
     * @param below 最低年龄
     * @param top  最高年龄
     * @return 学生列表
     */
    Flux<Student> findByAgeBetween(int below, int top);

    /**
     * 根据年龄范围查找
     *
     * @param below 最低年龄
     * @param top  最高年龄
     * @return 学生列表
     */
    @Query("{age: {'$gte':?0, '$lt':?1}}")
    Flux<Student> queryByAge(int below, int top);
}
