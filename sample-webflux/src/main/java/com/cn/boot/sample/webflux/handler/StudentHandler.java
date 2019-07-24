package com.cn.boot.sample.webflux.handler;

import com.cn.boot.sample.webflux.model.Student;
import com.cn.boot.sample.webflux.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Chen Nan
 */
@Component
public class StudentHandler {

    @Autowired
    private StudentRepository studentRepository;

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(studentRepository.findAll(), Student.class);
    }

    public Mono<ServerResponse> save(ServerRequest serverRequest) {
        Mono<Student> studentMono = serverRequest.bodyToMono(Student.class);
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(studentRepository.saveAll(studentMono), Student.class);
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return studentRepository.findById(id)
                .flatMap(student -> studentRepository.delete(student).then(ServerResponse.ok().build()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        Mono<Student> studentMono = serverRequest.bodyToMono(Student.class);
        String id = serverRequest.pathVariable("id");

        return studentRepository.findById(id)
                .flatMap(findStu -> studentMono.flatMap(stu -> {
                    findStu.setName(stu.getName());
                    findStu.setAge(stu.getAge());
                    return ServerResponse
                            .ok()
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .body(studentRepository.save(stu), Student.class);
                }))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> findById(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(studentRepository.findById(id), Student.class);
    }

    public Mono<ServerResponse> findByAge(ServerRequest serverRequest) {
        int below = Integer.parseInt(serverRequest.pathVariable("below"));
        int top = Integer.parseInt(serverRequest.pathVariable("top"));
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(studentRepository.findByAgeBetween(below, top), Student.class);
    }
}
