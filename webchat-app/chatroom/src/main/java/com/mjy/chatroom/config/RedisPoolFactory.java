package com.mjy.chatroom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author
 * @description
 * @create 2021-11-26 0:00
 */
@Component
public class RedisPoolFactory {

    @Autowired
    private RedisConfig redisConfig;

    /**
     * 将redis注入spring
     */
    @Bean
    public JedisPool jedisPoolFactory(){
        JedisPoolConfig jedisConfig=new JedisPoolConfig();
        jedisConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisConfig.setMaxTotal(redisConfig.getPoolMaxIdle());
        jedisConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait()*1000);
        JedisPool jedisPool=new JedisPool(jedisConfig,redisConfig.getHost(),redisConfig.getPort(),redisConfig.getTimeout()*1000, redisConfig.getPassword(),0);
        return jedisPool;

    }
}
