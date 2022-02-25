package com.mjy.chatroom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan(basePackages = "com.mjy.chatroom.mapper")
@ComponentScan("com.mjy")
public class ChatroomApplication extends SpringBootServletInitializer {

    // 为了使内置tomcat和外置tomcat自由切换
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ChatroomApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);
    }

}
