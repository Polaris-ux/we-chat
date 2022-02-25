package com.mjy.chatroom.redis.key;

/**
 * @author
 * @description
 * @create 2021-11-26 14:11
 */
public interface PrefixKey {

    /**
     * 过期时间
     * @return
     */
    int expireSeconds();

    /**
     * 存储在redis中的key
     * @return
     */
    String getKey();
}
