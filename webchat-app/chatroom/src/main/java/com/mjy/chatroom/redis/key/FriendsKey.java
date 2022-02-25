package com.mjy.chatroom.redis.key;

/**
 * @author
 * @description
 * @create 2021-11-26 15:43
 */
public class FriendsKey extends BaseKey{
    public static final FriendsKey MY_FRIENDS=new FriendsKey("friends",10);

    public FriendsKey(String prefix) {
        super(prefix);
    }

    public FriendsKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }
}
