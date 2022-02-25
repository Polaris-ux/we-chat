package com.mjy.chatroom;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@SpringBootTest
class ChatroomApplicationTests {

    @Autowired
    private JedisPool jedisPool;
    @Test
    void contextLoads() {
        Jedis resource = jedisPool.getResource();
        System.out.println(resource);
    }

}
