package com.javatechie.multithreadingexample.controller;

import com.javatechie.multithreadingexample.entity.User;
import com.javatechie.multithreadingexample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = "application/json")
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file : files) {
            userService.saveUsers(file);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(produces = "application/json")
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return userService.findAllUsers()
                .thenApply(ResponseEntity::ok);
    }

    // 2 threads in AsyncConfig and 3 calls for findAllUsers
    @GetMapping(value = "/multi-threads", produces = "application/json")
    public ResponseEntity findAllUsersByMultiThreads() {
        CompletableFuture<List<User>> users1 = userService.findAllUsers();
        CompletableFuture<List<User>> users2 = userService.findAllUsers();
        CompletableFuture<List<User>> users3 = userService.findAllUsers();
        CompletableFuture.allOf(users1, users2, users3).join();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /*
        2022-08-31 11:06:04.473  INFO 21939 --- [   userThread-2] c.j.m.service.UserService                : get list of users by userThread-2
        2022-08-31 11:06:04.473  INFO 21939 --- [   userThread-1] c.j.m.service.UserService                : get list of users by userThread-1
        2022-08-31 11:06:04.633  INFO 21939 --- [   userThread-1] c.j.m.service.UserService                : get list of users by userThread-1
     */
}
