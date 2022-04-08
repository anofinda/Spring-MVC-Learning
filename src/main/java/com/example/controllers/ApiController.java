package com.example.controllers;

import com.example.entity.User;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author dongyudeng
 */
@RestController
@RequestMapping("/Api")
public class ApiController {
    @Autowired
    UserService userService;

    @GetMapping("/users")
    public Callable<List<User>> getUsers() {
        return () -> {
            return userService.getAllUsers();
        };
    }

    @GetMapping("/users/{id}")
    public DeferredResult<User> getUserById(@PathVariable("id")long id){
        DeferredResult<User> result=new DeferredResult<>(3000L);
        new Thread(()->{
            User user=userService.getUserById(id);
            result.setResult(user);
        }).start();
        return result;
    }

    @GetMapping("/number")
    public long getUserNumber(){
        return userService.getUserNumber();
    }
}
