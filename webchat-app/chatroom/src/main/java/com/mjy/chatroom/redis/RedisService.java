package com.mjy.chatroom.redis;

import com.alibaba.fastjson.JSONObject;
import com.mjy.chatroom.entity.Users;
import com.mjy.chatroom.redis.key.PrefixKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @author
 * @description
 * @create 2021-11-26 14:49
 */

@Component
public class RedisService {

    @Autowired
    private JedisPool jedisPool;


    /**
     * 从redis中获取缓存
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(PrefixKey prefix,String key,Class<T> clazz){
        Jedis jedis=null;

        try {
            jedis=jedisPool.getResource();
            // 拼接key
            String realKey=prefix.getKey()+key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }
    }




    /**
     * 向redis中保存信息
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(PrefixKey prefix,String key,T value){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String str = beanToString(value);
            if(str==null||str.length()==0){
                return false;
            }

            String realKey=prefix.getKey()+key;
            // 获取过期时间
            int expireTime = prefix.expireSeconds();
            if(expireTime<=0){
                jedis.set(realKey,str);
            }else{
                jedis.setex(realKey,expireTime,str);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean set(PrefixKey prefix,String key,String jsonStr){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            if(jsonStr==null||jsonStr.length()==0){
                return false;
            }

            String realKey=prefix.getKey()+key;
            // 获取过期时间
            int expireTime = prefix.expireSeconds();
            if(expireTime<=0){
                jedis.set(realKey,jsonStr);
            }else{
                jedis.setex(realKey,expireTime,jsonStr);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     *将key中存储的数字值加一,若key不存在，则key对应的value值会被初始化1
     * @param prefix
     * @param key
     * @return
     */
    public Long incr(PrefixKey prefix,String key){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=prefix.getKey()+key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 根据key删除记录
     * @param prefix
     * @param key
     * @return
     */
    public Long delete(PrefixKey prefix,String key){
        Jedis jedis=null;
        try {
            jedis=jedisPool.getResource();
            String realKey=prefix.getKey()+key;
            return jedis.del(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param prefix
     * @param key
     * @return
     */
    public boolean isExist(PrefixKey prefix,String key){
        Jedis jedis=null;
        try {
            jedis = jedisPool.getResource();
            String realKey=prefix.getKey()+key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 转化为字符串或JSON字符串
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value){
        if(value==null){
            return null;
        }
        Class<?> aClass = value.getClass();
        if(aClass==int.class||aClass==Integer.class){
            return String.valueOf(value);
        }else if(aClass==long.class||aClass==Long.class){
            return String.valueOf(value);
        }else if(aClass==String.class){
            return (String)value;
        }else{
            return JSON.toJSONString(value);
        }
    }

    /**
     * 将字符串转化为对象
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str,Class<T> clazz){
        if(str==null||str.length()==0){
            return null;
        }
        if(clazz==int.class||clazz==Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz==long.class||clazz==Long.class){
            return (T)Long.valueOf(str);
        }else if(clazz==String.class){
            return (T)str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    /**
     * 将jedis连接放回到连接池中
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis!=null){
            jedis.close();
        }
    }
}
