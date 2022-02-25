package com.mjy.chatroom.controller;

import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author
 * @description
 * @create 2021-11-01 23:53
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("/t1")
    @ResponseBody
    public String test(){
        return "hello world";
    }

    @RequestMapping("/getUserById/{id}")
    @ResponseBody
    public Users testDao(@PathVariable("id") String id){
        return userService.getUserById(id);
    }
}
