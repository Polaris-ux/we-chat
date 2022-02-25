package com.mjy.chatroom.redis.key;

/**
 * @author
 * @description
 * @create 2021-11-26 14:14
 */
public abstract class BaseKey implements PrefixKey{

    private int expireSeconds;
    private String prefix;

    public BaseKey(String prefix) {
        // 0代表永不过期
        this(prefix,0);
    }

    public BaseKey(String prefix,int expireSeconds) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getKey() {
        String className=getClass().getSimpleName();
        return className+":"+prefix+":";
    }
}
