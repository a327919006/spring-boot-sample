package com.cn.boot.sample.webflux.repository;

import com.cn.boot.sample.webflux.model.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * @author Chen Nan
 */
public interface StudentRepository extends ReactiveMongoRepository<Student, String> {

}
